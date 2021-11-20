package top.kwseeker.jvm.spi.sandbox;

import com.alibaba.jvm.sandbox.provider.api.ModuleJarLoadingChain;
import org.apache.commons.io.FileUtils;
import top.kwseeker.jvm.spi.sandbox.classloader.ProviderClassLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ServiceLoader;

/**
 * 这部分代码抽离自 Alibaba 开源项目 jvm-sandbox。
 * 使用SPI机制加载可插拔的增强模块，具体参考 DefaultProviderManager 这个类 init()。
 * JVM-Sandbox 自定义了类加载器，类加载器继承URLClassLoader,从指定的URL路径中加载Class资源
 */
public class JVMSandboxMain {

    //final ProviderClassLoader providerClassLoader = new ProviderClassLoader(providerJarFile, getClass().getClassLoader());
    //// load ModuleJarLoadingChain
    //inject(moduleJarLoadingChains, ModuleJarLoadingChain.class, providerClassLoader, providerJarFile);

    public static void main(String[] args) throws IOException {
        final Collection<ModuleJarLoadingChain> moduleJarLoadingChains = new ArrayList<>();

        String providerLibDirPath = System.getProperty("user.home") + "/.opt/sandbox/provider";     //实现类的资源路径
        File providerLibDir = new File(providerLibDirPath);
        //非递归地读取目录下拓展名为“jar”的文件到集合中, 这里只有一个 sandbox-mgr-provider.jar
        Collection<File> files = FileUtils.listFiles(providerLibDir, new String[]{"jar"}, false);
        for (final File providerJarFile : files) {
            //ProviderClassLoader继承RoutingURLClassLoader,可以指定哪个类由哪个类加载器加载
            final ProviderClassLoader providerClassLoader = new ProviderClassLoader(providerJarFile, JVMSandboxMain.class.getClassLoader());

            //指定使用 ProviderClassLoader 加载 ModuleJarLoadingChain
            final ServiceLoader<ModuleJarLoadingChain> serviceLoader = ServiceLoader.load(ModuleJarLoadingChain.class, providerClassLoader);
            for (final ModuleJarLoadingChain provider : serviceLoader) {
                //injectResource(provider);
                moduleJarLoadingChains.add(provider);
            }
        }

        moduleJarLoadingChains.forEach(chain -> System.out.println(chain.getClass().getName()));
    }
}
