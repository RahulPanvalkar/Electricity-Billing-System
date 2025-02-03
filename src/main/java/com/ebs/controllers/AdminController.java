package com.ebs.controllers;

import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.entities.UserType;
import com.ebs.services.AdminService;
import com.ebs.services.ConsumerService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerUtil.getLogger(AdminController.class);

    private final AdminService adminService;
    private final UserService userService;
    private final ConsumerService consumerService;

    @Autowired
    public AdminController(AdminService adminService, UserService userService, ConsumerService consumerService) {
        this.adminService = adminService;
        this.userService = userService;
        this.consumerService = consumerService;
    }

    @GetMapping("/dashboard")
    public ModelAndView getAdminDashboard() {
        return new ModelAndView("admin/admin-dashboard");
    }

    @GetMapping("/register")
    public ModelAndView getAdminRegPage() {
        return new ModelAndView("admin/admin-registration");
    }

    @PostMapping("/register")
    public ModelAndView registerAdmin(@ModelAttribute Admin admin) {
        logger.debug("admin >> [{}]", admin);
        ModelAndView mv = new ModelAndView();
        HashMap<String, Object> retMap = adminService.addAdmin(admin);
        mv.setViewName("admin/admin-registration");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

    @GetMapping("/users")
    public ModelAndView viewUsers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        logger.debug("page: [{}], size: [{}]", page, size);
        return userService.getUsers(page, size);
    }

    @GetMapping("/consumers")
    public ModelAndView viewConsumers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return consumerService.getConsumers(page, size);
    }

    @GetMapping("/add-consumer")
    public ModelAndView addConsumer() {
        return new ModelAndView("admin/add-consumer");
    }

    @PostMapping("/add-consumer")
    public ModelAndView handleAddConsumerReq(@ModelAttribute Consumer consumer) {
        ModelAndView mv = new ModelAndView();
        logger.debug("addConsumer >> consumer :: [" + consumer + "]");
        if (consumer == null) {
            mv.setViewName("admin/add-consumer");
        }

        HashMap<String, Object> retMap = consumerService.addConsumer(consumer);
        mv.setViewName("admin/add-consumer");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

    @GetMapping("/edit-consumer/{consumerNum}")
    public ModelAndView editConsumer(@PathVariable("consumerNum") String consumerNum) {

        ModelAndView mv = new ModelAndView();
        Optional<Consumer> consumerOpt = consumerService.getConsumerById(consumerNum);
        if (consumerOpt.isPresent()) {
            Consumer consumer = consumerOpt.get();
            logger.debug("editConsumer >> consumer :: [{}] ", consumer);
            mv.addObject("consumer", consumer);
        } else {
            logger.debug("editConsumer >> consumer not found..");
        }
        mv.setViewName("admin/edit-consumer");
        return mv;
    }

    @PostMapping("/edit-consumer")
    public ModelAndView updateConsumerData(@ModelAttribute Consumer consumer) {

        ModelAndView mv = new ModelAndView();
        logger.debug("consumer :: [{}] ", consumer);
        if (consumer == null) {
            mv.setViewName("admin/edit-consumer");
            // mv.addObject("admin",admin);
        }
        HashMap<String, Object> retMap = consumerService.updateConsumer(consumer);
        mv.setViewName("admin/edit-consumer");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

    @DeleteMapping("/remove-consumer/{consumerNum}")
    public ResponseEntity<Map<String, String>> updateConsumerData(@PathVariable("consumerNum") String consumerNum) {
        HashMap<String, String> retMap = consumerService.deleteConsumer(consumerNum);
        logger.debug("retMap: [{}]", retMap);
        return ResponseEntity.ok(retMap);
    }

    //to get the consumer details to fill the info in add connection form
    @GetMapping("/consumer/{consumerNum}")
    public ResponseEntity<?> getConsumerData(@PathVariable("consumerNum") String consumerNum) {

        logger.debug("getConsumerData >> consumerNum :: [{}]", consumerNum);
        if (consumerNum == null || consumerNum.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Consumer number cannot be empty.");
        }

        Optional<Consumer> consumerOptional = consumerService.getConsumerById(consumerNum);
        if (consumerOptional.isPresent()) {
            Consumer consumer = consumerOptional.get();
            logger.debug("getConsumerData >> consumer :: [{}]", consumer);
            return ResponseEntity.ok(consumer);
        }

        return ResponseEntity.status(404).body("{\"status\":\"failed\", \"message\": \"Consumer not found\" }");
    }

    @GetMapping("/edit-user/{userId}")
    public ModelAndView editUser(@PathVariable("userId") String userId) {
        ModelAndView mv = new ModelAndView();
        Optional<User> userOpt = userService.findById(userId);

        if (userOpt.isEmpty()) {
            logger.debug("User not found..");
            mv.setViewName("admin/consumers");
            return mv;
        }

        User user = userOpt.get();
        // if user is consumer
        if (UserType.C == user.getUserType()) {
            Optional<Consumer> consumerOpt = consumerService.getConsumerById(user.getUserCode());
            if (consumerOpt.isPresent()) {
                Consumer consumer = consumerOpt.get();
                logger.debug("consumer :: [{}] ", consumer);
                mv.setViewName("admin/edit-consumer");
                mv.addObject("consumer", consumer);
            } else {
                logger.debug("consumer not found..");
            }
        }
        return mv;
    }

    @DeleteMapping("/remove-user/{userId}")
    public ResponseEntity<HashMap<String, String>> removeUser(@PathVariable("userId") String userId) {
        Optional<User> userOpt = userService.findById(userId);
        HashMap<String, String> retMap = new HashMap<>();
        if (userOpt.isEmpty()) {
            logger.debug("User not found..");
            retMap.put("RESULT","fail");
            retMap.put("MSG","Invalid User Id");
            return ResponseEntity.ok(retMap);
        }

        User user = userOpt.get();
        // if user is consumer
        if (UserType.C == user.getUserType()) {
            Optional<Consumer> consumerOpt = consumerService.getConsumerById(user.getUserCode());
            if (consumerOpt.isPresent()) {
                retMap = consumerService.deleteConsumer(consumerOpt.get().getConsumerNum());
                retMap.put("MSG", "User deleted successfully!");
            } else {
                logger.debug("Consumer not found..");
                retMap.put("RESULT","fail");
                retMap.put("MSG","Invalid Consumer Number");
            }
            logger.debug("retMap: [{}]", retMap);
            return ResponseEntity.ok(retMap);
        }

        if (UserType.A == user.getUserType()) {
            Optional<Admin> adminOpt = adminService.findById(user.getUserCode());
            if (adminOpt.isPresent()) {
                retMap = adminService.deleteAdmin(adminOpt.get().getId());
                retMap.put("MSG", "User deleted successfully!");
            } else {
                logger.debug("Consumer not found..");
                retMap.put("RESULT","fail");
                retMap.put("MSG","Invalid Consumer Number");
            }
            logger.debug("retMap: [{}]", retMap);
            return ResponseEntity.ok(retMap);
        }
        return ResponseEntity.ok(retMap);
    }

}
