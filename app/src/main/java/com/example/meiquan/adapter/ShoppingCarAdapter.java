package com.example.meiquan.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;

public class ShoppingCarAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public ShoppingCarAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_commodityname, item.get("commodity_name").getAsString());
        helper.setText(R.id.tv_commodityprice, item.get("price").getAsString());
        String commodity_imageurl = item.get("commodity_imageurl").getAsString();
        ImageView img_commodity = helper.getView(R.id.img_commodity);
        Glide.with(mContext).load(Urls.HOST+commodity_imageurl).into(img_commodity);
    }
}
