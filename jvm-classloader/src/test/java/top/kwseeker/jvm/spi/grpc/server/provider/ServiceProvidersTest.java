package top.kwseeker.jvm.spi.grpc.server.provider;

import org.junit.Test;
import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ServiceLoader;

import static sun.misc.Launcher.getBootstrapClassPath;

public class ServiceProvidersTest {

    @Test
    public void testLoadClassByPriority() {
        //这里先尝试从AppClassLoader开始双亲委派，查找"top.kwseeker.jvm.spi.grpc.server.provider.ServerProvider"
        // configs = loader.getResources(fullName);
        Iterable<ServerProvider> iter = ServiceLoader.load(ServerProvider.class, ServerProvider.class.getClassLoader());
        if (!iter.iterator().hasNext()) {
            //SystemClassLoader 负责在JVM被启动时，加载来自 -classpath或者java.class.path指定的JAR类包和类路径
            //configs = ClassLoader.getSystemResources(fullName);
            System.out.println("exec getSystemResources() ...");
            iter = ServiceLoader.load(ServerProvider.class);
        }

        //加载实现类后，遍历读取到ArrayList

        //然后再按优先级进行排序

        //获取第一个，即优先级最高的那个
    }

    /**
     * 获取资源也符合双亲委派规则, 会先在各自的jar中搜索资源
     */
    @Test
    public void testGetResource() {
        String fullName = "META-INF/services/top.kwseeker.jvm.spi.grpc.server.provider.ServerProvider";
        //也是通过获取ServerProvider.class的类加载器再获取资源的，不过和后一种方式不同的是，这里会先resoleName(), 拼接了当前java所在包路径，
        // 即在ServerProvider所在路径下查找fullName文件
        URL url1 = ServerProvider.class.getResource(fullName);

        ClassLoader cl = ServerProvider.class.getClassLoader(); //AppClassLoader
        URL url2 = cl.getResource(fullName);

        //委派流程
        // 1 BootstrapClassLoader 从 jdk基本包中查找资源
        URLClassPath urlClassPath = getBootstrapClassPath();
        Resource res = urlClassPath.getResource(fullName);
        // 2 ExtClassLoader 从 ext 包中查找资源
        ClassLoader extCl = cl.getParent();
        URL url3 = ((URLClassLoader) extCl).findResource(fullName);
        // 3 AppClassLoader 从 当前项目根目录查找资源
        URL url4 = ((URLClassLoader) cl).findResource(fullName);
        // 4 某自定义Class 从 class所在package查找资源
        //URL url5 = MyClass.class.getResource(fullName);
        URL url6 = String.class.getResource("Object.class");
    }
}