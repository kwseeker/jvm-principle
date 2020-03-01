package top.kwseeker.jvm.reference;

import org.junit.Test;

import java.util.WeakHashMap;

/**
 * 弱引用测试
 * 弱引用：系统GC时可能会回收
 */
public class WeakReferenceTest {

    /**
     * 测试：
     *  创建一个可达的弱引用对象，然后手动调用GC，等待一段时间后查看对象是否还存在（回收因为优先级比较低并不会立即执行）
     * JVM参数：-XX:+PrintGCDetails
     *
     * 结果：
     *  weakHashMap size: 3
     *  weakHashMap size: 1
     */
    @Test
    public void testWeakReference() throws Exception {
        WeakHashMap<Object, Object> weakHashMap = new WeakHashMap<>();
        Object key1 = new Object();
        weakHashMap.put(key1, new Object());
        weakHashMap.put(new Object(), new Object());
        weakHashMap.put(new Object(), new Object());
        System.out.println("weakHashMap size: " + weakHashMap.size());
        System.gc();
        Thread.sleep(1000);
        System.out.println("weakHashMap size: " + weakHashMap.size());
    }
}