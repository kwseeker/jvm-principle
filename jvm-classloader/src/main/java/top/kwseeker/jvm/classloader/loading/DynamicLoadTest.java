package top.kwseeker.jvm.classloader.loading;

public class DynamicLoadTest {

    static {
        System.out.println("DynamicLoadTest static ...");   //1
    }

    public static void main(String[] args) {
        new A();
        System.out.println("main() ...");                   //4
        B b = null;                                                 //并不会加载
    }
}

class A {
    static {
        System.out.println("A static ...");                 //2
    }
    public A() {
        System.out.println("init A ...");                   //3
    }
}

class B {
    static {
        System.out.println("B static ...");
    }
    public B() {
        System.out.println("init B ...");
    }
}
