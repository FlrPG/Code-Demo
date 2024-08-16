package com.lmzz;

public class Demo1 {
    public static void main(String[] args) {
        processEquals();
    }

    private static void processEquals() {
        String a = new String("Hello");
        String b = new String("Hello");
        System.out.println(a == b);
        System.out.println(a.equals(b));

        String a1 = new String("Hello");
        String b1 = a1;
        System.out.println(a1 == b1);
        System.out.println(a1.equals(b1));

        String a2 = "Hello";
        String b2 = "Hello";
        System.out.println(a2 == b2);
        System.out.println(a2.equals(b2));

    }
}
