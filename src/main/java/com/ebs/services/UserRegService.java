package com.ebs.services;

import com.ebs.entities.User;
import com.ebs.entities.UserRegistration;

import java.util.HashMap;
import java.util.Map;

public interface UserRegService {
    Map<String, Object> verifyEmailIdAndSendOtp(UserRegistration userReg);

    HashMap<String, Object> addConsumer(User user, String opt);
}
