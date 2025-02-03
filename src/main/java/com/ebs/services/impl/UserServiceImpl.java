package com.ebs.services.impl;

import com.ebs.dao.UserDao;
import com.ebs.entities.*;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.PaginationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerUtil.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    @Lazy
    private final BCryptPasswordEncoder passwordEncoder;

    private final PaginationUtil paginationUtil;

    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder passwordEncoder, PaginationUtil paginationUtil) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.paginationUtil = paginationUtil;
    }

    public Optional<User> findAdminUserByIdAndPassword(String userName, String password) {
        if (userName == null || userName.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return Optional.empty();
        }
        String userNameType = "email";
        if (userName.matches("\\d+")) {
            userNameType = "id";
            if (userName.length() != 10) {
                return Optional.empty();
            }
        }
        return userDao.findAdminUserByIdAndPassword(userName, password, userNameType);
    }

    public Optional<User> findConsumerUserByIdAndPassword(String userName, String password) {
        if (userName == null || userName.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return Optional.empty();
        }
        String userNameType = "email";
        if (userName.matches("\\d+")) {
            userNameType = "id";
            if (userName.length() != 10) {
                return Optional.empty();
            }
        }
        return userDao.findConsumerUserByIdAndPassword(userName, password, userNameType);
    }

    public Optional<User> findUserByCode(String userCode) {
        return userDao.findUserByCode(userCode);
    }

    public Optional<User> findUserByEmail(String emailId) {
        return userDao.findByEmailId(emailId);
    }

    @Transactional
    public Optional<User> updateUser(User user) {
        logger.debug("user :: {}", user);
        // creating default password e.g def@1234 for Default User
        if (user == null) {
            logger.error("User value is null..");
            throw new RuntimeException("Invalid User");
        }

        userDao.save(user);

        Optional<User> newUserOp = userDao.findByEmailId(user.getEmailId());
        if (newUserOp.isEmpty()) {
            logger.error("newUser has not created..");
            throw new RuntimeException("User not saved");
        }
        logger.debug("newUser saved :: [{}]", newUserOp.get());
        return newUserOp;
    }

    @Transactional
    public HashMap<String, String> updateNameEmailAndMob(User user) {
        logger.debug("updateNameEmailAndMob >> user :: {}", user);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            int updatedRows = userDao.updateNameEmailAndMob(user.getUserCode(), user.getName(), user.getEmailId(), user.getMobNumber());

            if (updatedRows == 1) {
                returnMap.put("RESULT", "success");
                returnMap.put("MSG", "Data updated successfully!");
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! Please try again");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Transactional
    public void addUser(Consumer consumer) {
        logger.debug("consumer :: {}", consumer);
        String chars = consumer.getFullName().trim().toLowerCase().substring(0, 4);
        String numbers = consumer.getMobNumber().trim().substring(6);
        String password = chars + "@" + numbers;
        //logger.debug("password :: [{}]", password);
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(consumer);
        user.setUserId("1");
        user.setPassword(encodedPassword);
        userDao.save(user);
        Optional<User> newUserOp = userDao.findByEmailId(consumer.getEmailId());
        if (newUserOp.isEmpty()) {
            logger.error("newUser has not created..");
            throw new RuntimeException("User not saved");
        }
        logger.debug("newUser saved :: [{}]", newUserOp.get());
    }

    @Transactional
    public void addUser(Admin admin) {
        logger.debug("admin :: {}", admin);
        String chars = admin.getFirstName().trim().toLowerCase().substring(0, 3);
        String numbers = admin.getMobNumber().trim().substring(6);
        // creating default password e.g def@1234 for Default User
        String password = chars + "@" + numbers;
        logger.debug("password :: [{}]", password);
        String encodedPassword = passwordEncoder.encode(password);
        User user = new User(admin, encodedPassword);
        user.setUserId("1");
        userDao.save(user);
        Optional<User> newUserOp = userDao.findByEmailId(admin.getEmailId());
        if (newUserOp.isEmpty()) {
            logger.error("newUser has not created..");
            throw new RuntimeException("User not saved");
        }
        logger.debug("newUser saved :: [{}]", newUserOp.get());
    }

    @Transactional
    public void addUser(User user, UserRegistration userReg, String consumerNum) {
        logger.debug("consumerNum :: {}", consumerNum);
        logger.debug("userReg :: {}", userReg);
        logger.debug("user :: {}", user);
        // Encode password
        logger.debug("password :: [{}]", user.getPassword());
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setUserId("1");
        user.setPassword(encodedPassword);
        user.setName(userReg.getFirstName() + " " + userReg.getLastName());
        user.setMobNumber(userReg.getMobNumber());
        user.setUserType(UserType.C);
        user.setAddress(userReg.getAddress());
        user.setUserCode(consumerNum);
        logger.debug("updatedUser >> before saving :: {}", user);
        userDao.save(user);

        Optional<User> newUserOp = userDao.findByEmailId(user.getEmailId());
        if (newUserOp.isEmpty()) {
            logger.error("newUser has not created..");
            throw new RuntimeException("User not saved");
        }
        logger.debug("newUser :: [{}]", newUserOp.get());
    }

    @Transactional
    public HashMap<String, String> updatePassword(User user) {
        logger.debug("updatePassword >> user :: {}", user);
        // Encode password
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        HashMap<String, String> returnMap = new HashMap<>();
        try {
            int updatedRows = userDao.updatePassword(user.getUserId(), encodedPassword);

            if (updatedRows == 1) {
                returnMap.put("RESULT", "success");
                returnMap.put("MSG", "Password updated successfully!");
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! Please try again");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    public Optional<User> findById(String userId) {
        return userDao.findById(userId);
    }

    public ModelAndView getUsers(int page, int size) {
        ModelAndView mv = new ModelAndView("admin/view-users");
        try {
            int pageNumber = page > 0 ? page : 1; // Ensuring page starts from 1

            List<User> users = paginationUtil.findAll(pageNumber, size, User.class);
            long totalUsers = paginationUtil.countEntities(User.class); // Get total users

            int totalPages = (int) Math.ceil((double) totalUsers / size); // Calculate total pages
            logger.debug("totalPages : [{}]", totalPages);
            logger.debug("currentPage : [{}]", pageNumber);
            logger.debug("totalUsers : [{}]", totalUsers);

            mv.addObject("users", users); // Users for the current page
            mv.addObject("currentPage", pageNumber);
            mv.addObject("totalPages", totalPages);
            mv.addObject("totalUsers", totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

}
