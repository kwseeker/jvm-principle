package top.kwseeker.jvm.trigger;

public class Programmer extends User {

    static {        //static {} 是在初始化阶段执行的
        System.out.println("programmer static block");
    }
}
