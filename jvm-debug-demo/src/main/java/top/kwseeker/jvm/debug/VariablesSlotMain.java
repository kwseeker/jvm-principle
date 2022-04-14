package top.kwseeker.jvm.debug;

/**
 * 变量槽测试 （栈帧->局部变量表->变量槽）
 *
 * 构造方法和实例方法索引为0的变量槽存储this对象引用；
 *
 * 方法入参也存储到变量槽；
 *
 * 各种基本类型的数据占用的大小（long double 占用两个变量槽，其余基本数据类型占用一个变量槽）
 * 引用类型占用大小（一个变量槽）
 */
public class VariablesSlotMain {

    public static long staticEcho(long param) {
        return param;
    }

    private int func1() {
        return 0;
    }


    public long echo(long param) {
        //基本类型
        boolean boolA = true;
        byte byteB = 1;
        char charC = 'A';
        short shortD = 10;
        int intE = 20;
        float floatF = 10.0F;
        long longG = 20L;
        double doubleH = 10.0;
        int ret = func1();
        //对象引用
        Integer integerRef = 10;
        {   //slot重复利用, 退出此代码块releaseWhenOut的空间会被释放，然后这个slot会被分配给下一个局部变量
            int releaseWhenOut = 20;
        }
        //只是为了展示上一个变量槽结束索引
        int finalVal = 1;

        return param;
    }

    public static void main(String[] args) {
        VariablesSlotMain app = new VariablesSlotMain();
        System.out.println(app.echo(Thread.currentThread().getId()));
        System.out.println(staticEcho(Thread.currentThread().getId()));
    }
}
