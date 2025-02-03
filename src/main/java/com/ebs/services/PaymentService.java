package com.ebs.services;

import org.springframework.web.servlet.ModelAndView;

public interface PaymentService {
    ModelAndView getPaymentPage(String billNo, String totalAmount);

    ModelAndView quickBillPayment(String consumerNum);

    ModelAndView getQuickPaymentPage(String billNo, String consumerNum, String totalAmount);
}
