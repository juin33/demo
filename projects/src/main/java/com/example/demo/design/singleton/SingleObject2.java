package com.example.demo.design.singleton;

/**
 * @author kejun
 * 双检锁/双重校验锁
 * 支持高并发
 * @date 2019/2/19 上午11:15
 */
public class SingleObject2 {
    private volatile static SingleObject2 instance;

    private SingleObject2(){

    }

    public static SingleObject2 getInstance(){
        if(null == instance){
            synchronized (SingleObject2.class){
                if(null == instance){
                    instance = new SingleObject2();
                }
            }
        }
        return instance;
    }

    public void showMessage(){
        System.out.println("hello world");
    }

}
