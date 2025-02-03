package com.ebs.controllers;

import com.ebs.entities.User;
import com.ebs.services.ForgetPasswordService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/forget-password")
public class ForgetPasswordController {

    private static final Logger logger = LoggerUtil.getLogger(ForgetPasswordController.class);

    private final ForgetPasswordService forgetPasswordService;

    public ForgetPasswordController(ForgetPasswordService forgetPasswordService) {
        this.forgetPasswordService = forgetPasswordService;
    }

    @GetMapping("")
    public ModelAndView getForgetPasswordPage(){
        logger.debug("forgetPassword page called..");
        return new ModelAndView("public/forget-password");
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Object>> verifyEmailAndSendOTP(@RequestBody Map<String, String> request) {
        String emailId = request.get("emailId");
        logger.debug("resetPassword >> emailId: [{}]", emailId);
        Map<String, Object> response  = new HashMap<>();
        if (emailId == null || emailId.trim().isEmpty()){
            response.put("RESULT","fail");
            response.put("MSG","Email id is null or empty!");
            response.put("enableOTP", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        response = forgetPasswordService.verifyEmailIdAndSendOtp(emailId.trim());
        logger.debug("verifyEmailAndSendOTP >> response :: [{}] ",response);
        if("success".equalsIgnoreCase((String)response.get("RESULT"))){
            logger.debug("Success >> setting enableOTP value...");
            response.put("enableOTP", true);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verification")
    public ModelAndView verifyOTP(@RequestParam("emailId") String emailId, @RequestParam("otpCode") String otp,
            RedirectAttributes redirectAttributes) {
        logger.debug("verifyOTP >> emailId: [{}]", emailId);
        logger.debug("verifyOTP >> otp: [{}]", otp);
        ModelAndView mv = new ModelAndView();
        Map<String, Object>  retMap = new HashMap<>();
        if (emailId == null || emailId.trim().isEmpty() || otp == null || otp.trim().isEmpty()){
            retMap.put("RESULT","fail");
            retMap.put("MSG","Email id or OTP is null or empty!");
            retMap.put("enableOTP", false);
            mv.setViewName("public/forget-password");
            ModelAndViewUtil.addMVObject(retMap,mv);
            return mv;
        }

        retMap = forgetPasswordService.verifyOTP(emailId.trim(), otp.trim());

        if("success".equalsIgnoreCase((String) retMap.get("RESULT"))){
            redirectAttributes.addFlashAttribute("reqType", "reset");

            User user = (User) retMap.get("USER");
            logger.debug("verifyOTP >> user: [{}]", user);
            redirectAttributes.addFlashAttribute("USER", user);

            mv.setViewName("redirect:reset");
            return mv;
        }else {
            mv.setViewName("public/forget-password");
        }
        ModelAndViewUtil.addMVObject(retMap,mv);
        return mv;
    }

    @GetMapping("/reset")
    public ModelAndView getUpdatePasswordPage(
            @ModelAttribute("reqType") String reqType, @ModelAttribute("USER") User user) {

        logger.debug("getUpdatePasswordPage >> reqType : [{}]", reqType);
        logger.debug("getUpdatePasswordPage >> user : [{}]", user);
        ModelAndView mv = new ModelAndView();
        if(reqType == null || user == null || reqType.isEmpty()){
            mv.setViewName("redirect:/");
            return mv;
        }
        mv.setViewName("public/update-password");
        mv.addObject("reqType",reqType);
        mv.addObject("user",user);
        return mv;
    }

    @PostMapping("/reset")
    public ModelAndView resetPassword(@RequestParam("password1") String password, @RequestParam("userId") String userId) {
        logger.debug("userId: [{}] & password: [{}]", userId, password);
        return forgetPasswordService.resetPassword(userId, password);
    }

}
