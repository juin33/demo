package com.example.demo.sike.juc;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author kejun
 * @date 2019/11/13 下午2:50
 */
public class FutureTaskTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        List<Future<Integer>> futureLists = Lists.newArrayList();
        for (int i = 0; i < 5; i++) {
            int num = i;
            Future<Integer> future = threadPool.submit(() -> {
                Thread.sleep(1000);
                System.out.println("num:" + num);
                return num;
            });
            futureLists.add(future);
        }
        Integer sum = 0;
        for(Future<Integer> future:futureLists){
            sum+=future.get();
        }
        System.out.println(sum);

        int[] arr = {5,4,7,9,3,8,2,1};
        int[] sortArr = mergeSort(arr);
        for(int s : sortArr){
            System.out.println(s);
        }
        // 构造数据
        int length = 100000000;
        long[] longArr = new long[length];
        for (int i = 0; i < length; i++) {
            longArr[i] = ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE);
        }
        forkSum(longArr);
    }

    //fockjoin
    public static void forkSum(long[] arr) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinTask<Long> forkJoinTask = forkJoinPool.submit(new SumTask(arr,0,arr.length));
        long sum = forkJoinTask.get();
        System.out.println(sum);
        forkJoinPool.shutdown();
    }

    private static class SumTask extends RecursiveTask<Long> {
        private long[] arr;
        private int from;
        private int to;

        public SumTask(long[] arr, int from, int to) {
            this.arr = arr;
            this.from = from;
            this.to = to;
        }

        @Override
        protected Long compute() {
            // 小于1000的时候直接相加，可灵活调整
            if (to - from <= 1000) {
                long sum = 0;
                for (int i = from; i < to; i++) {
                    // 模拟耗时
                    sum += (arr[i] / 3 * 3 / 3 * 3 / 3 * 3 / 3 * 3 / 3 * 3);
                }
                return sum;
            }

            // 分成两段任务
            int middle = (from + to) / 2;
            SumTask left = new SumTask(arr, from, middle);
            SumTask right = new SumTask(arr, middle, to);

            // 提交左边的任务
            left.fork();
            // 右边的任务直接利用当前线程计算，节约开销
            Long rightResult = right.compute();
            // 等待左边计算完毕
            Long leftResult = left.join();
            // 返回结果
            return leftResult + rightResult;
        }
    }


    //归并排序
    public static int[] mergeSort(int[] arr) {
        return sort(arr, 0, arr.length - 1);
    }

    public static int[] sort(int[] arr, int L, int R) {
        if(L == R) {
            return arr;
        }
        int mid = L + ((R - L) >> 1);
        sort(arr, L, mid);
        sort(arr, mid + 1, R);
        return merge(arr, L, mid, R);
    }

    public static int[] merge(int[] arr, int L, int mid, int R) {
        int[] temp = new int[R - L + 1];
        int i = 0;
        int p1 = L;
        int p2 = mid + 1;
        // 比较左右两部分的元素，哪个小，把那个元素填入temp中
        while(p1 <= mid && p2 <= R) {
            temp[i++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];
        }
        // 上面的循环退出后，把剩余的元素依次填入到temp中
        // 以下两个while只有一个会执行
        while(p1 <= mid) {
            temp[i++] = arr[p1++];
        }
        while(p2 <= R) {
            temp[i++] = arr[p2++];
        }
        // 把最终的排序的结果复制给原数组
        for(i = 0; i < temp.length; i++) {
            arr[L + i] = temp[i];
        }
        return arr;
    }

}
