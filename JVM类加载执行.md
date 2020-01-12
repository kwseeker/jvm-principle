# JVM类加载执行
![JVM架构](./imgs/jvm-architecture.png)

以jvm-debug-demo为例说明。

## 编译

Java源文件编译成java字节码文件
    
```
javac -help
javac App.java
javap -help
javap -v -p App.class > App.txt
```

```java
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

```

字节码指令

关于学习字节码有什么用？可以了解ASM，javassist，cglib的实现。
在不想重新编译的前提下修改代码就需要理解应该怎么直接修改字节码文件。

class文件以文本格式打开的话显示的是16进制代码。

关于javap生成人可读的字节码文件(16进制文件生成下面格式文件)的规则，
参考: [深入理解JVM之Java字节码（.class）文件详解](https://windysha.github.io/2018/01/18/%E6%B7%B1%E5%85%A5%E7%90%86%E8%A7%A3JVM%E4%B9%8BJava%E5%AD%97%E8%8A%82%E7%A0%81%EF%BC%88-class%EF%BC%89%E6%96%87%E4%BB%B6%E8%AF%A6%E8%A7%A3/)

```C
Classfile /home/lee/mywork/java/java-base/jvm-principle/jvm-debug-demo/src/main/java/top/kwseeker/jvm/debug/App.class
  Last modified 2020-1-12; size 510 bytes
  MD5 checksum ae03eeb9a704f02cbcb27450e67ba158
  Compiled from "App.java"
public class top.kwseeker.jvm.debug.App
  //反编译并没有显示魔术，魔数是固定的表示这是字节码文件
  //主版本号
  minor version: 0                
  //副版本号      
  major version: 52                     
  //访问标志，详细参考文档：https://docs.oracle.com/javase/specs/jvms/se8/html/jvms-4.html#jvms-4.1-200-E.1
  //表示当前Class为public类型，允许使用invokespecial字节码指令的新语义
  flags: ACC_PUBLIC, ACC_SUPER    
//常量池（代码组成部分的列表），存储类的元数据
//包括文字字符串， 常量值， 当前类的类名， 字段名， 方法名， 各个字段和方法的描述符， 对当前类的字段和方法的引用信息， 当前类中对其他类的引用信息等等
Constant pool:   
  //#N=？中"？"是常量类型,右边是值
  //1号常量是Methodref，说明值是一个方法，
  //这里值表示把7号常量和18号常量用"."连接起来，是Object的无参构造方法             
  #1 = Methodref          #7.#18         // java/lang/Object."<init>":()V
  //表示19号常量是一个类
  #2 = Class              #19            // top/kwseeker/jvm/debug/App
  #3 = Methodref          #2.#18         // top/kwseeker/jvm/debug/App."<init>":()V
  #4 = Methodref          #2.#20         // top/kwseeker/jvm/debug/App.add:()I
  //表示java/lang/System.out:Ljava/io/PrintStream是一个类的成员变量
  #5 = Fieldref           #21.#22        // java/lang/System.out:Ljava/io/PrintStream;
  #6 = Methodref          #23.#24        // java/io/PrintStream.println:(I)V
  #7 = Class              #25            // java/lang/Object
  //构造方法
  #8 = Utf8               <init>
  //方法无返回值
  #9 = Utf8               ()V
  //方法体，看下面详解
  #10 = Utf8               Code
  //LineNumberTable用于存储源码行数和方法内指令对应关系，用于调试（看下面详解）
  #11 = Utf8               LineNumberTable
  #12 = Utf8               add
  //方法返回int类型
  #13 = Utf8               ()I
  //main方法
  #14 = Utf8               main
  //值表示方法入参为String[],无返回值
  //For example, the class name representing the two-dimensional array type int[][] is [[I, while the class name representing the type Thread[] is [Ljava/lang/Thread;.
  #15 = Utf8               ([Ljava/lang/String;)V
  #16 = Utf8               SourceFile
  #17 = Utf8               App.java
  //方法名和入参出参
  #18 = NameAndType        #8:#9          // "<init>":()V
  #19 = Utf8               top/kwseeker/jvm/debug/App
  #20 = NameAndType        #12:#13        // add:()I
  #21 = Class              #26            // java/lang/System
  #22 = NameAndType        #27:#28        // out:Ljava/io/PrintStream;
  #23 = Class              #29            // java/io/PrintStream
  #24 = NameAndType        #30:#31        // println:(I)V
  #25 = Utf8               java/lang/Object
  #26 = Utf8               java/lang/System
  #27 = Utf8               out
  #28 = Utf8               Ljava/io/PrintStream;
  #29 = Utf8               java/io/PrintStream
  #30 = Utf8               println
  #31 = Utf8               (I)V
//方法
{
  public top.kwseeker.jvm.debug.App();
    descriptor: ()V
    flags: ACC_PUBLIC
    //方法代码块
    Code:
      //最大操作数栈，局部变量所需存储空间，方法参数个数（每个方法默认有一个隐藏的this参数）
      stack=1, locals=1, args_size=1
        0: aload_0
        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
        4: return
      //源码第3行对应这里第0行（对应aload_0指令）
      LineNumberTable:
        line 3: 0

  public int add();
    descriptor: ()I
    flags: ACC_PUBLIC
    Code:
      stack=2, locals=4, args_size=1
        0: iconst_1
        1: istore_1
        2: iconst_2
        3: istore_2
        4: iload_1
        5: iload_2
        6: iadd
        7: bipush        100
        9: imul
        10: istore_3
        11: iload_3
        12: ireturn
      LineNumberTable:
        line 6: 0
        line 7: 2
        line 8: 4
        line 9: 11

  public static void main(java.lang.String[]);
    descriptor: ([Ljava/lang/String;)V
    flags: ACC_PUBLIC, ACC_STATIC
    Code:
      stack=2, locals=3, args_size=1
        0: new           #2                  // class top/kwseeker/jvm/debug/App
        3: dup
        4: invokespecial #3                  // Method "<init>":()V
        7: astore_1
        8: aload_1
        9: invokevirtual #4                  // Method add:()I
        12: istore_2
        13: getstatic     #5                  // Field java/lang/System.out:Ljava/io/PrintStream;
        16: iload_2
        17: invokevirtual #6                  // Method java/io/PrintStream.println:(I)V
        20: return
      LineNumberTable:
        line 13: 0
        line 14: 8
        line 15: 13
        line 16: 20
}
SourceFile: "App.java"
```

Class文件结构以及java版本对应关系
![](imgs/class-file-structure0.png)

16进制class文件的组织格式
![](imgs/class-file-structure.png)

> 字节码指令不一定是原子操作。

## 加载

类加载原理参考《类加载机制.md》

这里需要弄明白的几点：类何时被触发加载？类加载都做了什么？如何实现自定义的类加载器？

++类加载触发时机++：

Java 类的加载,使用了“延迟加载”机制,仅在第一次被使用时才会发生真正的加载。
JVM启动期间会预先加载一部分核心类库。
用户自定义的类何时首次被使用比较容易界定，但是rt.jar和ext/*.jar中的类首次使用估计要看main类加载流程了。

可以使用 -XX:+TraceClassLoading 查看类加载日志。

++类加载流程++：

1) 通过类的全限定名来获取定义此类的二进制字节流（class）；  
    这个二进制流并不一定要从class文件获取，还可以从网络获取(Applet)，从zip/Jar/War/Ear获取，
    运行时计算生成（动态代理），从其他文件生成（JSP）等。  
    通过双亲委派机制最终调用native defineClass0/1/2() 方法完整真正的类加载操作。
    至于defineClass到底干了什么就需要看JVM源码了。
2) 将字节流所代表的静态存储结构转化为方法区的运行时数据结构；  
    即将class文件中内容存储到JVM运行时数据区。

    每个线程都拥有自己的虚拟机栈、本地方法栈和程序计数器。

    ![](imgs/线程运行时数据区.png)

3) 在内存中生成一个代表这个类的java.lang.Class对象，作为方法区这个类的各种数据的访问入口。 

数组类的加载不是通过类加载器而是JVM直接创建。

加载阶段完成后虚拟机外部二进制字节流就按照虚拟机所需的格式存储在方法区之中，
方法区中的数据存储格式由虚拟机实现自行定义，虚拟机规范未规定此区域的具体数据结构。
然后在内存中实例化一个java.lang.Class类对象，这个对象将作为程序访问方法区中这些类型数据的外部接口。

++补充++：

类加载后的Class实例并不是类实例instanceKlass本身而是类镜像实例，Class估计是为了实现反射才出现的。

Java8之后类的静态字段（static成员）从instanceKlass迁移到了Class。

## 验证

验证加载阶段获得的二进制字节流.class文件是否符合jvm规范。

a) 文件格式验证  
b) 元数据验证  
c) 字节码验证  
d) 符号引用验证  

## 准备

准备阶段主要是给static变量分配内存(方法区中)，并设置初始值。

```
//准备阶段只将值设置为零值，在初始化阶段才将值设置为123
//因为准备阶段还未执行任何方法，将值设置为123的putstatic指令是在类构造器<clinit>()方法中
//而类构造方法在初始化时执行。
public static int value = 123；
//下面这种写法在准备阶段直接将值设置为123
//编译时将为value生成ConstantValue属性，在准备阶段虚拟机根据这个属性将value赋值为123
public static final int value = 123；
```
上面说的编译又是在哪个阶段？  
加载之前，通过编译生成class文件。

## 解析

虚拟机将常量池内的符号引用替换为直接引用。

解析阶段就更抽象了，稍微说一下，因为不太重要，有两个概念，符号引用，直接引用。说的通俗一点但是不太准确，比如在类A中调用了new B();大家想一想,我们编译完成.class文件后其实这种对应关系还是存在的，只是以字节码指令的形式存在，比如 "invokespecial #2" 大家可以猜到#2其实就是我们的类B了，那么在执行这一行代码的时候，JVM咋知道#2对应的指令在哪,这就是一个静态的家伙，假如类B已经加载到方法区了，地址为(#f00123),所以这个时候就要把这个#2转成这个地址(#f00123),这样JVM在执行到这时不就知道B类在哪了，就去调用了。(说的这么通俗，我都怀疑人生了).其他的，像方法的符号引用，常量的符号引用，其实都是一个意思，大家要明白，所谓的方法，常量，类，都是高级语言(Java)层面的概念，在.class文件中，它才不管你是啥，都是以指令的形式存在,所以要把那种引用关系(谁调用谁，谁引用谁)都转换为地址指令的形式。好了。说的够通俗了。大家凑合理解吧。这块其实不太重要，对于大部分coder来说，所以我就通俗的讲了讲。

## 初始化

根据程序中的赋值语句主动为类变量赋值。

这一块其实就是调用类的构造方法,注意是类的构造方法，不是实例构造函数，实例构造函数就是我们通常写的构造方法，类的构造方法是自动生成的，生成规则:
static变量的赋值操作、static代码块 按照出现的先后顺序来组装。

> 注意:
> 1) static变量的内存分配和初始化是在准备阶段.
> 2) 一个类可以是很多个线程同时并发执行，JVM会加锁保证单一性，所以不要在static代码块中搞一些耗时操作。避免线程阻塞。

> ++如果类还未初始化需要初始化的情况（注意区分Class的初始化和对象的初始化）++：  
> 1.使用new该类实例化对象的时候  
> 2.读取或设置类静态字段的时候（但被final修饰的字段，在编译器时就被放入常量池的静态字段除外static final）  
> 3.调用类静态方法的时候  
> 4.使用反射Class.forName(“xxxx”)对类进行反射调用的时候，该类需要初始化  
> 5.初始化一个类的时候，有父类，先初始化父类（注：1. 接口除外，父接口在调用的时候才会被初始化；2.子类引用父类静态字段，只会引发父类初始化）  
> 6.被标明为启动类的类（即包含main()方法的类）要初始化  
> 7.当使用JDK1.7的动态语言支持时，如果一个java.invoke.MethodHandle实例最后的解析结果 REF_getStatic、REF_putStatic、REF_invokeStatic的方法句柄，并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化

> 初始化顺序：    
> 1. 父类的静态变量和静态块赋值（按照声明顺序）  
> 2. 自身的静态变量和静态块赋值（按照声明顺序）  
> 3. 父类成员变量和块赋值（按照声明顺序）  
> 4. 父类构造器赋值  
>    如果父类中包含有参构造器，却没有无参构造器，则在子类构造器中一定要使用“super(参数)”指定调用父类的有参构造器，不然就会报错  
> 5. 自身成员变量和块赋值（按照声明顺序）  
> 6. 自身构造器赋值  


## 执行

比如：某个线程，执行嵌套调用方法，包含哪些存储空间（本地方法栈、程序计数器、虚拟机栈（栈帧（局部变量表、操作数栈、方法出口、动态链接））、堆、方法区），变量的值是怎么在各个存储空间流转的。

