package top.kwseeker.jvm.classloader.basic;

import java.io.IOException;

/**
 * 线程上下文类加载器，设置/获取操作，继承性
 */
public class ThreadContextClassLoaderDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread son = new Thread(() -> {
            try {
                Thread.currentThread().setContextClassLoader(MyClassLoader.getMyClassLoader());
                Thread grandson = new Thread(() -> {
                    System.out.println("grandson thread‘s context classloader: " + Thread.currentThread().getContextClassLoader());
                });
                grandson.start();
                System.out.println("son thread‘s context classloader: " + Thread.currentThread().getContextClassLoader());
                grandson.join();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        });
        son.start();
        System.out.println("main thread‘s context classloader: " + Thread.currentThread().getContextClassLoader());
        son.join();
    }
}
