package com.lmzz.streamapi;

import java.util.*;
import java.util.stream.Collectors;

public class FlatMapTest {
    /**
     * 需求：
     * 1.要求返回所有的key，格式为 list<Long>      提示:keyset
     * 2.要求最终返回所有value，格式为 List<Long>   提示:flatMap()，Function需要啥你就转成啥
     *
     * @param args
     */
    public static void main(String[] args) {
        Map<Long, List<Long>> map = new HashMap<>();
        map.put(1L, new ArrayList<>(Arrays.asList(1L, 2L, 3L)));
        map.put(2L, new ArrayList<>(Arrays.asList(4L, 5L, 6L)));

        List<Long> keyList = new ArrayList<>(map.keySet());
        System.out.println(keyList);

        List<Long> valueList = map.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        System.out.println(valueList);
    }
}