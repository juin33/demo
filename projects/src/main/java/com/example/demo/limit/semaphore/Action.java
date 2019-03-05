package com.example.demo.limit.semaphore;

import com.example.demo.limit.RateLimiter;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author kejun
 * @date 2019/2/22 下午3:52
 */
public class Action {
    private static final Logger logger = LoggerFactory.getLogger(Action.class);

    private RateLimiter rateLimiter;

    public Action(){
        rateLimiter = new RateLimiter(3);
    }

    public void doSomething() throws InterruptedException {
        try {
            rateLimiter.acquire(1);
            logger.info("do something");
            Thread.sleep(1000);
        } finally {
            rateLimiter.release(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Action action = new Action();
        List<Thread> threadLists = Lists.newArrayList();
        for(int i = 0; i<20;i++){
            threadLists.add(new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        action.doSomething();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }));
        }

        for (Thread thread : threadLists){
            thread.start();
        }

        for (Thread thread : threadLists){
            thread.join();
        }

    }
}
