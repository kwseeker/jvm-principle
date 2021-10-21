package top.kwseeker.jvm.spi.grpc.server.driver.netty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kwseeker.jvm.spi.grpc.server.driver.AbstractServerImplBuilder;

/**
 * 这个类用于构造Netty服务端的实例
 */
public final class NettyServerBuilder extends AbstractServerImplBuilder<NettyServerBuilder> {

    private static Logger LOG = LoggerFactory.getLogger(NettyServerBuilder.class);

    public static NettyServerBuilder forPort(int port) {
        return new NettyServerBuilder(port);
    }

    private NettyServerBuilder(int port) {
        LOG.info("Create Netty server on port {}", port);
        //这里要引入Netty的API，构建Netty Server
    }

}
