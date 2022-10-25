package top.kwseeker.jvm.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于JDK1.8将字符串常量池（注意区分静态常量池、运行时常量池和字符串常量池）放入堆空间，这里会消耗堆空间
 */
public class StringOomMock {

    static String base = "string";  //放入常量池

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i=0;i< Integer.MAX_VALUE;i++){
            String str = base + base;
            base = str;
            list.add(str.intern()); //str.intern() 将str值放入字符串常量池，并返回指向常量池中这个值的引用
        }
    }
}