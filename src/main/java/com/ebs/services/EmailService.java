package com.ebs.services;

public interface EmailService {
    public String generateOtp();

    boolean sendOtpEmail(String receiverEmailId, String otp);

    boolean sendRegOtp(String receiverEmailId, String otp);
}
