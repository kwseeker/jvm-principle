package top.kwseeker.jvm.classloader.custom;

/**
 * 防破解付费软件，类加载器
 * 思路：
 * 为每个用户生成唯一密钥，代码编译之后对class文件进行密钥加密，并将密钥传给用户，然后用户运行时将密钥存入指定资源路径，自定义类加载器加载时读取密钥解密再加载。
 */
public class SecrecyClassLoader {
    //只是多了个加解密过程，比较简单，日后有空再写
}
