package com.ebs.controllers;

import com.ebs.services.ConsumerService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private static final Logger logger = LoggerUtil.getLogger(ConsumerController.class);

    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/dashboard")
    public ModelAndView getConsumerDashboard() {
        return new ModelAndView("consumer/consumer-dashboard");
    }

    @GetMapping("/bill-history")
    public ModelAndView getBillsForConsumer(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return consumerService.getBillsByConsumerNum(page, size);
    }

    @GetMapping("/current-bill")
    public ModelAndView getPendingBill(){
        return consumerService.getBillByConsumerNo();
    }

}
