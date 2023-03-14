package com.lmzz.system.io.MultiplexingThreads;

import com.sun.org.apache.bcel.internal.generic.NEW;

/**
 * 多路复用器多线程版本
 */
public class MainThread {

    public static void main(String[] args) {

        //混杂模式，只有一个线程负责accept,其他线程负责 分配 client R/W
        SelectorThreadGroup stg = new SelectorThreadGroup(3);

        stg.bind(9999);
    }
}
