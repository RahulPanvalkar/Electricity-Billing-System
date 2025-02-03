package com.ebs.utils;

import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.CustomUserDetails;
import com.ebs.entities.User;
import com.ebs.services.AdminService;
import com.ebs.services.ConsumerService;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtil {

    private static final Logger logger = LoggerUtil.getLogger(UserUtil.class);

    private static AdminService adminService;
    private static ConsumerService consumerService;

    public UserUtil(@Lazy AdminService adminServiceObj, @Lazy ConsumerService consumerServiceObj) {
        adminService = adminServiceObj;
        consumerService = consumerServiceObj;
    }

    private static Optional<User> getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails userDetails) {
                // getting userCode from CustomUserDetails
                return Optional.of(userDetails.getUser());
            }
        }
        return Optional.empty();
    }

    public static <T> Optional<T> getLoggedInUser(Class<T> clazz) {
        // Get the logged-in user
        Optional<User> userOp = getLoggedInUser();
        if (userOp.isEmpty()) {
            return Optional.empty(); // No user logged in
        }

        User user = userOp.get();

        // Check if the requested class type is Consumer
        if (clazz.isAssignableFrom(Consumer.class)) {
            return consumerService.getConsumerById(user.getUserCode())
                    .map(clazz::cast); // Cast the result to the requested type
        } else if (clazz.isAssignableFrom(Admin.class)) {
            return adminService.findById(user.getUserCode())
                    .map(clazz::cast); // Cast the result to the requested type
        }

        // Add more conditions here if you have other types
        return Optional.empty(); // Default return for unsupported types
    }

}
