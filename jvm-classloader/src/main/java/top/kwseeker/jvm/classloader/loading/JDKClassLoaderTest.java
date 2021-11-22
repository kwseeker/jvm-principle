package top.kwseeker.jvm.classloader.loading;

import sun.misc.Launcher;

import java.net.URL;
import java.util.Properties;

public class JDKClassLoaderTest {

    public static void main(String[] args) {
        //String 位于 jre/lib/rt.jar ()
        System.out.println(String.class.getClassLoader());
        //DESKeyFactory 位于 jre/lib/ext/sunjce_provider.jar
        System.out.println(com.sun.crypto.provider.DESKeyFactory.class.getClassLoader().getClass().getName());
        //本地 classpath 中的类
        System.out.println("classpath: " + JDKClassLoaderTest.class.getResource("/").getPath());
        System.out.println("classpath: " + Thread.currentThread().getContextClassLoader().getResource("").getPath());
        System.out.println(JDKClassLoaderTest.class.getClassLoader().getClass().getName());

        System.out.println();
        ClassLoader appClassLoader = ClassLoader.getSystemClassLoader();
        ClassLoader extClassloader = appClassLoader.getParent();
        ClassLoader bootstrapLoader = extClassloader.getParent();
        System.out.println("the bootstrapLoader : " + bootstrapLoader);
        System.out.println("the extClassloader : " + extClassloader);
        System.out.println("the appClassLoader : " + appClassLoader);

        System.out.println();
        System.out.println("bootstrapLoader加载以下文件：");
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i]);
        }

        System.out.println();
        System.out.println("extClassloader加载以下文件：");
        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println();
        System.out.println("appClassLoader加载以下文件：");
        Properties properties = System.getProperties();
        System.out.println(System.getProperty("java.class.path"));
    }
}
