public interface TestInterface {
//    int aa;//error: variable or field 'aa' cannot be declared final
//    public static final String bb = "bb";
    default void test(){
        System.out.println("test");
    };

}
