package com.lmzz.threadlocal.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMapUtil4 {

    private ThreadLocalMapUtil4() {
    }

    private static final ThreadLocal<Map<String, Object>> tl = ThreadLocal.withInitial(() -> new HashMap<>(8));

    public static void put(String k, Object v) {
        getContentMap().put(k, v);
    }

    public static Object get(String k) {
        return getContentMap().get(k);
    }

    public static void remove(String k) {
        getContentMap().remove(k);
    }

    public static void clear() {
        tl.remove();
    }

    private static Map<String, Object> getContentMap() {
        return tl.get();
    }

    public static void main(String[] args) {
        ThreadLocalMapUtil4.put("mainKey", "mainValue");

        new Thread(() -> {
            ThreadLocalMapUtil4.put("threadKey", "threadValue");

            System.out.println("get main value in thread:" + ThreadLocalMapUtil4.get("mainKey"));
            System.out.println("get thread value in thread:" + ThreadLocalMapUtil4.get("threadKey"));
        }).start();

        System.out.println("get thread value in main:" + ThreadLocalMapUtil4.get("threadKey"));
        System.out.println("get main value in main:" + ThreadLocalMapUtil4.get("mainKey"));
    }
}
