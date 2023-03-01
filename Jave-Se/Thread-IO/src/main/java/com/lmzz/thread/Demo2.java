package com.lmzz.thread;

//把new Thread().start()隐藏到某个类的内部
public class Demo2 {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main start");
        new Work().begin();
        System.out.println("main end");
        Thread.sleep(2 * 1000);
    }

    static class Work implements Runnable {
        public void begin() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3 * 1000);
                System.out.println("work run");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
