package top.kwseeker.jvm.jit;

/**
 * JIT优化：对象栈上内存分配
 *
 * -XX:+DoEscapeAnalysis (默认就是开启的)
 * #还可以添加下面参数查看内存回收情况
 * -XX:+PrintGCDetails
 * jmap -histo <pid>   #查看堆内存对象实例情况
 */
public class ObjectStackAllocTest {

    public static void main(String[] args) throws InterruptedException {
        long a1 = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {         //对象虽然没有逃逸，但是创建的对象太多，栈空间不足，最终还是会将放不下的对象放到堆里面
        //for (int i = 0; i < 10; i++) {            //如果调小的话，栈可以放下，就不会占用堆空间
            alloc();
        }
        long a2 = System.currentTimeMillis();
        System.out.println("cost " + (a2 - a1) + " ms");

        // 为了方便查看堆内存中对象个数，线程sleep
        Thread.sleep(1000000);
    }

    private static void alloc() {
        User user = new User();
    }

    static class User {
    }
}
