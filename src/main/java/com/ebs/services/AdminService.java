package com.ebs.services;

import com.ebs.entities.Admin;
import com.ebs.entities.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface AdminService {

    Optional<Admin> findById(String id);

    Map<String, Object> getAdminById(String id);

    Admin getAdmin(String username);

    HashMap<String, Object> addAdmin(Admin admin);

    void createDefaultAdminUser(Admin admin);

    HashMap<String, String> updateAdminInfo(User user);

    HashMap<String, String> deleteAdmin(String id);

}