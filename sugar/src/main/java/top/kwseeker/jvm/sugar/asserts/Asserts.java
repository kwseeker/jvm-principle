package top.kwseeker.jvm.sugar.asserts;

/**
 * 语法糖：assert
 * 内部添加了个 $assertionsDisabled 字段，并和 assert 后的条件一起组成 if 条件，如果条件不满足就抛出 AssertionError
 * 注意断言错误是抛出错误 AssertionError，然后就退出程序，只适用于断言很重要的条件，重要到条件不满足就只能退出程序
 * java -jar tools/cfr-0.152.jar --sugarasserts false sugar/target/classes/top/kwseeker/jvm/sugar/asserts/Asserts.class
 */
public class Asserts {

    public void something() {
        int a = 1, b = 1;
        assert a == b;
        System.out.println("a == b");
        assert a != b: "Error cause: a != b";
        System.out.println("a != b");
    }
}
//public class Asserts {
//    static final /* synthetic */ boolean $assertionsDisabled;
//
//    public void something() {
//        boolean a = true;
//        boolean b = true;
//        if (!$assertionsDisabled && a != b) {
//            throw new AssertionError();
//        }
//        System.out.println("a == b");
//        if (!$assertionsDisabled && a == b) {
//            throw new AssertionError((Object)"Error cause: a != b");
//        }
//        System.out.println("a != b");
//    }
//
//    static {
//        $assertionsDisabled = !Asserts.class.desiredAssertionStatus();
//    }
//}