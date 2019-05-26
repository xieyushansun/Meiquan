package com.example.meiquan.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.NewsAdapter;
import com.example.meiquan.adapter.RankAdapter;
import com.example.meiquan.entity.RankUser;
import com.example.meiquan.util.GlideImageLoader;
import com.google.gson.JsonObject;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nanchen.compresshelper.CompressHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import me.shaohui.bottomdialog.BottomDialog;

public class NewsFragment extends Fragment {
    BottomDialog bottomDialog;
    @BindView(R.id.lv_new) RecyclerView lv_new;
    @BindView(R.id.sw_refresh) SwipeRefreshLayout sw_refresh;
    @BindView(R.id.tabhost) TabHost tabhost;
    @BindView(R.id.lv_rank) RecyclerView lv_rank;
    @BindView(R.id.btn_rankbysport) Button btn_rankbysport;
    @BindView(R.id.btn_rankbyfood) Button btn_rankbyfood;
    private final int IMAGE_PICKER = 100;
    List<JsonObject> newList;
    List<RankUser> rankList;
    private File image = null;
    private ImageView m_img_showimage;

    private RankAdapter rankAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置状态栏无色
        /*if (Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        View view = inflater.inflate(R.layout.fragment_new, null, false);
        ButterKnife.bind(this, view);

        initTabhost();

        lv_new.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_rank.setLayoutManager(new LinearLayoutManager(getContext()));
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadnew();
            }
        });
        sw_refresh.setRefreshing(true);
        loadnew();
        getRank("sport");
        return view;
    }
    void getRank(String rankway){
        OkGo.<String>post(Urls.GetRankServlet)
                .params("phone", GlobalData.phone)
                .params("rankway", rankway)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        rankList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(RankUser.class));

                        //RankAdapter rankAdapter = new RankAdapter(R.layout.listitem_rank);
                        rankAdapter = new RankAdapter(R.layout.listitem_rank);
                        rankAdapter.bindToRecyclerView(lv_rank);

                        rankAdapter.setNewData(rankList);
                        rankAdapter.sortByFood();
                        btn_rankbysport.setTextColor(getResources().getColor(R.color.grey,null));

                    }
                });
    }
    void initTabhost(){
        tabhost.setup();

        tabhost.addTab(tabhost.newTabSpec("one").setIndicator("好友动态").setContent(R.id.tab1));
        tabhost.addTab(tabhost.newTabSpec("two").setIndicator("今日排行").setContent(R.id.tab2));
        TextView tv1 = tabhost.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tv1.setTextColor(getResources().getColor(R.color.colorPrimary,null ));
        tabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                TextView tv1 = tabhost.getTabWidget().getChildAt(tabhost.getCurrentTab()).findViewById(android.R.id.title);
                tv1.setTextColor(getResources().getColor(R.color.colorPrimary,null ));
                TextView tv2 = tabhost.getTabWidget().getChildAt(1-tabhost.getCurrentTab()).findViewById(android.R.id.title);
                tv2.setTextColor(getResources().getColor(R.color.grey,null ));

            }
        });
    }
    void loadnew(){
        OkGo.<String>post(Urls.GetNewsServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        newList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        NewsAdapter newsAdapter = new NewsAdapter(R.layout.listitem_news, getFragmentManager());
                        newsAdapter.bindToRecyclerView(lv_new);
                        newsAdapter.setNewData(newList);
                        //加载完成
                        sw_refresh.setRefreshing(false);
                    }
                });
    }
    @OnClick(R.id.img_addmynews) void addNews(){
        //KeyboardUtils.showSoftInput(getActivity());

        //showToast("添加新闻");
        bottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        TextView tv_sendnews = v.findViewById(R.id.tv_sendnews);
                        ImageView img_chooseimage = v.findViewById(R.id.img_chooseimage);
                        ImageView img_showimage = v.findViewById(R.id.img_showchoosedimage);
                        m_img_showimage = img_showimage;
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
                                if (image != null){
                                    OkGo.<String>post(Urls.AddUserNewsServlet)
                                            .params("phone", GlobalData.phone)
                                            .params("content", content)
                                            .params("flag_isImage", 1)
                                            .params("image", image)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    if (response.body().compareTo("1") == 0){
                                                        if (!(image.getPath()).isEmpty()){
                                                            image = null;
                                                        }
                                                        ToastUtils.showShort("发送成功");
                                                    }
                                                    else{
                                                        Toasty.info(getContext(), "发送失败", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                }
                                            });
                                }else{
                                    OkGo.<String>post(Urls.AddUserNewsServlet)
                                            .params("phone", GlobalData.phone)
                                            .params("content", content)
                                            .params("flag_isImage", 0)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    if (response.body().compareTo("1") == 0){

                                                        ToastUtils.showShort("发送成功");
                                                    }
                                                    else{
                                                        Toasty.info(getContext(), "发送失败", Toast.LENGTH_SHORT, true).show();
                                                    }
                                                }
                                            });
                                }

                            }
                        });
                        img_chooseimage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //启动图片选择器
                                ImagePicker imagePicker = ImagePicker.getInstance();
                                imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
                                imagePicker.setShowCamera(true);  //显示拍照按钮
                                imagePicker.setCrop(true);        //允许裁剪（单选才有效）
                                imagePicker.setSaveRectangle(true); //是否按矩形区域保存
                                imagePicker.setSelectLimit(1);    //选中数量限制
                                imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
                                imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                                imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
                                imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
                                imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

                                Intent intent = new Intent(getContext(), ImageGridActivity.class);
                                startActivityForResult(intent, IMAGE_PICKER);
                            }
                        });
                    }
                });
                bottomDialog.setLayoutRes(R.layout.news_bottom_dialog);
                bottomDialog.setDimAmount(0.1f);
                bottomDialog.show();
    }
    @OnClick(R.id.btn_rankbyfood) void rankByFood(){
        rankAdapter.sortByFood();
        btn_rankbyfood.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        btn_rankbysport.setTextColor(getResources().getColor(R.color.grey,null));

    }
    @OnClick(R.id.btn_rankbysport) void rankBySport(){
        rankAdapter.sortBySport();
        btn_rankbysport.setTextColor(getResources().getColor(R.color.colorPrimary, null));
        btn_rankbyfood.setTextColor(getResources().getColor(R.color.grey,null));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                image = CompressHelper.getDefault(getActivity()).compressToFile(new File(images.get(0).path));

                m_img_showimage.setVisibility(View.VISIBLE);
                Glide.with(this).load(images.get(0).path).into(m_img_showimage);

            } else {
                ToastUtils.showShort("没有数据");
            }
        }
    }


    /*void closedialog(){
        bottomDialog.dismiss();
    }*/



}
