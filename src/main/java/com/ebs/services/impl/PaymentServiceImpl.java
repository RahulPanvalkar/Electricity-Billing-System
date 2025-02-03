package com.ebs.services.impl;

import com.ebs.dao.BillDao;
import com.ebs.entities.Bill;
import com.ebs.entities.Consumer;
import com.ebs.exception.InvalidSessionException;
import com.ebs.services.ConsumerService;
import com.ebs.services.PaymentService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.UserUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {
    private static final Logger logger = LoggerUtil.getLogger(PaymentServiceImpl.class);

    private final ConsumerService consumerService;
    private final BillDao billDao;

    public PaymentServiceImpl(@Lazy ConsumerService consumerService, @Lazy BillDao billDao) {
        this.consumerService = consumerService;
        this.billDao = billDao;
    }

    public ModelAndView getPaymentPage(String billNo, String totalAmount) {
        ModelAndView mv = new ModelAndView();
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }
        Consumer consumer = loggedInUserOp.get();

        mv.setViewName("consumer/bill-pay/payment-page");
        mv.addObject("consumer", consumer);
        mv.addObject("billNo", billNo);
        mv.addObject("totalAmount", totalAmount);
        return mv;
    }

    public ModelAndView quickBillPayment(String consumerNum) {
        ModelAndView mv = new ModelAndView("public/home");
        try {
            Optional<Consumer> consumerOp = consumerService.getConsumerById(consumerNum);
            logger.debug("consumerOp :: [" + consumerOp + "]");
            if (consumerOp.isEmpty()) {
                logger.debug("invalid consumer!");
                mv.addObject("message", "Invalid Consumer Number!");
                return mv;
            }
            Optional<Bill> billOp = billDao.findPendingBillByConsumerNum(consumerNum);
            mv.setViewName("consumer/bill-pay/pending-bill");
            if (billOp.isPresent()) {
                mv.addObject("currentBill", billOp.get());
            }
        } catch (Exception e) {
            logger.error("Exception occurred : ", e);
            mv.addObject("message", "something went wrong, please try again");
        }
        return mv;
    }

    public ModelAndView getQuickPaymentPage(String billNo, String consumerNum, String totalAmount) {
        ModelAndView mv = new ModelAndView("consumer/bill-pay/payment-page");
        mv.addObject("billNo", billNo);
        mv.addObject("totalAmount", totalAmount);
        return mv;
    }
}
