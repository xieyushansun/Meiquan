package com.example.meiquan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.meiquan.Activity.CheckMyOrderActivity;
import com.example.meiquan.Activity.CommodityFoodActivity;
import com.example.meiquan.Activity.CommoditySportActivity;
import com.example.meiquan.Activity.ShoppingCarActivity;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.FoodCommodityAdapter;
import com.example.meiquan.adapter.tuijieCommodityAdapter;
import com.example.meiquan.util.GlideImageLoader;
import com.example.meiquan.util.GlideImageLoaderOfBanner;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreFragment extends Fragment {

    @BindView(R.id.banner_banner) Banner banner;
    @BindView(R.id.lv_tuijiecommodity) RecyclerView lv_tuijiecommodity;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store, null, false);
        ButterKnife.bind(this, view);
        //初始化顶部滚动图片
        initBanner();
        initTuijie();
        lv_tuijiecommodity.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }
    void initBanner(){
        List<Integer> images=new ArrayList<>();
        images.add(R.drawable.banner1);
        images.add(R.drawable.banner2);
        images.add(R.drawable.banner3);
        images.add(R.drawable.banner4);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoaderOfBanner());
        //设置图片集合
        banner.setImages(images);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
    }
    @OnClick(R.id.img_jinxuanshicai) void showFoodCommodity(){
        startActivity(new Intent(getActivity(), CommodityFoodActivity.class));
    }
    @OnClick(R.id.img_yundongqicai) void showSportCommodity(){
        startActivity(new Intent(getActivity(), CommoditySportActivity.class));
    }
    @OnClick(R.id.img_myorder) void showMyOrder(){
        startActivity(new Intent(getActivity(), CheckMyOrderActivity.class));
    }
    @OnClick(R.id.img_shoppingcar) void showShoppingCar(){
        startActivity(new Intent(getActivity(), ShoppingCarActivity.class));
    }
    void initTuijie(){
        OkGo.<String>post(Urls.GetCommodityServlet)
                .params("commodity_type", "allcommodity")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> commodityTuijieList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        tuijieCommodityAdapter tuijieCommodityAdapter = new tuijieCommodityAdapter(R.layout.listitem_commodity);
                        tuijieCommodityAdapter.bindToRecyclerView(lv_tuijiecommodity);
                        tuijieCommodityAdapter.setNewData(commodityTuijieList);
                    }
                });
    }
}
