package com.example.meiquan.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;

public class tuijieCommodityAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public tuijieCommodityAdapter(int layoutResId) {
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

            }
        });

        //点击加入购物车
        ImageView img_addtoshoppingcar = helper.getView(R.id.img_addtoshoppingcar);
        img_addtoshoppingcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
