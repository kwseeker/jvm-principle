# JVM参数与JVM工具使用

## JVM参数

[HotSpot JVM参数官方文档](https://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)

主要分为三类：标准参数、-X非标准参数、-XX非标准参数。

命令行查看参数说明
```sh
java -help  #查看标准参数
java -X     #查看非标准参数
java -XX:+PrintFlagsFinal   #查看所有-XX参数
jinfo -flags <pid>          #查看正在运行的java进程启用的参数
jinfo -flag <premeter> <pid>   #查看正在运行java进程某个参数的值
```

+ 常用配置参数
![JVM常用参数](imgs/JVM常用参数.png)

  | 参数 | 功能 |
  |:----|:--------|
  | -Xms | 初始堆大小。如：-Xms256m |
  | -Xmx | 最大堆大小。如：-Xmx512m |
  | -Xmn | 新生代大小。通常为 Xmx 的 1/3 或 1/4。新生代 = Eden + 2 个 Survivor 空间。实际可用空间为 = Eden + 1 个 Survivor，即 90% |
  | -Xss | JDK1.5+ 每个线程堆栈大小为 1M，一般来说如果栈不是很深的话， 1M 是绝对够用了的。|
  | -XX:NewRatio | 新生代与老年代的比例，如 –XX:NewRatio=2，则新生代占整个堆空间的1/3，老年代占2/3 |
  | -XX:SurvivorRatio | 新生代中 Eden 与 Survivor 的比值。默认值为 8。即 Eden 占新生代空间的 8/10，另外两个 Survivor 各占 1/10 |
  | -XX:+HeapDumpOnOutOfMemoryError | 让虚拟机在发生内存溢出时 Dump 出当前的内存堆转储快照，以便分析用 |
  | -XX:-+DisableExplicitGC | 禁用 System.gc()，由于该方法默认会触发 FGC，并且忽略参数中的 UseG1GC 和 UseConcMarkSweepGC，因此必要时可以禁用该方法。|
  | -XX:+ExplicitGCInvokesConcurrent | 该参数可以改变上面的行为，也就是说，System.gc() 后不使用 FGC ，而是使用配置的并发收集器进行并发收集。注意：使用此选项就不要 使用 上面的选项。 |
  | -XX:-ScavengeBeforeFullGC | 由于大部分 FGC 之前都会 YGC，减轻了 FGC 的压力，缩短了 FGC 的停顿时间，但也可能你不需要这个特性，那么你可以使用这个参数关闭，默认是 ture 开启。 |
  | -XX:MaxTenuringThreshold={value} | 新生代 to 区的对象在经过多次 GC 后，如果还没有死亡，则认为他是一个老对象，则可以晋升到老年代，而这个年龄（GC 次数）是可以设置的，有就是这个参数。默认值时15。超过15 则认为是无限大(因为age变量时4个 bit，超过15无法表达)。但该参数不是唯一决定对象晋升的条件。当 to 区不够或者改对象年龄已经达到了平均晋升值或者大对象等等条件。|
  | -XX:TargetSurvivorRatio={value} | 决定对何时晋升的不仅只有 XX:MaxTenuringThreshold 参数，如果在 Survivor 空间中相同年龄所有对象大小的总和大鱼 Survivor 空间的一半（默认50%），年龄大于或等于该年龄的对象就可以直接进入老年代。无需在乎 XX:MaxTenuringThreshold参数。因此，MaxTenuringThreshold 只是对象晋升的最大年龄。如果将 TargetSurvivorRatio 设置的很小，对象将晋升的很快。|
  | -XX:PretenureSizeThresholds={value} | 除了年龄外，对象的体积也是影响晋升的一个关键，也就是大对象。如果一个对象新生代放不下，只能直接通过分配担保机制进入老年代。该参数是设置对象直接晋升到老年代的阈值，单位是字节。只要对象的大小大于此阈值，就会直接绕过新生代，直接进入老年代。注意： 这个参数只对 Serial 和 ParNew 有效，ParallelGC 无效 ，默认情况下该值为0，也就是不指定最大的晋升大小，一切有运行情况决定。|
  | -XX:-UseTLAB | 禁用线程本地分配缓存。TLAB 的全称是 Thread LocalAllocation Buffer ，即线程本地线程分配缓存，是一个线程私有的内存区域。该设计是为了加速对象分配速度。由于对象一般都是分配在堆上，而对是线程共享的。因此肯定有锁，虽然使用 CAS 的操作，但性能仍有优化空间。通过为每一个线程分配一个 TLAB 的空间（在 eden 区），可以消除多个线程同步的开销。默认开启。 |
  | -XX:TLABSize | 指定 TLAB 的大小
  | -XX:+PrintTLAB | 跟踪 TLAB 的使用情况。用以确定是用多大的 TLABSize。 |
  | -XX:+ResizeTLAB | 自动调整 TLAB 大小。 |

+ 调试参数

  | 参数 | 功能 |
  |:----|:--------|
  | -XX:+PrintGC | 输出简要GC日志 |
  | -XX:+PrintGCDetails | 输出详细GC日志 |
  | -Xloggc:gc.log | 输出GC日志到文件 |
  | -XX:+PrintGCTimeStamps | 输出GC的时间戳（以JVM启动到当期的总时长的时间戳形式）|
  | -XX:+PrintGCDateStamps | 输出GC的时间戳（以日期的形式，如 2013-05-04T21:53:59.234+0800）|
  | -XX:+PrintHeapAtGC | 在进行GC的前后打印出堆的信息 |
  | -verbose:gc ||
  | -XX:+PrintReferenceGC | 打印年轻代各个引用的数量以及时长 |
  | -XX:+TraceClassLoading | 程序运行时输出类加载过程信息 |


## JVM调试工具

想要的工具人家都做好了。

### jps（JVM进程状态工具）

常用参数：  
```
-q 不输出类名、Jar名和传入main方法的参数  
-m 输出传入main方法的参数  
-l 输出main类或Jar的全限名  
-v 输出传入JVM的参数  
```

### jinfo（实时查看和调整JVM配置参数）

常用参数：  
```
jinfo -flag <name> PID  
jinfo -flag [+|-]<name> PID – 布尔型参数  
jinfo -flag <name>=<value> PID – 数字或字符型参数  
```

### Jstat (JVM统计信息监视工具)

```
jstat -gc 14852 500 4
```

S0C、S1C、S0U、S1U：Survivor 0/1区容量（Capacity）和使用量（Used） EC、EU：Eden区容量和使用量 OC、OU：年老代容量和使用量 PC、PU：永久代容量和使用量 YGC、YGT：年轻代GC次数和GC耗时 FGC、FGCT：Full GC次数和Full GC耗时 GCT：GC总耗时

### jstatd（JVM的jstat守护进程）

### Jmap（Java内存映射工具）

### jconsole（监视与管理）