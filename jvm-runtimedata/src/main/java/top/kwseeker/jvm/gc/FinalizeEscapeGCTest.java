package top.kwseeker.jvm.gc;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 借助Finalize逃避一次GC
 */
public class FinalizeEscapeGCTest {

    static List<User> list = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        int i = 0;
        int j = 0;
        for (int k = 0; k < 10; k++) {
            //id > 0 的被list强引用
            list.add(new User(++i, UUID.randomUUID().toString()));
            //id < 0 的无引用
            new User(--j, UUID.randomUUID().toString());
        }

        System.gc();
        Thread.sleep(1000L);    //Finalizer线程优先级很低，等待它完成
        System.out.println("list size: " + list.size());

        //去除对自救成功的对象的引用
        list.removeIf(user -> user.getId() < 0);

        System.gc();
        Thread.sleep(1000L);
        System.out.println("list size: " + list.size());
    }

    @Data
    @AllArgsConstructor
    static class User {
        int id;
        String uuid;

        @Override
        protected void finalize() throws Throwable {
            System.out.println("Yes, I will revival! :）");
            //记住list的引用复活
            list.add(this);
        }
    }
}

//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//Yes, I will revival! :）
//list size: 20             //第二次无法复活
//list size: 10