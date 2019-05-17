package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.NutrientAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class FoodDetailActivity extends AppCompatActivity {

    NutrientAdapter nutrientAdapter = new NutrientAdapter(R.layout.listitem_foodnutrient);
    @BindView(R.id.lv_nutrient) RecyclerView lv_nutrient;
    @BindView(R.id.tv_calorynumber) TextView tv_calorynumber;
    @BindView(R.id.tv_foodname) TextView tv_foodname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_food_detail);
        ButterKnife.bind(this);

        String foodName = getIntent().getStringExtra("foodName");
        tv_foodname.setText(foodName);
        lv_nutrient.setLayoutManager(new LinearLayoutManager(this));
        nutrientAdapter.bindToRecyclerView(lv_nutrient);

        getFoodDetail(foodName);
    }

    @OnClick(R.id.btn_back) void back(){
        finish();
    }
    void getFoodDetail(String foodname) {
        OkGo.<String>post(Urls.GetFoodNutrientServlet)
                .params("foodname", foodname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> jsonObjectList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        tv_calorynumber.setText(jsonObjectList.get(0).get("number").toString().replace("大卡", "").replace("\"", ""));
                        nutrientAdapter.setNewData(jsonObjectList);
                    }
                });
    }
}
