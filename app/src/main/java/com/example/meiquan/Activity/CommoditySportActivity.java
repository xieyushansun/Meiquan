package com.example.meiquan.Activity;

import android.os.Bundle;
import android.view.Window;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.SportCommodityAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CommoditySportActivity extends AppCompatActivity {
    @BindView(R.id.lv_sportcommodity) RecyclerView lv_sportcommodity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_commodity_sport);
        ButterKnife.bind(this);
        lv_sportcommodity.setLayoutManager(new LinearLayoutManager(this));
        getSport();
    }
    void getSport(){
        OkGo.<String>post(Urls.GetCommodityServlet)
                .params("commodity_type", "commodity_sport")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> commoditySportList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        SportCommodityAdapter sportCommodityAdapter = new SportCommodityAdapter(R.layout.listitem_commodity);
                        sportCommodityAdapter.bindToRecyclerView(lv_sportcommodity);
                        sportCommodityAdapter.setNewData(commoditySportList);
                    }
                });
    }
}
