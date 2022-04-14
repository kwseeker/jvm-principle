package top.kwseeker.jvm.debug;

import java.util.function.Function;

/**
 * 方法调用指令测试
 *
 * 虚方法和非虚方法调用指令测试
 */
public class MethodCallInstructionMain extends Parent implements IApi {

    public MethodCallInstructionMain() {
        System.out.println("in constructor");
    }

    private void privateFunc() {
        System.out.println("in privateFunc");
    }

    @Override
    public void func1() {
        System.out.println("in func1");
    }

    public void publicFunc() {
        System.out.println("in publicFunc");
    }

    public static void staticFunc() {
        System.out.println("in staticFunc");
    }

    public static <T> void eval(T raw, Function<T, T> function) {
        System.out.println("in functional");
        function.apply(raw);
    }

    /**
     *  0 new #11 <top/kwseeker/jvm/debug/MethodCallInstructionMain>
     *  3 dup
     *  4 invokespecial #12 <top/kwseeker/jvm/debug/MethodCallInstructionMain.<init> : ()V>
     *  7 astore_1
     *  8 aload_1
     *  9 invokespecial #13 <top/kwseeker/jvm/debug/MethodCallInstructionMain.privateFunc : ()V>
     * 12 aload_1
     * 13 invokevirtual #14 <top/kwseeker/jvm/debug/MethodCallInstructionMain.parentFunc : ()V>
     * 16 aload_1
     * 17 invokevirtual #15 <top/kwseeker/jvm/debug/MethodCallInstructionMain.func1 : ()V>
     * 20 aload_1
     * 21 invokevirtual #16 <top/kwseeker/jvm/debug/MethodCallInstructionMain.publicFunc : ()V>
     * 24 aload_1
     * 25 invokeinterface #17 <top/kwseeker/jvm/debug/IApi.func1 : ()V> count 1
     * 30 invokestatic #18 <top/kwseeker/jvm/debug/MethodCallInstructionMain.staticFunc : ()V>
     * 33 iconst_2
     * 34 invokestatic #19 <java/lang/Integer.valueOf : (I)Ljava/lang/Integer;>
     * 37 astore_2
     * 38 ldc #20 <2.1>
     * 40 invokestatic #21 <java/lang/Float.valueOf : (F)Ljava/lang/Float;>
     * 43 astore_3
     * 44 aload_2
     * 45 invokedynamic #22 <apply, BootstrapMethods #0>
     * 50 invokestatic #23 <top/kwseeker/jvm/debug/MethodCallInstructionMain.eval : (Ljava/lang/Object;Ljava/util/function/Function;)V>
     * 53 aload_3
     * 54 invokedynamic #24 <apply, BootstrapMethods #1>
     * 59 invokestatic #23 <top/kwseeker/jvm/debug/MethodCallInstructionMain.eval : (Ljava/lang/Object;Ljava/util/function/Function;)V>
     * 62 return
     */
    public static void main(String[] args) {

        // invokespecial 调用<init>方法、私有方法
        MethodCallInstructionMain app = new MethodCallInstructionMain();
        app.privateFunc();

        // invokevirtual 调用虚方法
        app.parentFunc(); //同((Parent) app).parentFunc();
        app.func1();
        app.publicFunc();

        // invokeinterface 调用接口方法
        ((IApi) app).func1();

        // invokestatic 调用静态方法
        MethodCallInstructionMain.staticFunc();

        // 调用动态解析出的方法
        Integer intVal = 2;
        Float floatVal = 2.1F;
        eval(intVal, v -> v * v);
        eval(floatVal, v -> v * v);
    }
}

class Parent {

    public Parent() {
        System.out.println("in Parent$constructor");
    }

    void parentFunc() {
        System.out.println("in Parent$staticFunc");
    }
}

interface IApi {
    void func1();
}
