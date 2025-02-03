package com.ebs.controllers;

import com.ebs.entities.User;
import com.ebs.entities.UserRegistration;
import com.ebs.services.UserRegService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class UserRegController {

    private static final Logger logger = LoggerUtil.getLogger(UserRegController.class);

    @Autowired
    UserRegService userRegService;
    @GetMapping("")
    public ModelAndView register() {
        logger.debug("register page called...");
        return new ModelAndView("public/register");
    }

    @PostMapping("/user-verification")
    public ModelAndView getVerificationPage(@ModelAttribute UserRegistration userReg){
        logger.debug("userReg : [{}]", userReg);
        ModelAndView mv  = new ModelAndView();

        Map<String, Object> retMap = userRegService.verifyEmailIdAndSendOtp(userReg);
        logger.debug("retMap : [{}]", retMap);
        if ("success".equalsIgnoreCase((String) retMap.get("RESULT"))){
            mv.setViewName("public/user-verification");
        } else{
            mv.setViewName("public/register");
        }

        ModelAndViewUtil.addMVObject(retMap,mv);
        return mv;
    }

    @PostMapping("")
    public ModelAndView registerConsumer(@RequestParam String otpCode, @ModelAttribute User user) {
        logger.debug("user >> [{}]",user);
        ModelAndView mv = new ModelAndView();
        HashMap<String, Object> retMap = userRegService.addConsumer(user,otpCode);
        mv.setViewName("public/register");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

}
