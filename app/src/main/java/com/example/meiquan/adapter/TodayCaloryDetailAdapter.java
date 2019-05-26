package com.example.meiquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.google.gson.JsonObject;

public class TodayCaloryDetailAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public TodayCaloryDetailAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_name, item.get("name").getAsString());
        helper.setText(R.id.tv_amount, item.get("amount").getAsString());
        helper.setText(R.id.tv_when, item.get("when").getAsString());
    }
}
