package com.ebs.controllers;

import com.ebs.entities.CostPerUnit;
import com.ebs.services.CostPerUnitService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class UpdateCostController {

    private static final Logger logger = LoggerUtil.getLogger(CostPerUnitService.class);

    private final CostPerUnitService costPerUnitService;

    public UpdateCostController(CostPerUnitService costPerUnitService) {
        this.costPerUnitService = costPerUnitService;
    }

    @GetMapping("admin/update-cost")
    public ModelAndView getUpdateCost() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/update-cost");
        Optional<CostPerUnit> costPerUnitOp = costPerUnitService.getCostPerUnitById(1);
        if (costPerUnitOp.isPresent()){
            CostPerUnit costPerUnit = costPerUnitOp.get();
            logger.debug("getUpdateCost >> costPerUnit :: [{}]", costPerUnit);
            mv.addObject("costPerUnit", costPerUnit);
        }

        return mv;
    }

    @PostMapping("admin/update-cost")
    public ModelAndView updateCostPerUnit(@ModelAttribute CostPerUnit costPerUnit) {
        ModelAndView mv = new ModelAndView();
        logger.debug("updateCostPerUnit >> costPerUnit :: ["+costPerUnit+"]");
        HashMap<String, String> retMap = costPerUnitService.updateCostPerUnit(costPerUnit);
        mv.setViewName("admin/update-cost");
        ModelAndViewUtil.addMVObject(retMap,mv);
        return mv;
    }
}
