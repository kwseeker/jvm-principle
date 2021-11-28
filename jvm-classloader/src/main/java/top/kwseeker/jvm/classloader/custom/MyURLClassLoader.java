package top.kwseeker.jvm.classloader.custom;

import java.net.URL;
import java.net.URLClassLoader;

public class MyURLClassLoader extends URLClassLoader {

    private static final URL url = getSystemResource("");

    //URLClassLoader没有无参构造方法（必须要指定类加载路径），子类也必须自定义类加载器
    public MyURLClassLoader() {
        super(new URL[]{url});
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return super.findClass(name);
    }

    public static void main(String[] args) throws Exception {
        String aClassName = "top.kwseeker.jvm.classloader.custom.A";

        MyURLClassLoader myURLClassLoader = new MyURLClassLoader();
        Class<?> clazz1 = myURLClassLoader.findClass(aClassName);
        Object a1 = clazz1.newInstance();
        clazz1.getMethod("print").invoke(a1);

        Class<?> clazz2 = Thread.currentThread().getContextClassLoader().loadClass(aClassName);

        System.out.println(clazz1 == clazz2);
    }
}
