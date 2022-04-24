package top.kwseeker.jvm.runtime;

/**
 * 大对象堆上分配测试(直接放老年代)
 *  -Xms128M -Xmx128M -XX:+PrintGCDetails -XX:+PrintFlagsFinal -XX:+UseSerialGC -XX:PretenureSizeThreshold=10000000
 *  ！！！ PretenureSizeThreshold 只适用于 Serial 和 ParNew 两个GC收集器。
 *
 * 初始栈空间
 * Heap
 *  def new generation   total 39296K, used 3497K [0x00000000f8000000, 0x00000000faaa0000, 0x00000000faaa0000)
 *   eden space 34944K,  10% used [0x00000000f8000000, 0x00000000f836a748, 0x00000000fa220000)
 *   from space 4352K,   0% used [0x00000000fa220000, 0x00000000fa220000, 0x00000000fa660000)
 *   to   space 4352K,   0% used [0x00000000fa660000, 0x00000000fa660000, 0x00000000faaa0000)
 *  tenured generation   total 87424K, used 0K [0x00000000faaa0000, 0x0000000100000000, 0x0000000100000000)
 *    the space 87424K,   0% used [0x00000000faaa0000, 0x00000000faaa0000, 0x00000000faaa0200, 0x0000000100000000)
 */
public class BigObjectAllocOnHeapTest {

    public static void main(String[] args) {
        byte[] bigObj = new byte[15*1000*1024]; // > 10000000 Byte
        //Heap
        // def new generation   total 39296K, used 3497K [0x00000000f8000000, 0x00000000faaa0000, 0x00000000faaa0000)
        //  eden space 34944K,  10% used [0x00000000f8000000, 0x00000000f836a748, 0x00000000fa220000)
        //  from space 4352K,   0% used [0x00000000fa220000, 0x00000000fa220000, 0x00000000fa660000)
        //  to   space 4352K,   0% used [0x00000000fa660000, 0x00000000fa660000, 0x00000000faaa0000)
        // tenured generation   total 87424K, used 15000K [0x00000000faaa0000, 0x0000000100000000, 0x0000000100000000)
        //   the space 87424K,  17% used [0x00000000faaa0000, 0x00000000fb946010, 0x00000000fb946200, 0x0000000100000000)
    }
}
