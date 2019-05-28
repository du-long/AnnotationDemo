package top.itdl.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    public static void main(String[] args) {


        try {
            Class<?> c = Class.forName("top.itdl.annotation.TestClass");
            TestClass o = (TestClass) c.newInstance();
            Field[] fields = c.getDeclaredFields();

            for (Field field : fields) {
                if (field.isAnnotationPresent(BindAddress.class)) {
                    BindAddress bindAddress = field.getAnnotation(BindAddress.class);
                    field.setAccessible(true);
                    field.set(o, bindAddress.value());
                } else if (field.isAnnotationPresent(BindPort.class)) {
                    BindPort bindPort = field.getAnnotation(BindPort.class);
                    field.setAccessible(true);
                    field.set(o, bindPort.value());
                }
            }
            o.printInfo();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
