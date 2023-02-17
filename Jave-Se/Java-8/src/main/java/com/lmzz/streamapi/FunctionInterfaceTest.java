package com.lmzz.streamapi;

public class FunctionInterfaceTest {
    public static void main(String[] args) {
//        lambdaMethod(() -> System.out.print("1"));
        lambdaMethod((Param1) () -> System.out.print("1"));
    }

    public static void lambdaMethod(Param1 param) {
        param.print();
    }

    public static void lambdaMethod(Param2 param) {
        param.print();
    }

    interface Param1 {
        void print();
    }

    interface Param2 {
        void print();
    }
}
