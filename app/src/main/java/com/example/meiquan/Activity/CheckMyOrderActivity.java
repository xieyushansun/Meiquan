package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.CheckMyOrderAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class CheckMyOrderActivity extends AppCompatActivity {

    @BindView(R.id.lv_myorder) RecyclerView lv_myorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_checkmy_order);
        ButterKnife.bind(this);
        lv_myorder.setLayoutManager(new LinearLayoutManager(this));

        getData();

    }
    void getData(){
        OkGo.<String>post(Urls.GetMyOrderServlet)
                .params("userphone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> myOrderList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        CheckMyOrderAdapter checkMyOrderAdapter = new CheckMyOrderAdapter(R.layout.listitem_myorder);
                        checkMyOrderAdapter.bindToRecyclerView(lv_myorder);
                        checkMyOrderAdapter.setNewData(myOrderList);
                    }
                });
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
}
