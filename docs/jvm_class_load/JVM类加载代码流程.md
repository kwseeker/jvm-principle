# JVM类加载代码流程

示例代码：

```java
public static void main(String[] args) {
    new A();
}
```

如上示例，要研究的问题：

+ new A() 是如何触发类加载的？
+ class A类加载流程
  + 类查找
  + 类加载



## 1 new A() 触发类加载

**触发类加载的时机**（参考《深入理解Java虚拟机7.2章节》）：

+ 创建对象

  `new`

+ 访问类的静态变量

  `getstatic` `putstatic`

+ 访问类的静态方法

  `invokestatic`

+ 反射 `Class.forName`

+ 初始化类的子类

+ 执行main方法, 加载主类

+ 动态语言支持

**到底是怎么触发的**：即为何new A() 之后会先跳到`AppClassLoader`的`loadClass`方法？

这个是具体的JVM实现的，待续 ... 



## 2 类加载流程

这里不管双亲委派，只看类的查找和加载。

### 2.1 并行加载

源码中有看到一个并行加载的概念。显示如果当前 `ClassLoader` 有注册到 `ParallelLoaders`，则会实例化一个 `parallelLockMap = new ConcurrentHashMap<>();`

这个Map为每一个 class 设置了一个锁（如果没有注册到`ParallelLoaders`是以`ClassLoader`实例作为锁，那么这个类就只能一个个地加载），从而支持同时加载不同限定名的 class 。

`ParallelLoaders` 本质是个集合：

```java
private static final Set<Class<? extends ClassLoader>> loaderTypes =
    	Collections.newSetFromMap(
    		new WeakHashMap<Class<? extends ClassLoader>, Boolean>());
//还提供了两个方法
register(Class<? extends ClassLoader>):boolean
isRegistered(Class<? extends ClassLoader>):boolean
```

那么哪里将`ClassLoader`注册到`ParallelLoaders`呢？

```java
// ClassLoader抽象类中有实现这个方法，即所有类加载器都会继承这个方法
@CallerSensitive
protected static boolean registerAsParallelCapable() {
    Class<? extends ClassLoader> callerClass =
        Reflection.getCallerClass().asSubclass(ClassLoader.class);
    return ParallelLoaders.register(callerClass);
}
```

搜索方法使用，可以看到标准库常用`ClassLoader`实现都有在static代码块中调用这个方法。

即普遍支持并行加载不同的类。

```
java.net
	FactoryURLClassLoader
        ClassLoader.registerAsParallelCapable();
	URLClassLoader
        ClassLoader.registerAsParallelCapable();
java.security
	SecureClassLoader
        ClassLoader.registerAsParallelCapable();
sun.misc
	Launcher.AppClassLoader
        ClassLoader.registerAsParallelCapable();
	Launcher.ExtClassLoader
        ClassLoader.registerAsParallelCapable();
```

### 2.2 类class字节码查找

实例中的class A是由`AppClassLoader`加载的，看下具体代码流程：

```java
AppClassLoader.findClass(String name)
	URLClassLoader.findClass(String name)
		URLClassLoader.defineClass(String name, Resource res)
			SecureClassLoader.defineClass(String name, byte[] b, int off, int len, CodeSource cs)
				ClassLoader.defineClass(String name, byte[] b, int off, int len,
                                         ProtectionDomain protectionDomain)
    				//native 方法
               		ClassLoader.defineClass1(String name, byte[] b, int off, int len,
                                         ProtectionDomain pd, String source);
```

`URLClassLoader.findClass()`核心代码

```java
String path = name.replace('.', '/').concat(".class");
//ucp是URLClassPath,里面存放着一组classpath路径
//0 = {URL@547} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/charsets.jar"
//1 = {URL@548} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/cldrdata.jar"
//2 = {URL@549} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/dnsns.jar"
//3 = {URL@550} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/jaccess.jar"
//4 = {URL@551} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/localedata.jar"
//5 = {URL@552} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/nashorn.jar"
//6 = {URL@553} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/sunec.jar"
//7 = {URL@554} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/sunjce_provider.jar"
//8 = {URL@555} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/sunpkcs11.jar"
//9 = {URL@556} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/ext/zipfs.jar"
//10 = {URL@557} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/jce.jar"
//11 = {URL@558} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/jfr.jar"
//12 = {URL@559} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/jsse.jar"
//13 = {URL@560} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/management-agent.jar"
//14 = {URL@561} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/resources.jar"
//15 = {URL@562} "file:/usr/lib/jvm/openlogic-openjdk-8u292-b10-linux-x64/jre/lib/rt.jar"
//16 = {URL@563} "file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/target/classes/"
//17 = {URL@564} "file:/home/lee/.m2/repository/commons-io/commons-io/2.4/commons-io-2.4.jar"
//18 = {URL@565} "file:/home/lee/.m2/repository/org/apache/commons/commons-lang3/3.11/commons-lang3-3.11.jar"
//19 = {URL@566} "file:/home/lee/.m2/repository/mysql/mysql-connector-java/8.0.13/mysql-connector-java-8.0.13.jar"
//20 = {URL@567} "file:/home/lee/.m2/repository/com/google/protobuf/protobuf-java/3.6.1/protobuf-java-3.6.1.jar"
//21 = {URL@568} "file:/home/lee/.m2/repository/ch/qos/logback/logback-core/1.2.3/logback-core-1.2.3.jar"
//22 = {URL@569} "file:/home/lee/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar"
//23 = {URL@570} "file:/home/lee/.m2/repository/org/slf4j/slf4j-api/1.7.25/slf4j-api-1.7.25.jar"
//24 = {URL@571} "file:/opt/idea-IU-202.6397.94/lib/idea_rt.jar"
//25 = {URL@572} "file:/opt/idea-IU-202.6397.94/plugins/java/lib/rt/debugger-agent.jar"

//有两种资源加载器JarLoader、FileLoader（分别用于加载Jar包和本地classpath中的class文件）。
//此处加载A.class的是URLClassPath$FileLoader类型, FileLoader会递归扫描classpath中的class文件，
//一旦找到会封装成Resource返回
Resource res = ucp.getResource(path, false);
if (res != null) {
    try {
        return defineClass(name, res);
    } catch (IOException e) {
        throw new ClassNotFoundException(name, e);
    }
} else {
    return null;
}

//Java Manifest相关逻辑
//	1）package校验 getManifest() isSealed()
//Java 安全模型相关逻辑
//	1）代码签名 getCodeSigners()
//	2）保护域 getProtectionDomain()

//native方法加载
protected final Class<?> defineClass(String name, byte[] b, int off, int len,
                                     ProtectionDomain protectionDomain)
    throws ClassFormatError
{
    protectionDomain = preDefineClass(name, protectionDomain);
    String source = defineClassSourceLocation(protectionDomain);
    Class<?> c = defineClass1(name, b, off, len, protectionDomain, source);
    postDefineClass(c, protectionDomain);
    return c;
}
```

总结：

1）从众多`URLClassPath`（`URLClassPath`用来记录每个`ClassLoader`对应的载入`.class`档案的路径，如：`java包`或`本地classpath`，分别用`JarLoader`、`FileLoader`读取）中查找目标类的文件；然后封装成`Resource`对象返回；

2）中间还有一些校验、签名逻辑、获取保护域等逻辑，是`Manifest`和`Java安全模型`（保护用户终端免受不可信来源的程序的侵犯）的内容，后面再看。

3）读取class文件为byte[] 交给 `navtive` 方法 `defineClass1` 加载。

### 2.3 native 方法加载类

```java
Class<?> c = defineClass1(name, b, off, len, protectionDomain, source);
```

由具体的JVM实现，待续 ...



## 拓展资料

+ [Java Security](https://docs.oracle.com/javase/8/docs/technotes/guides/security/index.html) (official)

+ [Java安全——安全管理器、访问控制器和类装载器](https://developer.aliyun.com/article/57223)

+ [Java-安全模型]([https://liuyehcf.github.io/2018/01/19/Java-%E5%AE%89%E5%85%A8%E6%A8%A1%E5%9E%8B/](https://liuyehcf.github.io/2018/01/19/Java-安全模型/))