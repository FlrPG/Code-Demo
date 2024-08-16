import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class Test {

    /**
     * 直接【求余】和【按位】运算的差别验证
     */
    public static void main(String[] args) {

//        test1();
//        test2();
//        test3();
        test4();
    }

    private static void test4() {
        long time1 = System.currentTimeMillis();
        HashMap<String, Object> map1 = new HashMap<>();
        for (int i = 0; i < 100000; i++) {
            map1.put(String.valueOf(i), i + "");
        }

        long time2 = System.currentTimeMillis();
        HashMap<String, Object> map2 = new HashMap<>(150000);
        for (int i = 0; i < 100000; i++) {
            map2.put(String.valueOf(i), i + "");
        }
        long time3 = System.currentTimeMillis();
        System.out.println("map1 运算的时间为: " + (time2 - time1));
        System.out.println("map2 运算的时间为: " + (time3 - time2));

    }

    private static void test3() {
        HashSet<Object> hashSet = new HashSet<>();
        hashSet.add(null);
        hashSet.add(null);

        TreeSet<Object> treeSet = new TreeSet<>();
//        treeSet.add(null);//error: null cannot be added to a TreeSet

        TreeMap<Object, Object> treeMap = new TreeMap<>();
        treeMap.put(null, null);// error: null cannot be added to a TreeMap
        treeMap.put(null, null);


        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put(null, null);
        hashMap.put(null, null);


    }

    private static void test1() {
        long time1 = System.currentTimeMillis();
        int num1 = 0;
        int count = 10000 * 10000;
        int nSize = 1;
        for (long i = 0; i < count; i++) {
            nSize++;
            if (nSize == 11) {
                nSize = 1;
            }
            num1 = 9999 % (1 << nSize);
        }
        long time2 = System.currentTimeMillis();
        int num2 = 0;
        nSize = 1;
        for (long i = 0; i < count; i++) {
            nSize++;
            if (nSize == 11) {
                nSize = 1;
            }
            num2 = 9999 & (1 << nSize - 1);
        }
        long time3 = System.currentTimeMillis();
        System.out.println("最后的结果 num1=" + num1 + "，num2=" + num2);
        System.out.println("% 运算的时间为: " + (time2 - time1));
        System.out.println("& 运算的时间为: " + (time3 - time2));
    }

    private static void test2() {
        long time1 = System.currentTimeMillis();
        int num1 = 0;
        int count = 10000 * 10000;
        for (long i = 0; i < count; i++) {
            if (i == 50000) {
                num1 = 9999 % 512;
            } else {
                num1 = 9999 % 1024;
            }
        }
        long time2 = System.currentTimeMillis();

        int num2 = 0;
        for (long i = 0; i < count; i++) {
            if (i == 50000) {
                num2 = 9999 & (512 - 1);
            } else {
                num2 = 9999 & (1024 - 1);
            }
        }
        long time3 = System.currentTimeMillis();
        System.out.println("最后的结果 num1=" + num1 + "，num2=" + num2);
        System.out.println("% 运算的时间为: " + (time2 - time1));
        System.out.println("& 运算的时间为: " + (time3 - time2));
    }

}
