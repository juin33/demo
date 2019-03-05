package com.example.demo.websocket.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author kejun
 * @date 2019/3/5 上午11:18
 */
@Controller
public class ViewController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @GetMapping("/nasus")
    @CrossOrigin
    public String getView(){
        logger.info("nasus success");
        return "nasus";
    }
}
