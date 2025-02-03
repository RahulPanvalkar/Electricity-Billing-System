package com.ebs.controllers;

import com.ebs.services.PaymentService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainController {

    private static final Logger logger = LoggerUtil.getLogger(MainController.class);

    private final PaymentService paymentService;

    public MainController(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/")
    public ModelAndView home(HttpServletRequest request, HttpServletResponse response){
        logger.debug("loading home..");
        ModelAndView mv = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        mv.setViewName("public/home");
        return mv;
    }

    @GetMapping("/sign-in")
    public ModelAndView login() {
        logger.debug("login page called...");
        return new ModelAndView("public/login");
    }


    @GetMapping("/logout")
    public String logoutAdmin(HttpServletRequest request, HttpServletResponse response) {
        return "redirect:/";
    }

    @RequestMapping("/about")
    public ModelAndView getAbout(){
        logger.debug("login page called...");
        return new ModelAndView("public/about");
    }

    @PostMapping("/quick-bill")
    public ModelAndView quickBillPayment(@RequestParam("consumerNo") String consumerNo){
        logger.debug("consumerNo : "+consumerNo);
        return paymentService.quickBillPayment(consumerNo);
    }

}
