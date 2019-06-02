package top.kwseeker.classloader.grpcServer;

import top.kwseeker.classloader.grpcServerProvider.ServerBuilder;

public class GrpcServerMain {

    public static void main(String[] args) {
        try {
            Server server = ServerBuilder.forPort(8081)
                    .addService(new ServerServiceDefinition())
                    .build();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
