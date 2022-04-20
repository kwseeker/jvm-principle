package top.kwseeker.jvm.runtime;

import org.openjdk.jol.info.ClassLayout;
import sun.misc.Unsafe;
import top.kwseeker.jvm.util.ClassIntrospector;
import top.kwseeker.jvm.util.ObjectInfo;

import java.lang.reflect.Field;

/**
 * 对象内存占用测试
 * 这个内存占用大小可以使用Unsafe获取
 */
public class ObjectMemorySizeTest {

    private final static Unsafe UNSAFE;
    static {
        try {
            //Unsafe是单例模式对象，包含一个 theUnsafe 静态成员实例
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new Error();
        }
    }

    public static void main(String[] args) throws IllegalAccessException {
        final ClassIntrospector ci = new ClassIntrospector();
        ObjectInfo res;

        res = ci.introspect( new ObjectC());
        //res = ci.introspect( new ObjectD());
        System.out.println( res.getDeepSize() );    //？？？ 这个算的到底是啥

        ClassLayout layout = ClassLayout.parseInstance(new ObjectC());
        System.out.println(layout.toPrintable());   //16bytes
    }

    private static class ObjectC {
        ObjectD[] array = new ObjectD[2];

        public ObjectC(){
            array[0] = new ObjectD();
            array[1] = new ObjectD();
        }
    }

    private static class ObjectD {
        int value;
    }
}