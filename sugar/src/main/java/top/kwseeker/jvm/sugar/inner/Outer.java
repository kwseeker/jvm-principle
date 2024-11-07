package top.kwseeker.jvm.sugar.inner;

/**
 * 语法糖：内部类
 * java -jar tools/cfr-0.152.jar --removeinnerclasssynthetics false sugar/target/classes/top/kwseeker/jvm/sugar/inner/Outer.class
 * 内部类其实包含一个指向外部类实例的引用
 */
public class Outer {

    class Inner {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class StaticInner {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
//public class Outer {
//
//    static class StaticInner {
//        private String name;
//
//        StaticInner() {
//        }
//
//        public String getName() {
//            return this.name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
//
//    class Inner {
//        private String name;
//        final /* synthetic */ Outer this$0;
//
//        Inner(Outer this$0) {
//            this.this$0 = this$0;
//        }
//
//        public String getName() {
//            return this.name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//    }
//}