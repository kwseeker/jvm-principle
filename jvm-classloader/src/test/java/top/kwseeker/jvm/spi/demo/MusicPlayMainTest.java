package top.kwseeker.jvm.spi.demo;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.*;

public class MusicPlayMainTest extends TestCase {

    /**
     * ServiceLoader.load() 原理
     */
    @Test
    public void testServiceLoad() throws IOException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class<IMusic> clazz = IMusic.class;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<IMusic> service = Objects.requireNonNull(clazz, "Service interface cannot be null");
        ClassLoader loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
        AccessControlContext acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;

        // 重载(什么重载只是清空缓存，然后建个迭代器，载入是在获取服务时才做的)
        LinkedHashMap<String, IMusic> providers = new LinkedHashMap<>();
        providers.clear();
        // new LazyIterator

        // for (IMusic music : musics) { ... } 创建新的迭代器，先从缓存中取，取不到再从LazyIterator中找
        Iterator<Map.Entry<String, IMusic>> knownProvidersIterator = providers.entrySet().iterator();
        Enumeration<URL> configs = null;
        Iterator<String> pending = null;
        String nextName = null;
        while (true) {
            boolean hasKnown = knownProvidersIterator.hasNext();
            if (hasKnown) { //缓存中有，从缓存中取
                // 1 首先从缓存（LinkedHashMap）中获取已经加载的IMusic服务
                Map.Entry<String, IMusic> entry = knownProvidersIterator.next();
                System.out.println("key: " + entry.getKey());
                IMusic music = knownProvidersIterator.next().getValue();
                music.play();
            } else {        //缓存中没有，迭代LazyIterator, 使用类加载器加载
                // 2 LazyIterator 懒加载方式从文件重新读取
                if (configs == null) {
                    String spiConfigFilePath =  "META-INF/services/" + service.getName();
                    configs = loader == null ?
                            ClassLoader.getSystemResources(spiConfigFilePath) :
                            loader.getResources(spiConfigFilePath);
                }
                //  解析每一行代码将服务定义放入到ArrayList容器中
                while ((pending == null) || !pending.hasNext()) {
                    if (!configs.hasMoreElements()) {
                        return;
                    }
                    //一行行读取，先检查格式是否合法
                    //合法且缓存中没有这个类（即没有加载过），就放到ArrayList, 最终返回ArrayList的iterator
                    pending = parse(service, configs.nextElement(), providers); //pending是Iterator实例
                }
                //遍历从SPI文件中解析出来的类名
                nextName = pending.next();

                String cn = nextName;
                nextName = null;
                Class<?> c = Class.forName(cn, false, loader);
                if (!service.isAssignableFrom(c))   //是否是IMusic的子类（翻译是：是否可以从c转换为service）
                    System.out.println("Provider " + cn  + " not a subtype");
                IMusic p = service.cast(c.newInstance());
                //添加一下缓存
                providers.put(cn, p);
                p.play();
            }
        }
    }

    private Iterator<String> parse(Class<?> service, URL u, LinkedHashMap<String, IMusic> providers) throws ServiceConfigurationError
    {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names, providers)) >= 0);
        } catch (IOException x) {
            //fail(service, "Error reading configuration file", x);
        } finally {
            try {
                if (r != null) r.close();
                if (in != null) in.close();
            } catch (IOException y) {
                //fail(service, "Error closing configuration file", y);
            }
        }
        return names.iterator();
    }

    /**
     * 一行行地读取，去掉注释，再去掉首尾的空格，空格和制表符校验
     * 获取第一位字符的unicode码值,判断是否符合Java标识符, 第一个字母：只能是字母，_，$
     * 再逐个字符检查是否是分割符，且为“.”
     * 都满足，且缓存和ArrayList实例names不存在同名的，就加到ArrayList实例names中
     */
    private int parseLine(Class<?> service, URL u, BufferedReader r, int lc,
                          List<String> names, LinkedHashMap<String, IMusic> providers)
            throws IOException, ServiceConfigurationError
    {
        String ln = r.readLine();
        if (ln == null) {
            return -1;
        }
        int ci = ln.indexOf('#');
        if (ci >= 0) ln = ln.substring(0, ci);
        ln = ln.trim();
        int n = ln.length();
        if (n != 0) {
            if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                System.out.println("Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                System.out.println("Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    System.out.println("Illegal provider-class name: " + ln);
            }
            if (!providers.containsKey(ln) && !names.contains(ln))
                names.add(ln);
        }
        return lc + 1;
    }
}