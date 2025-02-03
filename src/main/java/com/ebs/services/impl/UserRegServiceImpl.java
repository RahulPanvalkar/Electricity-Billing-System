package com.ebs.services.impl;

import com.ebs.dao.ConsumerDao;
import com.ebs.dao.UserRegistrationDao;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.entities.UserRegistration;
import com.ebs.services.EmailService;
import com.ebs.services.UserRegService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UserRegServiceImpl implements UserRegService {

    private static final Logger logger = LoggerUtil.getLogger(UserRegServiceImpl.class);

    private final UserService userService;
    private final UserRegistrationDao userRegDao;
    private final ConsumerDao consumerDao;
    private final EmailService emailService;

    public UserRegServiceImpl(UserService userService, UserRegistrationDao userRegDao, ConsumerDao consumerDao, EmailService emailService) {
        this.userService = userService;
        this.userRegDao = userRegDao;
        this.consumerDao = consumerDao;
        this.emailService = emailService;
    }

    @Transactional
    public Map<String, Object> verifyEmailIdAndSendOtp(UserRegistration userReg) {
        Map<String, Object> returnMap = new HashMap<>();

        try {
            if (userReg == null) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "User details not found!");
                return returnMap;
            }

            String emailId = userReg.getEmailId();
            logger.debug("emailId : [{}]", emailId);
            if (emailId == null || emailId.trim().isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Email Id!");
                return returnMap;
            }

            Optional<User> userOpt = userService.findUserByEmail(emailId);
            logger.debug("userOpt : [{}]", userOpt);
            if (userOpt.isPresent()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "User Already Registered!");
                return returnMap;
            }
            String otp = emailService.generateOtp();
            logger.debug("otp :: [{}]", otp);
            boolean emailSent = emailService.sendRegOtp(emailId, otp);
            if (emailSent) {
                Optional<UserRegistration> existingUserRegOpt = userRegDao.findUserRegByEmail(emailId);
                LocalDateTime expiration = LocalDateTime.now().plusMinutes(5);
                if (existingUserRegOpt.isEmpty()) {
                    userReg.setRegId("1");
                    userReg.setActive('N');
                    userReg.setVerCode(otp);
                    userReg.setExpiresAt(Timestamp.valueOf(expiration));
                    logger.debug("userReg >> [{}]", userReg);
                    userRegDao.save(userReg);
                } else {
                    UserRegistration existingUserReg = existingUserRegOpt.get();
                    existingUserReg.setVerCode(otp);
                    existingUserReg.setExpiresAt(Timestamp.valueOf(expiration));
                    logger.debug("existingUserReg >> [{}]", existingUserReg);
                    userRegDao.save(existingUserReg);
                }

                Optional<UserRegistration> userRegOpt = userRegDao.findNonActiveUserReg(emailId);
                logger.debug("userRegOpt :: [{}]", userRegOpt);
                if (userRegOpt.isPresent()) {
                    UserRegistration newUserReg = userRegOpt.get();
                    returnMap.put("userReg", newUserReg);
                    returnMap.put("RESULT", "success");
                    returnMap.put("MSG", "OTP has been sent to your email.");
                } else {
                    returnMap.put("RESULT", "Fail");
                    returnMap.put("MSG", "Registration failed, please try again");
                }

            } else {
                returnMap.put("RESULT", "Fail");
                returnMap.put("MSG", "Failed to send OTP. Please try again later.");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "Fail");
            returnMap.put("MSG", "Something went wrong!");
        }

        return returnMap;
    }


    @Transactional
    public HashMap<String, Object> addConsumer(User user, String opt) {
        logger.debug("user :: [{}]", user);
        HashMap<String, Object> returnMap = new HashMap<>();

        try {
            String emailId = user.getEmailId();
            Optional<Consumer> existingConsumerOp = consumerDao.getConsumerByMobOrEmail(user.getMobNumber(), user.getEmailId());
            if (existingConsumerOp.isPresent()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Consumer already exists");
                return returnMap;
            }

            Optional<UserRegistration> userRegOpt = userRegDao.findUserRegByEmail(emailId);
            if (userRegOpt.isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Email Id!");
                return returnMap;
            }

            UserRegistration userReg = userRegOpt.get();
            logger.debug("userReg :: [{}]", userReg);
            String verCode = userReg.getVerCode();
            logger.debug("verCode :: [{}]", verCode);

            if (verCode.equals(opt)) {
                Timestamp expiresAt = userReg.getExpiresAt();
                Timestamp current = Timestamp.valueOf(LocalDateTime.now());
                logger.debug("expiresAt :: [{}], current :: [{}]", expiresAt, current);
                if (current.before(expiresAt)) {
                    returnMap.put("RESULT", "success");
                    returnMap.put("MSG", "OTP validation successful!");
                } else {
                    returnMap.put("RESULT", "fail");
                    returnMap.put("MSG", "OTP expired. Please try again.");
                    return returnMap;
                }
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid OTP. Please try again.");
                return returnMap;
            }

            Consumer consumer = new Consumer(userReg);
            consumer.setConsumerNum("1");
            logger.debug("consumer :: {}", consumer);
            // Insert in consumer table
            consumerDao.save(consumer);
            Optional<Consumer> updatedConsumerOp = consumerDao.getConsumerByMobOrEmail(consumer.getMobNumber(), consumer.getEmailId());
            if (updatedConsumerOp.isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong while adding consumer! please try again");
                return returnMap;
            }

            String consumerNum = updatedConsumerOp.get().getConsumerNum();
            logger.debug("consumerNum :: {}", consumerNum);

            // Insert in users table
            userService.addUser(user, userReg, consumerNum);

            // Change active value to 'Y' in user_registration
            userReg.setActive('Y');
            logger.debug("userReg :: [{}]", userReg);
            userRegDao.save(userReg);

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Registration Successful!");
            returnMap.put("Register", true);
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }

        logger.debug("returnMap :: [{}]", returnMap);
        return returnMap;
    }

}
