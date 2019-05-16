package top.kwseeker.classLoader.ch7.case4;

/**
 * 使用组合的方式实现父类子类
 *
 * 如果某项属性可能不是所有子类的必需属性，则可以将其以组合的方式为某些子类添加这一属性
 * 如果子类这个属性由不同的实现的化，就将其定义为接口由子类implement。
 */
public class CompositionTest {

    static class Draw {
        void draw() {
            System.out.println("draw");
        }
    }

    static class Erase {
        void erase() {
            System.out.println("erase");
        }
    }

    //Work 组合 Draw 和 Erase
    static class Work {
        void work() {
            new Draw().draw();
            new Erase().erase();
        }
    }

    public static void main(String[] args) {
        new Work().work();
    }
}
