/**
 * 定制自己的类加载器
 *
 * 类加载流程是一个复杂的过程，且大部分工作都是JVM native 方法实现的，
 * 自定义类加载器也一般只是通过重写JDK标准类加载器实现，常见的就是覆写 URLClassLoader （大多类加载器都是拓展的这个类加载器），
 * 框架开发中自定义类加载器非常普遍。
 *
 * 双亲委派由loadClass()实现,实际加载操作是 findClass()实现的，说成“查找并加载”更合适
 *
 * AppClassLoader.findClass(String name)
 * 	URLClassLoader.findClass(String name)
 * 		URLClassLoader.defineClass(String name, Resource res)
 * 			SecureClassLoader.defineClass(String name, byte[] b, int off, int len, CodeSource cs)
 * 				ClassLoader.defineClass(String name, byte[] b, int off, int len,
 *                                          ProtectionDomain protectionDomain)
 *     				//native 方法
 *                  ClassLoader.defineClass1(String name, byte[] b, int off, int len,
 *                                          ProtectionDomain pd, String source);
 *
 * ClassLoader 方法
 *
 *      getParent                   双亲委派父加载器引用
 *
 *      getResource                 获取类资源路径
 *      getResourceAsStream
 *      getResources
 *      getSystemClassLoader
 *      getSystemResource
 *      getSystemResourceAsStream
 *      getSystemResources
 *
 *      loadClass                   双亲委派机制实现方法，类加载通过调用findClass
 *
 *      defineClass                 类加载流程
 *      defineClass
 *      defineClass
 *      defineClass
 *      definePackage
 *
 *      findClass                   空方法
 *      findLibrary
 *      findLoadedClass             已加载类
 *      findResource
 *      findResources
 *      findSystemClass
 *      getClassLoadingLock
 *      getPackage
 *      getPackages
 *
 *      registerAsParallelCapable   注册为支持并发加载
 *      resolveClass
 *      setSigners
 *      addClass
 */
package top.kwseeker.jvm.classloader.custom;