package top.kwseeker.jvm.classloader.custom;

import sun.misc.Resource;
import sun.misc.URLClassPath;

import java.io.IOException;
import java.net.URL;
import java.security.AccessController;

/**
 * 增强版自定义类加载器
 * 把并行加载、双亲委派都加进去
 */
public class MyProClassLoader extends ClassLoader {

    private static final URL[] URLS = {getSystemResource("")};

    /**
     * 支持并行加载
     */
    static {
        ClassLoader.registerAsParallelCapable();
    }

    /**
     * 支持双亲委派，比如设置父类加载器为AppClassLoader，只需要通过指定parent为AppClassLoader
     */
    public MyProClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        URLClassPath ucp = new URLClassPath(URLS, AccessController.getContext());

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

        MyProClassLoader myProClassLoader = new MyProClassLoader();
        Class<?> clazz1 = myProClassLoader.loadClass(aClassName);   //使用双亲委派需要用loadClass
        Class<?> clazz2 = Thread.currentThread().getContextClassLoader().loadClass(aClassName);

        System.out.println(clazz1 == clazz2);   //这里可以看到结果为true,因为 A实际被委派给了AppClassLoader加载
    }
}
