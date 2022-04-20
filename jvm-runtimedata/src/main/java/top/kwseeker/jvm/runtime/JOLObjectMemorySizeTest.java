package top.kwseeker.jvm.runtime;

import org.openjdk.jol.info.ClassLayout;

/**
 * OpenJDK官方提供的拓展包可以查看对象占用内存的大小
 *  -XX:-UseCompressedOops 关闭指针压缩
 *  -XX:+UseCompressedOops 默认开启的压缩所有对象指针, -XX:+PrintFlagsFinal 可以看到（即开启了对象指针压缩）
 *      bool UseCompressedOops                        := true                                {lp64_product}
 *  -XX:+UseCompressedClassPointers 默认开启的压缩对象头里的类型指针 Klass Pointer (只压缩对象头中的类型指针)
 *      bool UseCompressedClassPointers               := true                                {lp64_product}
 *
 * @link https://github.com/openjdk/jol jol-samples 里面提供了28个示例
 */
public class JOLObjectMemorySizeTest {

    public static void main(String[] args) {
        //java.lang.Object object internals:
        // OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
        //      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        //      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)              //对象头-markword（前两行）:８bytes
        //      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)     //对象头-类型指针：４bytes
        //     12     4        (loss due to the next object alignment)                                                                      //对齐填充：前面占用12字节，最小的８的公倍数是16, 所以填充４bytes
        //Instance size: 16 bytes                                                                                                           //即 Object对象占了16字节
        //Space losses: 0 bytes internal + 4 bytes external = 4 bytes total                                                                 //空间浪费：这里只有因为对齐浪费了4bytes
        //jol 新版本（0.16）将对象头前两块合并了，但是VALUE不直观了
        //java.lang.Object object internals:
        //OFF  SZ   TYPE DESCRIPTION               VALUE
        //  0   8        (object header: mark)     0x0000000000000001 (non-biasable; age: 0)
        //  8   4        (object header: class)    0xf80001e5
        // 12   4        (object alignment gap)
        //Instance size: 16 bytes
        //Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
        ClassLayout layout = ClassLayout.parseInstance(new Object());
        System.out.println(layout.toPrintable());

        //[I object internals:
        // OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
        //      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        //      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        //      8     4        (object header)                           6d 01 00 f8 (01101101 00000001 00000000 11111000) (-134217363)
        //     12     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)          //多了个数组长度4bytes
        //     16     0    int [I.<elements>                             N/A                                                            //实例数据，因为空的所以0bytes
                                                                                                                                        //因为前面正好16bytes，对齐不用浪费了
        //Instance size: 16 bytes
        //Space losses: 0 bytes internal + 0 bytes external = 0 bytes total
        ClassLayout layout1 = ClassLayout.parseInstance(new int[]{});
        System.out.println(layout1.toPrintable());

        //top.kwseeker.jvm.runtime.JOLObjectMemorySizeTest$A object internals:
        // OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
        //      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
        //      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
        //      8     4                    (object header)                           99 cb 00 f8 (10011001 11001011 00000000 11111000) (-134165607)
        //     12     4                int A.id                                      0

        //     16     8               long A.c                                       0                      //可以看到居然和代码出现的位置不一样，其实是JVM自动做了字段重排为了减小内存间隙
        //     24     1               byte A.b                                       0                      //字段重排规则：8->4->2->1，还是8字节对齐 TODO: 字段重排详细规则？
        //     25     3                    (alignment/padding gap)
        //     28     4   java.lang.String A.name                                    null
        //     32     4   java.lang.Object A.o                                       null
        //     36     4                    (loss due to the next object alignment)
        //Instance size: 40 bytes
        //Space losses: 3 bytes internal + 4 bytes external = 7 bytes total
        ClassLayout layout2 = ClassLayout.parseInstance(new A());
        System.out.println(layout2.toPrintable());
    }

    public static class A {
        //8B mark word
        //4B Klass Pointer 如果关闭压缩-XX:-UseCompressedClassPointers或-XX:-UseCompressedOops，则占用8B
        int id;         //4B
        String name;    //4B 如果关闭压缩-XX:-UseCompressedOops，则占用8B
        byte b;         //1B
        Object o;       //4B 如果关闭压缩-XX:-UseCompressedOops，则占用8B
        long c;         //8B
    }
}
