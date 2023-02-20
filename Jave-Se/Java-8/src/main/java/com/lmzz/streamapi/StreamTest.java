package com.lmzz.streamapi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StreamTest {


    private static List<Person> list;
    private static List<Person> list2;

    static {
        list = new ArrayList<>();
        list.add(new Person("i", 18, "杭州", 999.9));
        list.add(new Person("am", 19, "温州", 777.7));
        list.add(new Person("iron", 21, "杭州", 888.8));
        list.add(new Person("iron", 17, "宁波", 888.8));

        list2 = new ArrayList<>();
        list2.add(new Person("i", 18, "杭州", 999.9));
        list2.add(new Person("am", 19, "温州", 777.7));
        list2.add(new Person("iron", 21, "杭州", 888.8));
        list2.add(new Person("man", 17, "宁波", 888.8));
    }

    public static void main(String[] args) {
        //Collectors.toMap key 不能重复
//        extracted();

        //collect() 实现聚合
        Optional<Person> max1 = list2.stream().collect(Collectors.maxBy(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                return o1.getAge() - o2.getAge();
            }
        }));
        System.out.println(max1);
        System.out.println(max1.orElse(null));
        Optional<Person> max2 = list2.stream().max(Comparator.comparingInt(Person::getAge));

        // GROUP BY address, age
        Map<String, Map<Integer, List<Person>>> group1 = list2.stream()
                .collect(Collectors.groupingBy(Person::getAddress, Collectors.groupingBy(Person::getAge)));
        System.out.println(group1);
        // 解决了按字段分组、按多个字段分组，我们再考虑一个问题：有时我们分组的条件不是某个字段，而是某个字段是否满足xx条件



    }

    private static void extracted() {
    /*//Duplicate key Exception
    Map<String, Person> nameToPersonMap = list
            .stream()
            .collect(Collectors.toMap(Person::getName, person -> person));*/
        Map<String, Person> nameToPersonMap = list
                .stream()
                .collect(Collectors.toMap(Person::getName, person -> person, (t, t2) -> t2));
        System.out.println(nameToPersonMap);

        Map<String, Person> nameToPersonMap2 = list
                .stream()
                .collect(Collectors.toMap(Person::getName, Function.identity()));
        System.out.println(nameToPersonMap);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    static class Person {
        private String name;
        private Integer age;
        private String address;
        private Double salary;

        @Override
        public String toString() {
            return "Person{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    ", address='" + address + '\'' +
                    ", salary=" + salary +
                    '}';
        }
    }
}
