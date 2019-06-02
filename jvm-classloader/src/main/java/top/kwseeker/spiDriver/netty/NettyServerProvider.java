package top.kwseeker.spiDriver.netty;

import top.kwseeker.classloader.grpcServerProvider.ServerProvider;

public class NettyServerProvider extends ServerProvider {

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 5;
    }

    @Override
    protected NettyServerBuilder builderForPort(int port) {
        return NettyServerBuilder.forPort(port);
    }
}
