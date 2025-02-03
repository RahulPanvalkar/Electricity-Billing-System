package com.ebs.services.impl;

import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.exception.InvalidSessionException;
import com.ebs.services.UpdatePasswordService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import com.ebs.utils.UserUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class UpdatePasswordServiceImpl implements UpdatePasswordService {

    private static final Logger logger = LoggerUtil.getLogger(UpdatePasswordServiceImpl.class);

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdatePasswordServiceImpl(UserService userService, @Lazy BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    @GetMapping("admin/password/update")
    public ModelAndView getUpdatePasswordPage() {
        ModelAndView mv = new ModelAndView("public/update-password");
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired!");
        }
        Admin admin = loggedInUserOp.get();
        mv.addObject("admin", admin);
        mv.addObject("userType", "admin");
        mv.addObject("reqType", "change");
        return mv;
    }

    @GetMapping("user/validate-password")
    public ResponseEntity<?> validateUserPass(@RequestParam("oldPassword") String oldPass) {
        logger.debug("validateUserPass >> oldPass :: [{}]", oldPass);
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired!");
        }

        if (oldPass == null || oldPass.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Password is null or an empty string."));
        }

        // getting user details
        Optional<User> userOptional = userService.findUserByCode(loggedInUserOp.get().getId());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            logger.debug("validateUserPass >> user :: [{}]", user);

            // Use password encoder to check if the old password matches
            if (passwordEncoder.matches(oldPass.trim(), user.getPassword())) {
                return ResponseEntity.ok(Map.of("status", "success", "message", "Passwords validated successfully."));
            }

            return ResponseEntity.badRequest().body(Map.of("status", "failed", "message", "Invalid old password."));
        }

        return ResponseEntity.status(404).body(Map.of("status", "failed", "message", "User not found."));
    }


    @PostMapping("admin/password/update/change")
    public ModelAndView changeAdminPassword(@RequestParam("oldPassword") String oldPass, @RequestParam("password1") String password) {
        ModelAndView mv = new ModelAndView("public/update-password");
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired!");
        }
        Admin admin = loggedInUserOp.get();
        mv.addObject("reqType", "change");
        mv.addObject("userType", "admin");
        mv.addObject("admin", admin);

        HashMap<String, String> retMap = new HashMap<>();
        logger.debug("password: [{}]", password);
        if (password == null || password.trim().isEmpty()) {
            retMap.put("RESULT", "fail");
            retMap.put("MSG", "Invalid Password!");
            ModelAndViewUtil.addMVObject(retMap, mv);
            return mv;
        }

        // Create a User object to pass to the view
        Optional<User> userOptional = userService.findUserByCode(admin.getId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Use passwordEncoder.matches() for comparing new and old password
            if (passwordEncoder.matches(oldPass, user.getPassword())) {
                user.setPassword(password);
                retMap = userService.updatePassword(user);
            } else {
                retMap.put("RESULT", "fail");
                retMap.put("MSG", "Invalid old Password!");
            }

        }

        logger.debug("retMap :: [{}]", retMap);
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }


    public ModelAndView getUpdatePassword() {
        ModelAndView mv = new ModelAndView();
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired!");
        }
        Consumer consumer = loggedInUserOp.get();
        mv.setViewName("public/update-password");
        mv.addObject("consumer", consumer);
        mv.addObject("userType", "consumer");
        mv.addObject("reqType", "change");
        return mv;
    }

    public ModelAndView changeConsumerPassword(String oldPass, String password) {
        ModelAndView mv = new ModelAndView("public/update-password");
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired!");
        }
        Consumer consumer = loggedInUserOp.get();
        mv.addObject("reqType", "change");
        mv.addObject("consumer", consumer);

        HashMap<String, String> retMap = new HashMap<>();
        logger.debug("old password: [{}]", oldPass);
        logger.debug("password: [{}]", password);
        if (oldPass == null || oldPass.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            retMap.put("RESULT", "fail");
            retMap.put("MSG", "Invalid Password!");
            ModelAndViewUtil.addMVObject(retMap, mv);
            return mv;
        }

        // Create a User object to pass to the view
        Optional<User> userOptional = userService.findUserByCode(consumer.getConsumerNum());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // Use passwordEncoder.matches() for comparing new and old password
            if (passwordEncoder.matches(oldPass, user.getPassword())) {
                user.setPassword(password);
                retMap = userService.updatePassword(user);
            } else {
                retMap.put("RESULT", "fail");
                retMap.put("MSG", "Invalid old Password!");
            }
        }

        logger.debug("retMap :: [{}]", retMap);
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

}
