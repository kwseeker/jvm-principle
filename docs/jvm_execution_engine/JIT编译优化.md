# JIT编译优化

更多参考：《深入理解Java虚拟机》chapter11。



## 编译优化措施

+ **公共子表达式消除**

  为了防止返回结果必定相同的子表达式重复执行。分为局部公共子表达式消除和全局公共子表达式消除。

  ```java
  int d = (c*b)*12+a+(a+b*c);
  //可能经历下面过程优化
  int d = E*12+a+(a+E);
  int d = E*13+a*2;
  ```

+ **数组边界检查消除**

  

+ **方法内联**

  将方法调用直接使用方法体中的代码进行替换,它减少了方法调用过程中压栈与入栈的开销,同时为之后的一些优化手段提供条件。

  ```java
  private int add4(int x1, int x2, int x3, int x4) {
    return add2(x1, x2) + add2(x3, x4);
  }
  private int add2(int x1, int x2) {
    return x1 + x2;
  }
  //被优化为
  private int add4(int x1, int x2, int x3, int x4) {
    return x1 + x2 + x3 + x4;
  }
  ```

+ **逃逸分析(不是优化，而是后面三种优化的前提)**

  逃逸分析(Escape Analysis)是动态分析对象作用域的分析算法。
  如果某个对象在某个区块被创建，作用域始终在这个区块中，则没有逃逸；如果对象作用于此区块之外，则发生逃逸。

  逃逸分析包括:  
  １）全局变量赋值逃逸  
  ２）方法返回值逃逸  
  ３）实例引用发生逃逸   
  ４）线程逃逸:赋值给类变量或可以在其他线程中访问的实例变量  

  开关逃逸分析：

  ```
  -XX:+DoEscapeAnalysis : 表示开启逃逸分析
  -XX:-DoEscapeAnalysis : 表示关闭逃逸分析
  ```

  举例：

  ```java
  public class EscapeAnalysis {
    //全局变量
    public static Object object;
  
    public void globalVariableEscape(){//全局变量赋值逃逸
      object = new Object();
    }
  
    public Object methodEscape(){
      //方法返回值逃逸
      return new Object();
    }
  
    public void instancePassEscape(){ 
      //实例引用发生逃逸
      this.speak(this);
    }
    public void speak(EscapeAnalysis escapeAnalysis){
      System.out.println("Escape Hello");
    }
  }
  ```

+ **对象的栈上内存分配**

  对象分配流程：

  ![](https://images2018.cnblogs.com/blog/1137223/201807/1137223-20180724163533976-1404788272.png)

  如果对象始终作用于某个区块内部，就像局部变量一样，则可以将此对象内存分配放到栈上。随着栈空间一起创建和消亡，好处是避免参与GC回收，降低系统内存回收的压力。

  >  没有逃逸的对象虽说先尝试在栈上分配，但是当栈空间不足的时候，还是会在堆上进行分配。
  >
  >  TLAB，全称Thread Local Allocation Buffer, 即：线程本地分配缓存。这是一块线程专用的内存分配区域。TLAB占用的是eden区的空间。在TLAB启用的情况下（默认开启），JVM会为每一个线程分配一块TLAB区域。
  >
  >  这是为了加速对象的分配。由于对象一般分配在堆上，而堆是线程共用的，因此可能会有多个线程在堆上申请空间，而每一次的对象分配都必须线程同步，会使分配的效率下降。考虑到对象分配几乎是Java中最常用的操作，因此JVM使用了TLAB这样的线程专有区域来避免多线程冲突，提高对象分配的效率。

  需要开启：  

  ```sh
  -XX:+DoEscapeAnalysis
  #还可以添加下面参数查看内存回收情况
  -XX:+PrintGCDetails
  ```

  ```java
  public class EscapeAnalysisTest {
    public static void main(String[] args) {
      long a1 = System.currentTimeMillis();
      for (int i = 0; i < 1000000; i++) {
        alloc();
      }
      // 查看执行时间
      long a2 = System.currentTimeMillis();
      System.out.println("cost " + (a2 - a1) + " ms");
      // 为了方便查看堆内存中对象个数，线程sleep
      try {
        Thread.sleep(100000);
      } catch (InterruptedException e1) {
        e1.printStackTrace();
      }
    }
  
    private static void alloc() {
      User user = new User();
    }
  
    static class User {
    }
  }
  ```

  ```
  jmap -histo <pid>   #查看堆内存对象实例情况
  ```

  疑问：为何存储在栈上的对象，实例数是８万多，而不是０？栈内存怎么回收的？

+ **标量替换**

  标量(Scalar) 是指一个无法再分解成更小的数据的数据。

  在JIT阶段,如果经过逃逸分析,发现一个对象不会被外界访问的话,那么经过JIT优化,就会把这个对象拆解成若干个其中包含的若干个成员变量来代替。

  ```java
  //有一个类A
  public class A{
    public int a=1;
    public int b=2
  }
  //方法getAB使用类A里面的a,b
  private void getAB(){
    A x = new A();
    x.a;
    x.b;
  }
  //JVM在编译的时候会直接编译成
  private void getAB(){
    a = 1;
    b = 2;
  }
  //这就是标量替换
  ```

+ **同步锁消除**

  同样基于逃逸分析,当加锁的变量不会发生逃逸,是线程私有的完全没有必要加锁。 在JIT编译时期就可以将同步锁去掉,以减少加锁与解锁造成的资源开销。

  需要开启

  ```sh
  -XX:+DoEscapeAnalysis -XX:+EliminateLocks
  ```

  ```java
  public class TestLockEliminate {
    public static String getString(String s1, String s2) {
      //StringBuffer内部有加synchronized
      //但sb没有逃逸
      StringBuffer sb = new StringBuffer();
      sb.append(s1);  //append()不会加synchronized
      sb.append(s2);
      return sb.toString();
    }
  
    public static void main(String[] args) {
      long tsStart = System.currentTimeMillis();
      for (int i = 0; i < 100000000; i++) {
        getString("TestLockEliminate ", "Suffix");
      }
      System.out.println("一共耗费：" + (System.currentTimeMillis() - tsStart) + " ms");
      //System.gc();
    }
  }
  ```

 