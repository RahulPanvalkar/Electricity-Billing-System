package com.ebs.services.impl;

import com.ebs.dao.AdminDao;
import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.services.AdminService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class AdminServiceImpl implements AdminService {

    private static final Logger logger = LoggerUtil.getLogger(AdminServiceImpl.class);

    private final AdminDao adminDao;
    private final UserService userService;

    @Autowired
    public AdminServiceImpl(AdminDao adminDao, UserService userService) {
        this.adminDao = adminDao;
        this.userService = userService;
    }

    public Optional<Admin> findById(String id) {
        return adminDao.findById(id);
    }

    public Map<String, Object> getAdminById(String id) {
        Map<String, Object> returnMap = new HashMap<>();
        Optional<Admin> adminOp = adminDao.findById(id);
        if (adminOp.isEmpty()) {
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Admin not found!");
            return returnMap;
        }

        returnMap.put("admin", adminOp.get());
        returnMap.put("RESULT", "success");
        returnMap.put("MSG", "Admin Found!");
        return returnMap;
    }

    public Admin getAdmin(String username) {
        Optional<Admin> adminOpt;
        if (username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            adminOpt = adminDao.findByEmailId(username);
        } else {
            try {
                adminOpt = adminDao.findById(username);
            } catch (NumberFormatException e) {
                throw new UsernameNotFoundException("Invalid user ID format");
            }
        }

        if (adminOpt.isEmpty()) {
            throw new UsernameNotFoundException("Invalid user");
        }
        logger.debug("adminOpt : {}", adminOpt);
        return adminOpt.get();
    }

    @Transactional
    public HashMap<String, Object> addAdmin(Admin admin) {
        logger.debug("admin :: {}", admin);
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            // Check if the admin already exists
            Admin existingAdmin = adminDao.getAdminByMobOrEmail(admin.getMobNumber(), admin.getEmailId());
            if (existingAdmin != null) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Admin already exists");
                return returnMap;
            }

            admin.setId("1");
            // Insert in admin table
            adminDao.save(admin);

            Admin updatedAdmin = adminDao.getAdminByMobOrEmail(admin.getMobNumber(), admin.getEmailId());
            logger.debug("updatedAdmin :: {}", updatedAdmin);

            String adminId = updatedAdmin.getId();
            logger.debug("adminId :: {}", adminId);

            // Check if the admin was added successfully
            if (adminId == null || !adminId.trim().startsWith("ADM")) {
                logger.error("Invalid value assigned to id field :: [{}]", adminId);
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                return returnMap;
            }

            // Insert in users table
            userService.addUser(updatedAdmin);

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Admin added successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Transactional
    public void createDefaultAdminUser(Admin admin) {
        logger.debug("admin :: {}", admin);
        try {
            // Insert in admin table
            adminDao.save(admin);
            // to get updated admin with assigned id
            Admin savedAdmin = adminDao.getAdminByMobOrEmail(admin.getMobNumber(), admin.getEmailId());
            logger.debug("updatedAdmin :: {}", savedAdmin);

            String adminId = savedAdmin.getId();
            logger.debug("adminId :: {}", adminId);

            // Check if the admin was added successfully
            if (adminId == null || !adminId.trim().startsWith("ADM")) {
                logger.error("Invalid value assigned to id field :: [{}]", adminId);
                throw new RuntimeException("Invalid value assigned to id field");
            }

            // Insert in users table
            userService.addUser(savedAdmin);

        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public HashMap<String, String> updateAdminInfo(User user) {
        logger.debug("user :: {}", user);
        HashMap<String, String> returnMap = new HashMap<>();

        try {
            Optional<Admin> adminOptional = adminDao.findById(user.getUserCode());
            if (adminOptional.isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Admin not found!");
                return returnMap;
            }

            String[] fullNameArray = user.getName().split("\\s+");
            logger.debug("fullNameArray :: {}", Arrays.toString(fullNameArray));

            Admin admin = adminOptional.get();
            admin.setFirstName(fullNameArray[0]);
            admin.setLastName(fullNameArray[1]);
            admin.setEmailId(user.getEmailId());
            admin.setMobNumber(user.getMobNumber());

            Admin updatedAdmin = adminDao.save(admin);
            logger.debug("updatedAdmin :: {}", updatedAdmin);

            returnMap = userService.updateNameEmailAndMob(user);
            String result = returnMap.get("RESULT");
            logger.debug("updateAdminInfo >> returnMap >> result :: [{}]", result);
            if (!"success".equalsIgnoreCase(result)) {
                return returnMap;
            }

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Profile updated successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Override
    public HashMap<String, String> deleteAdmin(String id) {
        logger.debug("adminId :: [{}]", id);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            if (id == null || id.trim().isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Admin Id");
                return returnMap;
            }

            Optional<Admin> adminOpt = adminDao.findById(id.trim());

            if (adminOpt.isPresent()) {
                Admin admin = adminOpt.get();
                logger.debug("admin :: [{}]", admin);
                adminDao.delete(admin);

                Optional<User> userOptional = userService.findUserByCode(id.trim());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    userService.deleteUser(user);
                    returnMap.put("RESULT", "success");
                    returnMap.put("MSG", "Admin deleted successfully!");
                } else {
                    logger.warn("User not found for userCode : {}", id);
                    throw new RuntimeException("User not found");
                }

            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Admin not found!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT","fail");
            returnMap.put("MSG","Something went wrong!");
        }
        return returnMap;
    }


}