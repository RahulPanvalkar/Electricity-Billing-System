package com.ebs.services.impl;

import com.ebs.dao.BillDao;
import com.ebs.entities.Bill;
import com.ebs.entities.Consumer;
import com.ebs.entities.EConnection;
import com.ebs.services.BillService;
import com.ebs.services.ConnectionService;
import com.ebs.services.ConsumerService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.PaginationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class BillServiceImpl implements BillService {

    private static final Logger logger = LoggerUtil.getLogger(BillServiceImpl.class);

    private final BillDao billDao;
    private final ConnectionService connectionService;
    private final ConsumerService consumerService;
    private final PaginationUtil paginationUtil;

    public BillServiceImpl(BillDao billDao, ConnectionService connectionService, ConsumerService consumerService, PaginationUtil paginationUtil) {
        this.billDao = billDao;
        this.connectionService = connectionService;
        this.consumerService = consumerService;
        this.paginationUtil = paginationUtil;
    }

    public Optional<Bill> getBillById(String billNo) {
        return billDao.findById(billNo);
    }

    public Optional<Bill> getBillByConsumerNum(String consumerNum) {
        return billDao.findPendingBillByConsumerNum(consumerNum);
    }

    public Optional<Bill> getBillDataByConsumerNoAndStatus(String consumerNum) {
        return billDao.getBillDataByConsumerNoAndStatus(consumerNum);
    }

    public ModelAndView getBills(int page, int size) {
        ModelAndView mv = new ModelAndView("admin/view-bills");
        try {
            int pageNumber = page > 0 ? page : 1; // Ensuring page starts from 1

            List<Bill> bills = paginationUtil.findAll(pageNumber, size, Bill.class);
            long totalUsers = paginationUtil.countEntities(Bill.class); // Get total consumers

            int totalPages = (int) Math.ceil((double) totalUsers / size); // Calculate total pages

            mv.addObject("bills", bills); // bills for the current page
            mv.addObject("currentPage", pageNumber);
            mv.addObject("totalPages", totalPages);
            mv.addObject("totalUsers", totalUsers);
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
        }
        return mv;
    }


    public ResponseEntity<Map<String, Object>> getPreviousBill(String consumerNum) {
        Map<String, Object> returnMap = new HashMap<>();
        logger.debug("consumerNum :: [{}]", consumerNum);

        if (consumerNum == null || consumerNum.trim().isEmpty()) {
            returnMap.put("status", "fail");
            returnMap.put("message", "Consumer number cannot be empty.");
            return ResponseEntity.badRequest().body(returnMap);
        }

        try {
            // Attempt to retrieve the bill
            Date currentDate = new Date(System.currentTimeMillis());
            Optional<Bill> billOp = billDao.findPreviousBill(consumerNum, currentDate);
            if (billOp.isPresent()) {
                logger.debug("bill :: [{}]", billOp.get());
                returnMap.put("status", "success");
                returnMap.put("found", "Bill");
                returnMap.put("bill", billOp.get());
                return ResponseEntity.ok(returnMap);
            }

            // Attempt to retrieve the connection if no bill is found
            EConnection connection = connectionService.getConnectionByConsumerNum(consumerNum);
            if (connection != null) {
                logger.debug("getMeterNumAndPrevBal >> connection :: [{}]", connection);
                returnMap.put("status", "success");
                returnMap.put("found", "Connection");
                returnMap.put("connection", connection);
                return ResponseEntity.ok(returnMap);
            } else {
                // Check if consumer exists even if connection is not found
                Optional<Consumer> optionalConsumer = consumerService.getConsumerById(consumerNum);
                if (optionalConsumer.isPresent()) {
                    returnMap.put("status", "fail");
                    returnMap.put("message", "Connection not found for existing consumer.");
                    return ResponseEntity.ok(returnMap);
                }
            }

            returnMap.put("status", "fail");
            returnMap.put("message", "Invalid consumer number");
        } catch (Exception e) {
            logger.error("Error occurred while retrieving details for consumerNum [{}]: {}", consumerNum, e.getMessage(), e);
            returnMap.put("status", "error");
            returnMap.put("message", "An error occurred while processing your request.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(returnMap);
        }

        return ResponseEntity.status(HttpStatus.OK).body(returnMap);

    }

    @Transactional
    public void updateBillStatus(String billNo) {
        logger.debug("updateBillStatus >> billNo :: " + billNo);
        Optional<Bill> optionalBill = billDao.findById(billNo);
        if (optionalBill.isPresent()) {
            Bill bill = optionalBill.get();
            logger.debug("updateBillStatus >> " + bill);
            bill.setStatus("Paid");
            bill.setPaymentDate(new Date(System.currentTimeMillis()));
            billDao.save(bill);
        } else {
            throw new IllegalArgumentException("Bill with billNo " + billNo + " not found");
        }
    }

    @Transactional
    public HashMap<String, String> addBill(Bill bill) {
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            // Check if the bill already exists
            Optional<Bill> existingBillOp = billDao.getBillByConsumerNumAndBillDate(bill.getConsumerNum(), bill.getBillDate());
            if (existingBillOp.isPresent()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Bill already exists");
                return returnMap;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            bill.setBillDate(currentDate);
            bill.setStatus("Pending");
            bill.setBillNo("000"); //provide value to avoid exception
            logger.debug("details added >> bill :: [{}]", bill);
            try {
                billDao.save(bill);
                Optional<Bill> newBillOp = billDao.findPendingBillByConsumerNum(bill.getConsumerNum());
                if (newBillOp.isEmpty()) {
                    throw new RuntimeException("Bill not saved");
                }
                logger.debug("newBill :: {}", newBillOp.get());
            } catch (Exception e) {
                logger.error("Exception occurred :: ", e);
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return returnMap;
            }

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Bill created successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }


    @Transactional
    public HashMap<String, String> deleteBill(String billNo) {
        logger.debug("deleteBill >> billNo :: [{}]", billNo);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            if (billNo == null || billNo.trim().isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Bill No");
                return returnMap;
            }

            Optional<Bill> billOptional = billDao.findById(billNo.trim());

            if (billOptional.isPresent()) {
                Bill bill = billOptional.get();
                logger.debug("deleteBill >> bill :: [{}]", bill);
                billDao.delete(bill);
                returnMap.put("RESULT", "success");
                returnMap.put("MSG", "Bill deleted successfully!");
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Bill not found!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }

        return returnMap;
    }

    @Transactional
    public HashMap<String, Object> updateBill(Bill bill) {
        logger.debug("bill :: {}", bill);
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            Optional<Bill> existingBillOpt = billDao.findById(bill.getBillNo());
            if (existingBillOpt.isEmpty()) {
                logger.error("Bill not found..");
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Bill not found.");
                return returnMap;
            }

            // set updated values in existing bill
            Bill existingBill = setUpdatedValues(bill, existingBillOpt.get());
            logger.debug("existingBill :: [{}]", existingBill);

            billDao.save(existingBill);

            Optional<Bill> updatedBill = billDao.findById(bill.getBillNo());
            logger.debug("updatedConn :: {}", updatedBill);

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Bill details updated!");
            returnMap.put("bill", updatedBill);
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    private Bill setUpdatedValues(Bill bill, Bill existingBill) {
        try {
            existingBill.setMonth(bill.getMonth());
            existingBill.setCurrentReading(bill.getCurrentReading());
            existingBill.setPreviousReading(bill.getPreviousReading());
            existingBill.setTotalUnits(bill.getTotalUnits());
            existingBill.setCurrentAmount(bill.getCurrentAmount());
            existingBill.setPreviousBalance(bill.getPreviousBalance());
            existingBill.setTotalAmount(bill.getTotalAmount());
            existingBill.setDueDate(bill.getDueDate());
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
        }
        return existingBill;
    }
}
