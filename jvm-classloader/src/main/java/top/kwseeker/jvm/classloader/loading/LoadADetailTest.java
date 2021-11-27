package top.kwseeker.jvm.classloader.loading;

import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;

public class LoadADetailTest {

    public static void main(String[] args) throws IOException {
        String aClassName = "top/kwseeker/jvm/classloader/loading/A";
        URL aUrl = new URL("file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/target/classes/");
        //省略双亲委派流程，直接说AppClassLoader加载流程
        URL[] urls = {aUrl};
        AccessControlContext acc = AccessController.getContext();
        URLClassPath aClassPath = new URLClassPath(urls, acc);

        //findClass
        Resource aRes = aClassPath.getResource(aClassName.replace('.', '/').concat(".class"), false);
        URL url1 = aRes.getCodeSourceURL();

        //defineClass
        byte[] b = aRes.getBytes();
        //省略一些类校验：res.getManifest() isSealed() res.getCodeSigners() getProtectionDomain()

        //最终由 ClassLoader 的 native方法 defineClass1 实现加载
        //Class<?> c = defineClass1(name, b, off, len, protectionDomain, source);
    }
}
