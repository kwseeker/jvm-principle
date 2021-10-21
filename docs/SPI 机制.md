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

 研究`ServiceLoader.load()`怎么实现的，以及有哪些高级用法（如指定加载优先级等）。





## 附录

### 参考资料

+ [高级开发必须理解的Java中SPI机制](https://www.jianshu.com/p/46b42f7f593c)