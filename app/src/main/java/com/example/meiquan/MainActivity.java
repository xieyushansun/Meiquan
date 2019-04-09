package com.example.meiquan;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class MainActivity extends AppCompatActivity {
    // 绑定控件和变量
    @BindView(R.id.ed_phone) EditText ed_phone;
    @BindView(R.id.ed_password) EditText ed_password;

    @OnClick(R.id.btn_login) void login(){
        if (ed_phone.getText().toString().isEmpty() || ed_password.getText().toString().isEmpty()){
            showToast("请输入用户名或密码！");
        }

        OkGo.<String>post(Urls.LoginServlet)
                .params("phone", ed_phone.getText().toString())
                .params("password", ed_password.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //如果账号密码正确，就跳转页面
                        if (response.body().compareTo("1") == 0){
                            showToast("登录成功！");
                            startActivity(new Intent(MainActivity.this, TabActivity.class));
                        }
                        else if (response.body().compareTo("-1") == 0){
                            showToast("账号不存在，请先注册！");
                        }
                        else if(response.body().compareTo("0") == 0)
                        {
                            showToast("密码错误，请重新输入！");
                        }
                    }
                });
    }
    @OnClick(R.id.btn_regist) void regist(){
        startActivity(new Intent(MainActivity.this, registActivity.class));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 绑定视图
        ButterKnife.bind(this);
    }

    /**
     * 显示气泡
     * @param msg 需要显示的信息
     */
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}