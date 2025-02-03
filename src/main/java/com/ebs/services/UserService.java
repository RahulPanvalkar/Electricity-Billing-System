package com.ebs.services;

import com.ebs.entities.Admin;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.entities.UserRegistration;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Optional;

public interface UserService {

    Optional<User> findAdminUserByIdAndPassword(String userName, String password);

    Optional<User> findConsumerUserByIdAndPassword(String userName, String password);

    Optional<User> findUserByCode(String userCode);

    Optional<User> findUserByEmail(String emailId);

    Optional<User> updateUser(User user);

    Optional<User> findById(String userId);

    HashMap<String, String> updateNameEmailAndMob(User user);

    void addUser(Consumer consumer);

    void addUser(Admin admin);

    void addUser(User user, UserRegistration userReg, String consumerNum);

    HashMap<String, String> updatePassword(User user);

    ModelAndView getUsers(int page, int size);

    void deleteUser(User user);

}
