package com.ebs.controllers;

import com.ebs.entities.Consumer;
import com.ebs.entities.EConnection;
import com.ebs.services.ConnectionService;
import com.ebs.utils.LoggerUtil;
import com.ebs.utils.ModelAndViewUtil;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class EConnectionController {

    private static final Logger logger = LoggerUtil.getLogger(AdminController.class);

    private final ConnectionService connService;

    public EConnectionController(ConnectionService connService) {
        this.connService = connService;
    }

    @GetMapping("admin/connections")
    public ModelAndView viewConsumers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        return connService.getConnections(page, size);
    }

    @GetMapping("admin/add-connection")
    public ModelAndView addConnection() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("admin/add-connection");
        return mv;
    }

    @PostMapping("admin/add-connection")
    public ModelAndView handleAddConnectionReq(@ModelAttribute EConnection eConnection) {
        ModelAndView mv = new ModelAndView();
        logger.debug("handleAddConnectionReq >> connection :: ["+eConnection+"]");
        if(eConnection == null){
            mv.setViewName("admin/add-connection");
            //mv.addObject("admin",admin);
        }
        HashMap<String, String> retMap = connService.addConnection(eConnection);
        mv.setViewName("admin/add-connection");
        ModelAndViewUtil.addMVObject(retMap,mv);
        return mv;
    }

    @DeleteMapping("admin/remove-connection/{recordNo}")
    public ResponseEntity<Map<String, String>> deleteConnection(@PathVariable("recordNo") String connId){
        HashMap<String, String> retMap = new HashMap<>();
        logger.debug("deleteConnection >> connId :: [{}]",connId);
        if (connId == null || connId.trim().isEmpty()) {
            retMap.put("RESULT","fail");
            retMap.put("MSG","Invalid Connection No");
            return ResponseEntity.ok(retMap);
        }
        retMap = connService.deleteConnection(connId.trim());
        return ResponseEntity.ok(retMap);
    }

    @GetMapping("admin/connection/edit/{connId}")
    public ModelAndView editConnection(@PathVariable("connId") String connId) {
        ModelAndView mv = new ModelAndView();
        Optional<EConnection> connectionOpt = connService.getConnectionById(connId);
        if (connectionOpt.isPresent()){
            EConnection connection = connectionOpt.get();
            logger.debug("editConnection >> connection :: [{}] ", connection);
            mv.addObject("connection",connection);
        } else {
            logger.debug("editConnection >> connection not found..");
        }
        mv.setViewName("admin/edit-connection");
        return mv;
    }


    @PostMapping("admin/connection/edit")
    public ModelAndView updateConnectionData(@ModelAttribute EConnection eConnection) {
        ModelAndView mv = new ModelAndView();
        logger.debug("EConnection :: [{}] ", eConnection);
        if(eConnection == null){
            mv.setViewName("admin/edit-connection");
           // mv.addObject("admin",admin);
        }

        HashMap<String, Object> retMap = connService.updateConnection(eConnection);
        mv.setViewName("admin/edit-connection");
        ModelAndViewUtil.addMVObject(retMap, mv);
        return mv;
    }



}
