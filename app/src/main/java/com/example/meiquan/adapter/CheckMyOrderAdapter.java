package com.example.meiquan.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import androidx.recyclerview.widget.RecyclerView;

public class CheckMyOrderAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    public CheckMyOrderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        helper.setText(R.id.tv_commodityname, item.get("commodity_name").getAsString());
        helper.setText(R.id.tv_commodityprice, item.get("price").getAsString());
        helper.setText(R.id.tv_buynumber, "x"+item.get("buynumber").getAsString());
        helper.setText(R.id.tv_totalmoney, "共计："+item.get("totalmoney").getAsString());
        helper.setText(R.id.tv_buytime, item.get("buytime").getAsString().replace(".0", ""));
        String commodity_imageurl = item.get("commodity_imageurl").getAsString();
        ImageView img_commodity = helper.getView(R.id.img_commodity);

        Glide.with(mContext).load(Urls.HOST+commodity_imageurl).into(img_commodity);
        String isaccepted = item.get("isaccepted").getAsString();
        if (isaccepted.compareTo("1") == 0){
            Button btn_confirmaccept = helper.getView(R.id.btn_confirmaccept);
            btn_confirmaccept.setText("收货成功");
            btn_confirmaccept.setTextColor(Color.RED);
            btn_confirmaccept.setEnabled(false);
        }
        Button btn_confirmaccept = helper.getView(R.id.btn_confirmaccept);
        btn_confirmaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_buyrecord = item.get("id_buyrecord").getAsString();
                OkGo.<String>post(Urls.ConfirmAcceptServlet)
                        .params("id_buyrecord", id_buyrecord)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                btn_confirmaccept.setText("收货成功");
                                btn_confirmaccept.setEnabled(false);
                                btn_confirmaccept.setTextColor(Color.RED);
                            }
                        });
            }
        });

    }
}
