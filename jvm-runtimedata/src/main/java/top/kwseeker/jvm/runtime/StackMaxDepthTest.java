package top.kwseeker.jvm.runtime;

/**
 * JVM栈最大深度(并不是固定值，取决于栈空间大小与栈帧大小)
 *  局部变量表内容越多，栈帧越大（注意栈帧本身信息也占一部分栈帧内存），栈深度越小。
 *  当栈空间耗完，会打印“栈溢出”错误 StackOverflowError
 *  可以用于监控服务器运行状态信息，调参数优化应用内存占用，避免内存浪费
 * -XX:+PrintFlagsFinal 打印所有JVM参数值
 *  intx ThreadStackSize                           = 1024                                {pd product}   //默认１M
 * -Xss256K (设置线程栈大小，单位不区分大小写256k也可以)
 *  intx ThreadStackSize                          := 256                                 {pd product}   //改为了256K
 */
public class StackMaxDepthTest {

    private int count = 0;

    public void testStack() {
        count++;
        testStack();
    }

    public void testStack(int a, long b, String c) {
        count++;
        a++;        //1个slot
        b++;        //2个slot
        c += "1";   //1个slot
        testStack(a, b, c);
    }

    public void test() {
        try {
            testStack();
            //testStack(1, 2, "1");
        } catch (Throwable e) {
            System.out.println(e);
            System.out.println("stack depth:" + count);
        }
    }

    public static void main(String[] args) throws Exception {
        new StackMaxDepthTest().test();
        Thread.sleep(1000 * 1000);
    }
}

//默认1M时：

//java.lang.StackOverflowError
//stack depth:18167
//java.lang.StackOverflowError
//stack depth:5594

//改为256K后

//java.lang.StackOverflowError
//stack depth:1821
//java.lang.StackOverflowError
//stack depth:1061