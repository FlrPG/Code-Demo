package com.lmzz.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建
//        test1();

        //包装 Runnable、Callable
        test2();

    }

    private static void test2() throws ExecutionException, InterruptedException {
        FutureTask<String> runnableFutureTask = new FutureTask<>(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "--> run");
            }
        }, "futureTask - runnable");

        FutureTask<String> callableFutureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "futureTask - Callable";
            }
        });

        callableFutureTask.get();
    }

    private static void test1() throws InterruptedException, ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName() + "--> run");
                Thread.sleep(5 * 1000);
                return "Callable().call";
            }
        });

        new Thread(futureTask).start();
        System.out.println(Thread.currentThread().getName() + "========>启动任务");


        String result = futureTask.get();
        System.out.println("任务执行结束，result====>" + result);
    }
}
