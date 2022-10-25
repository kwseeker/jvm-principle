package top.kwseeker.jvm.constantpool;

/**
 * Spring intern() gc 测试
 * intern()方法的作用是如果字符串不在常量池中，则将该字符串的引用放入常量池，并将引用返回，如果在常量池中，则将常量池中字符串的引用返回。
 */
public class StringInternTest {

    public static void main(String[] args) {
        String s1 = new String("hello");        //s1指向堆
        String s2 = s1.intern();                        //s2指向常量池（JDK8常量池也在堆中分配）
        System.out.println(s1 == s2);                   //false
        System.out.println(s1 == "hello");              //false
        System.out.println(s2 == "hello");              //true

        String s3 = new StringBuilder().append(s1).append(s2).toString();
        String s4 = s3.intern();
        System.out.println(s3 == s4);                   //true
        System.out.println(s3 == "hellohello");         //true
    }
}
