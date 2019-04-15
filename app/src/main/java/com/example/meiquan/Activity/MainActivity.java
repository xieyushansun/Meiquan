package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {
    // 绑定控件和变量
    @BindView(R.id.ed_phone) EditText ed_phone;
    @BindView(R.id.ed_password) EditText ed_password;
    @OnClick(R.id.btn_login) void login(){
        if (ed_phone.getText().toString().isEmpty() || ed_password.getText().toString().isEmpty()){
            /*
            此处修改了，使得不需要用户名密码也可以登录！！！！！！
            */
            GlobalData.phone = "1";
            GlobalData.password = "1";
            startActivity(new Intent(MainActivity.this, TabActivity.class));
            /*
            此处修改了，使得不需要用户名密码也可以登录！！！！！！
            */
            return;
            //showToast("请输入用户名或密码！");
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
                            GlobalData.phone = ed_phone.getText().toString();
                            GlobalData.password = ed_password.getText().toString();
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
        //startActivity(new Intent(MainActivity.this, TabActivity.class));
        //showToast(""+ NetworkUtils.getIPAddress(true));
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
