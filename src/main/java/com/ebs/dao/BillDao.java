package com.ebs.dao;

import com.ebs.entities.Bill;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface BillDao {
    Optional<Bill> findById(String billNo);

    Optional<Bill> findPendingBillByConsumerNum(String consumerNum);

    Optional<Bill> findPreviousBill(String consumerNum, Date currentDate);

    Optional<Bill> getBillDataByConsumerNoAndStatus(String consumerNum);

    Optional<Bill> getBillByConsumerNumAndBillDate(String consumerNum, Date billDate);

    List<Bill> getAllBillsByConsumerNum(String consumerNum);

    void save(Bill bill);

    void delete(Bill bill);
}
