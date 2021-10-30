package top.kwseeker.jvm.spi.grpc.server.driver.netty;

import top.kwseeker.jvm.spi.grpc.server.provider.ServerProvider;

public class NettyServerProvider extends ServerProvider {

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 1;
    }

    @Override
    protected NettyServerBuilder builderForPort(int port) {
        return NettyServerBuilder.forPort(port);
    }
}
