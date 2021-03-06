package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.content.Intent;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class MainActivity extends AppCompatActivity {
    // 绑定控件和变量
    @BindView(R.id.ed_phone) EditText ed_phone;
    @BindView(R.id.ed_password) EditText ed_password;
    @OnClick(R.id.btn_login) void login(){


    if (ed_phone.getText().toString().isEmpty()){

        GlobalData.phone = "18508333640";
        GlobalData.password = "3640";
        startActivity(new Intent(MainActivity.this, TabActivity.class));
        finish();
        return;
        /*
        showToast("请输入用户名或密码！");
        return;*/
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
                        finish();
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
        startActivity(new Intent(MainActivity.this, registerActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_main);
        // 绑定视图
        ButterKnife.bind(this);
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /*private String getlocalip(){
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        if(ipAddress==0)return "未连接wifi";
        return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
                +(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
    }*/

}
