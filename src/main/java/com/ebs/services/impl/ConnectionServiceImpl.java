package com.ebs.services.impl;

import com.ebs.controllers.AdminController;
import com.ebs.dao.ConnectionDao;
import com.ebs.entities.Consumer;
import com.ebs.entities.EConnection;
import com.ebs.services.ConnectionService;
import com.ebs.services.ConsumerService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.PaginationUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private static final Logger logger = LoggerUtil.getLogger(AdminController.class);

    private final ConnectionDao connectionDao;
    private final ConsumerService consumerService;
    private final PaginationUtil paginationUtil;

    public ConnectionServiceImpl(ConnectionDao connectionDao, ConsumerService consumerService, PaginationUtil paginationUtil) {
        this.connectionDao = connectionDao;
        this.consumerService = consumerService;
        this.paginationUtil = paginationUtil;
    }

    public Optional<EConnection> getConnectionById(String connId) {
        return connectionDao.findById(connId);
    }

    public EConnection getConnectionByConsumerNum(String consumerNo) {
        return connectionDao.findByConsumerNum(consumerNo);
    }

    public List<EConnection> getAllConnections() {
        return connectionDao.findAll();
    }

    public ModelAndView getConnections(int page, int size) {
        ModelAndView mv = new ModelAndView("admin/view-connections");
        try {
            int pageNumber = page > 0 ? page : 1; // Ensuring page starts from 1

            List<EConnection> connections = paginationUtil.findAll(pageNumber, size, EConnection.class);
            long totalUsers = paginationUtil.countEntities(EConnection.class); // Get total consumers

            int totalPages = (int) Math.ceil((double) totalUsers / size); // Calculate total pages

            mv.addObject("connections", connections); // Connections for the current page
            mv.addObject("currentPage", pageNumber);
            mv.addObject("totalPages", totalPages);
            mv.addObject("totalUsers", totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    @Transactional
    public HashMap<String, String> addConnection(EConnection connection) {
        logger.debug("addConnection >> connection :: {}", connection);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            // Check if the consumer already exists
            EConnection existingConnection = connectionDao.getConnectionByConsumerNumOrMeterNo(connection.getConsumerNum(), connection.getMeterNum());
            if (existingConnection != null) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Connection already exists");
                return returnMap;
            }

            Date currentDate = new Date(System.currentTimeMillis());
            connection.setStartDate(currentDate);
            connection.setConnId("000"); //provide value to avoid exception
            try {
                connectionDao.save(connection);
                EConnection newConnection = connectionDao.findByConsumerNum(connection.getConsumerNum());
                logger.debug("addConnection >> newConnection :: {}", newConnection);

                //updating consumer details, adding connId
                Optional<Consumer> consumerOptional = consumerService.getConsumerById(connection.getConsumerNum());
                if (consumerOptional.isPresent()) {
                    Consumer consumer = consumerOptional.get();
                    logger.debug("addConnection >> consumer :: {}", consumer);
                    logger.debug("addConnection >> connId :: {}", newConnection.getConnId());
                    consumer.setConnId(newConnection.getConnId());
                    logger.debug("addConnection >> consumer.connId :: {}", consumer.getConnId());
                    Optional<Consumer> updatedConsumerOp = consumerService.updateConnection(consumer);

                    if (updatedConsumerOp.isPresent()) {
                        if (updatedConsumerOp.get().getConnId() == null) {
                            throw new Exception("ConnId is null after save");
                        }
                    } else {
                        throw new Exception("Connection not updated");
                    }
                    logger.debug("addConnection >> ConnectionId added in Consumer table");
                }

            } catch (Exception e) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return returnMap;
            }

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Connection added successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }


    @Transactional
    public HashMap<String, String> deleteConnection(String connId) {
        logger.debug("deleteConnection >> connId :: [{}]", connId);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            if (connId == null || connId.trim().isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Consumer No");
                return returnMap;
            }

            Optional<EConnection> eConnOptional = connectionDao.findById(connId.trim());

            if (eConnOptional.isPresent()) {
                EConnection connection = eConnOptional.get();
                logger.debug("deleteConnection >> connection :: [{}]", connection);
                connectionDao.delete(connection);
                returnMap.put("RESULT", "success");
                returnMap.put("MSG", "Connection deleted successfully!");
            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Connection not found!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Transactional
    public HashMap<String, Object> updateConnection(EConnection connection) {
        logger.debug("connection :: {}", connection);
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            EConnection existingConn = connectionDao.findByConsumerNum(connection.getConsumerNum());
            if (existingConn == null) {
                logger.error("Connection not found..");
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Connection not found.");
                return returnMap;
            }

            existingConn.setFullName(connection.getFullName());
            existingConn.setAddress(connection.getAddress());
            existingConn.setMobNumber(connection.getMobNumber());
            existingConn.setMeterNum(connection.getMeterNum());
            existingConn.setType(connection.getType());
            logger.debug("existingConnection :: [{}]", existingConn);

            connectionDao.save(existingConn);
            Optional<EConnection> updatedConnOpt = connectionDao.findById(existingConn.getConnId());

            if (updatedConnOpt.isEmpty()) {
                logger.error("Connection not found..");
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                return returnMap;
            }

            EConnection updatedConn = updatedConnOpt.get();
            logger.debug("updatedConn :: {}", updatedConn);
            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Connection details updated!");
            returnMap.put("connection", updatedConn);
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }
}
