package top.kwseeker.jvm.jit;

/**
 * JIT优化：同步锁消除
 *
 * -XX:+DoEscapeAnalysis (默认开启)
 * -XX:+EliminateLocks　(默认开启)
 * 耗时：2915ms
 *
 * 把逃逸分析关掉再看耗时：
 * -XX:-DoEscapeAnalysis
 * 耗时：4567ms
 */
public class LockEliminateTest {

    public static void main(String[] args) {
        long tsStart = System.currentTimeMillis();
        for (int i = 0; i < 100000000; i++) {
            getString("TestLockEliminate ", "Suffix");
        }
        System.out.println("一共耗费：" + (System.currentTimeMillis() - tsStart) + " ms");
    }

    static String getString(String s1, String s2) {
        //sb未逃逸出此方法块，且不存在锁竞争,JIT对锁进行消除
        StringBuffer sb = new StringBuffer();
        sb.append(s1);
        sb.append(s2);
        return sb.toString();
    }
}
