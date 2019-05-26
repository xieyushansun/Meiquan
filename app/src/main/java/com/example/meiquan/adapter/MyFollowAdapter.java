package com.example.meiquan.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFollowAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public MyFollowAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_nickname, item.get("nickname").getAsString());
        helper.setText(R.id.tv_phone, item.get("followphone").getAsString());

        String headimage_url = item.get("headimage_url").getAsString();
        if (!headimage_url.isEmpty()) {
            CircleImageView circleImageView = helper.getView(R.id.circle_headImage);
            Glide.with(mContext).load(Urls.HOST+headimage_url).into(circleImageView);
        }
    }
}
