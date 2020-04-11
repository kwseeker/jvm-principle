package top.kwseeker.jvm.runtime;

import java.util.ArrayList;
import java.util.List;

/**
 * 由于JDK1.8将常量池放入堆空间，这里会消耗堆空间
 */
public class StringOomMock {

    static String base = "string";  //放入常量池

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        for (int i=0;i< Integer.MAX_VALUE;i++){
            String str = base + base;
            base = str;
            list.add(str.intern());
        }
    }
}