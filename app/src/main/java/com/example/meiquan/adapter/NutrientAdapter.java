package com.example.meiquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.google.gson.JsonObject;

public class NutrientAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public NutrientAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        if (item.get("name").getAsString().compareTo("卡路里") == 0){
            return;
        }
        helper.setText(R.id.tv_nutrientname, item.get("name").getAsString());
        helper.setText(R.id.tv_nutrientnumber, item.get("number").getAsString());
    }
}
