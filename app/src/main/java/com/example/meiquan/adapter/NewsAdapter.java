package com.example.meiquan.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.meiquan.Activity.PhotoViewActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import me.shaohui.bottomdialog.BottomDialog;

public class NewsAdapter extends BaseQuickAdapter<JsonObject, BaseViewHolder> {
    BottomDialog bottomDialog;
    FragmentManager fragmentManager;

    public NewsAdapter(int layoutResId, @Nullable List<JsonObject> data) {
        super(layoutResId, data);
    }

    public NewsAdapter(int layoutResId, FragmentManager fragmentManager) {
        super(layoutResId);

        this.fragmentManager = fragmentManager;
    }

    @Override
    protected void convert(BaseViewHolder helper, JsonObject item) {
        //R.id.tv_content, R.id.img_showimage, R.id.tv_sendtime
        helper.setText(R.id.tv_phone, item.get("phone").getAsString());
        helper.setText(R.id.tv_content, item.get("content").getAsString());
        helper.setText(R.id.tv_sendtime, item.get("sendtime").getAsString());
        helper.setText(R.id.tv_nickname, item.get("nickname").getAsString());
        helper.setText(R.id.tv_likenumber, item.get("likenumber").getAsString());

        String news_id = item.get("id").getAsString();

        RecyclerView lv_comment = helper.getView(R.id.lv_comment);
        lv_comment.setLayoutManager(new LinearLayoutManager(mContext));

        showComment(news_id, lv_comment);

        //获取动态中图片
        String s1 = item.get("image_url").getAsString();
        if(!s1.isEmpty()){
            String image_url = Urls.HOST + item.get("image_url").getAsString();
            ImageView img_showimage = helper.getView(R.id.img_showimage);
            img_showimage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(image_url).into(img_showimage);
            img_showimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, PhotoViewActivity.class);
                    intent.putExtra("image_url", image_url);

                    ActivityUtils.startActivity(intent);
                }
            });
        }
        //获取头像
        String s2 = item.get("headimage_url").getAsString();
        if (!s2.isEmpty()){
            String headimage_url = Urls.HOST + item.get("headimage_url").getAsString();
            ImageView img_showheadimage = helper.getView(R.id.circle_headImage);
            Glide.with(mContext).load(headimage_url).into(img_showheadimage);
        }

        LikeButton likeButton = helper.getView(R.id.btn_like);
        TextView tv_likenumber = helper.getView(R.id.tv_likenumber);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                int number = Integer.valueOf(tv_likenumber.getText().toString());
                number++;
                tv_likenumber.setText(String.valueOf(number));
                clicklike("like", news_id);
            }

            @Override
            public void unLiked(LikeButton likeButton) {

                int number = Integer.valueOf(tv_likenumber.getText().toString());
                if (number>0){
                    number--;
                    tv_likenumber.setText(String.valueOf(number));
                    clicklike("unlike", news_id);
                }
            }
        });
        ImageView img_comment = helper.getView(R.id.img_comment);

        img_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCommentBox(fragmentManager, news_id);
            }
        });
    }

    void clicklike(String flag, String news_id){
        OkGo.<String>post(Urls.LikeServlet)
                .params("id", news_id)
                .params("flag", flag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                    }
                });
    }

    private void showCommentBox(FragmentManager fragmentManager, String news_id){

        bottomDialog = BottomDialog.create(fragmentManager)
                        .setViewListener(new BottomDialog.ViewListener() {
            @Override
            public void bindView(View v) {
                TextView tv_sendnews = v.findViewById(R.id.tv_sendnews);
                final EditText ed_message = v.findViewById(R.id.ed_message);
                tv_sendnews.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = ed_message.getText().toString();
                        //bottomDialog.dismiss();
                        if (content.isEmpty()){
                            ToastUtils.showShort("请输入内容");
                            return;
                        }
                        sendComment(content, news_id);
                    }
                });
            }
        });
        bottomDialog.setLayoutRes(R.layout.remark_bottom_dialog);
        bottomDialog.setDimAmount(0.1f);
        bottomDialog.show();
    }

    void showComment(String newsid, RecyclerView lv_comment){
        OkGo.<String>post(Urls.GetNewsCommentServlet)
                .params("newsid", newsid)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> commentList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        CommentAdapter commentAdapter = new CommentAdapter(R.layout.listitem_comment);
                        commentAdapter.bindToRecyclerView(lv_comment);
                        commentAdapter.setNewData(commentList);

                    }
                });
    }
    void sendComment(String content, String news_id){
        OkGo.<String>post(Urls.AddCommentServlet)
                .params("content", content)
                .params("news_id", news_id)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().compareTo("1") == 0){
                            ToastUtils.showShort("评论成功");
                        }else {
                            ToastUtils.showShort("评论失败");
                        }
                    }
                });
    }

}
