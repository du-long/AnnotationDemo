package top.itdl.annotation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
