# jmap

[官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jmap.html)

这里主要分析dump的信息的组成和含义，下面使用的案例源码位于 jvm-tools 模块。

`jmap -histo <PID>`:

```ini
 #编号 	实例数量		 占用字节数量	类名("["开头的是数组)
 num     #instances         #bytes  class name
----------------------------------------------
   1:          1512       14145920  [I					# int 数组
   2:          2637        2077144  [B					# byte 数组
   3:         11271         990768  [C					# char 数组
   4:          8076         193824  java.lang.String
   5:          2551         142856  jdk.internal.org.objectweb.asm.Item
   6:           865          99048  java.lang.Class
   7:          2001          88976  [Ljava.lang.Object;	# java.lang.Object类型数组
   8:           148          83840  [Ljdk.internal.org.objectweb.asm.Item;
   9:          1322          40384  [Ljava.lang.Class;
...
```

`jmap -heap <PID>`:

配置参数：

+ MinHeapFreeRatio MaxHeapFreeRatio

  用于动态调整堆内存的大小。

```ini
Attaching to process ID 15677, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.333-b02

using thread-local object allocation.
Parallel GC with 8 thread(s)

# 堆内存初始化配置
Heap Configuration:
   MinHeapFreeRatio         = 0			# 设置的JVM堆最小空闲比率（默认40%），对应 -XX:MinHeapFreeRatio
   MaxHeapFreeRatio         = 100     	# 设置的JVM堆最大空闲比率（默认70%），对应 -XX:MaxHeapFreeRatio
   MaxHeapSize              = 8380219392 (7992.0MB)	# JVM堆最大空间
   NewSize                  = 174587904 (166.5MB)	# 新生代默认空间
   MaxNewSize               = 2793406464 (2664.0MB)	# 新生代最大空间
   OldSize                  = 349700096 (333.5MB)	# 老年代默认空间
   NewRatio                 = 2						# 老年代：新生代 空间比率
   SurvivorRatio            = 8						# Eden: Suvivor 空间比率
   MetaspaceSize            = 21807104 (20.796875MB)	# 元空间大小
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 131596288 (125.5MB)
   used     = 18425040 (17.571487426757812MB)
   free     = 113171248 (107.92851257324219MB)
   14.001185200603835% used
From Space:
   capacity = 21495808 (20.5MB)
   used     = 0 (0.0MB)
   free     = 21495808 (20.5MB)
   0.0% used
To Space:
   capacity = 21495808 (20.5MB)
   used     = 0 (0.0MB)
   free     = 21495808 (20.5MB)
   0.0% used
PS Old Generation
   capacity = 349700096 (333.5MB)
   used     = 0 (0.0MB)
   free     = 349700096 (333.5MB)
   0.0% used

2671 interned Strings occupying 190632 bytes.
```

`jmap -dump:format=b,file=heapDump 6900`：

以二进制格式输出到 heapDump 文件。

