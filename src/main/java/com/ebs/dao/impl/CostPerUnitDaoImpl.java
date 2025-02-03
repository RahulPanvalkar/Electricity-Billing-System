package com.ebs.dao.impl;

import com.ebs.dao.CostPerUnitDao;
import com.ebs.entities.CostPerUnit;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class CostPerUnitDaoImpl implements CostPerUnitDao {

    private static final Logger logger = LoggerUtil.getLogger(CostPerUnitDaoImpl.class);

    private final HibernateTemplate hibernateTemplate;

    public CostPerUnitDaoImpl(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    public Optional<CostPerUnit> findById(int id) {
        return hibernateTemplate.execute(session -> {
            String hql = "SELECT c FROM CostPerUnit c WHERE c.id = :id";
            Query<CostPerUnit> query = session.createQuery(hql, CostPerUnit.class);
            query.setParameter("id", id);
            return query.uniqueResultOptional();
        });
    }

    @Transactional
    public int updateCostPerUnit(CostPerUnit costPerUnit) {
        Integer result = hibernateTemplate.execute(session -> {
            String hql = "UPDATE CostPerUnit c SET c.unitsZeroToHundred = ?1, c.unitsOneHundredOneToThreeHundred = ?2, " +
                    "c.unitsThreeHundredOneToFiveHundred = ?3, c.unitsFiveHundredOneAndAbove = ?4 WHERE c.id = 1";
            Query<?> query = session.createQuery(hql);
            query.setParameter(1, costPerUnit.getUnitsZeroToHundred());
            query.setParameter(2, costPerUnit.getUnitsOneHundredOneToThreeHundred());
            query.setParameter(3, costPerUnit.getUnitsThreeHundredOneToFiveHundred());
            query.setParameter(4, costPerUnit.getUnitsFiveHundredOneAndAbove());
            return query.executeUpdate();
        });
        return result != null ? result : 0; // Handle null return
    }
}
