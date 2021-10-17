package top.kwseeker.jvm.optimization;

public class FullCpuTest {

    public static void main(String[] args) {
        new Thread(()->{
            while (true) {}
        }).start();
        new Thread(()->{
            while (true) {}
        }).start();
    }
}
