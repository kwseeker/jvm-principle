package top.kwseeker.classLoader.ch7.case1;

/**
 * 被动引用
 * 通过子类引用父类中定义的静态字段，只有父类会被初始化
 */
public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}
