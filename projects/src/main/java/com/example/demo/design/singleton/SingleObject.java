package com.example.demo.design.singleton;

/**
 * @author kejun
 * 单例模式
 * @date 2019/2/11 下午3:50
 */
public class SingleObject {
    private  static SingleObject instance = new SingleObject();

    private SingleObject(){}

    public static SingleObject getInstance(){
        return instance;
    }

    public void showMessage(){
        System.out.println("hello world");
    }
}
