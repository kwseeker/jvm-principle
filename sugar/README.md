# Java各种语法糖实现原理分析

```shell
# CFR 参数选项
java -jar tools/cfr-0.152.jar --help
```
也可以使用 JAD 反编译，不需要像 CFR 那样设置参数。

+ enum
+ switch 支持 String 与枚举

  参数： `--decodeenumswitch` 和 `--decodestringswitch`

+ 泛型
+ 自动装箱和拆箱
+ 方法变长参数
+ 内部类
+ 条件编译
+ 断言
+ 数值字面量
+ for-each
  
  参数： `--arrayiter` 和 `--collectioniter`

+ Lambda表达式

  参数： `--decodelambdas`