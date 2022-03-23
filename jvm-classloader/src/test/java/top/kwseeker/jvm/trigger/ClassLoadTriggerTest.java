package top.kwseeker.jvm.trigger;

import org.junit.Test;

import java.lang.reflect.Field;

/**
 * ！！！ 下面只能说明这些初始化时机一定完成了加载，但是并不能说明什么时候触发的加载
 * -XX:+TraceClassLoading
 */
public class ClassLoadTriggerTest {

    @Test
    public void test() {
        User user;                          //不会加载
    }

    @Test
    public void testNew() {
        new User();                         //会加载 [Loaded top.kwseeker.jvm.trigger.User from file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/target/test-classes/]
    }

    @Test
    public void testGetPutStatic() {
        //int defaultAge = User.age;          //会加载 [Loaded top.kwseeker.jvm.trigger.User from file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/target/test-classes/]
        User.age = 18;                      //会加载 [Loaded top.kwseeker.jvm.trigger.User from file:/home/lee/mywork/java/java-base/jvm-principle/jvm-classloader/target/test-classes/]
    }

    @Test
    public void testInvokeStatic() {
        User.method();                      //会加载，同上
    }

    @Test
    public void testReflectionCall() throws NoSuchFieldException, ClassNotFoundException {
        //Field ageField = User.class.getDeclaredField("age");    //会加载，同上
        //Class<?> clazz = User.class;                            //会加载，这个属于哪种情况???
        //Class.forName("top.kwseeker.jvm.trigger.User");         //会加载，属于反射
    }

    @Test
    public void testInitialChildClass() {
        //Programmer programmer = new Programmer();   //会先加载父类
        System.out.println(Programmer.age);         //会加载父类和子类
                                                    // 但是这里子类没有执行static{}, 因为子类没有初始化，因为Programmer.age是执行的User的 getstatic 指令，User一定是初始化了的，
                                                    // 但是Programmer没有任何满足初始化的条件，所以只是类加载了但是没有初始化
    }

    @Test
    public void testDefaultInterfaceMethod() {
        Program program = new Program();            //父类IWork会被先加载
    }
}
