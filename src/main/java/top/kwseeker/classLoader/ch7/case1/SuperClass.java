package top.kwseeker.classLoader.ch7.case1;

public class SuperClass {
    static {
        System.out.println("SuperClass init");
    }
    public static int value = 123;
}
