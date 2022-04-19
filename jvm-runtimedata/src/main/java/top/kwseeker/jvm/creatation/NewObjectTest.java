package top.kwseeker.jvm.creatation;

import java.lang.reflect.InvocationTargetException;

/**
 * 对象创建与内存分配流程
 *
 * 一个 Xxx = new Xxx() 可能包含如下过程
 *  1 Xxx类加载
 *  2
 *  3
 *  4
 *
 */
public class NewObjectTest implements Cloneable {

    public void run() {
        System.out.println("do run ...");
    }

    public static void main(String[] args) {
        NewObjectTest app = new NewObjectTest();

    }

    //public static void main(String[] args) throws CloneNotSupportedException,
    //                                                NoSuchMethodException,
    //                                                IllegalAccessException,
    //                                                InvocationTargetException,
    //                                                InstantiationException {
    //    NewObjectTest app = new NewObjectTest();
    //    app.run();
    //
    //    NewObjectTest app1 = (NewObjectTest) app.clone();
    //    app1.run();
    //
    //    NewObjectTest app2 = NewObjectTest.class.getConstructor().newInstance();
    //    app2.run();
    //}
    // 克隆、反射创建实例的指令并不是new
    // 0 new #5 <top/kwseeker/jvm/creatation/NewObjectTest>
    // 3 dup
    // 4 invokespecial #6 <top/kwseeker/jvm/creatation/NewObjectTest.<init>>
    // 7 astore_1
    // 8 aload_1
    // 9 invokevirtual #7 <top/kwseeker/jvm/creatation/NewObjectTest.run>
    //12 aload_1
    //13 invokevirtual #8 <java/lang/Object.clone>
    //16 checkcast #5 <top/kwseeker/jvm/creatation/NewObjectTest>
    //19 astore_2
    //20 aload_2
    //21 invokevirtual #7 <top/kwseeker/jvm/creatation/NewObjectTest.run>
    //24 ldc #5 <top/kwseeker/jvm/creatation/NewObjectTest>
    //26 iconst_0
    //27 anewarray #9 <java/lang/Class>
    //30 invokevirtual #10 <java/lang/Class.getConstructor>
    //33 iconst_0
    //34 anewarray #11 <java/lang/Object>
    //37 invokevirtual #12 <java/lang/reflect/Constructor.newInstance>
    //40 checkcast #5 <top/kwseeker/jvm/creatation/NewObjectTest>
    //43 astore_3
    //44 aload_3
    //45 invokevirtual #7 <top/kwseeker/jvm/creatation/NewObjectTest.run>
    //48 return

    static class User {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
