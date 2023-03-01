package com.lmzz.threadlocal.util;

import java.util.HashMap;
import java.util.Map;

public class ThreadLocalUtil2 {

    private ThreadLocalUtil2() {
    }

    private static final ThreadLocal<Map<String, Object>> tl = new MapThreadLocal();

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

    private static class MapThreadLocal extends ThreadLocal<Map<String, Object>> {
        @Override
        protected Map<String, Object> initialValue() {
            return new HashMap<>(8);
        }
    }


}
