package com.example.demo.limit;

import java.util.concurrent.Semaphore;

/**
 * @author kejun
 * 信号量
 * @date 2019/2/22 下午3:18
 */
public class RateLimiter {
    private Semaphore semaphore;

    public RateLimiter(int permits){
        semaphore = new Semaphore(permits);
    }

    public void acquire(int permits) throws InterruptedException {
        semaphore.acquire(permits);
    }

    public void release(int permits){
        semaphore.release(permits);
    }
}
