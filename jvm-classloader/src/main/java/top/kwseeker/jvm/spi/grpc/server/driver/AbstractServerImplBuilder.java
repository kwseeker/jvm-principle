package top.kwseeker.jvm.spi.grpc.server.driver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kwseeker.jvm.spi.grpc.server.Server;
import top.kwseeker.jvm.spi.grpc.server.ServerImpl;
import top.kwseeker.jvm.spi.grpc.server.ServerServiceDefinition;
import top.kwseeker.jvm.spi.grpc.server.provider.ServerBuilder;

/**
 * 创建Server的通用方法
 * @param <T>
 */
public class AbstractServerImplBuilder<T extends AbstractServerImplBuilder<T>> extends ServerBuilder<T> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractServerImplBuilder.class);

    public static ServerBuilder<?> forPort(int port) {
        throw new UnsupportedOperationException("Subclass failed to hide static factory");
    }

    public T addService(ServerServiceDefinition var1) {
        LOG.info("add service into Server");
        return (T) this;
    }

    public Server build() {
        LOG.info("build server");
        ServerImpl server = new ServerImpl(this);   //这里通过具体的ServerBuilder创建服务实例
        return server;
    }
}
