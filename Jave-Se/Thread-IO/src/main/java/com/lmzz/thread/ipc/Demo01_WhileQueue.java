package com.lmzz.thread.ipc;

import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

//IPC Inter-Process Communication
public class Demo01_WhileQueue {

    private static final LinkedList<String> linkedList = new LinkedList<>();

    public static void put(String obj) throws InterruptedException {
        while (!linkedList.isEmpty()) {
            System.out.println("生产者：还未消费完,等待。。。");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("生产者：插入" + obj);
        linkedList.add(obj);

    }

    public static void get() throws InterruptedException {

        while (linkedList.isEmpty()) {
            System.out.println("消费者：无货了,等待。。。");
            TimeUnit.SECONDS.sleep(1);
        }
        System.out.println("消费者：消费了" + linkedList.removeLast());
    }


    public static void main(String[] args) {
        //生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        put(i + "");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        ).start();

        //消费者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }


}
