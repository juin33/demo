package com.example.demo.support;

import org.apache.http.HttpResponse;

/**
 * @author kejun
 * @date 2018/11/8 下午5:11
 */
public class ThreadTest extends Thread {


    public void run(){
        try {
//            for(int i = 0;i<50;i++){
                HttpResponse response = HttpClient.post("http://creditease-echo-interactive.test.91gfd.cn/api/inner/count/stat?token=dashu&type=1&date=20181108");
                System.out.println(response);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
