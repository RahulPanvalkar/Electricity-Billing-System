package com.ebs.controllers;

import com.ebs.services.BillService;
import com.ebs.services.PaymentService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class PaymentController {

    private static final Logger logger = LoggerUtil.getLogger(PaymentController.class);

    private final PaymentService paymentService;
    private final BillService billService;

    public PaymentController(PaymentService paymentService, BillService billService) {
        this.paymentService = paymentService;
        this.billService = billService;
    }

    @PostMapping({"consumer/pending-bill/payment"})
    public ModelAndView getPaymentPage(@RequestParam("billNo") String billNo, @RequestParam("totalAmount") String totalAmount) {
        return paymentService.getPaymentPage(billNo, totalAmount);
    }

    @PostMapping({"pending-bill/payment/process", "consumer/pending-bill/payment/process"})
    public ModelAndView payBill(@RequestParam("billNo") String billNo, @RequestParam("expiryDate") String cardExpiryDate) {
        logger.debug("Inside payBill >> billNo :: " + billNo);
        ModelAndView mv = new ModelAndView();
        LocalDate expiryDate = LocalDate.parse(cardExpiryDate);
        LocalDate currentDate = LocalDate.now();
        logger.debug("currentDate : " + currentDate + " & expiryDate : " + expiryDate);
        if (currentDate.isAfter(expiryDate)) {
            logger.warn("card has expired!");
            mv.addObject("message", "Payment Failed, Expired Card Used");
            mv.setViewName("consumer/bill-pay/payment-page");
            return mv;
        }

        try {
            billService.updateBillStatus(billNo);
            logger.debug("payment success!");
            mv.addObject("paid", true);
            mv.addObject("message", "Payment Successful");
            mv.setViewName("consumer/bill-pay/payment-page");
        } catch (Exception e) {
            logger.error("Exception occurred in payBill : ", e);
            mv.addObject("message", "Payment failed, please try again");
            mv.setViewName("consumer/bill-pay/payment-page");
        }
        return mv;
    }

    @PostMapping("/pending-bill/payment")
    public ModelAndView getQuickPaymentPage(@RequestParam String billNo, @RequestParam String consumerNum, @RequestParam String totalAmount) {
        ModelAndView mv = new ModelAndView("consumer/bill-pay/payment-page");
        mv.addObject("billNo", billNo);
        mv.addObject("totalAmount", totalAmount);
        return mv;
    }
}
