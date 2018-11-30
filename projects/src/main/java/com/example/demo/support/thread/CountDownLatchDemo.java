package com.example.demo.support.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author kejun
 * @date 2018/11/27 下午3:08
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException{
        final CountDownLatch startGate = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(5);
        //线程池
        ExecutorService executorService = Executors.newCachedThreadPool();
        //创建5个线程
        for(int i = 1;i <= 5;i++){
            final int num=i;
            Thread thread =new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println(num+"号选手准备就绪,等待裁判员哨声响起..");
                        //相当于同步锁Synchronized中的await()方法
                        startGate.await();
                        try {
                            long time = (long) (Math.random()*10000);
                            System.out.println(num+"号选手花了"+time+"ms");
                            Thread.sleep(time);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(num+"号选手到达终点..");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }finally {
                        //相当于同步锁Synchronized中的notify()方法,区别在于countDown需要执行5次后才能唤醒await()
                        endGate.countDown();
                    }
                }
            });
            executorService.execute(thread);
        }
        long start=System.nanoTime();
        System.out.println("裁判员哨声响起..");
        Thread.sleep(10000);
        //唤醒执行startGate.await()的线程,让线程往下执行
        startGate.countDown();
        //等待被唤醒,主程序才能继续往下执行,线程中每次执行endGate.countDown()就减1,当为零的时候,主程序往下执行
        endGate.await();
        long end=System.nanoTime();
        System.out.println("所有运动员到达终点,耗时:"+(end-start));
        //关闭线程池
        executorService.shutdown();
    }
}
