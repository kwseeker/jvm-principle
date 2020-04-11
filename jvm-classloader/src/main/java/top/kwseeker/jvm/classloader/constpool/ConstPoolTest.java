package top.kwseeker.jvm.classloader.constpool;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 查看哪些值会进入class常量池
 */
public class ConstPoolTest {

    private boolean aBoolean = true;
    private byte aByte = 40;
    private char aChar = 'a';
    private short aShort = 39;
    private int aInt = 38;
    private float aFloat = 37F;
    private long aLong = 36L;
    private double aDouble = 35.0;

    private final boolean bBoolean = false;
    private final byte bByte = 41;
    private final char bChar = 'b';
    private final short bShort = 42;
    private final int bInt = 43;
    private final float bFloat = 44F;
    private final long bLong = 45L;
    private final double bDouble = 46.0;

    private String str = "hello";
    private ReentrantLock lock = new ReentrantLock();

    public ConstPoolTest() {
    }

    public ConstPoolTest(boolean aBoolean) {
        this.aBoolean = aBoolean;
    }

    public static void main(String[] args) {
    }
}

/**
 * 引用类型：
 * 所有构造方法引用
 * 所有成员变量引用 Fieldref
 * 表示字段方法的名称或类型
 * 类中的方法以及实现的接口的方法的引用
 * 值类型：
 * final修饰的所有基本数据类型的值
 * 非final修饰的float\long\double类型的值
 * 字符串类型的值（“”引起来的值）
 *
 * Constant pool:
 *     #1 = Methodref          #34.#83       // java/lang/Object."<init>":()V
 *     #2 = Fieldref           #33.#84       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aBoolean:Z
 *     #3 = Fieldref           #33.#85       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aByte:B
 *     #4 = Fieldref           #33.#86       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aChar:C
 *     #5 = Fieldref           #33.#87       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aShort:S
 *     #6 = Fieldref           #33.#88       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aInt:I
 *     #7 = Float              37.0f
 *     #8 = Fieldref           #33.#89       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aFloat:F
 *     #9 = Long               36l
 *    #11 = Fieldref           #33.#90       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aLong:J
 *    #12 = Double             35.0d
 *    #14 = Fieldref           #33.#91       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.aDouble:D
 *    #15 = Fieldref           #33.#92       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bBoolean:Z
 *    #16 = Fieldref           #33.#93       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bByte:B
 *    #17 = Fieldref           #33.#94       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bChar:C
 *    #18 = Fieldref           #33.#95       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bShort:S
 *    #19 = Fieldref           #33.#96       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bInt:I
 *    #20 = Float              44.0f
 *    #21 = Fieldref           #33.#97       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bFloat:F
 *    #22 = Long               45l
 *    #24 = Fieldref           #33.#98       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bLong:J
 *    #25 = Double             46.0d
 *    #27 = Fieldref           #33.#99       // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.bDouble:D
 *    #28 = String             #100          // hello
 *    #29 = Fieldref           #33.#101      // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.str:Ljava/lang/String;
 *    #30 = Class              #102          // java/util/concurrent/locks/ReentrantLock
 *    #31 = Methodref          #30.#83       // java/util/concurrent/locks/ReentrantLock."<init>":()V
 *    #32 = Fieldref           #33.#103      // top/kwseeker/jvm/classloader/constpool/ConstPoolTest.lock:Ljava/util/concurrent/locks/ReentrantLock;
 *    #33 = Class              #104          // top/kwseeker/jvm/classloader/constpool/ConstPoolTest
 *    #34 = Class              #105          // java/lang/Object
 *    #35 = Utf8               aBoolean
 *    #36 = Utf8               Z
 *    #37 = Utf8               aByte
 *    #38 = Utf8               B
 *    #39 = Utf8               aChar
 *    #40 = Utf8               C
 *    #41 = Utf8               aShort
 *    #42 = Utf8               S
 *    #43 = Utf8               aInt
 *    #44 = Utf8               I
 *    #45 = Utf8               aFloat
 *    #46 = Utf8               F
 *    #47 = Utf8               aLong
 *    #48 = Utf8               J
 *    #49 = Utf8               aDouble
 *    #50 = Utf8               D
 *    #51 = Utf8               bBoolean
 *    #52 = Utf8               ConstantValue
 *    #53 = Integer            0
 *    #54 = Utf8               bByte
 *    #55 = Integer            41
 *    #56 = Utf8               bChar
 *    #57 = Integer            98
 *    #58 = Utf8               bShort
 *    #59 = Integer            42
 *    #60 = Utf8               bInt
 *    #61 = Integer            43
 *    #62 = Utf8               bFloat
 *    #63 = Utf8               bLong
 *    #64 = Utf8               bDouble
 *    #65 = Utf8               str
 *    #66 = Utf8               Ljava/lang/String;
 *    #67 = Utf8               lock
 *    #68 = Utf8               Ljava/util/concurrent/locks/ReentrantLock;
 *    #69 = Utf8               <init>
 *    #70 = Utf8               ()V
 *    #71 = Utf8               Code
 *    #72 = Utf8               LineNumberTable
 *    #73 = Utf8               LocalVariableTable
 *    #74 = Utf8               this
 *    #75 = Utf8               Ltop/kwseeker/jvm/classloader/constpool/ConstPoolTest;
 *    #76 = Utf8               (Z)V
 *    #77 = Utf8               main
 *    #78 = Utf8               ([Ljava/lang/String;)V
 *    #79 = Utf8               args
 *    #80 = Utf8               [Ljava/lang/String;
 *    #81 = Utf8               SourceFile
 *    #82 = Utf8               ConstPoolTest.java
 *    #83 = NameAndType        #69:#70       // "<init>":()V
 *    #84 = NameAndType        #35:#36       // aBoolean:Z
 *    #85 = NameAndType        #37:#38       // aByte:B
 *    #86 = NameAndType        #39:#40       // aChar:C
 *    #87 = NameAndType        #41:#42       // aShort:S
 *    #88 = NameAndType        #43:#44       // aInt:I
 *    #89 = NameAndType        #45:#46       // aFloat:F
 *    #90 = NameAndType        #47:#48       // aLong:J
 *    #91 = NameAndType        #49:#50       // aDouble:D
 *    #92 = NameAndType        #51:#36       // bBoolean:Z
 *    #93 = NameAndType        #54:#38       // bByte:B
 *    #94 = NameAndType        #56:#40       // bChar:C
 *    #95 = NameAndType        #58:#42       // bShort:S
 *    #96 = NameAndType        #60:#44       // bInt:I
 *    #97 = NameAndType        #62:#46       // bFloat:F
 *    #98 = NameAndType        #63:#48       // bLong:J
 *    #99 = NameAndType        #64:#50       // bDouble:D
 *   #100 = Utf8               hello
 *   #101 = NameAndType        #65:#66       // str:Ljava/lang/String;
 *   #102 = Utf8               java/util/concurrent/locks/ReentrantLock
 *   #103 = NameAndType        #67:#68       // lock:Ljava/util/concurrent/locks/ReentrantLock;
 *   #104 = Utf8               top/kwseeker/jvm/classloader/constpool/ConstPoolTest
 *   #105 = Utf8               java/lang/Object
**/