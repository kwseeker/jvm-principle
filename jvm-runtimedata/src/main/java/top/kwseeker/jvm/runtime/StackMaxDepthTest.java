package top.kwseeker.jvm.runtime;

/**
 * JVM栈最大深度
 * 局部变量表内容越多，栈帧越大，栈深度越小。
 * -XX:+PrintFlagsFinal 打印所有JVM参数值
 *   intx ThreadStackSize                           = 1024                                {pd product}
 */
public class StackMaxDepthTest {

    private int count = 0;

    public void testStack(){
        count++;
        testStack();
    }

    public void testStack(int a, int b, String c){
        a++;
        b++;
        c +="1";
        count++;
        //StringBuilder sb = new StringBuilder();
        testStack(a,b,c);
    }

    public void test(){
        try {
            //testStack();
            testStack(1,2, "1");
        } catch (Throwable e) {
            System.out.println(e);
            System.out.println("stack height:"+count);
        }
    }

    public static void main(String[] args) throws Exception {
        new StackMaxDepthTest().test();
        Thread.sleep(1000*1000);
    }
}

//java.lang.StackOverflowError
//stack height:18162
//
//java.lang.StackOverflowError
//stack height:6824