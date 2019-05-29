# 自定义注解学习
参考文献：[https://www.cnblogs.com/foxy/p/7879460.html](https://www.cnblogs.com/foxy/p/7879460.html)

## 什么是注解
Annotation（注解）就是Java提供了一种源程序中的元素关联任何信息或者任何元数据（metadata）的途径和方法。

Annotation 是被动的元数据，永远不会有主动行为

Annotation 一般会配合反射一起使用

## 自定义注解示例
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

## 注解的使用
   以下内容会用到少许反射的知识，如果不了解反射，建议先去学习反射的基本使用。
### 属性注解的使用（使用注解代替 findViewById() 方法）

#### 声明注解
```
@Retention(RetentionPolicy.RUNTIME) // 指定注解的有效范围
@Target(ElementType.FIELD) // 指定注解修饰成员变量
public @interface BindView { // 声明注解
    public int value(); // 注解的值
}
```
#### 使注解生效

```
private static void find(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();// 获取对应的 Class
        Field[] fields = aClass.getDeclaredFields(); // 获取所有成员变量
        for (Field field : fields) {
            if (field.isAnnotationPresent(BindView.class)) { // 如果是被 BindView 注解修饰
                int viewId = field.getAnnotation(BindView.class).value(); // 获取到注解修饰的View的ID
                View view = activity.findViewById(viewId); // 查找View
                field.setAccessible(true); // 暴力访问
                try {
                    field.set(activity, view); // 将View设置给对应的变量
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
```

### 方法注解的使用（使用注解代替 setOnClickListener() 方法）

#### 声明注解
```
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClick {
    public int[] value();
}
```
#### 使注解生效

```
private static void onClick(final Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(OnClick.class)) {
                method.setAccessible(true);
                OnClick annotation = method.getAnnotation(OnClick.class);
                int[] value = annotation.value();
                for (int i : value) {
                    final View view = activity.findViewById(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity, view);//按钮被点击后执行注解修饰的方法
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
```
### 在 Activity 中使用
```
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BinderView.bind(this);
    }

    @OnClick({R.id.btn_title, R.id.btn_test1, R.id.btn_test2})
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_title:
                tvTitle.setText("设置标题");
                break;
            case R.id.btn_test1:
                Toast.makeText(this, "点击测试一", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_test2:
                Toast.makeText(this, "点击测试二", Toast.LENGTH_SHORT).show();
                break;
            default:

        }
    }
}
```
### BinderView 类
```
public class BinderView {
    public static void bind(Activity activity) {
        find(activity);
        onClick(activity);
    }

    private static void find(Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();// 获取对应的 Class
        Field[] fields = aClass.getDeclaredFields(); // 获取所有成员变量
        for (Field field : fields) {
            if (field.isAnnotationPresent(BindView.class)) { // 如果是被 BindView 注解修饰
                int viewId = field.getAnnotation(BindView.class).value(); // 获取到注解修饰的View的ID
                View view = activity.findViewById(viewId); // 查找View
                field.setAccessible(true); // 暴力访问
                try {
                    field.set(activity, view); // 将View设置给对应的变量
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void onClick(final Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (final Method method : methods) {
            if (method.isAnnotationPresent(OnClick.class)) {
                method.setAccessible(true);
                OnClick annotation = method.getAnnotation(OnClick.class);
                int[] value = annotation.value();
                for (int i : value) {
                    final View view = activity.findViewById(i);
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                method.invoke(activity, view);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        }
    }
}

```
