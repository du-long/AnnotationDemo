# 自定义注解使用
参考文献：[https://www.cnblogs.com/foxy/p/7879460.html](https://www.cnblogs.com/foxy/p/7879460.html)

## 什么是注解
Annotation（注解）就是Java提供了一种源程序中的元素关联任何信息或者任何元数据（metadata）的途径和方法。

Annotation是被动的元数据，永远不会有主动行为

## 自定义注解
``` 
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@Documented
@Inherited
public @interface Bind {
    int value() default 1;
    boolean canBeNull() default false;
}
```
这就是自定义注解的形式，我们用@interface 表明这是一个注解，Annotation只有成员变量，没有方法。Annotation的成员变量在Annotation定义中以“无形参的方法”形式来声明，其方法名定义了该成员变量的名字，其返回值定义了该成员变量的类型。比如上面的value和canBeNull。

## 元注解
可以看到自定义注解里也会有注解存在，给自定义注解使用的注解就是元注解。

### @Rentention Rentention
用来标记自定义注解的有效范围，他的取值有以下三种：
* RetentionPolicy.SOURCE: 只在源代码中保留 一般都是用来增加代码的理解性或者帮助代码检查之类的，比如我们的Override。
* RetentionPolicy.CLASS: 默认的选择，能把注解保留到编译后的字节码class文件中，仅仅到字节码文件中，运行时是无法得到的。
* RetentionPolicy.RUNTIME: ，注解不仅 能保留到class字节码文件中，还能在运行通过反射获取到，这也是我们最常用的。

### @Target
指定Annotation用于修饰哪些程序元素。

@Target也包含一个名为”value“的成员变量，该value成员变量类型为ElementType[ ]，ElementType为枚举类型，值有如下几个：
* ElementType.TYPE：能修饰类、接口或枚举类型
* ElementType.FIELD：能修饰成员变量
* ElementType.METHOD：能修饰方法
* ElementType.PARAMETER：能修饰参数
* ElementType.CONSTRUCTOR：能修饰构造器
* ElementType.LOCAL_VARIABLE：能修饰局部变量
* ElementType.ANNOTATION_TYPE：能修饰注解
* ElementType.PACKAGE：能修饰包

### @Documented 
使用了该注解的可以在javadoc中找到
### @Interited 
使用了该注解的表示注解里的内容可以被子类继承，比如父类中某个成员使用了上述@From(value)，From中的value能给子类使用到。