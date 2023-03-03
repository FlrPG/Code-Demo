package com.lmzz.thread.ipc;

import java.util.LinkedList;

public class Demo02_WaitNotify {
    private static final LinkedList<String> linkedList = new LinkedList<>();

    public synchronized void put(String obj) throws InterruptedException {
        while (!linkedList.isEmpty()) {
            System.out.println("生产者：还未消费完,等待。。。");
            this.wait();
        }
        System.out.println("生产者：插入" + obj);
        linkedList.add(obj);
        this.notify();

    }

    public synchronized void get() throws InterruptedException {
        while (linkedList.isEmpty()) {
            System.out.println("消费者：无货了,等待。。。");
            this.wait();
        }
        System.out.println("消费者：消费了" + linkedList.removeLast());
        this.notify();
    }


}

class Test {
    public static void main(String[] args) {
        Demo03_WaitNotifyAll demo02WaitNotify = new Demo03_WaitNotifyAll();
        //生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        demo02WaitNotify.put(i + "");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        ).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        demo02WaitNotify.put(i + "");
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
                        demo02WaitNotify.get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();


    }
}
