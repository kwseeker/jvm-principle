# JStack

[官方文档](https://docs.oracle.com/javase/8/docs/technotes/tools/unix/jstack.html)

```txt
Usage:
    jstack [-l] <pid>
        (to connect to running process)
    jstack -F [-m] [-l] <pid>
        (to connect to a hung process)
    jstack [-m] [-l] <executable> <core>
        (to connect to a core file)
    jstack [-m] [-l] [server_id@]<remote server IP or hostname>
        (to connect to a remote debug server)

Options:
    -F  to force a thread dump. Use when jstack <pid> does not respond (process is hung)
    -m  to print both java and native frames (mixed mode)
    -l  long listing. Prints additional information about locks
    -h or -help to print this help message
```

这里主要分析输出信息的组成和含义，下面使用的案例源码位于 jvm-tools 模块。

> 注意：
>
> 对线程、堆进行 Dump 时（执行jstack、jmap等命令时），是想要获取线程或者堆在特定时刻的状态和信息。为了确保这些信息的准确性和一致性，JVM在进行Dump时会暂停所有线程。也需要进入安全点才行。

**一些重要或可能让人困惑的状态解析**：

+ **Waiting on xxx**

  + Waiting on condition

    正在等待某个条件或资源，通常是 Object.wait() 或 Condition.await() 中的 object 或 condition 对象。

  + Waiting on monitor entry

    正在等待获取synchronized锁。

+ **parking to wait for**

  线程挂起并在等待获取某个锁对象。

+ **Locked ownable synchronizers**

  用于显示线程当前持有的可重入锁（ReentrantLock）或其他同步器（如 Semaphore、CountDownLatch 等）的信息；这里的同步器应该是指实现了 AQS 的同步器。并不能显示获取到的 synchronzied 锁。

  通过查看持有锁信息和等待的锁信息可以定位死锁，JStack 可以自动检测出死锁信息。

+ **in Object.wait()**

  线程调用了 wait 等待某个对象监视器通知并放弃监视器锁。

**案例1 （JStack4ThreadWait）**：

线程等待状态信息 Dump。

```shell
# 执行时间
2024-11-26 13:27:02
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.333-b02 mixed mode):

# 线程名 线程序号 是否是守护线程 线程优先级 操作系统优先级 线程ID 本地线程ID 
# waiting on condition： 等待某个资源或条件发生来唤醒自己
# 线程状态与堆栈信息
# Locked ownable synchronizers：线程持有的可重入锁或其他同步器(AQS)
"Attach Listener" #13 daemon prio=9 os_prio=0 tid=0x00007f247c001000 nid=0x2795 waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"thread2" #12 prio=5 os_prio=0 tid=0x00007f24dc35f000 nid=0x2781 waiting for monitor entry [0x00007f24a91fa000]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at top.kwseeker.jvm.tools.jstack.JStack4ThreadWait.lambda$main$1(JStack4ThreadWait.java:19)
	- waiting to lock <0x0000000719916820> (a java.lang.Object)
	at top.kwseeker.jvm.tools.jstack.JStack4ThreadWait$$Lambda$2/1828972342.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)

   Locked ownable synchronizers:
	- None

"thread1" #11 prio=5 os_prio=0 tid=0x00007f24dc35d800 nid=0x2780 runnable [0x00007f24a92fa000]
   java.lang.Thread.State: RUNNABLE
	at top.kwseeker.jvm.tools.jstack.JStack4ThreadWait.lambda$main$0(JStack4ThreadWait.java:12)
	- locked <0x0000000719916820> (a java.lang.Object)
	at top.kwseeker.jvm.tools.jstack.JStack4ThreadWait$$Lambda$1/1480010240.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)

   Locked ownable synchronizers:
	- None

"Service Thread" #10 daemon prio=9 os_prio=0 tid=0x00007f24dc2cc000 nid=0x277c runnable [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"C1 CompilerThread3" #9 daemon prio=9 os_prio=0 tid=0x00007f24dc2c7000 nid=0x277b waiting on condition [0x0000000000000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

...

"main" #1 prio=5 os_prio=0 tid=0x00007f24dc00b800 nid=0x2767 in Object.wait() [0x00007f24e4bd6000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x0000000719916830> (a java.lang.Thread)
	at java.lang.Thread.join(Thread.java:1257)
	- locked <0x0000000719916830> (a java.lang.Thread)
	at java.lang.Thread.join(Thread.java:1331)
	at top.kwseeker.jvm.tools.jstack.JStack4ThreadWait.main(JStack4ThreadWait.java:25)

   Locked ownable synchronizers:
	- None

"VM Thread" os_prio=0 tid=0x00007f24dc1cb000 nid=0x2773 runnable 

"GC task thread#0 (ParallelGC)" os_prio=0 tid=0x00007f24dc022800 nid=0x276b runnable 

...

"GC task thread#7 (ParallelGC)" os_prio=0 tid=0x00007f24dc02e800 nid=0x2772 runnable 

"VM Periodic Task Thread" os_prio=0 tid=0x00007f24dc2cf000 nid=0x277d waiting on condition 

JNI global references: 316
```

**案例1 （JStack4DealLock）**：

线程死锁状态信息Dump。

```shell
2024-11-26 13:58:11
Full thread dump Java HotSpot(TM) 64-Bit Server VM (25.333-b02 mixed mode):
...

# thread2 获取锁 0x000000071991ce20 等待获取锁 0x000000071991cdf0， 这里 waiting on condition 
"thread2" #12 prio=5 os_prio=0 tid=0x00007fe4a4372800 nid=0x2e66 waiting on condition [0x00007fe45dbfc000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000071991cdf0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock.lambda$main$1(JStack4DealLock.java:34)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock$$Lambda$2/1607521710.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)

   Locked ownable synchronizers:
	- <0x000000071991ce20> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)

# thread1 获取锁 0x000000071991cdf0 等待获取锁 0x000000071991ce20
"thread1" #11 prio=5 os_prio=0 tid=0x00007fe4a4371000 nid=0x2e65 waiting on condition [0x00007fe45dcfc000]
   java.lang.Thread.State: WAITING (parking)
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000071991ce20> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock.lambda$main$0(JStack4DealLock.java:20)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock$$Lambda$1/1452126962.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)

   Locked ownable synchronizers:
	- <0x000000071991cdf0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)

...

"main" #1 prio=5 os_prio=0 tid=0x00007fe4a400b800 nid=0x2e40 in Object.wait() [0x00007fe4a95d6000]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x000000071991ce40> (a java.lang.Thread)
	at java.lang.Thread.join(Thread.java:1257)
	- locked <0x000000071991ce40> (a java.lang.Thread)
	at java.lang.Thread.join(Thread.java:1331)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock.main(JStack4DealLock.java:43)

   Locked ownable synchronizers:
	- None

...
JNI global references: 316

# JStack 可以自动检测出死锁信息
Found one Java-level deadlock:
=============================
"thread2":
  waiting for ownable synchronizer 0x000000071991cdf0, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
  which is held by "thread1"
"thread1":
  waiting for ownable synchronizer 0x000000071991ce20, (a java.util.concurrent.locks.ReentrantLock$NonfairSync),
  which is held by "thread2"

Java stack information for the threads listed above:
===================================================
"thread2":
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000071991cdf0> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock.lambda$main$1(JStack4DealLock.java:34)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock$$Lambda$2/1607521710.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)
"thread1":
	at sun.misc.Unsafe.park(Native Method)
	- parking to wait for  <0x000000071991ce20> (a java.util.concurrent.locks.ReentrantLock$NonfairSync)
	at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.parkAndCheckInterrupt(AbstractQueuedSynchronizer.java:836)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquireQueued(AbstractQueuedSynchronizer.java:870)
	at java.util.concurrent.locks.AbstractQueuedSynchronizer.acquire(AbstractQueuedSynchronizer.java:1199)
	at java.util.concurrent.locks.ReentrantLock$NonfairSync.lock(ReentrantLock.java:209)
	at java.util.concurrent.locks.ReentrantLock.lock(ReentrantLock.java:285)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock.lambda$main$0(JStack4DealLock.java:20)
	at top.kwseeker.jvm.tools.jstack.JStack4DealLock$$Lambda$1/1452126962.run(Unknown Source)
	at java.lang.Thread.run(Thread.java:750)

Found 1 deadlock.
```

