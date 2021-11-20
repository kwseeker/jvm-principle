package top.kwseeker.performance;

/**
 * 模拟线程阻塞对CPU的影响
 */
public class ThreadBlock2CpuAffection {

    public static void main(String[] args) throws InterruptedException {
        //假设每次线程都会阻塞２s,同时每秒都有新的线程进来
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(50000);
                    System.out.println("returned");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            Thread.sleep(5);
        }
        System.out.println("out");
    }
}
