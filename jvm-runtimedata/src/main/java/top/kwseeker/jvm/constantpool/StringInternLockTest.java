package top.kwseeker.jvm.constantpool;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用 String.intern() 作为同步锁对象
 * -Xms100m -Xmx100m
 */
public class StringInternLockTest {

    public static final String LOCK_PREFIX = "lock_";

    public static void main(String[] args) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1024*1024; i++) {
            sb.append("A");
        }
        String longStr = sb.toString();
        String s1 = longStr.intern();
        System.out.println(s1 == longStr);  //true

        //for (int i = 1; i <= 80; i++) {
        //    String s2 = (longStr + "" + i);
        //    synchronized ((LOCK_PREFIX + s2).intern()) {    // synchronized 会一直保持引用么？
        //        System.out.println("sync" + i);
        //    }
        //}

        //List<String> refs = new ArrayList<>();
        //for (int i = 0; i <= 80; i++) {
        //    String s2 = (longStr + "" + i);
        //    String s3 = (LOCK_PREFIX + s2).intern();
        //    refs.add(s3);
        //}

        List<String> refs = new ArrayList<>();
        for (int i = 0; i <= 80; i++) {
            refs.add(longStr + "" + i);
        }

        System.out.println();
        refs.clear();
        System.gc();                //没有将refs全部回收？字符串常量池和堆其他空间回收机制不一样么？
        Thread.sleep(10000);
        System.gc();
        System.out.println();
    }
}
