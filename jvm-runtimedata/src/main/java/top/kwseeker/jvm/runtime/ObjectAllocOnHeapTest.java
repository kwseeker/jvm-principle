package top.kwseeker.jvm.runtime;

/**
 * 对象内存分配流程测试
 * -Xms128M -Xmx128M -XX:+PrintGCDetails -XX:+PrintFlagsFinal -XX:-UseAdaptiveSizePolicy
 * eden from to parOldGen 默认并不是想象中严格的 8:1:1:20，以为是因为+UseAdaptiveSizePolicy(默认开启)修改了，但是关闭此参数还是一样
 * Heap
 *  PSYoungGen      total 38400K, used 3336K [0x00000000fd580000, 0x0000000100000000, 0x0000000100000000)
 *   eden space 33280K, 10% used [0x00000000fd580000,0x00000000fd8c21a8,0x00000000ff600000)
 *   from space 5120K, 0% used [0x00000000ffb00000,0x00000000ffb00000,0x0000000100000000)
 *   to   space 5120K, 0% used [0x00000000ff600000,0x00000000ff600000,0x00000000ffb00000)
 *  ParOldGen       total 87552K, used 0K [0x00000000f8000000, 0x00000000fd580000, 0x00000000fd580000)
 *   object space 87552K, 0% used [0x00000000f8000000,0x00000000f8000000,0x00000000fd580000)
 *  Metaspace       used 2981K, capacity 4494K, committed 4864K, reserved 1056768K
 *   class space    used 311K, capacity 386K, committed 512K, reserved 1048576K
 */
public class ObjectAllocOnHeapTest {

    public static void main(String[] args) {
        ObjectAllocOnHeapTest app = new ObjectAllocOnHeapTest();
        //初始eden区有将近30M空间剩余
        // 1 20M内存对象
        //app.test20M();
        // 2 30M内存对象，勉强放下
        //app.test30M();
        // 3 40M
        //app.test40M();

        app.testEdenIncr();
    }

    // 放到eden区，占用70%
    public void test20M() {
        byte[] obj = new byte[20*1000*1024];
    }

    // eden区勉强放下，占用100%
    public void test30M() {
        byte[] obj = new byte[30*1000*1024];
    }

    // 直接丢到老年代了
    // PSYoungGen      total 38400K, used 3336K [0x00000000fd580000, 0x0000000100000000, 0x0000000100000000)
    //  eden space 33280K, 10% used [0x00000000fd580000,0x00000000fd8c21a8,0x00000000ff600000)
    //  from space 5120K, 0% used [0x00000000ffb00000,0x00000000ffb00000,0x0000000100000000)
    //  to   space 5120K, 0% used [0x00000000ff600000,0x00000000ff600000,0x00000000ffb00000)
    // ParOldGen       total 87552K, used 40000K [0x00000000f8000000, 0x00000000fd580000, 0x00000000fd580000)
    //  object space 87552K, 45% used [0x00000000f8000000,0x00000000fa710010,0x00000000fd580000)
    public void test40M() {
        byte[] obj = new byte[40*1000*1024];
    }

    public void testEdenIncr() {
        byte[] obj = new byte[25*1000*1024];
        //Heap
        //PSYoungGen      total 38400K, used 31336K [0x00000000fd580000, 0x0000000100000000, 0x0000000100000000)
        //eden space 33280K, 94% used [0x00000000fd580000,0x00000000ff41a1c8,0x00000000ff600000)
        //from space 5120K, 0% used [0x00000000ffb00000,0x00000000ffb00000,0x0000000100000000)
        //to   space 5120K, 0% used [0x00000000ff600000,0x00000000ff600000,0x00000000ffb00000)
        //ParOldGen       total 87552K, used 0K [0x00000000f8000000, 0x00000000fd580000, 0x00000000fd580000)
        //object space 87552K, 0% used [0x00000000f8000000,0x00000000f8000000,0x00000000fd580000)
        //Metaspace       used 3156K, capacity 4494K, committed 4864K, reserved 1056768K
        //class space    used 338K, capacity 386K, committed 512K, reserved 1048576K
        byte[] obj1 = new byte[3*1000*1024];    //Eden:94% from/to:0%， 堆占用 31283.2K
        byte[] obj2 = new byte[3*1000*1024];    //Eden区空间不足（Allocation Failure）触发一次 Minor GC(Young GC)
                                                //将obj先尝试放到from区发现放不下直接丢到老年代，obj1放到了from区
        // [GC (Allocation Failure) [PSYoungGen: 30670K->3496K(38400K)] 30670K->28504K(125952K), 0.0174480 secs] [Times: user=0.08 sys=0.01, real=0.02 secs]
        // 即 回收前年轻代占用30670K回收后占用3496K年轻代总空间38400K, 回收前堆占用30670K回收后堆占用28504K堆总空间125952K, YoungGC耗时、用户耗时、系统耗时、实际耗时
        // 并没有回收obj和obj1,为何堆内存总占用还小了 30670K->28504K ??? 原因：GC计算并不是很准确。
        //Heap
        //PSYoungGen      total 38400K, used 6829K [0x00000000fd580000, 0x0000000100000000, 0x0000000100000000)
        //eden space 33280K, 10% used [0x00000000fd580000,0x00000000fd8c14b8,0x00000000ff600000)
        //from space 5120K, 68% used [0x00000000ff600000,0x00000000ff96a020,0x00000000ffb00000)
        //to   space 5120K, 0% used [0x00000000ffb00000,0x00000000ffb00000,0x0000000100000000)
        //ParOldGen       total 87552K, used 25008K [0x00000000f8000000, 0x00000000fd580000, 0x00000000fd580000)
        //object space 87552K, 28% used [0x00000000f8000000,0x00000000f986c010,0x00000000fd580000)
        //Metaspace       used 3146K, capacity 4494K, committed 4864K, reserved 1056768K
        //class space    used 338K, capacity 386K, committed 512K, reserved 1048576K
    }
}
