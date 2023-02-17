package com.lmzz.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ConvertUtil {


    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyFuncGen) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }

        Map<K, V> map = new HashMap<>();
        for (V v : list) {
            K k = keyFuncGen.apply(v);
            if (k == null) {
                continue;
            }
            map.put(k, v);

        }
        return map;
    }

    public static <K, V> Map<K, V> listToMap(List<V> list, Function<V, K> keyFuncGen, Predicate<V> filter) {
        if (list == null || list.isEmpty()) {
            return new HashMap<>();
        }
        Map<K, V> map = new HashMap<>();
        for (V v : list) {
            K k = keyFuncGen.apply(v);
            if (k == null || !filter.test(v)) {
                continue;
            }
            map.put(k, v);

        }
        return map;
    }

    public <T, R> List<R> resultToList(List<T> targetList, Function<T, R> function) {
        if (targetList == null || targetList.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> ansList = new ArrayList<>();
        for (T t : targetList) {
            R r = function.apply(t);
            if (r != null) {
                ansList.add(r);
            }
        }
        return ansList;
    }

    public <T, R> List<R> resultToList(List<T> targetList, Function<T, R> function, Predicate<T> filter) {
        if (targetList == null || targetList.isEmpty()) {
            return new ArrayList<>();
        }
        List<R> ansList = new ArrayList<>();
        for (T t : targetList) {
            R r = function.apply(t);
            if (r == null || !filter.test(t)) {
                continue;
            }
            ansList.add(r);
        }
        return ansList;
    }

    public static <T> void foreachIfNonNull(List<T> sourceList, Consumer<T> consumer) {

    }




    private static List<Person> list;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("iron", 21, "杭州", 888.8));
        list.add(new Person("man", 17, "宁波", 888.8));
    }

    public static void main(String[] args) {
        Map<String, Person> map = listToMap(list, Person::getName);
        System.out.println(map);

        Map<String, Person> map1 = listToMap(list, Person::getAddress, a -> a.getSalary() > 888);
        System.out.println(map1);
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;
    }


}
