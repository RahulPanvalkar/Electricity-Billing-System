package com.ebs.utils;

import com.ebs.entities.Bill;
import com.ebs.entities.Consumer;
import org.apache.logging.log4j.Logger;
import org.hibernate.query.Query;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaginationUtil {

    private final HibernateTemplate hibernateTemplate;

    private static final Logger logger = LoggerUtil.getLogger(PaginationUtil.class);

    public PaginationUtil(HibernateTemplate hibernateTemplate) {
        this.hibernateTemplate = hibernateTemplate;
    }

    // Fetch paginated data
    public <T> List<T> findAll(int pageNumber, int pageSize, Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class type cannot be null.");
        }
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than 0.");
        }

        logger.debug("pageNum: [{}], pageSize: [{}], class: [{}]",pageNumber, pageSize, clazz.getSimpleName());
        return hibernateTemplate.execute(session -> {
            String className = clazz.getAnnotation(javax.persistence.Entity.class) != null
                    && !clazz.getAnnotation(javax.persistence.Entity.class).name().isEmpty()
                    ? clazz.getAnnotation(javax.persistence.Entity.class).name()
                    : clazz.getSimpleName(); // Use custom entity name if specified

            String hql = "FROM " + className;
            Query<T> query = session.createQuery(hql, clazz);
            query.setFirstResult((pageNumber - 1) * pageSize); // Pagination starts from 0
            query.setMaxResults(pageSize);
            return query.getResultList();
        });
    }


    // Count total entities
    public long countEntities(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Class type cannot be null.");
        }

        // Get the entity name (custom or class simple name)
        String entityName = clazz.getAnnotation(javax.persistence.Entity.class) != null
                && !clazz.getAnnotation(javax.persistence.Entity.class).name().isEmpty()
                ? clazz.getAnnotation(javax.persistence.Entity.class).name()
                : clazz.getSimpleName();

        Long result = hibernateTemplate.execute(session -> {
            String hql = "SELECT COUNT(e) FROM " + entityName + " e";
            Query<Long> query = session.createQuery(hql, Long.class);
            return query.uniqueResult(); // May return null
        });
        logger.debug("Total {} entities: [{}]", entityName, result);
        return result != null ? result : 0L; // Default to 0 if result is null
    }

    // Fetch paginated consumers list
    public List<Bill> findBillsByConsumerNum(int pageNumber, int pageSize, Consumer consumer) {
        if (consumer == null) {
            throw new IllegalArgumentException("Consumer data cannot be null.");
        }
        if (pageNumber < 1 || pageSize < 1) {
            throw new IllegalArgumentException("Page number and page size must be greater than 0.");
        }
        logger.debug("pageNum: [{}], pageSize: [{}]",pageNumber, pageSize);
        return hibernateTemplate.execute(session -> {
            String hql = "FROM Bill b WHERE b.consumerNum = :consumerNum";
            Query<Bill> query = session.createQuery(hql, Bill.class);
            query.setParameter("consumerNum", consumer.getConsumerNum());
            query.setFirstResult((pageNumber - 1) * pageSize); // 0-based index adjustment
            query.setMaxResults(pageSize);
            return query.list();
        });
    }

}
