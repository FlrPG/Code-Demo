package com.lmzz.system.io.MultiplexingThreads;


/**
 * 多路复用器多线程版本
 */
public class MainThread {

    public static void main(String[] args) {
        //1. 混杂模式，只有一个线程负责accept,其他线程包括自己负责 分配 client R/W
        //2. 一个线程负责accept,其他线程负责 分配 client R/W
//        implementV1();

        //单独一个Boss线程组负责accept,一个worker 线程组负责处理连接后的RW
        implementV2();
    }

    private static void implementV1() {
        //1.创建IO Thread
        //2.我应该 把监听9999 server 注册到某一个selector 上，
        //混杂模式，只有一个线程负责accept,其他线程负责 分配 client R/W
        SelectorThreadGroup stg = new SelectorThreadGroup(3);

        stg.bind(9999);
    }

    private static void implementV2() {
        SelectorThreadGroup boss = new SelectorThreadGroup(3);
        SelectorThreadGroup worker = new SelectorThreadGroup(3);

        boss.setWorker(worker);

        boss.bind(9999);
        boss.bind(8888);
        boss.bind(7777);
        boss.bind(6666);

    }
}
