package com.ebs.controllers;

import com.ebs.services.UpdatePasswordService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/")
public class UpdatePasswordController {

    private static final Logger logger = LoggerUtil.getLogger(UpdatePasswordController.class);

    private final UpdatePasswordService updatePasswordService;

    public UpdatePasswordController(UpdatePasswordService updatePasswordService) {
        this.updatePasswordService = updatePasswordService;
    }

    @GetMapping("admin/password/update")
    public ModelAndView getUpdatePasswordPage() {
        return updatePasswordService.getUpdatePasswordPage();
    }

    @GetMapping("user/validate-password")
    public ResponseEntity<?> validateUserPass(@RequestParam("oldPassword") String oldPass) {
        return updatePasswordService.validateUserPass(oldPass);
    }


    @PostMapping("admin/password/change")
    public ModelAndView changeAdminPassword(@RequestParam("oldPassword") String oldPass, @RequestParam("password1") String password) {
        return updatePasswordService.changeAdminPassword(oldPass, password);
    }


    @GetMapping("consumer/password/update")
    public ModelAndView getUpdatePassword() {
        return updatePasswordService.getUpdatePassword();
    }

    @PostMapping("consumer/password/change")
    public ModelAndView changeConsumerPassword(@RequestParam("oldPassword") String oldPass, @RequestParam("password1") String password) {
        return updatePasswordService.changeConsumerPassword(oldPass, password);
    }

}
