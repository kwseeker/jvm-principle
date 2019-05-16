package top.kwseeker.classLoader.ch7.case4;

import java.io.IOException;
import java.io.InputStream;

/**
 * 类加载器和类共同确立其在JVM中的唯一性
 */
public class ClassLoaderTest {

    public static void main(String[] args) throws Exception {
        //ClassLoader myLoader = new ClassLoader() {  //这个ClassLoader默认类型？
        //    @Override
        //    public Class<?> loadClass(String name) throws ClassNotFoundException {
        //        try {
        //            String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
        //            InputStream is = getClass().getResourceAsStream(fileName);
        //            if(is == null) {
        //                System.out.println("使用父加载器加载");
        //                return super.loadClass(name);
        //            }
        //            byte[] b = new byte[is.available()];
        //            is.read(b);
        //            return defineClass(name, b, 0, b.length);
        //        } catch (IOException e) {
        //            e.printStackTrace();
        //            throw new ClassNotFoundException(name);
        //        }
        //    }
        //};
        //
        //Object object = myLoader.loadClass("top.kwseeker.classLoader.ch7.case4.ClassLoaderTest").newInstance();
        //
        //Class clazz = object.getClass();
        //System.out.println(clazz);   //object Class 由自定义类加载器加载
        //System.out.println(object instanceof top.kwseeker.classLoader.ch7.case4.ClassLoaderTest); //top.kwseeker.classLoader.ch7.case4.ClassLoaderTest Class 由系统类加载器加载
        //
        //ClassLoader classLoader = object.getClass().getClassLoader();
        //ClassLoader classLoader1 = ClassLoaderTest.class.getClassLoader();
        //if(classLoader.getParent().equals(classLoader1)){
        //    System.out.println("true");
        //}
    }
}
