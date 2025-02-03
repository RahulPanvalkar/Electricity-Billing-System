package com.ebs.dao;

import com.ebs.entities.Consumer;

import java.util.List;
import java.util.Optional;

public interface ConsumerDao {

    List<Consumer> findAll();

    Optional<Consumer> findById(String consumerNum);

    Optional<Consumer> getConsumerByMobOrEmail(String mobNumber, String emailId);

    void save(Consumer consumer);

    void delete(Consumer consumer);

    Optional<Consumer> findByEmailId(String emailId);

    int updateNameEmailAndMob(Consumer consumer);

    int updateConnection(Consumer consumer);
}
