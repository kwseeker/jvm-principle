package top.kwseeker.jvm.classloader.basic;

import java.io.IOException;



public class TestMyClassLoader {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        ClassLoader myClassLoader = MyClassLoader.getMyClassLoader();
        //ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        //Class<?> clazz1 = threadContextClassLoader.loadClass("java.lang.Object");
        Class<?> clazz2 = myClassLoader.loadClass("java.lang.Object");
        //System.out.println(clazz1);
        System.out.println(clazz2);
        //System.out.println("class1 == class2 ? " + (clazz1 == clazz2));
    }
}
