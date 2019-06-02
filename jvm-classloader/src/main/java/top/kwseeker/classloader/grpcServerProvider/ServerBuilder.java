package top.kwseeker.classloader.grpcServerProvider;

import top.kwseeker.classloader.grpcServer.Server;
import top.kwseeker.classloader.grpcServer.ServerServiceDefinition;

public abstract class ServerBuilder<T extends ServerBuilder<T>> {

    public ServerBuilder() {
    }

    public static ServerBuilder<?> forPort(int port) throws Exception {
        return ServerProvider.provider().builderForPort(port);
    }

    public abstract T addService(ServerServiceDefinition var1);

    public abstract Server build();
}
