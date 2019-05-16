package top.kwseeker.classLoader.ch7.case1;

public class SubClass extends SuperClass {
    static {
        System.out.println("SubClass init");
    }
}
