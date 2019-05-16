package top.kwseeker.classLoader.ch7.case3;

/**
 * 常量在编译阶段会存入调用类的常量池，本质上没有直接引用到定义常量的类，不会触发定义常量的类的初始化
 * TODO：常量传播优化（ConstClass的常量HELLOWORLD存入到NotInitialization的常量池）
 */
public class NotInitialization {
    public static void main(String[] args) {
        System.out.println(ConstClass.HELLOWORLD);
    }
}
