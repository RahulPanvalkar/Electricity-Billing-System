package com.ebs.dao.impl;

import com.ebs.dao.BillDao;
import com.ebs.entities.Bill;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public class BillDaoImpl implements BillDao {

    private static final Logger logger = LoggerUtil.getLogger(BillDaoImpl.class);

    private final HibernateTemplate hibernateTemplate;

    public BillDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<Bill> findById(String billNo) {
        return hibernateTemplate.execute(session -> {
            String sql = "FROM Bill b WHERE b.billNo = :billNo";
            Query<Bill> query = session.createQuery(sql, Bill.class);
            query.setParameter("billNo", billNo);
            return query.uniqueResultOptional();
        });
    }

    public Optional<Bill> findPendingBillByConsumerNum(String consumerNum) {
        return hibernateTemplate.execute(session -> {
            String hql = "SELECT b FROM Bill b WHERE b.consumerNum = :consumerNum AND b.status = 'Pending'";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumerNum);
            return query.uniqueResultOptional();
        });
    }

    public Optional<Bill> findPreviousBill(String consumerNum, Date currentDate) {
        return hibernateTemplate.execute(session -> {
            String hql = "SELECT b FROM Bill b WHERE b.consumerNum = :consumerNum AND b.billDate < :currentBillDate ORDER BY b.billDate DESC";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumerNum);
            query.setParameter("currentBillDate", currentDate);
            query.setMaxResults(1); // fetch only first result
            return query.uniqueResultOptional();
        });
    }

    public Optional<Bill> getBillDataByConsumerNoAndStatus(String consumerNum) {
        return hibernateTemplate.execute(session -> {
            String hql = "SELECT b FROM Bill b WHERE b.consumerNum = :consumerNum AND b.status = 'Unpaid'";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumerNum);
            return query.uniqueResultOptional();
        });
    }

    public Optional<Bill> getBillByConsumerNumAndBillDate(String consumerNum, Date billDate) {
        return hibernateTemplate.execute(session -> {
            String hql = "SELECT b FROM Bill b WHERE b.consumerNum= :consumerNum AND b.billDate= :billDate";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumerNum);
            query.setParameter("billDate", billDate);
            return query.uniqueResultOptional();
        });
    }

    public List<Bill> getAllBillsByConsumerNum(String consumerNum) {
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Bill b WHERE b.consumerNum = :consumerNum";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumerNum);
            return query.list();
        });
    }

    public void save(Bill bill) {
        logger.debug("saving bill");
        hibernateTemplate.save(bill);
        hibernateTemplate.flush(); // Force Hibernate to execute the SQL
        logger.debug("bill saved successfully");
    }

    public void delete(Bill bill) {
        hibernateTemplate.delete(bill);
    }
}
