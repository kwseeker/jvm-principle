package top.kwseeker.jvm.spi.demo.driver;

import top.kwseeker.jvm.spi.demo.IMusic;

public class QQMusic implements IMusic {

    @Override
    public void play() {
        System.out.println("QQ音乐 playing ...");
    }
}
