package top.kwseeker.jvm.sugar.enums;

/**
 * 语法糖：enum
 * Java enum 是一个语法糖，它实际是通过继承 Enum 实现
 * Enum 中包含两个字段: name, ordinal
 * name 记录枚举常量的名称，ordinal 记录枚举常量的索引，从0开始
 * 使用 CFR --sugarenums false 可以阻止CFR解析枚举语法糖
 * java -jar cfr-0.152.jar --sugarenums false Season.class
 */
public enum Season {
    SPRING,
    SUMMER,
    AUTUMN,
    WINTER;
}

// 实际的实现
// 枚举类 Enum 不允许手写类继承 Enum
// 这里可以看到枚举实际是 final 类型
//public final class Season extends Enum<Season> {
//    public static final /* enum */ Season SPRING = new Season("SPRING", 0);
//    public static final /* enum */ Season SUMMER = new Season("SUMMER", 1);
//    public static final /* enum */ Season AUTUMN = new Season("AUTUMN", 2);
//    public static final /* enum */ Season WINTER = new Season("WINTER", 3);
//    private static final /* synthetic */ Season[] $VALUES;
//
//    public static Season[] values() {
//        return (Season[])$VALUES.clone();
//    }
//
//    public static Season valueOf(String name) {
//        return Enum.valueOf(Season.class, name);
//    }
//
//    private Season(String string, int n) {
//        super(string, n);
//    }
//
//    static {
//        $VALUES = new Season[]{SPRING, SUMMER, AUTUMN, WINTER};
//    }
//}
