package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.TodayCaloryDetailAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class TodayCaloryDetailActivity extends AppCompatActivity {
    @BindView(R.id.lv_todaycalorydetail)
    RecyclerView lv_todaycalorydetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_today_calory_detail);
        ButterKnife.bind(this);
        lv_todaycalorydetail.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        String foodOrsport = intent.getStringExtra("foodOrsport");
        getDetail(foodOrsport);

    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
    void getDetail(String foodOrsport){
        OkGo.<String>post(Urls.GetTodayCaloryDetailServlet)
                .params("phone", GlobalData.phone)
                .params("foodOrsport", foodOrsport)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> todaycalorydetailList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        TodayCaloryDetailAdapter todayCaloryDetailAdapter = new TodayCaloryDetailAdapter(R.layout.listitem_todaycalorydetail);
                        todayCaloryDetailAdapter.bindToRecyclerView(lv_todaycalorydetail);
                        todayCaloryDetailAdapter.setNewData(todaycalorydetailList);
                    }
                });
    }
}
