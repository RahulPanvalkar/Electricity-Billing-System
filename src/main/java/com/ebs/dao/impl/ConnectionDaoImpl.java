package com.ebs.dao.impl;

import com.ebs.dao.ConnectionDao;
import com.ebs.entities.EConnection;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ConnectionDaoImpl implements ConnectionDao {

    private static final Logger logger = LoggerUtil.getLogger(ConnectionDaoImpl.class);

    private final HibernateTemplate hibernateTemplate;

    public ConnectionDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public List<EConnection> findAll() {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM EConnection";
            Query<EConnection> query = session.createQuery(hql, EConnection.class);
            return query.list();
        });
    }

    public Optional<EConnection> findById(String connId) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM EConnection c WHERE c.connId = :connId";
            Query<EConnection> query = session.createQuery(sql, EConnection.class);
            query.setParameter("connId", connId);
            return query.uniqueResultOptional();
        });
    }

    public EConnection findByConsumerNum(String consumerNum) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM EConnection c WHERE c.consumerNum = :consumerNum";
            Query<EConnection> query = session.createQuery(sql, EConnection.class);
            query.setParameter("consumerNum", consumerNum);
            return query.uniqueResult();
        });
    }

    public EConnection getConnectionByConsumerNumOrMeterNo(String consumerNum, String meterNum) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM EConnection c WHERE c.consumerNum= :consumerNum OR c.meterNum= :meterNum";
            Query<EConnection> query = session.createQuery(sql, EConnection.class);
            query.setParameter("consumerNum", consumerNum);
            query.setParameter("meterNum", meterNum);
            return query.uniqueResult();
        });
    }

    public void save(EConnection eConnection) {
        hibernateTemplate.save(eConnection);
    }

    public void delete(EConnection eConnection) {
        hibernateTemplate.delete(eConnection);
    }


}
