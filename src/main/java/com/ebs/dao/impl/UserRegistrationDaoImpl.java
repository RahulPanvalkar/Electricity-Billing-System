package com.ebs.dao.impl;

import com.ebs.dao.UserRegistrationDao;
import com.ebs.entities.UserRegistration;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public class UserRegistrationDaoImpl implements UserRegistrationDao {

    private final HibernateTemplate hibernateTemplate;

    private static final Logger logger = LoggerUtil.getLogger(UserDaoImpl.class);

    public UserRegistrationDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<UserRegistration> findUserRegByEmail(String emailId) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM UserRegistration ur WHERE ur.emailId = :emailId";
            Query<UserRegistration> query = session.createQuery(hql, UserRegistration.class);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    public Optional<UserRegistration> findNonActiveUserReg(String emailId) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM UserRegistration ur WHERE ur.emailId = :emailId AND ur.active='N'";
            Query<UserRegistration> query = session.createQuery(hql, UserRegistration.class);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    public void save(UserRegistration userReg) {
        hibernateTemplate.save(userReg);
    }
}
