package top.kwseeker.jvm.classloader.basic;

class One {}

public class LoadProcedure {

    public static class Two {}

    public static void main(String[] args) throws ClassNotFoundException {
        //TODO：为何main线程上下文类加载器是AppClassLoader？
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        System.out.println(classLoader);

        Class<?> clazz = classLoader.loadClass("top.kwseeker.jvm.classloader.basic.One");
        //TODO: 为何加载内部类会失败，内部类和外部类加载什么区别？
        //Class<?> clazz1 = classLoader.loadClass("top.kwseeker.jvm.classloader.basic.LoadProcedure.Two");
        System.out.println(clazz.getClassLoader());
    }
}
