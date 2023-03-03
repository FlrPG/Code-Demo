package com.lmzz.threadpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class SimpleThreadPool {

    //任务队列
    private BlockingQueue<Runnable> workQueue;

    //工作线程
    private List<Worker> workers = new ArrayList<>();

    /**
     * 构造器
     *
     * @param poolSize  线程数
     * @param workQueue 任务队列
     */
    SimpleThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建线程，并加入线程池
        for (int i = 0; i < poolSize; i++) {
            Worker work = new Worker();
            work.start();
            workers.add(work);
        }
    }


    void execute(Runnable runnable) {
        try {
            workQueue.put(runnable);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    class Worker extends Thread {
        @Override
        public void run() {
            // 循环获取任务，如果任务为空则阻塞等待
            while (true) {
                try {
                    Runnable task = workQueue.take();
                    task.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

class SimpleThreadPoolTest {

    public static void main(String[] args) {

        SimpleThreadPool simpleThreadPool = new SimpleThreadPool(2, new ArrayBlockingQueue<Runnable>(2));

        simpleThreadPool.execute(() -> {
            System.out.println("第1个任务开始");
            sleep(3);
            System.out.println("第1个任务结束");
        });
        simpleThreadPool.execute(() -> {
            System.out.println("第2个任务开始");
            sleep(4);
            System.out.println("第2个任务结束");
        });
        simpleThreadPool.execute(() -> {
            System.out.println("第3个任务开始");
            sleep(5);
            System.out.println("第3个任务结束");
        });
    }


    private static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
