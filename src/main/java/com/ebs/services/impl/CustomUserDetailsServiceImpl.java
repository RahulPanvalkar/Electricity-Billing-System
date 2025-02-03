package com.ebs.services.impl;

import com.ebs.dao.UserDao;
import com.ebs.entities.CustomUserDetails;
import com.ebs.entities.User;
import com.ebs.services.CustomUserDetailsService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private static final Logger logger = LoggerUtil.getLogger(CustomUserDetailsServiceImpl.class);

    private final UserDao userDao;

    @Autowired
    public CustomUserDetailsServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    // to log in using both id and email
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("loadUserByUsername >> username : " + username);
        Optional<User> userOpt;
        if (username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            logger.debug("checking for email..");
            userOpt = userDao.findByEmailId(username);
            logger.debug("checking for email>> [{}]", userOpt);
        } else {
            try {
                logger.debug("checking for id..");
                userOpt = userDao.findById(username);
            } catch (NumberFormatException e) {
                throw new UsernameNotFoundException("Invalid user ID format");
            }
        }

        if (userOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        logger.debug("UserOpt : {}", userOpt);
        return new CustomUserDetails(userOpt.get());
    }
}

