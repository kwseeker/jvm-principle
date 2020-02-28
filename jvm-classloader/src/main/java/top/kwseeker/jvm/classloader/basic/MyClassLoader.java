package top.kwseeker.jvm.classloader.basic;

import sun.net.www.ParseUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 不遵循双亲委派模型
 */
public class MyClassLoader extends URLClassLoader {

    public static ClassLoader getMyClassLoader() throws IOException {
        String classpath = System.getProperty("java.class.path");
        File[] cpFiles = getClassPath(classpath);
        URL[] urls = pathToURLs(cpFiles);
        return new MyClassLoader(urls);
    }

    private MyClassLoader(URL[] urls) {
        super(urls);
    }

    //这个方法在JDK官方的类加载器都是做委托加载类用的（不遵循双亲委派的话不用实现这个接口）
    //@Override
    //public Class<?> loadClass(String name) throws ClassNotFoundException {
    //    //可以拓展额外类加载逻辑
    //    //...
    //    return super.loadClass(name);
    //}

    //这个方法才是真正的通过native方法干实事的接口
    //如果只用这个方法加载类的话，就无法保证类加载优先级和避免类重复加载，需要执行检查
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        //1） 搜索类是否已经被加载
        Class<?> clazz = super.findLoadedClass(name);
        if(clazz != null) {
            return clazz;
        }
        //2） 搜索class资源并加载类
        return super.findClass(name);
    }

    private static URL[] pathToURLs(File[] path) throws IOException {
        URL[] urls = new URL[path.length];
        for (int i = 0; i < path.length; i++) {
            urls[i] = ParseUtil.fileToEncodedURL(path[i].getCanonicalFile());
        }
        // DEBUG
        for (int i = 0; i < urls.length; i++) {
          System.out.println("urls[" + i + "] = " + '"' + urls[i] + '"');
        }
        return urls;
    }

    private static File[] getClassPath(String cp) {
        File[] path;
        if (cp != null) {
            int count = 0, maxCount = 1;
            int pos = 0, lastPos = 0;
            // Count the number of separators first
            while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
                maxCount++;
                lastPos = pos + 1;
            }
            path = new File[maxCount];
            lastPos = pos = 0;
            // Now scan for each path component
            while ((pos = cp.indexOf(File.pathSeparator, lastPos)) != -1) {
                if (pos - lastPos > 0) {
                    path[count++] = new File(cp.substring(lastPos, pos));
                } else {
                    // empty path component translates to "."
                    path[count++] = new File(".");
                }
                lastPos = pos + 1;
            }
            // Make sure we include the last path component
            if (lastPos < cp.length()) {
                path[count++] = new File(cp.substring(lastPos));
            } else {
                path[count++] = new File(".");
            }
            // Trim array to correct size
            if (count != maxCount) {
                File[] tmp = new File[count];
                System.arraycopy(path, 0, tmp, 0, count);
                path = tmp;
            }
        } else {
            path = new File[0];
        }
        // DEBUG
        for (int i = 0; i < path.length; i++) {
          System.out.println("path[" + i + "] = " + '"' + path[i] + '"');
        }
        return path;
    }
}
