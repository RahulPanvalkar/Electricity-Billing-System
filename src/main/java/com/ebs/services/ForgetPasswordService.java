package com.ebs.services;

import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public interface ForgetPasswordService {
    Map<String, Object> verifyEmailIdAndSendOtp(String emailId);

    Map<String, Object> verifyOTP(String emailId, String otp);

    ModelAndView resetPassword(String userId, String password);

}
