package top.kwseeker.jvm.debug;

public class App {

    public int sum(int a, int b) {
        return a + b;
    }

    public int compute() {
        int a = 1;
        int b = 2;
        return sum(a, b) * 100;
    }

    public static void main(String[] args) {
        App app = new App();
        int result = app.compute();
        System.out.println(result);
    }
}
