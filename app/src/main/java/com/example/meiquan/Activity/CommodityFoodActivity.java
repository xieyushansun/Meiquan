package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.Window;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.FoodCommodityAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class CommodityFoodActivity extends AppCompatActivity {
    @BindView(R.id.lv_foodcommodity) RecyclerView lv_foodcommodity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_commodity_food);
        ButterKnife.bind(this);
        lv_foodcommodity.setLayoutManager(new LinearLayoutManager(this));
        getFood();
    }
    void getFood(){
        OkGo.<String>post(Urls.GetCommodityServlet)
                .params("commodity_type", "commodity_food")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> commodityFoodList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        FoodCommodityAdapter foodCommodityAdapter = new FoodCommodityAdapter(R.layout.listitem_commodity);
                        foodCommodityAdapter.bindToRecyclerView(lv_foodcommodity);
                        foodCommodityAdapter.setNewData(commodityFoodList);
                    }
                });
    }
}
