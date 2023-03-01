package com.lmzz.thread;

import java.util.concurrent.*;

public class Demo1 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        new Thread() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " --> run");
            }
        }.start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " --> run");
            }
        }).start();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<String> submit = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName() + " --> run");
                return "1";
            }
        });
        String s = submit.get();
        System.out.println(s);

        executorService.shutdown();
//        threadAndRunnable();
    }

    private static void threadAndRunnable() {
        new Thread(new Runnable() {
            public void run() {
                System.out.println("Runnable's run method is running");
            }
        }) {
            @Override
            public void run() {
                System.out.println("Thread's run method is running");
            }
        }.start();
    }
}
