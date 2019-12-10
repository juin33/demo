package com.example.demo.sike.misc;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author kejun
 * @date 2019/11/14 下午2:17
 */
public class UnsafeDemo {
    private volatile int count = 0;

    private static long offset;
    private static Unsafe unsafe;
    static {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            unsafe = (Unsafe) f.get(null);
            offset = unsafe.objectFieldOffset(UnsafeDemo.class.getDeclaredField("count"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void increment() {
        int before = count;
        // 失败了就重试直到成功为止
        while (!unsafe.compareAndSwapInt(this, offset, before, before + 1)) {
            before = count;
        }
    }

    public int getCount() {
        return count;
    }

    private boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, offset, expect, update);
    }

//    public static void main(String[] args) throws InterruptedException {
//        UnsafeDemo counter =  new UnsafeDemo();
//        ExecutorService threadPool = Executors.newFixedThreadPool(100);
//
//        // 起100个线程，每个线程自增10000次
//        IntStream.range(0, 100)
//                .forEach(i->threadPool.submit(()->IntStream.range(0, 10000)
//                        .forEach(j->counter.increment())));
//
//        threadPool.shutdown();
//
//        Thread.sleep(2000);
//
//        // 打印1000000
//        System.out.println(counter.getCount());
//    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println(xmind());
    }

    public static String xmind() throws ExecutionException, InterruptedException {
        ExecutorService executor= Executors.newFixedThreadPool(2);
        //小红买酒任务，这里的future2代表的是小红未来发生的操作，返回小红买东西这个操作的结果
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(()-> {
            System.out.println("begin");
            return "12345";
        },executor);

        future2.thenRun(()->{
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("run");
        });
        return future2.get();
    }
}
