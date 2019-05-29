package top.itdl.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 指定注解的有效范围
@Target(ElementType.FIELD) // 指定注解修饰成员变量
public @interface BindView { // 声明注解
    public int value(); // 注解的值
}
