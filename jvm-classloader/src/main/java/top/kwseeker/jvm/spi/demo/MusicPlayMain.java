package top.kwseeker.jvm.spi.demo;

import java.util.ServiceLoader;

/**
 * SPI最简Demo
 */
public class MusicPlayMain {

    public static void main(String[] args) {
        ServiceLoader<IMusic> musics = ServiceLoader.load(IMusic.class);
        for (IMusic music : musics) {
            music.play();
        }
    }
}
