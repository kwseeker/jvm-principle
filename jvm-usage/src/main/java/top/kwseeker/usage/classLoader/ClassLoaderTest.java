package top.kwseeker.usage.classLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * ClassLoader#getResources()
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws IOException {
        //String packageName = "java.util.concurrent";
        //String packageName = "top.kwseeker.usage.classLoader";
        String packageName = "org.slf4j.event";

        Enumeration<URL> urlEnumeration;
        ClassLoader classLoader = ClassLoaderTest.class.getClassLoader();

        //getResources
        urlEnumeration = classLoader.getResources(packageName.replace(".", "/"));
        URL url;
        while (urlEnumeration.hasMoreElements()) {
            url = urlEnumeration.nextElement();
            System.out.println(url);
        }

        //获取BootstrapClassLoader的加载路径
        URL[] urls = sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (URL url1 : urls) {
            System.out.println(url1);
        }



    }
}
