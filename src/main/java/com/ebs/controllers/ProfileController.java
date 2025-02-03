package com.ebs.controllers;

import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.exception.InvalidSessionException;
import com.ebs.services.AdminService;
import com.ebs.services.ConsumerService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import com.ebs.utils.UserUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class ProfileController {

    private static final Logger logger = LoggerUtil.getLogger(ProfileController.class);

    private final AdminService adminService;
    private final ConsumerService consumerService;

    public ProfileController(AdminService adminService, ConsumerService consumerService) {
        this.adminService = adminService;
        this.consumerService = consumerService;
    }

    @GetMapping("admin/profile")
    public ModelAndView getProfilePage() {
        ModelAndView mv = new ModelAndView();
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Admin admin = loggedInUserOp.get();
        logger.debug("admin : [{}]", admin);
        mv.setViewName("common/profile");
        mv.addObject("admin", admin);
        return mv;
    }

    @GetMapping("admin/profile/edit")
    public ModelAndView getEditProfilePage() {
        ModelAndView mv = new ModelAndView();
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Admin admin = loggedInUserOp.get();
        logger.debug("admin : [{}]", admin);
        mv.setViewName("common/edit-profile");
        mv.addObject("admin", admin);
        return mv;
    }

    @PostMapping("admin/profile/edit")
    public ModelAndView updateUserInfo(
            @RequestParam("fullName") String fullName,
            @RequestParam("mobile") String mobNumber,
            @RequestParam("email") String emailId) {

        ModelAndView mv = new ModelAndView();
        Optional<Admin> loggedInUserOp = UserUtil.getLoggedInUser(Admin.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Admin admin = loggedInUserOp.get();
        logger.debug("admin : [{}]", admin);
        mv.setViewName("admin/UpdateCost");

        logger.debug("User fullName: [{}] ", fullName);
        logger.debug("User mobNumber: [{}]", mobNumber);
        logger.debug("User emailId: [{}]", emailId);

        // Create a User object to pass to the view
        User user = new User();
        user.setUserCode(admin.getId());
        user.setName(fullName);
        user.setEmailId(emailId);
        user.setMobNumber(mobNumber);

        HashMap<String, String> retMap = adminService.updateAdminInfo(user);
        logger.debug("updateUserInfo >> retMap :: [{}]", retMap);

        Optional<Admin> updatedAdminOpt = adminService.findById(admin.getId());
        if (updatedAdminOpt.isPresent()) {
            Admin updateAdmin = updatedAdminOpt.get();
            mv.addObject("admin", updateAdmin);
        }
        mv.setViewName("common/edit-profile");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

    // FOR CONSUMER
    @GetMapping("consumer/profile")
    public ModelAndView getConsumerProfilePage(Authentication authentication) {
        ModelAndView mv = new ModelAndView();
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Consumer consumer = loggedInUserOp.get();
        logger.debug("consumer : [{}]", consumer);
        mv.setViewName("common/profile");
        mv.addObject("consumer", consumer);
        return mv;
    }

    @GetMapping("consumer/profile/edit")
    public ModelAndView getConsumerEditProfilePage() {
        ModelAndView mv = new ModelAndView();
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Consumer consumer = loggedInUserOp.get();
        logger.debug("consumer : [{}]", consumer);
        mv.setViewName("common/edit-profile");
        mv.addObject("consumer", consumer);
        return mv;
    }

    @PostMapping("consumer/profile/edit")
    public ModelAndView updateConsumerInfo(
            @RequestParam("fullName") String fullName,
            @RequestParam("mobile") String mobNumber,
            @RequestParam("email") String emailId) {

        ModelAndView mv = new ModelAndView();
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }

        Consumer consumer = loggedInUserOp.get();
        logger.debug("consumer : [{}]", consumer);
        mv.setViewName("common/edit-profile");
        mv.addObject("consumer", consumer);

        logger.debug("User fullName: [{}] ", fullName);
        logger.debug("User mobNumber: [{}]", mobNumber);
        logger.debug("User emailId: [{}]", emailId);

        User user = new User();
        user.setUserCode(consumer.getConsumerNum());
        user.setName(fullName);
        user.setEmailId(emailId);
        user.setMobNumber(mobNumber);

        HashMap<String, String> retMap = consumerService.updateConsumerInfo(user, consumer);
        logger.debug("updateConsumerInfo >> retMap :: [{}]", retMap);

        Optional<Consumer> updatedConsumerOpt = consumerService.getConsumerById(consumer.getConsumerNum());
        if (updatedConsumerOpt.isPresent()) {
            Consumer updatedConsumer = updatedConsumerOpt.get();
            mv.addObject("consumer", updatedConsumer);
        }
        mv.setViewName("common/edit-profile");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }

}
