package top.kwseeker.jvm.spi.grpc.server.provider;

import top.kwseeker.jvm.spi.grpc.server.Server;
import top.kwseeker.jvm.spi.grpc.server.ServerServiceDefinition;

public abstract class ServerBuilder<T extends ServerBuilder<T>> {

    public ServerBuilder() {
    }

    public static ServerBuilder<?> forPort(int port) throws Exception {
        return ServerProvider.provider().builderForPort(port);
    }

    public abstract T addService(ServerServiceDefinition var1);

    public abstract Server build();
}
