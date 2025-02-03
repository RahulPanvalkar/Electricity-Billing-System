package com.ebs.services.impl;

import com.ebs.dao.CostPerUnitDao;
import com.ebs.entities.CostPerUnit;
import com.ebs.services.CostPerUnitService;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Optional;

@Service
public class CostPerUnitServiceImpl implements CostPerUnitService {

    private static final Logger logger = LoggerUtil.getLogger(CostPerUnitServiceImpl.class);

    private final CostPerUnitDao costPerUnitDao;

    public CostPerUnitServiceImpl(CostPerUnitDao costPerUnitDao) {
        this.costPerUnitDao = costPerUnitDao;
    }

    public Optional<CostPerUnit> getCostPerUnitById(int id) {
        return costPerUnitDao.findById(id);
    }

    @Transactional
    public HashMap<String, String> updateCostPerUnit(CostPerUnit costPerUnit) {
        logger.debug("costPerUnit :: {}", costPerUnit);
        HashMap<String, String> returnMap = new HashMap<>();
        try {

            if (costPerUnit == null) {
                returnMap.put("RESULT", "failed");
                returnMap.put("MSG", "Required cost values");
                return returnMap;
            }

            int updatedRowCount = costPerUnitDao.updateCostPerUnit(costPerUnit);
            logger.debug("updatedRowCount : {}", updatedRowCount);

            if (updatedRowCount != 1) {
                returnMap.put("RESULT", "failed");
                returnMap.put("MSG", "Something went wrong! please try again");
                return returnMap;
            }

            returnMap.put("RESULT", "success");
            returnMap.put("MSG", "Per unit cost updated successfully!");
        } catch (Exception e) {
            logger.error("Exception occurred :: ", e);
            returnMap.put("RESULT", "fail");
            returnMap.put("MSG", "Something went wrong! Please try again");
        }
        return returnMap;
    }

}
