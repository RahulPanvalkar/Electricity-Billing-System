package com.ebs.services;

import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ConsumerService {

    List<Consumer> getConsumers();

    ModelAndView getConsumers(int page, int size);

    Consumer getConsumer(String username);

    Optional<Consumer> getConsumerById(String consumerNum);

    HashMap<String, Object> addConsumer(Consumer consumer);

    ModelAndView getBillsByConsumerNum(int page, int size);

    HashMap<String, Object> updateConsumer(Consumer consumer);

    HashMap<String, String> updateConsumerInfo(User user, Consumer consumer);

    HashMap<String, String> deleteConsumer(String consumerNum);

    Optional<Consumer> updateConnection(Consumer consumer);

    ModelAndView getBillByConsumerNo();

}
