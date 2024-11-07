package top.kwseeker.jvm.sugar.generic;

/**
 * 由于经过类型擦除,所有的泛型类实例都关联到同一份字节码上,泛型类的所有静态变量是共享的
 */
public class StaticFieldInGenericType {

    public static void main(String[] args) {
        Inner<String> inner1 = new Inner<>();
        inner1.staticField = 1;
        Inner<Integer> inner2 = new Inner<>();
        inner2.staticField = 2;
        System.out.println(inner1.staticField);
    }

    static class Inner<T> {
        //public static T staticField; //泛型类型字段不能是静态的
        public static int staticField = 0; //带泛型的类型内部静态字段 JDK16+ 才支持
        public T field;
    }
}
//public class StaticFieldInGenericType
//{
//    static class Inner
//    {
//
//        public static int staticField = 0;
//        public Object field;
//
//
//        Inner()
//        {
//        }
//    }
//
//
//    public StaticFieldInGenericType()
//    {
//    }
//
//    public static void main(String args[])
//    {
//        Inner inner1 = new Inner();
//        Inner _tmp = inner1;
//        Inner.staticField = 1;
//        Inner inner2 = new Inner();
//        Inner _tmp1 = inner2;
//        Inner.staticField = 2;
//        Inner _tmp2 = inner1;
//        System.out.println(Inner.staticField);
//    }
//}