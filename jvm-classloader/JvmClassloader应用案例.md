# JVM应用案例

## 1 GPRC ServerProvider 的实现（Classloader SPI）

使用GRPC实现远程调用的微服务的服务端的创建和启动
```
server = ServerBuilder.forPort(port)
                .addService(new GreeterImpl())
                .build()
                .start();
  
public static ServerBuilder<?> forPort(int port) {
    return ServerProvider.provider().builderForPort(port);
}
```
这里面应用了  
Builder模式；  
SPI Provider模式解藕，动态选择服务提供方；  
abstract class用于拓展。  

https://github.com/grpc/grpc-java

GRPC Netty服务器创建流程  
![](https://mmbiz.qlogo.cn/mmbiz_png/gjWdyyzOYp6KScf1L4sicRQdsyicichN3CZQOhBiaJicvRRZwRUic610zBXviaVcBmYTMO95yRc424sHPq4bHBLUevK8g/0?wx_fmt=png)

这里不打算讲细节，只是将GRPC ServerProvider 的代码结构以最简单的方式
抽离出来，加深对SPI的理解，以方便在工作中使用。
如果想要了解GRPC是怎么使用Netty的，可以看grpc-java/netty模块。

1）首先定义服务Provider抽象类或接口ServerProvider（核心），包括获取并选择服务实例的方法，及Builder接口；
2）根据上面抽象类或接口，实现SPI服务（如：NettyServer，ServerProvider实现类NettyServerProvider，及选择优先级）, 包括服务Builder；
   这部分在其他模块实现或者由第三方实现，当使用时把包引入，并通过SPI文件引入类名。  
3）实现ServerProviders，用于加载服务的所有的实现；  
4）通过ServerProvider的静态方法从加载的所有的服务实现中选择优先级最高的实现返回。 
