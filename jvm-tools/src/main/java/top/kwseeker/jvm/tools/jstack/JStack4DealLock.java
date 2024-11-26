package top.kwseeker.jvm.tools.jstack;

import java.util.concurrent.locks.ReentrantLock;

public class JStack4DealLock {

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        //thread1 先获取锁1 然后等待一会再去获取锁2， thread2 则相反
        Thread thread1 = new Thread(() -> {
            try {
                lock1.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock2.lock();
                System.out.println("thread1 is running");
            } finally {
                lock1.unlock();
            }
        }, "thread1");
        Thread thread2 = new Thread(() -> {
            try {
                lock2.lock();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lock1.lock();
                System.out.println("thread2 is running");
            } finally {
                lock2.unlock();
            }
        }, "thread2");
        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();
        System.out.println("main thread end");
    }
}
