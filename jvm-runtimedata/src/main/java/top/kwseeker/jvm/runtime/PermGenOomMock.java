package top.kwseeker.jvm.runtime;

import java.io.File;
import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * JDK1.8 LinuxＭint 测试很快吃掉４G内存后就不继续吃内存了，也没有报元空间OOM？？？
 *  为何Heap，4G内存都吃完了，元空间才消耗了仅仅４M(类信息也仅仅４00K)
 *
 *  Heap
 *  PSYoungGen      total 709632K, used 275968K [0x00000007ab300000, 0x00000007f1680000, 0x00000007fe000000)
 *   eden space 275968K, 100% used [0x00000007ab300000,0x00000007bc080000,0x00000007bc080000)
 *   from space 433664K, 0% used [0x00000007bc080000,0x00000007bc080000,0x00000007d6800000)
 *   to   space 418816K, 0% used [0x00000007d7d80000,0x00000007d7d80000,0x00000007f1680000)
 *  ParOldGen       total 2714624K, used 2714500K [0x0000000705800000, 0x00000007ab300000, 0x00000007ab300000)
 *   object space 2714624K, 99% used [0x0000000705800000,0x00000007ab2e1050,0x00000007ab300000)
 *  Metaspace       used 3780K, capacity 4540K, committed 4864K, reserved 1056768K
 *   class space    used 399K, capacity 428K, committed 512K, reserved 1048576K
 *
 * jinfo -flag MaxMetaspaceSize 19149
 * -XX:MaxMetaspaceSize=18446744073709547520    //这个值默认170亿G,相当于无限了
 *
 * -XX:MetaspaceSize=40m -XX:MaxMetaspaceSize=40m -XX:+PrintGCDetails
 */
public class PermGenOomMock{

    public static void main(String[] args) {
        URL url = null;
        List<ClassLoader> classLoaderList = new ArrayList<>();
        List<Object> classList = new ArrayList<>();
        ClassLoadingMXBean loadingMXBean  = ManagementFactory.getClassLoadingMXBean();
        try {
            url = new File("/tmp").toURI().toURL();
            URL[] urls = {url};
            while (true){
                ClassLoader loader = new URLClassLoader(urls);
                //消耗堆,
                //保存加载器防止类被卸载
                classLoaderList.add(loader);
                //每个ClassLoader对象,对同一个类进行加载,会产生不同的Class对象
                loader.loadClass("top.kwseeker.jvm.runtime.Test");

                //System.out.println("total: " + loadingMXBean.getTotalLoadedClassCount());
                //System.out.println("active: " + loadingMXBean.getLoadedClassCount());
                //System.out.println("unloaded: " + loadingMXBean.getUnloadedClassCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
