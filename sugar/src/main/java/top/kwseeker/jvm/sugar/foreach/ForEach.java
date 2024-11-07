package top.kwseeker.jvm.sugar.foreach;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 语法糖：for-each
 * java -jar tools/cfr-0.152.jar --arrayiter false --collectioniter false sugar/target/classes/top/kwseeker/jvm/sugar/foreach/ForEach.class
 */
public class ForEach {

    public static void main(String[] args) {
        int[] intArray = {1, 2, 3};
        for (int i : intArray) {
            System.out.println(i);
        }
        List<String> list = Arrays.asList("a", "b", "c");
        for (String s : list) {
            System.out.println(s);
        }
        //遍历时删除, 为何会抛出 ConcurrentModificationException 异常？
        List<String> list2 = new ArrayList<>();
        list2.add("a");
        list2.add("b");
        list2.add("c");
        //for (String s : list2) {
        //    System.out.println(s);
        //    list2.remove(s);
        //}
        Iterator iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            String s = (String) iterator2.next();
            System.out.println(s);
            list2.remove(s);
        }
    }
}
//public class ForEach {
//    public static void main(String[] args) {
//        int[] intArray;
//        int[] nArray = intArray = new int[]{1, 2, 3, 4, 5};
//        int n = nArray.length;
//        for (int i = 0; i < n; ++i) {
//            int i2 = nArray[i];
//            System.out.println(i2);
//        }
//        List<String> list = Arrays.asList("a", "b", "c");
//        Iterator<String> iterator = list.iterator();
//        while (iterator.hasNext()) {
//            String s = iterator.next();
//            System.out.println(s);
//        }
//    }
//}
