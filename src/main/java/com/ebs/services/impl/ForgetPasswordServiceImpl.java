package com.ebs.services.impl;

import com.ebs.dao.UserDao;
import com.ebs.entities.User;
import com.ebs.services.EmailService;
import com.ebs.services.ForgetPasswordService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ForgetPasswordServiceImpl implements ForgetPasswordService {

    private static final Logger logger = LoggerUtil.getLogger(ForgetPasswordServiceImpl.class);

    private final UserService userService;
    private final UserDao userDao;
    private final EmailService emailService;

    @Autowired
    public ForgetPasswordServiceImpl(UserService userService, UserDao userDao, EmailService emailService) {
        this.userService = userService;
        this.userDao = userDao;
        this.emailService = emailService;
    }

    @Transactional
    public Map<String, Object> verifyEmailIdAndSendOtp(String emailId) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            Optional<User> optionalUser = userDao.findByEmailId(emailId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String otp = emailService.generateOtp();
                logger.debug("sendOtpEmail >> otp :: [{}]", otp);

                user.setVerCode(otp);
                LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
                user.setExpiresAt(Timestamp.valueOf(expiration));
                userDao.save(user);

                boolean emailSent = emailService.sendOtpEmail(emailId, otp);
                if (emailSent) {
                    returnMap.put("USER", user);
                    returnMap.put("RESULT", "Success");
                    returnMap.put("MSG", "OTP has been sent to your email.");
                } else {
                    returnMap.put("RESULT", "Fail");
                    returnMap.put("MSG", "Failed to send OTP. Please try again later.");
                }
            } else {
                returnMap.put("RESULT", "Fail");
                returnMap.put("MSG", "Invalid Email Id!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "Fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Transactional
    public Map<String, Object> verifyOTP(String emailId, String otp) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            Optional<User> optionalUser = userDao.findByEmailId(emailId);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String verCode = checkNullAndTrim(user.getVerCode());
                logger.debug("verifyOTP >> verCode :: [{}]", verCode);

                if (verCode.equals(otp)) {
                    Timestamp expiresAt = user.getExpiresAt();
                    Timestamp current = Timestamp.valueOf(LocalDateTime.now());
                    logger.debug("verifyOTP >> expiresAt :: [{}], current :: [{}]", expiresAt, current);
                    if (current.before(expiresAt)) {
                        returnMap.put("RESULT", "success");
                        returnMap.put("MSG", "OTP validation successful!");
                        returnMap.put("USER", user);
                    } else {
                        returnMap.put("RESULT", "fail");
                        returnMap.put("MSG", "OTP expired. Please try again.");
                    }
                } else {
                    returnMap.put("RESULT", "fail");
                    returnMap.put("MSG", "Invalid OTP. Please try again.");
                }
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Email Id!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    public ModelAndView resetPassword(String userId, String password) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("reqType", "reset");

        HashMap<String, String> retMap = new HashMap<>();
        logger.debug("userId: [{}] & password: [{}]", userId, password);
        if (userId == null || userId.trim().isEmpty()) {
            retMap.put("RESULT", "fail");
            retMap.put("MSG", "Something went wrong!");
            mv.setViewName("public/update-password");
            ModelAndViewUtil.addMVObject(retMap, mv);
            return mv;
        } else if (password == null || password.trim().isEmpty()) {
            retMap.put("RESULT", "fail");
            retMap.put("MSG", "Invalid Password!");
            mv.setViewName("public/update-password");
            ModelAndViewUtil.addMVObject(retMap, mv);
            return mv;
        }

        // Create a User object to pass to the view
        Optional<User> userOptional = userDao.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(password);
            retMap = userService.updatePassword(user);
            retMap.put("MSG", "Password reset successfully!");
        } else {
            retMap.put("RESULT", "fail");
            retMap.put("MSG", "Something went wrong!");
            mv.setViewName("public/update-password");
            ModelAndViewUtil.addMVObject(retMap, mv);
            return mv;
        }

        logger.debug("retMap :: [{}]", retMap);
        mv.setViewName("public/update-password");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

    private String checkNullAndTrim(String str) {
        return (str == null || str.equals("null")) ? "" : str.trim();
    }

}
