package top.kwseeker.jvm.reference;

import org.junit.Test;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 软引用测试
 * 软引用:内存不足时会被回收
 */
public class SoftReferenceTest {

    /**
     * 测试：
     *  调整堆内存大小为30M，先创建一个3M的软引用对象，
     *  然后循环创建500K强引用对象，直到内存不足，查看软引用是否会被回收
     * JVM参数: -Xmx30m -Xmn10m -XX:+PrintGCDetails -XX:+HeapDumpOnOutOfMemoryError
     *
     * 结果：5+44/2=27M时，系统把5M软引用的空间给回收了。 TODO：但是为何是27M的时候？
     * count: 43 srObj==null?false
     * [Full GC (Ergonomics) [PSYoungGen: 7841K->7680K(9216K)] [ParOldGen: 20197K->20197K(20480K)] 28039K->27878K(29696K), [Metaspace: 4950K->4950K(1056768K)], 0.0040896 secs] [Times: user=0.01 sys=0.00, real=0.00 secs]
     * [Full GC (Allocation Failure) [PSYoungGen: 7680K->2560K(9216K)] [ParOldGen: 20197K->20142K(20480K)] 27878K->22702K(29696K), [Metaspace: 4950K->4946K(1056768K)], 0.0054722 secs] [Times: user=0.02 sys=0.01, real=0.00 secs]
     * count: 44 srObj==null?true
     */
    @Test
    public void testSoftReference() {
        try {
            SoftReference<Object> srObj = new SoftReference<>(new byte[5*1024*1024]);
            List<byte[]> list = new ArrayList<>();

            for (int i=0; ; i++) {
                String isNull = (srObj.get() == null) ? "true":"false";
                System.out.println("count: " + i + " srObj==null?" + isNull);

                list.add(new byte[512*1024]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
