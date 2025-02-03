package com.ebs.services;

import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

public interface UpdatePasswordService {
    ModelAndView getUpdatePasswordPage();

    ResponseEntity<?> validateUserPass(String oldPass);

    ModelAndView changeAdminPassword(String oldPass, String password);

    ModelAndView getUpdatePassword();

    ModelAndView changeConsumerPassword(String oldPass, String password);
}
