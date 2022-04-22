package top.kwseeker.jvm.runtime;

import lombok.Data;

/**
 * 逃逸分析，未逃逸对象在栈上分配
 * -XX:+DoEscapeAnalysis JDK7后默认开启
 * -XX:-DoEscapeAnalysis
 *
 * JDK7后默认开启标量替换: time cost: 4
 * -Xmx100m -Xms100m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+PrintFlagsFinal
 * 关闭标量替换: time cost: 1057
 * -Xmx100m -Xms100m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+PrintFlagsFinal -XX:-EliminateAllocations  //开启逃逸分析但是去掉标量替换后发现有怎么都有GC发生（即分配到堆上了）
 */
public class AllocOnStackTest {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            allocSimple();
            //alloc();
        }
        long end = System.currentTimeMillis();
        System.out.println("time cost: " + (end - start));
    }

    //换了个小对象，开启逃逸分析但是关闭标量替换，还是分配到了堆上，并不是因为对象太大的原因。
    //有种说法：
    //HotSpot虚拟机只是开启逃逸分析并未使用栈上分配，也就是说栈上还是只能存基本类型数据或者引用，对象还是在堆上分配，除非使用了标量替换。可能是为了防止栈上分配的对象过多导致栈溢出。
    private static void allocSimple() {
        Simple simple = new Simple();
        simple.setId(1);
    }

    private static void alloc() {
        Addr addr = new Addr();
        addr.setAddress("ShenZhen");
        addr.setLongitude(114.03F);
        addr.setLatitude(22.32F);
        User user = new User();
        user.setId(1);
        user.setName("Arvin");
        user.setAddr(addr);
        //标量替换大概类似下面代码 (具体是什么样的后面看源码吧)
        //int userId = 1;
        //String userName = "Arvin";
        //String userAddrAddress = "ShenZhen";
        //float userAddrLongitude = 114.03F;
        //float userAddrLatitude = 22.32F;
    }

    @Data
    private static class Simple {
        private int id;
    }

    @Data
    private static class User {
        private int id;
        private String name;
        private Addr addr;
    }

    @Data
    private static class Addr {
        private String address;
        private float longitude;
        private float latitude;
    }
}
