package top.kwseeker.jvm.constantpool;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 String.intern() 作为同步锁对象
 * -Xms200m -Xmx200m -XX:+PrintStringTableStatistics
 *
 * 看到有文章说：“java原生的intern，不会自动清理常量池，可能会导致频繁的GC”，所以做了这个测试，
 * 但是实验结果看会自动清理字符串常量池，怀疑文章是错的 或者 说的JDK的旧版本。
 */
public class StringInternLockTest {

    public static final String LOCK_PREFIX = "lock_";

    public static void main(String[] args) throws InterruptedException {
        String name = ManagementFactory.getRuntimeMXBean().getName();
        System.out.println(name);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024*1024; i++) {
            sb.append("A");
        }
        String longStr = sb.toString();
        //String s1 = longStr.intern();
        //System.out.println(s1 == longStr);  //true
        Thread.sleep(5000);

        for (int i = 1; i <= 60; i++) {
            String s2 = (longStr + "" + i);
            synchronized ((LOCK_PREFIX + s2).intern()) {
                System.out.println("sync" + i);
            }
            Thread.sleep(500);
        }

        //List<String> refs = new ArrayList<>();
        //for (int i = 0; i <= 60; i++) {
        //    refs.add(longStr + "" + i);
        //    Thread.sleep(500);
        //    System.out.println(i);
        //}

        //List<Object> refs = new ArrayList<>();
        //for (int i = 0; i <= 500; i++) {
        //    for (int j = 0; j < 10240; j++) {
        //        refs.add(new Object());
        //      }
        //    Thread.sleep(100);
        //}

        System.out.println("开始回收");
        //refs.clear();
        //refs = null;
        System.gc();                //没有将refs全部回收？字符串常量池和堆其他空间回收机制不一样么？
        Thread.sleep(10000);
    }
}
