package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
                        shoppingCarAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                                //adapter.remove(position);
                                TextView tv_id_commodity = view.findViewById(R.id.tv_id_commodity);
                                String id_commodity = tv_id_commodity.getText().toString();
                                showPopupMenu(view, adapter, position, id_commodity);
                                shoppingCarAdapter.notifyDataSetChanged();
                                return false;
                            }
                        });
                    }
                });
    }
    private void showPopupMenu(View view, BaseQuickAdapter adapter, int deletePosition, String id_commodity) {
        // 这里的view代表popupMenu需要依附的view
        PopupMenu popupMenu = new PopupMenu(ShoppingCarActivity.this, view);
        // 获取布局文件
        popupMenu.getMenuInflater().inflate(R.menu.mymenu, popupMenu.getMenu());
        popupMenu.show();
        // 通过上面这几行代码，就可以把控件显示出来了
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                adapter.remove(deletePosition);
                OkGo.<String>post(Urls.DeleteShoppingCarServlet)
                        .params("phone", GlobalData.phone)
                        .params("id_commodity", id_commodity)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.body().compareTo("1") == 0){
                                    ToastUtils.showShort("已经将商品从购物车中移出");
                                }
                            }
                        });
                return false;
            }

        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // 控件消失时的事件
            }
        });
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
}
