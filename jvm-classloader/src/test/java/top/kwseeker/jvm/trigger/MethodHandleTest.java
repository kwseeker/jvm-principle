package top.kwseeker.jvm.trigger;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * 在JDK7中，新增了java.lang.invoke.MethodHandle，称之为“现代化反射”。
 *
 * A method handle is a typed, directly executable reference to an underlying method, constructor, field, or similar low-level operation, with optional transformations of arguments or return values.
 * 方法句柄是对底层方法、构造函数、字段或类似的低级操作的有类型的、直接可执行的引用，具有参数或返回值的可选转换。
 *
 * 其实反射和java.lang.invoke.MethodHandle都是间接调用方法的途径，但java.lang.invoke.MethodHandle比反射更简洁，用反射功能会写一大堆冗余代码。
 */
public class MethodHandleTest {

    @Override
    public String toString() {
        return super.toString() + "==MethodHandle";
    }

    public static void main(String[] args) throws Throwable {
        MethodHandleTest handle = new MethodHandleTest();

        MethodType methodType = MethodType.methodType(String.class);

        MethodHandles.Lookup lookup = MethodHandles.lookup();

        MethodHandle methodHandle = lookup.findVirtual(MethodHandleTest.class, "toString", methodType);
        String result = (String) methodHandle.invokeExact(handle);
        System.out.println(result);

        // or like this:
        MethodHandle methodHandle2 = methodHandle.bindTo(handle);
        String result2 = (String) methodHandle2.invokeWithArguments();
        System.out.println(result2);

        // 得到当前Class的不同表示方法，最后一个最好。一般我们在静态上下文用SLF4J得到logger用。
        System.out.println(MethodHandleTest.class);
        System.out.println(handle.getClass());
        System.out.println(MethodHandles.lookup().lookupClass()); // like getClass()
    }
}
