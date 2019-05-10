package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
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

public class registerActivity extends AppCompatActivity {
    @BindView(R.id.ed_phone) EditText ed_phone;
    @BindView(R.id.ed_password) EditText ed_password;
    @BindView(R.id.ed_passwordAgain) EditText ed_passwordAgain;

    @OnClick(R.id.btn_submit) void submit(){

        if (ed_phone.getText().toString().isEmpty()){
            showToast("请输入电话号码!");
            return;
        }
        if(ed_password.getText().toString().isEmpty()){
            showToast("请输入密码!");
            return;
        }
        if(ed_passwordAgain.getText().toString().isEmpty())
        {
            showToast("请再次输入密码!");
            return;
        }
        OkGo.<String>post(Urls.RegistServlet)
                .params("phone", ed_phone.getText().toString())
                .params("password", ed_password.getText().toString())
                .params("passwordAgain", ed_passwordAgain.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        if (response.body().compareTo("0") == 0){
                            showToast("请输入合法电话号码！");
                        }
                        else if (response.body().compareTo("1") == 0){
                            showToast("两次密码不一致！");
                        }
                        else if (response.body().compareTo("2") == 0){
                            showToast("该号码已经被注册！");
                        }
                        else if (response.body().compareTo("3") == 0){
                            showToast("注册成功！");
                            GlobalData.phone = ed_phone.getText().toString();
                            GlobalData.password = ed_password.getText().toString();
                            startActivity(new Intent(registerActivity.this, FirstRegisterCompleteUserInfo_step1.class));
                        }
                    }
                });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
