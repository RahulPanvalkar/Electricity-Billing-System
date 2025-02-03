package com.ebs.dao.impl;

import com.ebs.dao.AdminDao;
import com.ebs.entities.Admin;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class AdminDaoImpl implements AdminDao {

    private final HibernateTemplate hibernateTemplate;
    private static final Logger logger = LoggerUtil.getLogger(AdminDaoImpl.class);

    public AdminDaoImpl(SessionFactory sessionFactory) {
        this.hibernateTemplate = new HibernateTemplate(sessionFactory);
    }

    public Optional<Admin> findById(String id) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Admin WHERE id = :id";
            Query<Admin> query = session.createQuery(hql, Admin.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        });
    }

    public Admin getAdminByMobOrEmail(String mobNumber, String emailId) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Admin WHERE mob_number = :mobNumber OR email_id = :emailId";
            Query<Admin> query = session.createQuery(hql, Admin.class);
            query.setParameter("mobNumber", mobNumber);
            query.setParameter("emailId", emailId);

            List<Admin> admins = query.list();
            return admins.isEmpty() ? null : admins.get(0);
        });
    }

    public Optional<Admin> findByEmailId(String emailId) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Admin WHERE email_id = :emailId";
            Query<Admin> query = session.createQuery(hql, Admin.class);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    @Transactional
    public Admin save(Admin admin) {
        hibernateTemplate.save(admin);
        return admin;
    }

    @Override
    public void delete(Admin admin) {
        hibernateTemplate.delete(admin);
    }

}
