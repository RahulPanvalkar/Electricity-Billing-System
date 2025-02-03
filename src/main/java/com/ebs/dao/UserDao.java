package com.ebs.dao;

import com.ebs.entities.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> findById(String userId);

    // Find user by ID and password
    Optional<User> findUserByIdAndPassword(String userId, String password);

    // Find admin user by ID, password, and username type
    Optional<User> findAdminUserByIdAndPassword(String userId, String password, String userNameType);

    // Find consumer user by ID, password, and username type
    Optional<User> findConsumerUserByIdAndPassword(String userId, String password, String userNameType);

    // Find user by code
    Optional<User> findUserByCode(String userCode);

    // Find user by email
    Optional<User> findByEmailId(String emailId);

    // Update name, email, and mobile number
    int updateNameEmailAndMob(String userCode, String name, String email, String mobile);

    // Update password
    int updatePassword(String userId, String password);

    void save(User user);

    void delete(User user);
}
