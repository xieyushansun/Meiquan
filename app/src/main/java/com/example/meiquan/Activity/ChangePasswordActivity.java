package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    @BindView(R.id.ed_password) EditText ed_password;
    @BindView(R.id.ed_againpassword) EditText ed_againpassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);

    }
    void changePassword(String password){
        OkGo.<String>post(Urls.ChangePasswordServlet)
                .params("phone", GlobalData.phone)
                .params("newpassword", password)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if(response.body().compareTo("1") == 0){
                            ToastUtils.showShort("密码修改成功");
                        }else{
                            ToastUtils.showShort("密码修改失败");
                        }
                    }
                });
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
    @OnClick(R.id.btn_confirtmodify) void confirtmodify(){
        String password = ed_password.getText().toString();
        String againpassword = ed_againpassword.getText().toString();
        if (password.isEmpty()){
            ToastUtils.showShort("请输入新密码");
            return;
        }else if (againpassword.isEmpty()){
            ToastUtils.showShort("请再次输入密码");
            return;
        }

        if (password.compareTo(againpassword) != 0){
            ToastUtils.showShort("两次密码输入不一致");
            return;
        }

        changePassword(password);

    }
}
