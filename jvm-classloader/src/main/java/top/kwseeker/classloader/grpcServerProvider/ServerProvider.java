package top.kwseeker.classloader.grpcServerProvider;

import top.kwseeker.classloader.grpcServerProvider.ServiceProviders.PriorityAccessor;

/**
 *
 */
public abstract class ServerProvider {

    private static final ServerProvider provider = (ServerProvider) ServiceProviders.load(
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
