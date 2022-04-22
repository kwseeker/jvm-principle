package top.kwseeker.jvm.runtime;

import lombok.Data;

/**
 * 逃逸方法测试
 *
 * 开启逃逸分析和标量替换
 * -Xmx100m -Xms100m -XX:+DoEscapeAnalysis -XX:+PrintGC -XX:+EliminateAllocations
 *
 * 1）赋值给类变量或实例变量 (成功逃逸)
 * 2）方法返回值逃逸   //TODO 代码怎么写才会逃逸？
 * 3）实例引用逃逸     //TODO 代码怎么写才会逃逸？
 */
public class EscapeAnalysisTest {

    //1
    //public static User staticUser;
    //public User priUser;

    public static void main(String[] args) {
        //1
        //for (int i = 0; i < 100000000; i++) {
        //    staticAlloc();
        //}
        //EscapeAnalysisTest app = new EscapeAnalysisTest();
        //for (int i = 0; i < 100000000; i++) {
        //    app.alloc();
        //}

        //2
        //User user;
        //UserA userA = new UserA();
        EscapeAnalysisTest app = new EscapeAnalysisTest();
        //for (int i = 0; i < 100000000; i++) {
        //     //user = app.rtnAlloc();
        //     //app.otherMethod(app.rtnAlloc());
        //    userA.call(app.rtnAlloc());
        //}
        for (int i = 0; i < 100000000; i++) {
            app.rtnAlloc();
        }
    }

    //未逃逸
    //private static void alloc() {
    //    User lee = new User();
    //    lee.setId(1);
    //    lee.setName("Arvin");
    //}

    //1
    //private static void staticAlloc() {
    //    User lee = new User();
    //    lee.setId(1);
    //    lee.setName("Arvin");
    //    staticUser = lee;
    //}
    //private void alloc() {
    //    User lee = new User();
    //    lee.setId(1);
    //    lee.setName("Arvin");
    //    priUser = lee;
    //}

    //2 貌似并不是return就一定逃逸
    //private User rtnAlloc() {
    //    User lee = new User();
    //    lee.setId(1);
    //    lee.setName("Arvin");
    //    return lee;
    //}

    private void rtnAlloc() {
        User lee = new User();
        lee.setId(1);
        lee.setName("Arvin");
        otherMethod(lee);
    }

    private void otherMethod(User user) {
        user.setId(2);
    }

    @Data
    private static class UserA {
        public void call(User user) {
            user.setId(3);
        }
    }

    @Data
    private static class User {
        private int id;
        private String name;
    }
}
