package top.kwseeker.jvm.classloader.unload;

import top.kwseeker.jvm.classloader.custom.MyURLClassLoader;

/**
 * 类卸载测试
 * -XX:+TraceClassUnloading
 *
 * 自定义类加载器加载的类无用之后会被卸载（JVM自带的类加载器加载的类运行时都不会被回收）
 * 类被判定无用的条件：
 * 1. 改类所有对象实例都已经被回收
 * 2. 类的Class对象没有任何地方引用
 * 3. 类的CLassLoader被回收
 */
public class ClassUnloadTest {

    public static void main(String[] args) throws Exception {
        String aClassName = "top.kwseeker.jvm.classloader.custom.A";

        unloadClassLoadedBySelfDefinedClassLoader(aClassName);
        compareClassLoadedByJVMClassLoader(aClassName);

        System.gc();

        Thread.sleep(2000);
    }

    public static void unloadClassLoadedBySelfDefinedClassLoader(String aClassName) throws Exception {
        MyURLClassLoader myURLClassLoader = new MyURLClassLoader();
        Class<?> aClass = myURLClassLoader.findClass(aClassName);
        Object aObj = aClass.newInstance();

        aClass.getMethod("print").invoke(aObj);

        //使类无效化(因为在方法（栈帧）内部，下面写不写，退出栈帧都会释放)
        aObj = null;                //条件1
        aClass = null;              //条件2
        myURLClassLoader = null;    //条件3
    }

    /**
     * 对比JVM自带类加载器加载的类，不会被卸载
     */
    public static void compareClassLoadedByJVMClassLoader(String aClassName) throws Exception {
        ClassLoader systemCl = ClassLoader.getSystemClassLoader();
        Class<?> aClass = systemCl.loadClass(aClassName);
        Object aObj = aClass.newInstance();

        aClass.getMethod("print").invoke(aObj);

        aObj = null;                //条件1
        aClass = null;              //条件2
        systemCl = null;            //条件3   其实这里其他地方还有GC ROOT引用
    }
}
