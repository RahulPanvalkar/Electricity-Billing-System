package com.ebs.dao;

import com.ebs.entities.Admin;

import java.util.Optional;

public interface AdminDao {
    Optional<Admin> findById(String id);

    Admin getAdminByMobOrEmail(String mobNumber, String emailId);

    Optional<Admin> findByEmailId(String emailId);

    Admin save(Admin admin);

    void delete(Admin admin);
}
