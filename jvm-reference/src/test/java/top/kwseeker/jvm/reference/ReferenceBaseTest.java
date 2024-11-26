package top.kwseeker.jvm.reference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceBaseTest {

    @Test
    public void testStrongReference() {
        //默认就是创建强引用对象
        MyObject object = new MyObject("strong");
        System.out.println(object);
        object = null;
        System.gc();
    }

    @Test
    public void testReferenceUsage() {
        ////强引用
        //MyObject object1 = new MyObject();  //object1 作为 GC Roots
        //System.out.println(object1);
        //// object1 = null;      //这里不置空，或程序不执行结束，或程序不被杀死，则object1指向的对象将一直存在
        //System.gc();
        //System.out.println(object1);
        //
        ////软引用
        //MyObject object2 = new MyObject();
        //SoftReference<MyObject> srReference = new SoftReference<>(object2);   //创建软引用
        //object2 = null;
        //System.out.println(srReference.get());
        //System.gc();
        //System.out.println(srReference.get());  //一般情况还是会打印出原来的对象，可以修改VM Options, 强制内存不足，则会打印null

        //弱引用
        MyObject object3 = new MyObject("WeakReferenceTest");
        WeakReference<MyObject> wrReference = new WeakReference<>(object3);   //创建弱引用
        object3 = null;
        System.out.println(wrReference.get());
        System.gc();
        System.out.println(wrReference.get());  //这里将打印null
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyObject {
        private String name;

        //@Override
        //protected void finalize() throws Throwable {
        //    System.out.println("do something before gc ...");
        //}
    }
}
