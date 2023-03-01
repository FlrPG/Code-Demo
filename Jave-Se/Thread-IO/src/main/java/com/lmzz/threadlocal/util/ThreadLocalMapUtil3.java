package com.lmzz.threadlocal.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalMapUtil3 extends ThreadLocal<Map<String, Object>> {

    private ThreadLocalMapUtil3() {
    }

    private static final ThreadLocal<Map<String, Object>> tl = new ThreadLocalMapUtil3();

    public static void put(String k, Object v) {
        getContentMap().put(k, v);
        /*Map<String, Object> map = tl.get();
        if (map == null) {
            map = new HashMap<>();
            tl.set(map);
        }
        map.put(k, v);*/
    }

    public static Object get(String k) {
        return getContentMap().get(k);
        /*Map<String, Object> map = tl.get();
        if (map == null) {
            return null;
        }
        return map.get(k);*/
    }

    public static void remove(String k) {
        getContentMap().remove(k);
        /*Map<String, Object> map = tl.get();
        map.remove(k);*/
    }

    public static void clear() {
        tl.remove();
    }

    private static Map<String, Object> getContentMap() {
        return tl.get();
    }


    @Override
    protected Map<String, Object> initialValue() {
        return new HashMap<>(8);

        /*return new HashMap<String, Object>(8) {
            private static final long serialVersionUID = 3637958959138295593L;

            @Override
            public Object put(String key, Object value) {
                return super.put(key, value);
            }
        };*/
    }


    public static void main(String[] args) {
        ThreadLocalMapUtil3.put("mainKey", "mainValue");

        new Thread(() -> {
            ThreadLocalMapUtil3.put("threadKey", "threadValue");

            System.out.println("get main value in thread:" + ThreadLocalMapUtil3.get("mainKey"));
            System.out.println("get thread value in thread:" + ThreadLocalMapUtil3.get("threadKey"));
        }).start();

        System.out.println("get thread value in main:" + ThreadLocalMapUtil3.get("threadKey"));
        System.out.println("get main value in main:" + ThreadLocalMapUtil3.get("mainKey"));
    }
}
