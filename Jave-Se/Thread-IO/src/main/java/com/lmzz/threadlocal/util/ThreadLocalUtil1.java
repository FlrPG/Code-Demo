package com.lmzz.threadlocal.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil1 {

    private ThreadLocalUtil1(){}

    private static final ThreadLocal<Map<String, Object>> tl = new ThreadLocal<>();

    public static void put(String k, Object v) {
        Map<String, Object> map = tl.get();
        if (map == null) {
            map = new HashMap<>();
            tl.set(map);
        }
        map.put(k, v);
    }

    public static Object get(String k) {
        Map<String, Object> map = tl.get();
        if (map == null) {
            return null;
        }
        return map.get(k);
    }

    public static void remove(String k){
        Map<String, Object> map = tl.get();
        map.remove(k);
    }

    public static void clear() {
        tl.remove();
    }

}
