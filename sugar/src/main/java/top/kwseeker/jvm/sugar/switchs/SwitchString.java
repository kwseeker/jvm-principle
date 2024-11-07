package top.kwseeker.jvm.sugar.switchs;

/**
 * 语法糖：switch 支持 String
 * switch 支持 String 是靠语法糖
 * 使用 CFR --decodestringswitch false 可以阻止CFR解析此语法糖
 */
public class SwitchString {

    public static void main(String[] args) {
        System.out.println(greet("Alice"));
        System.out.println(greet("Bob"));
    }

    private static String greet(String name) {
        switch (name) {
            case "Alice":
                return "Hi, Alice";
            case "Bob":
                return "Hi, Bob";
            default:
                return "Hi, Stranger";
        }
    }
    //private static String greet(String name) {
    //    String string = name;
    //    int n = -1;
    //    switch (string.hashCode()) {
    //        case 63350368: {
    //            if (!string.equals("Alice")) break;
    //            n = 0;
    //            break;
    //        }
    //        case 66965: {
    //            if (!string.equals("Bob")) break;
    //            n = 1;
    //        }
    //    }
    //    switch (n) {
    //        case 0: {
    //            return "Hi, Alice";
    //        }
    //        case 1: {
    //            return "Hi, Bob";
    //        }
    //    }
    //    return "Hi, Stranger";
    //}
}
