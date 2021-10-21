package top.kwseeker.jvm.spi.grpc.server;

import top.kwseeker.jvm.spi.grpc.server.provider.ServerBuilder;

/**
 * 这部分代码抽离自Google开源项目 grpc-java。
 * 使用SPI机制加载通信组件，如：Netty。
 */
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
