package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.MyFollowAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class AllMyFollowsActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.lv_allmyfollows) RecyclerView lv_allmyfollows;
    ImageView img_addnewfriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_all_my_follows);
        ButterKnife.bind(this);
        img_addnewfriend = findViewById(R.id.img_addnewfriend);
        img_addnewfriend.setOnClickListener(this);
        lv_allmyfollows.setLayoutManager(new LinearLayoutManager(this));
        loadallmyfollow();
    }

    void loadallmyfollow(){
        OkGo.<String>post(Urls.GetAllMyFollowsServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> AllMyFollowList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        MyFollowAdapter myFollowAdapter = new MyFollowAdapter(R.layout.listitem_allmyfollows);
                        myFollowAdapter.bindToRecyclerView(lv_allmyfollows);
                        myFollowAdapter.setNewData(AllMyFollowList);
                    }
                });
    }
    @OnClick(R.id.btn_back) void back(){
        finish();
    }


    @Override
    public void onClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AllMyFollowsActivity.this);
        view = View.inflate(AllMyFollowsActivity.this, R.layout.addnewfriend_dialog, null);

        builder.setView(view);
        builder.setCancelable(true);

        EditText ed_followphone = view.findViewById(R.id.ed_followphone);
        Button btn_cancel=view.findViewById(R.id.btn_cancel); //取消按钮
        Button btn_comfirm=view.findViewById(R.id.btn_confirm); //确定按钮
        ImageView img_close = view.findViewById(R.id.img_close);  //关闭对话框按钮

        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_followphone.getText().toString().isEmpty()){
                    ToastUtils.showShort("请输入手机号");
                    return;
                }
                OkGo.<String>post(Urls.AddFriendServlet)
                        .params("phone", GlobalData.phone)
                        .params("followphone", ed_followphone.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.body().compareTo("1") == 0){
                                    ToastUtils.showShort("关注成功");
                                }else if (response.body().compareTo("-1") == 0) {
                                    ToastUtils.showShort("不能关注自己噢");
                                }else if(response.body().compareTo("-2") == 0){
                                    ToastUtils.showShort("不能重复关注该用户噢");
                                }else if (response.body().compareTo("-3") == 0){
                                    ToastUtils.showShort("没有该用户");
                                }else{
                                    ToastUtils.showShort("关注失败");
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                loadallmyfollow();
                            }
                        });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  //关闭对话框
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
}
