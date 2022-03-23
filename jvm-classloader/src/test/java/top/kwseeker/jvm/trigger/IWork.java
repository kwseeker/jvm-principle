package top.kwseeker.jvm.trigger;

public interface IWork {

    void doWork();

    default void prepare() {
        System.out.println("prepared!");
    }
}
