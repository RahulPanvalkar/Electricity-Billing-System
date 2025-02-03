package com.ebs.config;

import com.ebs.exception.InvalidSessionException;
import com.ebs.utils.LoggerUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerUtil.getLogger(GlobalExceptionHandler.class);
    
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        logger.debug("GlobalExceptionHandler >> inside handleException..");
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", ex);
        mv.addObject("exceptionName", ex.getClass().getSimpleName());
        mv.setViewName("public/error");
        return mv;
    }

    @ExceptionHandler(InvalidSessionException.class)
    public ModelAndView handleInvalidSessionException(InvalidSessionException ex, Model model) {
        logger.debug("GlobalExceptionHandler >> inside handleInvalidSessionException..");
        ModelAndView mv = new ModelAndView();
        mv.addObject("exception", ex);
        mv.addObject("exceptionName", ex.getClass().getSimpleName());
        mv.setViewName("public/error");
        return mv;
    }

}

