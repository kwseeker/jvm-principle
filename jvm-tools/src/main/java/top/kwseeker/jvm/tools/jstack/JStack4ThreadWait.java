package top.kwseeker.jvm.tools.jstack;

public class JStack4ThreadWait {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        //thread1 获取锁， thread2 等待锁
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("thread1 is running");
                while (true) {
                }
            }
        }, "thread1");
        Thread thread2 = new Thread(() -> {
            Thread.yield();
            synchronized (lock) {
                System.out.println("thread2 is running");
            }
        }, "thread2");
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("main thread end");
    }
}
