package com.example.demo.websocket.domain;

import java.io.Serializable;

/**
 * @author kejun
 * @date 2019/3/5 上午11:07
 * 客户端给服务器发送消息类
 */
public class Client2ServerMessage implements Serializable{
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
