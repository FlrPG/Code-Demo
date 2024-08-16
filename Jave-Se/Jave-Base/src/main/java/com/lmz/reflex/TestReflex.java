package com.lmz.reflex;

import com.sun.istack.internal.NotNull;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class TestReflex {

    public static void main(String[] args) {
        Class<Son> sonClass = Son.class;
        System.out.println(sonClass);

        Method[] methods = sonClass.getMethods();
        ClassInfoPrinter.print(methods);

    }
}

@MyAnnotation("annotation on Father")
class Father{
    private String fatherPrivateField;

    private String fatherPublicField;

    public String fatherPrivateMethod(String param) {
        return "";
    }

    public String fatherPublicMethod(String param) {
        return "";
    }
}

@MyAnnotation("annotation on MyInterface")
interface MyInterface<T, R>{
    R interfaceMethod(Integer param);
}


class Son extends Father implements MyInterface{
    int defaultField;
    private int privateField;
    public String publicField;
    protected int protectedField;

    // 带注解的字段
    @MyAnnotation("yyyy-MM-dd HH:mm:ss")
    private Date annotatedField;

    public Son() {
    }

    // 公有构造
    public Son(int privateField) {
        this.privateField = privateField;
    }

    // 私有构造
    private Son(String publicField, int privateField) {
        this.publicField = publicField;
        this.privateField = privateField;
    }

    // 接口实现
    @Override
    public String interfaceMethod(Integer param) {
        return param.toString();
    }

    // 带泛型的方法
    @MyAnnotation("/son/sonPrivateMethod")
    private void sonPrivateMethod(@NotNull List<? extends Father> param) {
        System.out.println("Son私有方法, param:" + param);
    }

    @Override
    public String toString() {
        return "{" +
                "publicField='" + publicField + '\'' +
                ", protectedField=" + protectedField +
                ", defaultField=" + defaultField +
                ", privateField=" + privateField +
                ", annotatedField=" + annotatedField +
                "」";
    }



}