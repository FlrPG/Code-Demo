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
        //test1：获取所有的Person的名字
        List<String> nameList = list.stream().map(Person::getName).collect(Collectors.toList());
        System.out.println(nameList);
        //test2：获取一个List，每个元素的内容为：{name}来自{address}
        List<String> nameAddressList = list.stream().map(p -> p.getName() + "来自" + p.getAddress()).collect(Collectors.toList());
        System.out.println(nameAddressList);
        System.out.println();

        //test3: 过滤出年龄大于等于18的Person
        List<Person> filterAgeList = list.stream().filter(p -> p.getAge() >= 18).collect(Collectors.toList());
        System.out.println(filterAgeList);
        //test4: 过滤出年龄大于等于18 并且 月薪大于等于888.8 并且 来自杭州的Person
        List<Person> filterList = list.stream()
                .filter(p -> p.getAge() >= 18 && p.getSalary() >= 888.8 && "杭州".equals(p.getAddress()))
                .collect(Collectors.toList());
        System.out.println(filterList);
        System.out.println();

        //test5: 查找
        Optional<Person> findFirst = list.stream().filter(p -> "杭州".equals(p.getAddress())).findFirst();
        findFirst.ifPresent(System.out::println);
        Optional<Person> findAny = list.stream().filter(p -> "杭州".equals(p.getAddress())).findAny();
        findAny.ifPresent(System.out::println);
        boolean anyMatch = list.stream().anyMatch(p -> "杭州".equals(p.getAddress()));
        System.out.println(anyMatch);
        System.out.println();


        //1.要求分组统计出各个城市的年龄总和，返回格式为 Map<String, Integer>。
        Map<String, Integer> cityAgeSum = list.stream().collect(Collectors.toMap(
                Person::getAddress,
                Person::getAge,
                Integer::sum
        ));
        Map<String, Integer> cityAgeSum2 = list.stream().collect(Collectors.groupingBy(Person::getAddress,Collectors.summingInt(Person::getAge)));
        System.out.println(cityAgeSum);
        System.out.println(cityAgeSum2);
        //2.要求得到Map<城市, List<用户工资>>
        Map<String, List<Person>> collect = list.stream().collect(Collectors.groupingBy(Person::getAddress));
        Map<String, Set<Person>> collect1 = list.stream()
                .collect(Collectors.groupingBy(Person::getAddress, Collectors.toSet()));
        Map<String, List<Double>> collect2 = list.stream()
                .collect(Collectors.groupingBy(Person::getAddress, Collectors.mapping(Person::getSalary, Collectors.toList())));
        System.out.println(collect2);


        //Collectors.toMap key 不能重复
//        extracted();

        //collect() 实现聚合
//        extracted1();
    }

    private static void extracted1() {
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
