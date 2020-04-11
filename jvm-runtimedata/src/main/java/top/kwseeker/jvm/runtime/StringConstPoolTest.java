package top.kwseeker.jvm.runtime;

public class StringConstPoolTest {

    private static String getString() {
        return "c";
    }

    public static void main(String[] args) {

        //JDK1.8

        String str1 = "abc";
        String str2 = new String("abc");                //new 会重新在堆内存中创建新对象，且不在常量池
        System.out.println(str1 == str2);           //false

        String str3 = new String("abc");
        System.out.println(str3 == str2);           //false

        String str4 = "a" + "b";                                //JVM在编译时自动转换为“ab”
        System.out.println(str4 == "ab");           //true

        final String s = "a";                                   //注意这里是字面量
        String str5 = s + "b";                                  //编译期可以确定str="ab"
        System.out.println(str5 == "ab");           //true

        String s1 = "a";
        String s2 = "b";
        String str6 = s1 + s2;                                  //运行时才确定str6的值,相当于new新对象
        System.out.println(str6 == "ab");           //false

        String str7 = "abc".substring(0, 2);                    //运行时确定
        System.out.println(str7 == "ab");           //false

        String str8 = "abc".toUpperCase();                      //运行时确定
        System.out.println(str8 == "ABC");          //false

        String s3 = "abc";
        String s4 = "ab" + getString();                         //运行时确定
        System.out.println(s3 == s4);               //false

        String s5 = "a";
        String s6 = "abc";
        String s7 = s5 + "bc";                                  //new “abc” 立即更新到常量池,发现常量池已经有“abc”,intern()返回常量池中“abc”地址
        System.out.println(s6 == s7.intern());      //true

        String c = "world";
        System.out.println(c.intern() == c);        //true
        String d = new String("mike");
        System.out.println(d.intern() == d);        //false

        String e = new String("jo") + new String("hn");     //e指向堆中的字符串“john”, e.intern()将“john”加入常量池发现常量池并没有“john”,
                                                                            // intern()将堆中“john”的地址放到常量池，并返回这个地址值
        System.out.println(e.intern() == e);        //true

        String s11 = new String("1");
        String s22 = "1";
        s11.intern();
        System.out.println(s11 == s22);

        String s33 = new String("1") + new String("1");
        String s44 = "11";
        s33.intern();
        System.out.println(s33 == s44);
    }
}
