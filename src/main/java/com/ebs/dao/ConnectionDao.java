package com.ebs.dao;

import com.ebs.entities.EConnection;

import java.util.List;
import java.util.Optional;

public interface ConnectionDao {
    List<EConnection> findAll();

    Optional<EConnection> findById(String connId);

    EConnection findByConsumerNum(String consumerNum);

    EConnection getConnectionByConsumerNumOrMeterNo(String consumerNum, String meterNum);

    void save(EConnection eConnection);

    void delete(EConnection eConnection);
}
