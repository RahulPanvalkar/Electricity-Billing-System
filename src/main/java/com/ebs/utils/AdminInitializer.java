package com.ebs.utils;

import com.ebs.entities.Admin;
import com.ebs.entities.User;
import com.ebs.services.AdminService;
import com.ebs.services.UserService;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AdminInitializer implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger logger = LoggerUtil.getLogger(AdminInitializer.class);

    private final AdminService adminService;
    private final UserService userService;

    public AdminInitializer(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try{
            // Check if the admin user already exists
            Optional<User> adminUserOp = userService.findUserByEmail("default.user@gmail.com");
            logger.debug("adminUser:: [{}]", adminUserOp);
            if (adminUserOp.isEmpty()) {
                // Create the default admin user
                Admin admin = new Admin();
                admin.setId("1");
                admin.setFirstName("Default");
                admin.setLastName("User");
                admin.setEmailId("default.user@gmail.com");
                admin.setMobNumber("1234567890");
                admin.setAddress("");

                // Save the admin user
                adminService.createDefaultAdminUser(admin);
                logger.info("Default admin user created.");
            } else {
                logger.info("Admin user already exists.");
            }
        } catch (Exception ex) {
            logger.error("Failed to create admin user");
            ex.printStackTrace();
        }

    }
}
