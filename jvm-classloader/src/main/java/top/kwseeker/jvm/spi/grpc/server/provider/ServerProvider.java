package top.kwseeker.jvm.spi.grpc.server.provider;

import top.kwseeker.jvm.spi.grpc.server.provider.ServiceProviders.PriorityAccessor;

/**
 *
 */
public abstract class ServerProvider {

    private static final ServerProvider provider = ServiceProviders.load(
            ServerProvider.class,
            ServerProvider.class.getClassLoader(),
            new PriorityAccessor<ServerProvider>() {
                public boolean isAvailable(ServerProvider provider) {
                    return provider.isAvailable();
                }
                public int getPriority(ServerProvider provider) {
                    return provider.priority();
                }
            });

    public ServerProvider() {
    }

    public static ServerProvider provider() throws Exception {
        if (provider == null) {
            throw new Exception("No functional server found.");
        } else {
            return provider;
        }
    }

    protected abstract boolean isAvailable();

    protected abstract int priority();

    protected abstract ServerBuilder<?> builderForPort(int var1);
}
