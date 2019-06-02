package top.kwseeker.spiDriver.myserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.kwseeker.spiDriver.builder.AbstractServerImplBuilder;

public class MyServerBuilder extends AbstractServerImplBuilder<MyServerBuilder> {

    private static Logger LOG = LoggerFactory.getLogger(MyServerBuilder.class);

    public static MyServerBuilder forPort(int port) {
        return new MyServerBuilder(port);
    }

    private MyServerBuilder(int port) {
        LOG.info("Create MyServer server on port {}", port);
        //这里要构建自己的Server
    }


}
