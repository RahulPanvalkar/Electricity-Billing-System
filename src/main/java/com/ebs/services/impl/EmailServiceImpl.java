package com.ebs.services.impl;

import com.ebs.services.EmailService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Logger logger = LoggerUtil.getLogger(EmailServiceImpl.class);

    private static final SecureRandom random = new SecureRandom();
    private static final int OTP_LENGTH = 6;

    @Autowired
    private JavaMailSender mailSender;

    @Value("$(spring.mail.username)")
    private String senderEmailId;


    public String generateOtp() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    public boolean sendOtpEmail(String receiverEmailId, String otp) {
        String subject = "Verification OTP";
        String emailBody =
                "<div style=\"max-width:516px;min-width:220px;border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px\"" +
                        "    align=\"center\">" +
                        "    <div style=\"font-family:Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word\">" +
                        "        <div style=\"font-size:24px\">Verify your account</div>" +
                        "    </div>" +
                        "    <div" +
                        "        style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:left\">" +
                        "        <br>Your verification code is:<br>" +
                        "        <div style=\"text-align:center;font-size:36px;margin-top:20px;line-height:44px\">" + otp + "</div><br>This code will expire in 5 minutes.<br>" +
                        "    </div>" +
                        "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(receiverEmailId);
            helper.setSubject(subject);
            helper.setText(emailBody, true);
            mailSender.send(message);
            logger.info("OTP email sent successfully to [{}]", receiverEmailId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to send OTP email to [{}]", receiverEmailId, e);
            return false;
        }
    }

    public boolean sendRegOtp(String receiverEmailId, String otp) {
        String subject = "Verification OTP";
        String emailBody =
                "<div style=\"max-width:516px;min-width:220px;border-style:solid;border-width:thin;border-color:#dadce0;border-radius:8px;padding:40px 20px\"" +
                        "    align=\"center\">" +
                        "    <div style=\"font-family:Arial,sans-serif;border-bottom:thin solid #dadce0;color:rgba(0,0,0,0.87);line-height:32px;padding-bottom:24px;text-align:center;word-break:break-word\">" +
                        "        <div style=\"font-size:24px\">Verify Your Email</div>" +
                        "    </div>" +
                        "    <div" +
                        "        style=\"font-family:Roboto-Regular,Helvetica,Arial,sans-serif;font-size:14px;color:rgba(0,0,0,0.87);line-height:20px;padding-top:20px;text-align:left\">" +
                        "        <br>Your Registration OTP is:<br>" +
                        "        <div style=\"text-align:center;font-size:36px;margin-top:20px;line-height:44px\">" + otp + "</div><br>This OTP will expire in 5 minutes.<br>" +
                        "    </div>" +
                        "</div>";

        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(receiverEmailId);
            helper.setSubject(subject);
            helper.setText(emailBody, true);
            mailSender.send(message);
            logger.info("OTP email sent successfully to {}", receiverEmailId);
            return true;
        } catch (MessagingException e) {
            logger.error("Failed to send OTP email to {}", receiverEmailId, e);
            return false;
        }
    }

}
