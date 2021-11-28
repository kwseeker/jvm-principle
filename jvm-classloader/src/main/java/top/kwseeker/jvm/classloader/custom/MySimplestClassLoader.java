package top.kwseeker.jvm.classloader.custom;

import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;

/**
 * 要实现一个最简单的类加载器，只需要实现最核心的功能（查找并调用native方法加载，即findClass()方法）
 */
public class MySimplestClassLoader extends ClassLoader {

    private static final URL[] URLS = {getSystemResource("")};

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        AccessControlContext acc = AccessController.getContext();
        URLClassPath ucp = new URLClassPath(URLS, acc);

        //查找类class文件
        String path = name.replace('.', '/').concat(".class");
        Resource res = ucp.getResource(path, false);

        //调用ClassLoader defineClass() 方法进行实际类加载
        try {
            byte[] b = res.getBytes();
            return defineClass(name, b, 0, b.length);
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(name, e);
        }
    }

    public static void main(String[] args) throws Exception {
        String aClassName = "top.kwseeker.jvm.classloader.custom.A";

        MySimplestClassLoader mySimplestClassLoader = new MySimplestClassLoader();
        Class<?> clazz1 = mySimplestClassLoader.findClass(aClassName);
        Class<?> clazz2 = Thread.currentThread().getContextClassLoader().loadClass(aClassName);

        System.out.println(clazz1 == clazz2);
    }
}
