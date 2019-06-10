package com.example.demo.hystrix.model;

/**
 * @author kejun
 * @date 2019/6/10 下午4:14
 */
public class UserAccount {
    private String userId;
    private String name;

    public UserAccount(String userId,String name){
        this.name = name;
        this.userId = userId;
    }
}
