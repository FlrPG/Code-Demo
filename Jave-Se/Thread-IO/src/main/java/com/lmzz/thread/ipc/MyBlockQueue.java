package com.lmzz.thread.ipc;

import java.util.LinkedList;

/**
 * 模拟BlockingQueue
 *
 * @param <T>
 */
public class MyBlockQueue<T> {
    private final LinkedList<T> queue = new LinkedList<>();
    int maxSize = 1;
    int count = 0;

    public MyBlockQueue(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("size can not less than 1");
        }
        maxSize = size;
    }
    public synchronized void put(T obj) throws InterruptedException {
        while (count >= maxSize) {
            System.out.println("插入阻塞。。。");
            this.wait();
        }
        queue.add(obj);
        count++;
        System.out.println("==> 插入：" + obj + "，队列容量：" + count);
        this.notifyAll();
    }
    public synchronized void get() throws InterruptedException {
        while (count <= 0) {
            System.out.println("获取阻塞。。。");
            this.wait();
        }
        T t = queue.removeLast();
        count--;
        System.out.println("==> 获取：" + t + "，队列容量：" + count);
        this.notifyAll();
    }
}


class TestBlQ {
    public static void main(String[] args) {
        MyBlockQueue<String> ipc = new MyBlockQueue<>(2);
        //生产者
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    ipc.put(i + "");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ).start();
        new Thread(() -> {
            for (int i = 5; i < 10; i++) {
                try {
                    ipc.put(i + "");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        ).start();

        //消费者
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    ipc.get();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }

}
