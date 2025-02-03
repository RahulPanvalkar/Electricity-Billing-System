package com.ebs.dao.impl;

import com.ebs.dao.ConsumerDao;
import com.ebs.entities.Consumer;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ConsumerDaoImpl implements ConsumerDao {

    private final HibernateTemplate hibernateTemplate;
    private static final Logger logger = LoggerUtil.getLogger(ConsumerDaoImpl.class);

    public ConsumerDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public List<Consumer> findAll() {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Consumer";
            Query<Consumer> query = session.createQuery(hql, Consumer.class);
            return query.list();
        });
    }

    public Optional<Consumer> findById(String consumerNum) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM Consumer c WHERE c.consumerNum = :consumerNum";
            Query<Consumer> query = session.createQuery(sql, Consumer.class);
            query.setParameter("consumerNum", consumerNum);
            return query.uniqueResultOptional();
        });
    }

    public Optional<Consumer> getConsumerByMobOrEmail(String mobNumber, String emailId) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM Consumer c WHERE c.mobNumber = :mobNumber AND c.emailId = :emailId";
            Query<Consumer> query = session.createQuery(sql, Consumer.class);
            query.setParameter("mobNumber", mobNumber);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    public void save(Consumer consumer) {
        hibernateTemplate.save(consumer);
    }

    public void delete(Consumer consumer) {
        hibernateTemplate.delete(consumer);
    }

    public Optional<Consumer> findByEmailId(String emailId) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM Consumer c WHERE c.emailId = :emailId";
            Query<Consumer> query = session.createQuery(sql, Consumer.class);
            query.setParameter("emailId", emailId);
            return query.uniqueResultOptional();
        });
    }

    @Transactional
    public int updateNameEmailAndMob(Consumer consumer) {
        Integer result = hibernateTemplate.execute(session -> {
            String sql = "UPDATE Consumer SET full_name = ?1, email_id = ?2, mob_number = ?3 WHERE consumer_num = ?4";
            Query<?> query = session.createQuery(sql);
            query.setParameter(1, consumer.getFullName());
            query.setParameter(2, consumer.getEmailId());
            query.setParameter(3, consumer.getMobNumber());
            query.setParameter(4, consumer.getConsumerNum());
            return query.executeUpdate();
        });
        return result != null ? result : 0; // Handle null return
    }

    public int updateConnection(Consumer consumer) {
        Integer result = hibernateTemplate.execute(session -> {
            String sql = "UPDATE Consumer SET conn_id = ?1 WHERE consumer_num = ?2";
            Query<?> query = session.createQuery(sql);
            query.setParameter(1, consumer.getConnId());
            query.setParameter(2, consumer.getConsumerNum());
            return query.executeUpdate();
        });
        return result != null ? result : 0; // Handle null return
    }
}
