package com.ebs.controllers;

import com.ebs.entities.Admin;
import com.ebs.entities.Bill;
import com.ebs.entities.CostPerUnit;
import com.ebs.exception.InvalidSessionException;
import com.ebs.services.*;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import com.ebs.utils.UserUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/")
public class BillController {

    private static final Logger logger = LoggerUtil.getLogger(BillService.class);

    private final AdminService adminService;
    private final BillService billService;
    private final CostPerUnitService costPerUnitService;


    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();
    private final Map<String, Long> requestTimestamps = new ConcurrentHashMap<>();

    public BillController(AdminService adminService, BillService billService, CostPerUnitService costPerUnitService) {
        this.adminService = adminService;
        this.billService = billService;
        this.costPerUnitService = costPerUnitService;
    }

    @GetMapping("admin/bills")
    public ModelAndView viewBills(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return billService.getBills(page, size);
    }


    @GetMapping("admin/previous-bill/{consumerNum}")
    public ResponseEntity<Map<String, Object>> getMeterNumAndPrevBal(@PathVariable("consumerNum") String consumerNum) {
        // Custom Rate Limiting with In-Memory Store
        long currentTime = System.currentTimeMillis();
        requestCounts.putIfAbsent(consumerNum, new AtomicInteger(0));
        requestTimestamps.putIfAbsent(consumerNum, currentTime);

        long timeElapsed = currentTime - requestTimestamps.get(consumerNum);

        // Reset the counter if more than a minute has passed
        if (timeElapsed > 60000) {
            requestCounts.get(consumerNum).set(0);
            requestTimestamps.put(consumerNum, currentTime);
        }

        if (requestCounts.get(consumerNum).incrementAndGet() > MAX_REQUESTS_PER_MINUTE) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "Too many requests. Please try again later."));
        }
        return billService.getPreviousBill(consumerNum);
    }


    @GetMapping("admin/add-bill")
    public ModelAndView addBill() {

        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/add-bill");

        Optional<CostPerUnit> costPerUnitOp = costPerUnitService.getCostPerUnitById(1);
        if (costPerUnitOp.isPresent()){
            CostPerUnit costPerUnit = costPerUnitOp.get();
            logger.debug("addBill >> costPerUnit :: [{}]", costPerUnit);
            mv.addObject("costPerUnit", costPerUnit);
        } else {
            logger.debug("addBill >> costPerUnit not found");
            mv.addObject("costPerUnit", new CostPerUnit());
        }

        return mv;
    }

    @PostMapping("admin/add-bill")
    public ModelAndView addBill(@ModelAttribute Bill bill) {
        Optional<Admin> loggedInUser = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUser.isEmpty()) {
            throw new InvalidSessionException("Invalid session! user not logged in");
        }

        Admin admin = loggedInUser.get();
        ModelAndView mv = new ModelAndView("admin/add-bill");
        logger.debug("bill :: [{}]",bill);
        if(bill == null){
            mv.addObject("admin",admin);
        }

        HashMap<String, String> retMap = billService.addBill(bill);
        ModelAndViewUtil.addMVObject(retMap,mv);
        return mv;
    }

    @DeleteMapping("admin/remove-bill/{recordNo}")
    public ResponseEntity<Map<String, String>> deleteBill(@PathVariable("recordNo") String billNo){

        HashMap<String, String> retMap = new HashMap<>();
        logger.debug("deleteBill >> billNo :: [{}]",billNo);
        if (billNo == null || billNo.trim().isEmpty()) {
            retMap.put("RESULT","fail");
            retMap.put("MSG","Invalid Bill No");
            return ResponseEntity.ok(retMap);
        }

        retMap = billService.deleteBill(billNo.trim());
        return ResponseEntity.ok(retMap);
    }

    @GetMapping("admin/bill/edit/{billNo}")
    public ModelAndView editBill(@PathVariable("billNo") String billNo) {

        ModelAndView mv = new ModelAndView();
        Optional<Bill> billOpt = billService.getBillById(billNo);
        if (billOpt.isPresent()){
            Bill bill = billOpt.get();
            logger.debug("bill :: [{}] ", bill);

            Optional<CostPerUnit> costPerUnitOp = costPerUnitService.getCostPerUnitById(1);
            if (costPerUnitOp.isPresent()){
                CostPerUnit costPerUnit = costPerUnitOp.get();
                logger.debug("costPerUnit :: [{}]", costPerUnit);
                mv.addObject("costPerUnit", costPerUnit);
            } else {
                logger.debug("costPerUnit not found");
                mv.addObject("costPerUnit", new CostPerUnit());
            }

            mv.addObject("bill",bill);
        } else {
            logger.error("bill not found..");
        }
        mv.setViewName("admin/edit-bill");
        return mv;
    }


    @PostMapping("admin/bill/edit")
    public ModelAndView updateBillData(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute Bill bill) {

        String username = userDetails.getUsername();
        Admin admin = adminService.getAdmin(username);

        ModelAndView mv = new ModelAndView();
        logger.debug("Bill :: [{}] ", bill);
        if(bill == null){
            mv.setViewName("admin/edit-bill");
            mv.addObject("admin",admin);
        }

        HashMap<String, Object> retMap = billService.updateBill(bill);
        mv.setViewName("admin/edit-bill");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

}
