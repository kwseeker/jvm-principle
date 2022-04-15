package top.kwseeker.jvm.debug;

/**
 * 编译阶段 Javac编译器会根据参数的静态类型决定使用哪个重载版本；
 * 而下面测试前两个接口调用，编译期根据传参只能知道静态类型而无法知道动态类型。
 */
public class DispatchCallMain {

    static abstract class Human {
    }

    static class Man extends Human {
    }
    static class Woman extends Human {
    }

    public void sayHello(Human guy) {
        System.out.println("hello, guy!");
    }

    public void sayHello(Man man) {
        System.out.println("hello, gentleman!");
    }

    public void sayHello(Woman guy) {
        System.out.println("hello, lady!");
    }

    public static void main(String[] args) {
        Human man = new Man();
        Human woman = new Woman();
        DispatchCallMain app = new DispatchCallMain();

        app.sayHello(man);
        app.sayHello(woman);

        app.sayHello((Man) man);
        app.sayHello((Woman) woman);
    }
}
