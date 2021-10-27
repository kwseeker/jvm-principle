# Java SPI 机制

SPI基础去看网文吧（可以看下面的附录）。这里主要讲实现原理。

SPI: Service Provider Interface (服务提供接口)。

SPI 用于本地服务发现和服务加载， “基于接口的编程＋策略模式＋配置文件”。



## 1 应用场景

### 1.1 应用场景

优点：解耦合、可实现可插拔。

适用于：**调用者根据实际使用需要，启用、扩展、或者替换框架的实现策略**。

### 1.2 使用案例

代码位置：`jvm-classlaoder@src.main.java.top.kwseeker.jvm.spi`。

+ **demo** 

  最简案例，比如：播放音乐，可选择网易云音乐、QQ音乐。

  很简单：一个接口、两个实现、一个配置、外加`ServiceLoader.load()`。

+ **grpc**

  抽离自开源项目grpc-java，介绍grpc怎么使用spi装载通信组件的，如：Netty。

+ **sandbox**

  抽离自开源项目jvm-sandbox，介绍jvm-sandbox怎么使用spi装载可插拔的增强模块的。

+ **其他**

  比如 slf4j 、Spring、Dubbo 中有大量的使用，看网文吧。

### 1.3 使用流程

1、引入服务提供者对接口（不一定是接口抽象类也行）的具体实现（Jar包或本地的一层封装）；

2、在工程的META-INF/services目录下创建一个以“接口全限定名”命名的文件，内容为实现类的全限定名；

3、主程序通过java.util.ServiceLoder动态装载实现模块（通过扫描META-INF/services目录下的配置文件找到实现类（必须携带一个不带参数的构造方法）的全限定名，把类加载到JVM）；



## 2 实现原理

 研究`ServiceLoader.load()`怎么实现的，以及有哪些高级用法（Java官方源码并没有高级用法，像加载优先级什么的是第三方框架在ServiceLoader基础上拓展的）。 

在`MusicPlayMainTest.class` 将`ServiceLoader`的`load()`使用面向过程的方式写了一遍，方便理解它一步步地都做了什么。

实现很简单：

> 读取目标接口类的SPI配置文件解析，拿到所有实现类的全路径名，然后使用类加载器加载。加载之后再缓存一下。
>
> 对外也仅提供了迭代器。在遍历的时候才加载。

核心代码：

```java
//获取类加载器
ClassLoader cl = Thread.currentThread().getContextClassLoader();
ClassLoader loader = (cl == null) ? ClassLoader.getSystemClassLoader() : cl;

//后面的操作是在通过迭代器迭代时才会真正解析文件加载类。

//读取SPI文件到Enumeration<URL>
String spiConfigFilePath =  "META-INF/services/" + service.getName();
Enumeration<URL> configs = loader == null ? 
    ClassLoader.getSystemResources(spiConfigFilePath) :
	loader.getResources(spiConfigFilePath);

//解析文件每一行获取实现类的全路径名
parseLine();

//类加载
Class<?> c = Class.forName(cn, false, loader);
if (!service.isAssignableFrom(c))   //是否是IMusic的子类（翻译是：是否可以从c转换为service）
    System.out.println("Provider " + cn  + " not a subtype");
IMusic p = service.cast(c.newInstance());

//spi定义的类装载后缓存在LinkedHashMap容器中
LinkedHashMap<String, IMusic> providers = new LinkedHashMap<>();
//添加一下缓存
providers.put(cn, p);
```



## 附录

### 参考资料

+ [高级开发必须理解的Java中SPI机制](https://www.jianshu.com/p/46b42f7f593c)