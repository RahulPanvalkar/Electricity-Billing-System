package com.ebs.config;


import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.services.AdminService;
import com.ebs.services.ConsumerService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ConsumerService consumerService;
    private final AdminService adminService;
    private static final Logger logger = LoggerUtil.getLogger(CustomAuthenticationSuccessHandler.class);

    @Autowired
    public CustomAuthenticationSuccessHandler(@Lazy ConsumerService consumerService, @Lazy AdminService adminService) {
        this.consumerService = consumerService;
        this.adminService = adminService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // Get roles from the authentication object
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        roles.forEach((role) -> logger.info("Role : {}", role));
        // Get the authenticated user details (UserDetails object)
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();  // Use authentication.getPrincipal()

        if (roles.contains("ROLE_ADMIN")) {

            // Fetch admin details and set in session
            String username = userDetails.getUsername();  // Get the username from the UserDetails object
            Admin admin = adminService.getAdmin(username);  // Fetch admin from database using username

            response.sendRedirect("/admin/dashboard");
        }
        else if (roles.contains("ROLE_CONSUMER")) {
            response.sendRedirect("consumer/dashboard");

            // Fetch admin details and set in session
            String username = userDetails.getUsername();  // Get the username from the UserDetails object
            Consumer consumer = consumerService.getConsumer(username);  // Fetch consumer from database using username
        }
        else {
            response.sendRedirect("/"); // Default URL for other roles or if no role matches
        }
    }
}
