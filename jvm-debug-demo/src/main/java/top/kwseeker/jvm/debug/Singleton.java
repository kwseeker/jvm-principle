package top.kwseeker.jvm.debug;

import java.util.concurrent.CountDownLatch;

/**
 * DCL单例模式，单例对象无volatile修饰, 因指令重排序导致读取到半初始化的对象，尝试复现
 */
public class Singleton {

    private static final CountDownLatch latch = new CountDownLatch(100);
    private static Singleton instance;              //错误写法，但是也很难出现读取到半初始化的对象(概率太低）
    //private static volatile Singleton instance;   //volatile 禁止指令重排序

    private String content = "hello";

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            synchronized (Singleton.class) {
                if (instance == null) {
                    instance = new Singleton(); //分为3条指令：new（半初始化）-> invokespecial (初始化) -> astore_1, 如果后两条指令重排序，
                    //可能让另一个线程判断 instance == null 时拿到一个半初始化的对象（content为null）
                }
            }
        }
        return instance;
    }

    public String getContent() {
        return content;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Singleton singleton = Singleton.getInstance();
                System.out.println(singleton.hashCode() + " " + singleton.getContent());
            }).start();
            latch.countDown();
        }

        Thread.sleep(1000);
    }
}
