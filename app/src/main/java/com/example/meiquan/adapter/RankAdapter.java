package com.example.meiquan.adapter;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.entity.RankUser;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.Comparator;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankAdapter extends BaseQuickAdapter<RankUser, BaseViewHolder> {
    //int i = 1;
    public RankAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, RankUser item) {

        //helper.getLayoutPosition();
        int ranknumber = helper.getAdapterPosition() + 1;
        helper.setText(R.id.tv_ranknumber, "" + ranknumber);
        //i++;
        helper.setText(R.id.tv_nickname, item.getNickname());
        helper.setText(R.id.tv_phone, item.getFollowphone());
        helper.setText(R.id.tv_food, "+"+item.getTotal_foodcalory());
        helper.setText(R.id.tv_sport, "- "+item.getTotal_sportcalory());

        String headimage_url = item.getHeadimage_url();
        if (!headimage_url.isEmpty()){
            CircleImageView circleImageView = helper.getView(R.id.circle_headImage);
            Glide.with(mContext).load(Urls.HOST+headimage_url).into(circleImageView);

        } else {
            CircleImageView circleImageView = helper.getView(R.id.circle_headImage);
            circleImageView.setImageResource(R.drawable.dog);
        }


    }

    public void sortBySport() {
        Collections.sort(getData(), new Comparator<RankUser>() {
            @Override
            public int compare(RankUser o1, RankUser o2) {
                return o2.getTotal_sportcalory() - o1.getTotal_sportcalory();
            }
        });
        notifyDataSetChanged();
    }
    public void sortByFood() {
        Collections.sort(getData(), new Comparator<RankUser>() {
            @Override
            public int compare(RankUser o1, RankUser o2) {
                return o2.getTotal_foodcalory() - o1.getTotal_foodcalory();
            }
        });
        notifyDataSetChanged();
    }
}
