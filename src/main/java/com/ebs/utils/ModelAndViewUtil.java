package com.ebs.utils;

import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

public class ModelAndViewUtil {

    private static final Logger logger = LoggerUtil.getLogger(ModelAndViewUtil.class);

    public static void addMVObject(HashMap<String, String> retMap, ModelAndView mv){
        String result = retMap.get("RESULT");
        String message = retMap.get("MSG");
        logger.debug("addMVObject >> result :: [{}] && message :: [{}]", result, message);
        if("success".equalsIgnoreCase(result)){
            mv.addObject("error",false);
            mv.addObject("message",message);
        }else{
            mv.addObject("error",true);
            mv.addObject("message",message);
        }
    }

    public static void addMVObject(Map<String, Object> retMap, ModelAndView mv) {
        String result = (String) retMap.get("RESULT");
        String message = (String) retMap.get("MSG");
        logger.debug("addMVObject >> result :: [{}] && message :: [{}]", result, message);

        if ("success".equalsIgnoreCase(result)) {
            mv.addObject("error", false);
            mv.addObject("message", message);
        } else {
            mv.addObject("error", true);
            mv.addObject("message", message);
        }

        // Add all entries from retMap to ModelAndView
        for (Map.Entry<String, Object> entry : retMap.entrySet()) {
            if (!"RESULT".equals(entry.getKey()) && !"MSG".equals(entry.getKey())) {
                mv.addObject(entry.getKey(), entry.getValue());
            }
        }
    }

}
