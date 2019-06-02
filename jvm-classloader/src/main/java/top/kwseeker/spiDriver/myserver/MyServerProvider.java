package top.kwseeker.spiDriver.myserver;

import top.kwseeker.classloader.grpcServerProvider.ServerProvider;

/**
 * 具体的
 */
public class MyServerProvider extends ServerProvider {

    @Override
    protected boolean isAvailable() {
        return true;
    }

    @Override
    protected int priority() {
        return 3;
    }

    /**
     * 具体的Server的实现
     * @param port
     * @return
     */
    @Override
    protected MyServerBuilder builderForPort(int port) {
        return MyServerBuilder.forPort(port);
    }
}
