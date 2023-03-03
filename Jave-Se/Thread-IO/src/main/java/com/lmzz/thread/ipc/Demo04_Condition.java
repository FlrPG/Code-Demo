package com.lmzz.thread.ipc;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Demo04_Condition {
    private static final LinkedList<String> linkedList = new LinkedList<>();

    //    ReentrantLock()
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition product = lock.newCondition();
    private final Condition consume = lock.newCondition();


    public void put(String obj) throws InterruptedException {
        lock.lock();

        try {
            while (!linkedList.isEmpty()) {
                System.out.println("生产者：还未消费完,等待。。。");
                product.await();
            }
            System.out.println("生产者：插入" + obj);
            linkedList.add(obj);
            consume.signal();
        } finally {
            lock.unlock();
        }

    }

    public void get() throws InterruptedException {
        lock.lock();
        try {
            while (linkedList.isEmpty()) {
                System.out.println("消费者：无货了,等待。。。");
                consume.await();
            }
            System.out.println("消费者：消费了" + linkedList.removeLast());
            product.signal();
        } finally {
            lock.unlock();
        }
    }


}

class Test4 {
    public static void main(String[] args) {
        Demo04_Condition ipc = new Demo04_Condition();
        //生产者
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    try {
                        ipc.put(i + "");
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
                for (int i = 5; i < 10; i++) {
                    try {
                        ipc.put(i + "");
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
                        ipc.get();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();


    }
}
