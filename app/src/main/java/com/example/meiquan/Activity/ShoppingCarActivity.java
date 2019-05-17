package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Window;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.ShoppingCarAdapter;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

public class ShoppingCarActivity extends AppCompatActivity {
    @BindView(R.id.lv_shoppingcar) RecyclerView lv_shoppingcar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_shopping_car);
        ButterKnife.bind(this);
        lv_shoppingcar.setLayoutManager(new LinearLayoutManager(this));
        getData();
    }
    void getData(){
        OkGo.<String>post(Urls.GetShoppingCarServlet)
                .params("userphone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> shoppingcarList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        ShoppingCarAdapter shoppingCarAdapter = new ShoppingCarAdapter(R.layout.listitem_shoppingcar);
                        shoppingCarAdapter.bindToRecyclerView(lv_shoppingcar);
                        shoppingCarAdapter.setNewData(shoppingcarList);
                    }
                });
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
}
