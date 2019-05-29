package top.itdl.annotation;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
