package com.example.demo.websocket.controller;

import com.example.demo.websocket.domain.Client2ServerMessage;
import com.example.demo.websocket.domain.Server2ClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

/**
 * @author kejun
 * @date 2019/3/5 上午11:10
 */
@RestController
public class WebSocketController {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketController.class);

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    // @MessageMapping 和 @RequestMapping 功能类似，浏览器向服务器发起请求时，映射到该地址。
    @MessageMapping(value = "/hello")
    //如果服务器接受到了消息，就会对订阅了 @SendTo 括号中的地址的浏览器发送消息。
    @SendTo("/nasus/getResponse")
    @CrossOrigin
    public Server2ClientMessage say(Client2ServerMessage message) throws InterruptedException {
        Thread.sleep(2000);
        logger.info("nasus getResponse success");
        //往指定的目的地发送消息，凡是订阅此目的地的都可以接收到
        simpMessageSendingOperations.convertAndSend("/nasus/getResponse2",new Server2ClientMessage("hello "+message.getName()+"!"));
        //返回到SendTo括号中的地址的浏览器发送消息。
        return new Server2ClientMessage("hello"+message.getName()+"!");
    }

    @MessageMapping(value = "/hello2")
    @SendToUser("/hello/greeting")
    @CrossOrigin
    public Server2ClientMessage greeting(Client2ServerMessage message) throws InterruptedException {
        Thread.sleep(1000);
        logger.info("sendToUser hello greeting");
        return new Server2ClientMessage("hello "+ message.getName()+"!");
    }
}
