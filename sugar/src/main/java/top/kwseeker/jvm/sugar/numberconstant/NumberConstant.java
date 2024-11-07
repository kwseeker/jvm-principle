package top.kwseeker.jvm.sugar.numberconstant;

/**
 * 语法糖：带下划线的数值字面量
 * 编译阶段会将下划线去掉
 * java -jar tools/cfr-0.152.jar sugar/target/classes/top/kwseeker/jvm/sugar/numberconstant/NumberConstant.class
 */
public class NumberConstant {

    public static void main(String[] args) {
        int a = 1_000_000_000;
        double b = 1_000_000_000.000_5;
        System.out.println(a);
        System.out.println(b);
    }
}
//public class NumberConstant {
//    public static void main(String[] args) {
//        int a = 1000000000;
//        double b = 1.0000000000005E9;
//        System.out.println(a);
//        System.out.println(b);
//    }
//}
