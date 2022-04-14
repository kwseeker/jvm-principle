package top.kwseeker.jvm.debug;

public class App1 {

    public int compute() {
        int a = 10;
        int b = 20;
        int c = 30;
        int d = 40;
        int e = 50;
        int f = 60;
        int g = 70;
        int h = 80;
        return (a + b + c + d + e + f + g) * 100;
    }

    public static void main(String[] args) {
        App1 app = new App1();
        int result = app.compute();
        System.out.println(result);
    }
}
