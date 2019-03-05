package com.example.demo.websocket.domain;

import java.io.Serializable;

/**
 * @author kejun
 * @date 2019/3/5 上午11:08
 * 服务器发送给客户端消息类
 */
public class Server2ClientMessage implements Serializable{
    private String responseMessage;

    public Server2ClientMessage(String responseMessage){
        this.responseMessage = responseMessage;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
