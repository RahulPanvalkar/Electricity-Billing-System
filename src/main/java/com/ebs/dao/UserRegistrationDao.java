package com.ebs.dao;

import com.ebs.entities.UserRegistration;

import java.util.Optional;

public interface UserRegistrationDao {
    Optional<UserRegistration> findUserRegByEmail(String emailId);

    Optional<UserRegistration> findNonActiveUserReg(String emailId);

    void save(UserRegistration userReg);
}
