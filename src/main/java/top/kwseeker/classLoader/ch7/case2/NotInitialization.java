package top.kwseeker.classLoader.ch7.case2;

import top.kwseeker.classLoader.ch7.case1.SuperClass;

public class NotInitialization {
    public static void main(String[] args) {
        //SuperClass scArray[] = new SuperClass[10];  //书上的写法
        SuperClass[] sca = new SuperClass[10];        //这样写更易于理解，SuperClass[] 并不是一个合法的类名，而是由虚拟机自动生成，
                                                        //直接继承于java.lang.Object的子类，创建动作由字节码指令newarray触发。
    }
}
