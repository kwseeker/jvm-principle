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
    public void testServiceLoad() throws IOException {
        Class<IMusic> clazz = IMusic.class;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class<IMusic> service = Objects.requireNonNull(clazz, "Service interface cannot be null");
        ClassLoader loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;
        AccessControlContext acc = (System.getSecurityManager() != null) ? AccessController.getContext() : null;

        // 重载
        LinkedHashMap<String, IMusic> providers = new LinkedHashMap<>();
        providers.clear();

        // new LazyIterator

        // for (IMusic music : musics) { ... }
        // 1 首先从缓存（LinkedHashMap）中获取已经加载的IMusic服务
        Iterator<Map.Entry<String, IMusic>> knownProvidersIterator = providers.entrySet().iterator();
        //  获取已经加载的IMusic服务容器的 Iterator
        while (knownProvidersIterator.hasNext()) {
            IMusic music = (IMusic) knownProvidersIterator.next();
            music.play();
        }

        // 2 LazyIterator 懒加载方式从文件重新读取（下面只是展示的第一次遍历）
        String spiConfigFilePath =  "META-INF/services/" + service.getName();
        Enumeration<URL> configs = loader == null ?
                ClassLoader.getSystemResources(spiConfigFilePath) :
                loader.getResources(spiConfigFilePath);
        //  解析每一行代码将服务定义放入到ArrayList容器中
        Iterator<String> pending = null;
        String nextName = null;
        while ((pending == null) || !pending.hasNext()) {
            if (!configs.hasMoreElements()) {
                break;
            }
            pending = parse(service, configs.nextElement());
        }
        nextName = pending.next();


    }

    private Iterator<String> parse(Class<?> service, URL u) throws ServiceConfigurationError
    {
        InputStream in = null;
        BufferedReader r = null;
        ArrayList<String> names = new ArrayList<>();
        try {
            in = u.openStream();
            r = new BufferedReader(new InputStreamReader(in, "utf-8"));
            int lc = 1;
            while ((lc = parseLine(service, u, r, lc, names)) >= 0);
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

    private int parseLine(Class<?> service, URL u, BufferedReader r, int lc,
                          List<String> names)
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
            //if ((ln.indexOf(' ') >= 0) || (ln.indexOf('\t') >= 0))
                //fail(service, u, lc, "Illegal configuration-file syntax");
            int cp = ln.codePointAt(0);
            if (!Character.isJavaIdentifierStart(cp))
                //fail(service, u, lc, "Illegal provider-class name: " + ln);
            for (int i = Character.charCount(cp); i < n; i += Character.charCount(cp)) {
                cp = ln.codePointAt(i);
                if (!Character.isJavaIdentifierPart(cp) && (cp != '.'))
                    //fail(service, u, lc, "Illegal provider-class name: " + ln);
            }
            if (!providers.containsKey(ln) && !names.contains(ln))
                names.add(ln);
        }
        return lc + 1;
    }
}