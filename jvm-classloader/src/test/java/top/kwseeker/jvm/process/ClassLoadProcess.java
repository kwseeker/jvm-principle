package top.kwseeker.jvm.process;

/**
 * 类加载流程调试
 */
public class ClassLoadProcess {

    public static final int INIT_DATA = 1;

    public void test() {
        System.out.println("in test ...");
    }

    public static void main(String[] args) {
        ClassLoadProcess process = new ClassLoadProcess();
        process.test();

        User user = new User();     //这里懒加载（按需加载）是怎么实现的？ TODO
    }
}
