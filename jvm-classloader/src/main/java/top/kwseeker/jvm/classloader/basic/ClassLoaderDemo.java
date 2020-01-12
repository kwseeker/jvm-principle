package top.kwseeker.jvm.classloader.basic;

import java.util.HashMap;

public class ClassLoaderDemo {

    private class One {}

    private class Two {}

    public static void main(String[] args) {
        //null, BootstrapClassLoader java代码中无权限获取实例
        System.out.println(Object.class.getClassLoader());                                      //BootstrapClassLoader
        System.out.println(ClassLoaderDemo.class.getClassLoader().getParent().getParent());     //BootstrapClassLoader
        System.out.println(ClassLoaderDemo.class.getClassLoader().getParent());                 //ExtClassLoader
        System.out.println(ClassLoaderDemo.class.getClassLoader());                             //AppClassLoader

        System.out.println(HashMap.class.getClassLoader());

        System.out.println(One.class.getClassLoader() == Two.class.getClassLoader());
    }
}
