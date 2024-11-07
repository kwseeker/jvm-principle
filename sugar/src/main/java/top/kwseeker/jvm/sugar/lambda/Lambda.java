package top.kwseeker.jvm.sugar.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * 语法糖：lambda 表达式
 * 借助 LambdaMetafactory.metafactory 实现函数式接口的方法
 * java -jar tools/cfr-0.152.jar --decodelambdas false sugar/target/classes/top/kwseeker/jvm/sugar/lambda/Lambda.class
 */
public class Lambda {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("a", "b", "c");

        // forEach 接收一个 lambda 表达式
        list.forEach(s -> System.out.println(s));

        list.forEach(System.out::println);
    }
}
//public class Lambda {
//    public static void main(String[] args) {
//        List<String> list = Arrays.asList("a", "b", "c");
//
//        list.forEach((Consumer<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$main$0(java.lang.String ), (Ljava/lang/String;)V)());
//
//        PrintStream printStream = System.out;
//        printStream.getClass();
//        list.forEach((Consumer<String>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, println(java.lang.String ), (Ljava/lang/String;)V)((PrintStream)printStream));
//    }
//
//    private static /* synthetic */ void lambda$main$0(String s) {
//        System.out.println(s);
//    }
//}
