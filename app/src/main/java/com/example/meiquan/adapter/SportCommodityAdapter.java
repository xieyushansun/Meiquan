package com.example.meiquan.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.Activity.AddOrderActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

public class SportCommodityAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public SportCommodityAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_commodityname, item.get("commodity_name").getAsString());
        helper.setText(R.id.tv_commodityprice, item.get("commodity_price").getAsString());
        //获取食物图片url
        String commodity_imageurl = Urls.HOST + item.get("commodity_imageurl").getAsString();
        ImageView img_commodity = helper.getView(R.id.img_commodity);
        Glide.with(mContext).load(commodity_imageurl).into(img_commodity);

        //点击立即购买
        Button btn_buy = helper.getView(R.id.btn_buy);
        btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddOrderActivity.class);
                intent.putExtra("id_commodity", item.get("id_commodity").getAsString());
                intent.putExtra("commodity_name", item.get("commodity_name").getAsString());
                intent.putExtra("commodity_price", item.get("commodity_price").getAsString());
                intent.putExtra("commodity_imageurl", item.get("commodity_imageurl").getAsString());
                ActivityUtils.startActivity(intent);
            }
        });

        //点击加入购物车
        ImageView img_addtoshoppingcar = helper.getView(R.id.img_addtoshoppingcar);
        img_addtoshoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OkGo.<String>post(Urls.AddToShoppingCarServlet)
                        .params("userphone", GlobalData.phone)
                        .params("id_commodity", item.get("id_commodity").getAsString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if(response.body().compareTo("1") == 0){
                                    ToastUtils.showShort("添加购物车成功");
                                }else if (response.body().compareTo("-1") == 0){
                                    ToastUtils.showShort("不可以重复添加噢");
                                }else{
                                    ToastUtils.showShort("添加购物车失败");
                                }

                            }
                        });
            }
        });

    }
}
