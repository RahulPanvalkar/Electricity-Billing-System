package com.ebs.services.impl;

import com.ebs.dao.ConsumerDao;
import com.ebs.entities.Bill;
import com.ebs.entities.Consumer;
import com.ebs.entities.User;
import com.ebs.exception.InvalidSessionException;
import com.ebs.services.BillService;
import com.ebs.services.ConsumerService;
import com.ebs.services.UserService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.PaginationUtil;
import com.ebs.utils.UserUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerServiceImpl implements ConsumerService {
    private static final Logger logger = LoggerUtil.getLogger(ConsumerServiceImpl.class);

    private final ConsumerDao consumerDao;
    private final UserService userService;
    private final BillService billService;
    private final PaginationUtil paginationUtil;

    public ConsumerServiceImpl(ConsumerDao consumerDao, UserService userService, @Lazy BillService billService, PaginationUtil paginationUtil) {
        this.consumerDao = consumerDao;
        this.userService = userService;
        this.billService = billService;
        this.paginationUtil = paginationUtil;
    }

    public List<Consumer> getConsumers() {
        return consumerDao.findAll();
    }

    public ModelAndView getConsumers(int page, int size) {
        ModelAndView mv = new ModelAndView("admin/view-consumers");
        try {
            int pageNumber = page > 0 ? page : 1; // Ensuring page starts from 1

            List<Consumer> consumers = paginationUtil.findAll(pageNumber, size, Consumer.class);
            long totalUsers = paginationUtil.countEntities(Consumer.class); // Get total consumers

            int totalPages = (int) Math.ceil((double) totalUsers / size); // Calculate total pages

            mv.addObject("consumers", consumers); // Users for the current page
            mv.addObject("currentPage", pageNumber);
            mv.addObject("totalPages", totalPages);
            mv.addObject("totalUsers", totalUsers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
    }

    public Consumer getConsumer(String username) {
        Optional<Consumer> consumerOpt = null;
        if (username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            consumerOpt = consumerDao.findByEmailId(username);
        } else {
            try {
                consumerOpt = consumerDao.findById(username);
            } catch (NumberFormatException e) {
                throw new UsernameNotFoundException("Invalid user ID format");
            }
        }

        if (consumerOpt.isEmpty()) {
            throw new UsernameNotFoundException("Invalid user");
        }
        logger.debug("consumerOpt : {}", consumerOpt);
        return consumerOpt.get();
    }

    public Optional<Consumer> getConsumerById(String consumerNum) {
        return consumerDao.findById(consumerNum);
    }


    @Transactional
    public HashMap<String, Object> addConsumer(Consumer consumer) {
        logger.debug("addConsumer >> consumer :: {}", consumer);
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            // Check if the consumer already exists
            Optional<Consumer> existingConsumerOp = consumerDao.getConsumerByMobOrEmail(consumer.getMobNumber(), consumer.getEmailId());
            if (existingConsumerOp.isPresent()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Consumer already exists");
                return returnMap;
            }

            consumer.setConsumerNum("1");
            // Insert in consumer table
            consumerDao.save(consumer);

            Optional<Consumer> updatedConsumerOp = consumerDao.getConsumerByMobOrEmail(consumer.getMobNumber(), consumer.getEmailId());
            logger.debug("updatedConsumerOp :: {}", updatedConsumerOp);


            // Check if the consumer was added successfully
            if (updatedConsumerOp.isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                return returnMap;
            }

            String consumerNum = updatedConsumerOp.get().getConsumerNum();
            logger.debug("consumerNum :: {}", consumerNum);

            // Insert in users table
            userService.addUser(updatedConsumerOp.get());

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Consumer added successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    public ModelAndView getBillsByConsumerNum(int page, int size) {
        Optional<Consumer> loggedInUser = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUser.isEmpty()) {
            throw new InvalidSessionException("Invalid session! user not logged in");
        }
        Consumer consumer = loggedInUser.get();

        ModelAndView mv = new ModelAndView("consumer/bill-history");
        try {

            int pageNumber = page > 0 ? page : 1; // Ensuring page starts from 1

            List<Bill> bills = paginationUtil.findBillsByConsumerNum(pageNumber, size, consumer);
            long totalUsers = paginationUtil.countEntities(Bill.class); // Get total consumers

            int totalPages = (int) Math.ceil((double) totalUsers / size); // Calculate total pages

            mv.addObject("bills", bills); // bills for the current page
            mv.addObject("currentPage", pageNumber);
            mv.addObject("totalPages", totalPages);
            mv.addObject("totalUsers", totalUsers);
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
        }
        return mv;
    }

    @Transactional
    public HashMap<String, Object> updateConsumer(Consumer consumer) {
        logger.debug("updateConsumer >> consumer :: {}", consumer);
        HashMap<String, Object> returnMap = new HashMap<>();
        try {
            Optional<Consumer> existingConsumerOp = consumerDao.getConsumerByMobOrEmail(consumer.getMobNumber(), consumer.getEmailId());
            if (existingConsumerOp.isEmpty()) {
                logger.error("Consumer not found..");
                returnMap.put("status", "fail");
                returnMap.put("message", "Consumer not found.");
                return returnMap;
            }

            Consumer existingConsumer = existingConsumerOp.get();
            existingConsumer.setFullName(consumer.getFullName());
            existingConsumer.setAddress(consumer.getAddress());
            existingConsumer.setMobNumber(consumer.getMobNumber());
            existingConsumer.setEmailId(consumer.getEmailId());
            logger.debug("updateConsumer >> existingConsumer :: [{}]", existingConsumer);

            consumerDao.save(existingConsumer);
            Optional<Consumer> updatedConsumerOpt = consumerDao.findById(existingConsumer.getConsumerNum());

            if (updatedConsumerOpt.isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
                return returnMap;
            }

            Consumer updatedConsumer = updatedConsumerOpt.get();
            logger.debug("updateConsumer >> updatedConsumer :: [{}]", updatedConsumer);

            // Insert in users table
            Optional<User> userOpt = userService.findUserByCode(updatedConsumer.getConsumerNum());
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                user.setName(updatedConsumer.getFullName());
                user.setMobNumber(updatedConsumer.getMobNumber());
                user.setEmailId(updatedConsumer.getEmailId());
                user.setAddress(updatedConsumer.getAddress());

                Optional<User> updatedUserOp = userService.updateUser(user);
                if (updatedUserOp.isPresent()) {
                    logger.debug("updateConsumer >> updatedUser :: {}", updatedUserOp.get());
                    returnMap.put("RESULT", "success");
                    returnMap.put("MSG", "Consumer details updated!");
                    returnMap.put("consumer", updatedConsumer);
                }

            } else {
                logger.error("updateConsumer >> User not found..");
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! please try again");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }


    @Transactional
    public HashMap<String, String> updateConsumerInfo(User user, Consumer consumer) {
        logger.debug("user :: {}", user);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            consumer.setFullName(user.getName());
            consumer.setEmailId(user.getEmailId());
            consumer.setMobNumber(user.getMobNumber());

            int updatedRows = consumerDao.updateNameEmailAndMob(consumer);

            if (updatedRows != 1) {
                logger.error("Consumer data updated wrongly");
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Something went wrong! Please try again");
                return returnMap;
            }

            returnMap = userService.updateNameEmailAndMob(user);

            String result = returnMap.get("RESULT");
            logger.debug("updateConsumerInfo >> returnMap >> result :: [{}]", result);
            if (!"success".equalsIgnoreCase(result)) {
                return returnMap;
            }

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Profile updated successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong!");
        }
        return returnMap;
    }

    @Transactional
    public HashMap<String, String> deleteConsumer(String consumerNum) {
        logger.debug("consumerNum :: [{}]", consumerNum);
        HashMap<String, String> returnMap = new HashMap<>();
        try {
            if (consumerNum == null || consumerNum.trim().isEmpty()) {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Invalid Consumer No");
                return returnMap;
            }

            Optional<Consumer> consumerOptional = consumerDao.findById(consumerNum.trim());

            if (consumerOptional.isPresent()) {
                Consumer consumer = consumerOptional.get();
                logger.debug("consumer :: [{}]", consumer);
                consumerDao.delete(consumer);

                Optional<User> userOptional = userService.findUserByCode(consumerNum.trim());
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    logger.debug("deleteConsumer >> user :: [{}]", user);
                    userService.deleteUser(user);
                    returnMap.put("RESULT", "success");
                    returnMap.put("MSG", "Consumer deleted successfully!");
                } else {
                    logger.warn("User not found for userCode : {}", consumerNum);
                    throw new RuntimeException("User not found");
                }

            } else {
                returnMap.put("RESULT", "fail");
                returnMap.put("MSG", "Consumer not found!");
            }
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT","fail");
            returnMap.put("MSG","Something went wrong!");
        }
        return returnMap;
    }



    @Transactional
    public Optional<Consumer> updateConnection(Consumer consumer) {
        logger.debug("consumer :: {}", consumer);
        Optional<Consumer> existingConsumerOp = consumerDao.findById(consumer.getConsumerNum());
        if (existingConsumerOp.isEmpty()) {
            logger.error("Consumer not found for id " + consumer.getConsumerNum());
            throw new RuntimeException("Consumer not found");
        }

        int updatedRows = consumerDao.updateConnection(consumer);

        if (updatedRows != 1) {
            logger.error("Consumer data updated wrongly");
            throw new RuntimeException("Connection not updated");
        }

        return consumerDao.findById(consumer.getConsumerNum());
    }


    public ModelAndView getBillByConsumerNo() {
        ModelAndView mv = new ModelAndView("consumer/bill-pay/pending-bill");
        Optional<Consumer> loggedInUserOp = UserUtil.getLoggedInUser(Consumer.class);
        if (loggedInUserOp.isEmpty()) {
            throw new InvalidSessionException("Session Expired! Please log in again.");
        }
        Consumer consumer = loggedInUserOp.get();
        mv.addObject("consumer", consumer);

        String consumerNum = consumer.getConsumerNum();
        logger.debug("consumerNum : " + consumerNum);

        Optional<Bill> billOp = billService.getBillByConsumerNum(consumerNum);

        logger.debug("bill :: [" + billOp + "]");
        if (billOp.isEmpty()) {
            logger.warn("No bill found for consumerNum: {}", consumerNum);
            return mv;
        }
        logger.debug("Bill found for consumerNum: {}", consumerNum);
        mv.addObject("currentBill", billOp.get());
        return mv;
    }

}
