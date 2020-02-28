package top.kwseeker.jvm.debug;

public class App {

    public int add() {
        int a = 1;
        int b = 2;
        int c = (a + b) * 100;
        return c;
    }

    public static void main(String[] args) {
        App app = new App();
        int result = app.add();
        System.out.println(result);
    }
}
