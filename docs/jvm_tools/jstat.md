# jstat

[官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstat.html)

```shell
# vmid:虚拟机标识符：可以是本地JVM进程ID，也可以是[protocol:][//]lvmid[@hostname[:port]/servername] 
# interval[s|ms]: 采样间隔(默认ms) 
# count:查询次数
jstat [ generalOption | outputOptions vmid [ interval[s|ms] [ count ] ]
```

常用选项: 

+ jstat -class<pid> : 显示加载class的数量，及所占空间等信息 

  ```ini
  Loaded  Bytes  Unloaded  Bytes     Time   
     763  1488.4        0     0.0       0.13
  ```

+ jstat -compiler <pid>显示VM实时编译的数量等信息 

  ```ini
  Compiled Failed Invalid   Time   FailedType FailedMethod
       189      0       0     0.09          0    
  ```

+ **jstat -gc <pid>**: 可以显示各个内存区域的容量以及 gc 的信息（gc的次数及耗时）

  比如：jstat -gc 15677 250 5

  ```ini
   S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT   
  20992.0 20992.0  0.0    0.0   128512.0 17993.2   341504.0     0.0     4480.0 782.8  384.0   76.6       0    0.000   0      0.000    0.000
  20992.0 20992.0  0.0    0.0   128512.0 17993.2   341504.0     0.0     4480.0 782.8  384.0   76.6       0    0.000   0      0.000    0.000
  20992.0 20992.0  0.0    0.0   128512.0 17993.2   341504.0     0.0     4480.0 782.8  384.0   76.6       0    0.000   0      0.000    0.000
  20992.0 20992.0  0.0    0.0   128512.0 17993.2   341504.0     0.0     4480.0 782.8  384.0   76.6       0    0.000   0      0.000    0.000
  20992.0 20992.0  0.0    0.0   128512.0 17993.2   341504.0     0.0     4480.0 782.8  384.0   76.6       0    0.000   0      0.000    0.000
  ```

  + **S0C** (Survivor Space 0 Capacity):
    第一个幸存者区（Survivor Space 0）的容量（以字节为单位）。
  + **S1C** (Survivor Space 1 Capacity):
    第二个幸存者区（Survivor Space 1）的容量（以字节为单位）。
  + **S0U** (Survivor Space 0 Usage):
    第一个幸存者区当前已使用的空间（以字节为单位）。
  + **S1U** (Survivor Space 1 Usage):
    第二个幸存者区当前已使用的空间（以字节为单位）。
  + **EC** (Eden Space Capacity):
    Eden 区的容量（以字节为单位）。
  + **EU** (Eden Space Usage):
    Eden 区当前已使用的空间（以字节为单位）。
  + **OC** (Old Generation Capacity):
    老年代（Old Generation）的容量（以字节为单位）。
  + **OU** (Old Generation Usage):
    老年代当前已使用的空间（以字节为单位）。
  + **MC** (Metaspace Capacity):
    元空间（Metaspace）的容量（以字节为单位）。
  + **MU** (Metaspace Usage):
    元空间当前已使用的空间（以字节为单位）。
  + **CCSC** (Compressed Class Space Capacity):
    压缩类空间（Compressed Class Space）的容量（以字节为单位）。
  + **CCSU** (Compressed Class Space Usage):
    压缩类空间当前已使用的空间（以字节为单位）。
  + **YGC** (Young Generation GC Count):
    年轻代垃圾收集的次数。
  + **YGCT** (Young Generation GC Time):
    年轻代垃圾收集累计花费的时间（以秒为单位）。
  + **FGC** (Full GC Count):
    完整垃圾收集（Full GC）的次数。
  + **FGCT** (Full GC Time):
    完整垃圾收集累计花费的时间（以秒为单位）。
  + **GCT** (Total Garbage Collection Time):
    垃圾收集总时间（包括年轻代和完整垃圾收集）（以秒为单位）。

+ **jstat -gccapacity <pid>**:可以显示，VM内存中三代(young，old，perm)对象的使用和占用大小 

  ```ini
  NGCMN    NGCMX     NGC     S0C   S1C       EC      OGCMN      OGCMX       OGC         OC       MCMN     MCMX      MC     CCSMN    CCSMX     CCSC    YGC    FGC 
  170496.0 2727936.0 170496.0 20992.0 20992.0 128512.0   341504.0  5455872.0   341504.0   341504.0      0.0 1056768.0   4480.0      0.0 1048576.0    384.0      0     0
  ```

  + **NGCMN** (New Generation Minimum Capacity):
  新生代（New Generation）的最小容量（以字节为单位）。
  + **NGCMX** (New Generation Maximum Capacity):
  新生代的最大容量（以字节为单位）。
  + **NGC** (New Generation Current Capacity):
  新生代当前的容量（以字节为单位）。
  + **S0C** (Survivor Space 0 Capacity):
  第一个幸存者区（Survivor Space 0）的容量（以字节为单位）。
  + **S1C** (Survivor Space 1 Capacity):
  第二个幸存者区（Survivor Space 1）的容量（以字节为单位）。
  + **EC** (Eden Space Capacity):
  Eden 区的容量（以字节为单位）。
  + **OGCMN** (Old Generation Minimum Capacity):
  老年代（Old Generation）的最小容量（以字节为单位）。
  + **OGCMX** (Old Generation Maximum Capacity):
  老年代的最大容量（以字节为单位）。
  + **OGC** (Old Generation Current Capacity):
  老年代当前的容量（以字节为单位）。
  + **OC** (Old Generation Current Capacity):
  老年代当前的容量（以字节为单位）。
  + **MCMN** (Metaspace Minimum Capacity):
  元空间（Metaspace）的最小容量（以字节为单位）。
  + **MCMX** (Metaspace Maximum Capacity):
  元空间的最大容量（以字节为单位）。
  + **MC** (Metaspace Current Capacity):
  元空间当前的容量（以字节为单位）。
  + **CCSMN** (Compressed Class Space Minimum Capacity):
  压缩类空间（Compressed Class Space）的最小容量（以字节为单位）。
  + **CCSMX** (Compressed Class Space Maximum Capacity):
  压缩类空间的最大容量（以字节为单位）。
  + **CCSC** (Compressed Class Space Current Capacity):
  压缩类空间当前的容量（以字节为单位）。
  + **YGC** (Young Generation GC Count):
  年轻代垃圾收集的次数。
  + **FGC** (Full GC Count):
  完整垃圾收集（Full GC）的次数。

+ **jstat -gcutil <pid>**:统计gc信息 

  ```ini
  S0     S1     E      O      M     CCS    YGC     YGCT    FGC    FGCT     GCT   
  0.00   0.00  14.00   0.00  17.47  19.94      0    0.000     0    0.000    0.000
  ```

  + **S0** (Survivor Space 0 Usage):
  第一个幸存者区（Survivor Space 0）的使用率（百分比）。
  + **S1** (Survivor Space 1 Usage):
  第二个幸存者区（Survivor Space 1）的使用率（百分比）。
  + **E** (Eden Space Usage):
  Eden 区的使用率（百分比）。
  + **O** (Old Generation Usage):
  老年代（Old Generation）的使用率（百分比）。
  + **M** (Metaspace Usage):
  元空间（Metaspace）的使用率（百分比）。
  + **CCS** (Compressed Class Space Usage):
  压缩类空间（Compressed Class Space）的使用率（百分比）。
  + **YGC** (Young Generation GC Count):
  年轻代垃圾收集的次数。
  + **YGCT** (Young Generation GC Time):
  年轻代垃圾收集累计花费的时间（以秒为单位）。
  + **FGC** (Full GC Count):
  完整垃圾收集（Full GC）的次数。
  + **FGCT** (Full GC Time):
  完整垃圾收集累计花费的时间（以秒为单位）。
  + **GCT** (Total Garbage Collection Time):
  垃圾收集总时间（包括年轻代和完整垃圾收集）（以秒为单位）。

+ jstat -gcnew <pid>:年轻代对象的信息 

+ jstat -gcnewcapacity<pid>: 年轻代对象的信息及其占用量 

+ jstat -gcold <pid>:old代对象的信息 

+ stat -gcoldcapacity <pid>: old代对象的信息及其占用量 

+ jstat -gcpermcapacity<pid>: perm对象的信息及其占用量 

+ jstat -printcompilation <pid>:当前VM执行的信息

