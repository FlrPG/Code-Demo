package com.lmzz.threadlocal.util;

public class MyThreadLocal {

    private MyThreadLocal() {
    }

    private static final ThreadLocal<Object> tl = new ThreadLocal<>();


    public static void put(Object object) {
        tl.set(object);
    }

    public static Object get() {
        return tl.get();
    }

    public static void remove() {
        tl.remove();
    }
}
