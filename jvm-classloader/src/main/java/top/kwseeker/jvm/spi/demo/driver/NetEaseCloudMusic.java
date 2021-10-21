package top.kwseeker.jvm.spi.demo.driver;

import top.kwseeker.jvm.spi.demo.IMusic;

/**
 * 网易云音乐
 */
public class NetEaseCloudMusic implements IMusic {

    @Override
    public void play() {
        System.out.println("网易云音乐 playing ...");
    }
}
