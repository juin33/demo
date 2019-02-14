package com.example.demo.design.singleton;

/**
 * @author kejun
 * 单例模式
 * @date 2019/2/11 下午3:52
 */
public class SingleObjectDemo {
    public static void main(String[] args) {
        SingleObject singleObject = SingleObject.getInstance();
        singleObject.showMessage();
    }
}
