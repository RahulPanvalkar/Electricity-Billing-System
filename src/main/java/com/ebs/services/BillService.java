package com.ebs.services;

import com.ebs.entities.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface BillService {

    Optional<Bill> getBillById(String billNo);

    Optional<Bill> getBillByConsumerNum(String consumerNum);

    Optional<Bill> getBillDataByConsumerNoAndStatus(String consumerNum);

    ModelAndView getBills(int page, int size);

    ResponseEntity<Map<String, Object>> getPreviousBill(String consumerNum);

    void updateBillStatus(String billNo);

    HashMap<String, String> addBill(Bill bill);

    HashMap<String, String> deleteBill(String billNo);

    HashMap<String, Object> updateBill(Bill bill);

}
