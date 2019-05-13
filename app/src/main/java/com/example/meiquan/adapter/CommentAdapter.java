package com.example.meiquan.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.google.gson.JsonObject;


public class CommentAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public CommentAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_nickname, item.get("nickname").getAsString()+"：");
        helper.setText(R.id.tv_comment, item.get("content").getAsString());

    }
}
