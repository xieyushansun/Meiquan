package com.example.meiquan.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.adapter.AddMealAdapter;
import com.example.meiquan.adapter.BreakfastAdapter;
import com.example.meiquan.adapter.CommentAdapter;
import com.example.meiquan.adapter.DinnerAdapter;
import com.example.meiquan.adapter.LunchAdapter;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdviceFragment extends Fragment {
    @BindView(R.id.lv_breakfast) RecyclerView lv_breakfast;
    @BindView(R.id.lv_lunch) RecyclerView lv_lunch;
    @BindView(R.id.lv_dinner) RecyclerView lv_dinner;
    @BindView(R.id.lv_addmeal) RecyclerView lv_addmeal;
    @BindView(R.id.tv_total_calory) TextView tv_total_calory;
    String total_caloryOfToday = "";
    int dayOfWeek = 0; //当前指向星期几
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_advice, null, false);
        ButterKnife.bind(this, view);
        //初始化RecycleView
        lv_breakfast.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_lunch.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_dinner.setLayoutManager(new LinearLayoutManager(getContext()));
        lv_addmeal.setLayoutManager(new LinearLayoutManager(getContext()));

        initHorizontalPicker(view); //初始化日期选择器

        //获取数据
        getBrekfastData();
        getLunchData();
        getDinnerData();
        getAddMealData();
        return view;
    }
    void initHorizontalPicker(View view){
        HorizontalPicker hpText = view.findViewById(R.id.hpText);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                dayOfWeek = index;
                getBrekfastData();
                getLunchData();
                getDinnerData();
                getAddMealData();

                //sex=selected.getText();
            }
        };
        List<HorizontalPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new HorizontalPicker.TextItem("日"));
        textItems.add(new HorizontalPicker.TextItem("一"));
        textItems.add(new HorizontalPicker.TextItem("二"));
        textItems.add(new HorizontalPicker.TextItem("三"));
        textItems.add(new HorizontalPicker.TextItem("四"));
        textItems.add(new HorizontalPicker.TextItem("五"));
        textItems.add(new HorizontalPicker.TextItem("六"));

        /*
        获取当前日期是星期几
         */
        Calendar cal = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        dayOfWeek = w;
        hpText.setItems(textItems,w);
        hpText.setChangeListener(listener);
    }
    void getBrekfastData(){
        OkGo.<String>post(Urls.GetWeekAdviceServlet)
                .params("totalcalory", GlobalData.total_calory)
                .params("dayofweek", dayOfWeek)
                .params("eattingtime", "早餐")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> BreakfastadviceList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        BreakfastAdapter breakfastAdapter = new BreakfastAdapter(R.layout.listitem_advice_breakfast);
                        breakfastAdapter.bindToRecyclerView(lv_breakfast);
                        breakfastAdapter.setNewData(BreakfastadviceList);
                        total_caloryOfToday = BreakfastadviceList.get(0).get("group_totalcalory").getAsString();
                        tv_total_calory.setText("总计："+total_caloryOfToday+"大卡");
                    }
                });
    }
    void getLunchData(){
        OkGo.<String>post(Urls.GetWeekAdviceServlet)
                .params("totalcalory", GlobalData.total_calory)
                .params("dayofweek", dayOfWeek)
                .params("eattingtime", "午餐")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> LunchadviceList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        LunchAdapter lunchAdapter = new LunchAdapter(R.layout.listitem_advice_lunch);
                        lunchAdapter.bindToRecyclerView(lv_lunch);
                        lunchAdapter.setNewData(LunchadviceList);
                    }
                });
    }
    void getDinnerData(){
        OkGo.<String>post(Urls.GetWeekAdviceServlet)
                .params("totalcalory", GlobalData.total_calory)
                .params("dayofweek", dayOfWeek)
                .params("eattingtime", "晚餐")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> DinneradviceList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        DinnerAdapter dinnerAdapter = new DinnerAdapter(R.layout.listitem_advice_dinner);
                        dinnerAdapter.bindToRecyclerView(lv_dinner);
                        dinnerAdapter.setNewData(DinneradviceList);
                    }
                });
    }
    void getAddMealData(){
        OkGo.<String>post(Urls.GetWeekAdviceServlet)
                .params("totalcalory", GlobalData.total_calory)
                .params("dayofweek", dayOfWeek)
                .params("eattingtime", "加餐")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        List<JsonObject> addMealadviceList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                        AddMealAdapter addmealAdapter = new AddMealAdapter(R.layout.listitem_advice_addmeal);
                        addmealAdapter.bindToRecyclerView(lv_addmeal);
                        addmealAdapter.setNewData(addMealadviceList);
                    }
                });
    }
}
