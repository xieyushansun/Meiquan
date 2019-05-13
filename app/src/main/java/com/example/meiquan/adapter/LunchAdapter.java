package com.example.meiquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.google.gson.JsonObject;

public class LunchAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public LunchAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_foodname, item.get("foodname").getAsString());
        helper.setText(R.id.tv_intake, item.get("intake").getAsString());
        helper.setText(R.id.tv_calory, item.get("calory").getAsString() + "大卡");
    }
}
