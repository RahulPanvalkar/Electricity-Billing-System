package com.ebs.services;

import com.ebs.entities.EConnection;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface ConnectionService {

    Optional<EConnection> getConnectionById(String connId);

    EConnection getConnectionByConsumerNum(String consumerNo);

    List<EConnection> getAllConnections();

    ModelAndView getConnections(int page, int size);

    HashMap<String, String> addConnection(EConnection connection);

    HashMap<String, String> deleteConnection(String connId);

    HashMap<String, Object> updateConnection(EConnection connection);
}
