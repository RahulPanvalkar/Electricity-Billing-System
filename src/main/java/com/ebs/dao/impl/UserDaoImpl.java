package com.ebs.dao.impl;

import com.ebs.dao.UserDao;
import com.ebs.entities.User;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class UserDaoImpl implements UserDao {

    private final HibernateTemplate hibernateTemplate;

    private static final Logger logger = LoggerUtil.getLogger(UserDaoImpl.class);

    @Autowired
    public UserDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<User> findById(String userId) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM User u WHERE u.userId = :userId";
            Query<User> query = session.createQuery(sql, User.class);
            query.setParameter("userId", userId);
            return query.uniqueResultOptional();
        });
    }

    // Find user by ID and password
    public Optional<User> findUserByIdAndPassword(String userId, String password) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM User u WHERE u.userId = :userId AND u.password = :password";
            Query<User> query = session.createQuery(sql, User.class);
            query.setParameter("id", userId);
            query.setParameter("password", password);
            return query.uniqueResultOptional();
        });
    }

    // Find admin user by ID, password, and username type
    public Optional<User> findAdminUserByIdAndPassword(String userId, String password, String userNameType) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM User u WHERE (CASE :userNameType WHEN 'email' THEN u.emailId = :userId ELSE u.userId = :userId END) AND u.password = :password AND u.userType = 'A'";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("userId", userId);
            query.setParameter("password", password);
            query.setParameter("userNameType", userNameType);
            return query.uniqueResultOptional();
        });
    }

    // Find consumer user by ID, password, and username type
    public Optional<User> findConsumerUserByIdAndPassword(String userId, String password, String userNameType) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM User u WHERE (CASE :userNameType WHEN 'email' THEN u.emailId = :userId ELSE u.userId = :userId END) AND u.password = :password AND u.userType = 'C'";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("userId", userId);
            query.setParameter("password", password);
            query.setParameter("userNameType", userNameType);
            return query.uniqueResultOptional();
        });
    }

    // Find user by code
    public Optional<User> findUserByCode(String userCode) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM User u WHERE u.userCode = :userCode";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("userCode", userCode);
            return query.uniqueResultOptional();
        });
    }

    // Find user by email
    public Optional<User> findByEmailId(String emailId) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM User u WHERE u.emailId = :emailId";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    // Update name, email, and mobile number
    @Transactional
    public int updateNameEmailAndMob(String userCode, String name, String email, String mobile) {
        Integer result = hibernateTemplate.execute(session -> {
            String sql = "UPDATE User SET name = ?1, email_id = ?2, mob_number = ?3 WHERE user_code = ?4";
            Query<?> query = session.createQuery(sql);
            query.setParameter(1, name);
            query.setParameter(2, email);
            query.setParameter(3, mobile);
            query.setParameter(4, userCode);
            return query.executeUpdate();
        });
        return result != null ? result : 0; // Handle null return
    }

    // Update password
    @Transactional
    public int updatePassword(String userId, String password) {
        Integer result = hibernateTemplate.execute(session -> {
            String hql = "UPDATE User SET password = :password WHERE user_id = :userId";
            Query<?> query = session.createQuery(hql);
            query.setParameter("password", password);
            query.setParameter("userId", userId);
            return query.executeUpdate();
        });
        return result != null ? result : 0; // Handle null return
    }

    @Transactional
    public void save(User user) {
        hibernateTemplate.save(user);
    }

    @Transactional
    public void delete(User user) {
        hibernateTemplate.delete(user);
    }

}
