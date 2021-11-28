package top.kwseeker.jvm.classloader.custom;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 思路：
 * 自定义类加载器，每隔一段时间检查一下class文件是否有变化，有变化，则重新加载class, 并替换之前的对象实例。
 * 测试方法：编译后将target中的A.class移动到模块根目录的file中
 */
public class HotSwapClassLoader extends URLClassLoader {

    private static final ScheduledExecutorService ses = Executors.newScheduledThreadPool(1);

    public static Map<String, byte[]> classBytesMap = new HashMap<>();
    public static Map<String, Object> beanMap = new HashMap<>();
    public static Map<String, Class<?>> beanClassMap = new HashMap<>();

    //URLClassLoader没有无参构造方法（必须要指定类加载路径），子类也必须自定义类加载器
    public HotSwapClassLoader(URL[] urls) {
        super(urls);
        ses.scheduleAtFixedRate(this::checkModifyAndReload, 3, 3, TimeUnit.SECONDS);
    }

    public void checkModifyAndReload() {
        try {
            System.out.println("check and reload ...");
            for (Map.Entry<String, byte[]> stringEntry : classBytesMap.entrySet()) {
                String className = stringEntry.getKey();
                byte[] srcBytes = stringEntry.getValue();

                URL url = getURLs()[0];
                String classFile = url.getPath() + className.replace('.', '/').concat(".class");
                byte[] bytes = getBytesFromURL(classFile);

                if (bytes.length != srcBytes.length
                        || !new String(srcBytes).equals(new String(bytes))) {
                    System.out.println("reload >>>>>>>");
                    //TODO 会报错： LinkageError: attempted  duplicate class definition for name, 因为一个类加载器不能重复加载同一个类，classLoader也要换
                    //TODO class文件变化检测还是得放类加载器外边，map也应该放到外边，原理很简单后面改吧
                    Class<?> clazz = findClass(className);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytesFromURL(String url) throws IOException {
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(url));
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);

        byte[] temp = new byte[2048];
        int size = 0;
        while ((size = in.read(temp)) != -1) {
            out.write(temp, 0, size);
        }
        in.close();
        return out.toByteArray();
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> clazz = super.findClass(name);
        try {

            //URLClassPath ucp = new URLClassPath(new URL[]{url}, AccessController.getContext());
            //byte[] bytes = ucp.getResource(name).getBytes();
            URL url = getURLs()[0];
            String classFile = url.getPath() + name.replace('.', '/').concat(".class");
            byte[] bytes = getBytesFromURL(classFile);

            classBytesMap.put(name, bytes);
            beanMap.put(name, clazz.newInstance());
            beanClassMap.put(name, clazz);
        } catch (InstantiationException | IllegalAccessException | IOException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(e.getMessage());
        }
        return clazz;
    }

    public static void main(String[] args) throws Exception {
        String targetDir = "file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/file/";
        String aClassName = "top.kwseeker.jvm.classloader.custom.A";

        HotSwapClassLoader hotSwapClassLoader = new HotSwapClassLoader(new URL[]{new URL(targetDir)});
        hotSwapClassLoader.loadClass(aClassName);

        while (true) {
            Thread.sleep(1000);
            if (beanClassMap.get(aClassName) != null) {
                beanClassMap.get(aClassName).getMethod("print").invoke(beanMap.get(aClassName));
            }
        }
    }
}
