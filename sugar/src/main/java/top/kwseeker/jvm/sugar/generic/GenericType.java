package top.kwseeker.jvm.sugar.generic;

import java.util.HashMap;
import java.util.Map;

/**
 * 语法糖：泛型类型
 * 泛型类型，在编译时校验类型，在编译后，泛型类型会被擦除
 * --removebadgenerics false 没效果, 可能不是这个参数
 * 改用 JAD 反编译
 * ./tools/jad sugar/target/classes/top/kwseeker/jvm/sugar/generic/GenericType.class
 */
public class GenericType {

    public static void main(String[] args) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("Arvin", 18);
        map.put("Bob", 19);
    }
}
// JAD 反编译结果
//public class GenericType
//{
//
//    public GenericType()
//    {
//    }
//
//    public static void main(String args[])
//    {
//        Map map = new HashMap();
//        map.put("Arvin", Integer.valueOf(18));
//        map.put("Bob", Integer.valueOf(19));
//    }
//}
