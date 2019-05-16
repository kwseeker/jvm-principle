package top.kwseeker.classLoader.ch7.classStaticMember;

/**
 * 注意区分类初始化和实例初始化：
 * 静态块和静态成员赋值在类加载的初始化阶段完成；
 * 构造方法和初始化块在类实例初始化阶段完成，构造函数先于构造块执行。
 */
public class ClassStaticMemberTest {
    private static Object obj = new Object();
    private static Object obj1;
    private static Object obj2;

    //静态块/静态初始化块
    static {
        obj1 = new Object();
    }

    public ClassStaticMemberTest() {

    }

    //初始化块，在构造函数之后执行
    {
        obj2 = new Object();
    }

    public static void main(String[] args) {
        new ClassStaticMemberTest();
    }
}
