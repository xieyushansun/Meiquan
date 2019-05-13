package com.example.meiquan.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.example.meiquan.Activity.AddFoodActivity;
import com.example.meiquan.Activity.AddSportActivity;
import com.example.meiquan.Activity.FoodDetailActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.entity.FoodCalory;
import com.example.meiquan.util.CalculateCaloryForOneDay;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodayFragment extends Fragment {
    @BindView(R.id.chart_food) BarChart chart_food;
    @BindView(R.id.chart_sport) BarChart chart_sport;
    @BindView(R.id.tv_todaycalory) TextView tv_todaycalory;
    @BindView(R.id.tv_total_food) TextView tv_total_food;
    @BindView(R.id.tv_total_sport) TextView tv_total_sport;
    List<String> data =new ArrayList<String>();
    @BindView(R.id.act_searchfooddetail) AutoCompleteTextView act_searchfooddetail;
    @OnClick(R.id.btn_addsport) void addsport(){
        startActivityForResult(new Intent(getActivity(), AddSportActivity.class), 110);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 110) {
            initTodayFoodDataFromSever();
        }
    }

    @OnClick(R.id.btn_addfood) void addfood(){
        startActivityForResult(new Intent(getActivity(), AddFoodActivity.class), 110);
    }
    @OnClick(R.id.img_food_refresh) void food_refresh(){
        initTodayFoodDataFromSever();
    }
    @OnClick(R.id.img_sport_refresh) void sport_refresh(){
        initTodaySportDataFromSever();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, null, false);
        ButterKnife.bind(this, view);
        initTodayFoodDataFromSever();
        initTodaySportDataFromSever();
        initActInput();
        act_searchfooddetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String foodName = parent.getItemAtPosition(position).toString(); //获取点击内容
                Intent intent = new Intent(getContext(), FoodDetailActivity.class);
                intent.putExtra("foodName", foodName );
                startActivity(intent);

            }
        });
        return view;
    }
    void initTotalCalory(){
        //计算今日可摄入卡路里
        CalculateCaloryForOneDay calculateCaloryForOneDay = new CalculateCaloryForOneDay();
        double temp = calculateCaloryForOneDay.getTotalCalory();
        int totalCalory = (int)temp;
        int total_sport = GlobalData.total_sport;
        int total_food = GlobalData.total_food;
        totalCalory = totalCalory - total_food + total_sport;
        if (totalCalory < 0){
            totalCalory = 0;
        }
        tv_todaycalory.setText(String.valueOf(totalCalory));
    }

    //食物图表
    void initTodayFoodDataFromSever(){
        OkGo.<String>post(Urls.TodayFoodServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        GlobalData.total_morning_food = (int)jsonObject.get("total_morning").getAsDouble();
                        GlobalData.total_aftermorning_food = (int)jsonObject.get("total_aftermorning").getAsDouble();
                        GlobalData.total_noon_food = (int)jsonObject.get("total_noon").getAsDouble();
                        GlobalData.total_afternoon_food = (int)jsonObject.get("total_afternoon").getAsDouble();
                        GlobalData.total_night_food = (int)jsonObject.get("total_night").getAsDouble();
                        GlobalData.total_afternight_food = (int)jsonObject.get("total_afternight").getAsDouble();
                        GlobalData.total_food = GlobalData.total_morning_food
                                + GlobalData.total_aftermorning_food
                                + GlobalData.total_noon_food
                                + GlobalData.total_afternoon_food
                                + GlobalData.total_night_food
                                + GlobalData.total_afternight_food;
                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showBarChartFood(chart_food, getBarData_food());
                        tv_total_food.setText("今日已摄入："+GlobalData.total_food+"大卡");
                        initTodaySportDataFromSever();
                    }
                });
    }
    private void showBarChartFood(final BarChart barChart, BarData barData) {
        chart_food.notifyDataSetChanged();
        chart_food.invalidate();
        barChart.setNoDataTextDescription("暂无数据");
        barChart.setData(barData); // 设置数据
        barChart.setDescription("");
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");
        barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果

        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
        barChart.setDrawValueAboveBar(true);//柱状图上面的数值显示在柱子上面还是柱子里面
        barChart.getXAxis().setDrawGridLines(false);//是否显示竖直标尺线
        barChart.getXAxis().setLabelsToSkip(0);//设置横坐标显示的间隔
        barChart.getXAxis().setDrawLabels(true);//是否显示X轴数值

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置 默认在上方
        barChart.getAxisRight().setDrawLabels(false);//右侧是否显示Y轴数值
        barChart.getAxisRight().setEnabled(false);//是否显示最右侧竖线
        barChart.getAxisRight().setDrawAxisLine(true);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.setBackgroundColor(getResources().getColor(R.color.chartbackgroundClolor, null));
        barChart.setGridBackgroundColor(getResources().getColor(R.color.chartbackgroundClolor, null));

        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setTextSize(10f);
        barChart.getXAxis().setTextSize(10f);
        barChart.getAxisLeft().setTextColor(R.color.grey);
        barChart.getXAxis().setTextColor(R.color.grey);
        barChart.getAxisLeft().setGridColor(getResources().getColor(R.color.grid, null));

        barChart.animateXY(1000,1500);
    }
    private BarData getBarData_food() {

        ArrayList<String> xValues = new ArrayList<String>();
        String []str_in_detail = {"早上", "上午", "中午", "下午", "晚上", "深夜"};
        for (int i = 0; i <6; i++) {
            xValues.add(str_in_detail[i]);
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(GlobalData.total_morning_food, 0));
        yValues.add(new BarEntry(GlobalData.total_aftermorning_food, 1));
        yValues.add(new BarEntry(GlobalData.total_noon_food, 2));
        yValues.add(new BarEntry(GlobalData.total_afternoon_food, 3));
        yValues.add(new BarEntry(GlobalData.total_night_food, 4));
        yValues.add(new BarEntry(GlobalData.total_afternight_food, 5));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setValueTextColor(Color.RED);
        //barDataSet.setBarSpacePercent(40);
        barDataSet.setBarSpacePercent(60f);

        barDataSet.setVisible(true);//是否显示柱状图柱子
        ArrayList<Integer> colors_bar = new ArrayList<Integer>();
        colors_bar.add(getResources().getColor(R.color.pink, null));
        barDataSet.setColors(colors_bar);
        barDataSet.setDrawValues(false);//是否显示柱子上面的数值


        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);
        barData.setValueTextSize(15);
        return barData;
    }

    //运动图表
    void initTodaySportDataFromSever(){
        OkGo.<String>post(Urls.TodaySportServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        GlobalData.total_morning_sport = (int)jsonObject.get("total_morning").getAsDouble();
                        GlobalData.total_aftermorning_sport = (int)jsonObject.get("total_aftermorning").getAsDouble();
                        GlobalData.total_noon_sport = (int)jsonObject.get("total_noon").getAsDouble();
                        GlobalData.total_afternoon_sport = (int)jsonObject.get("total_afternoon").getAsDouble();
                        GlobalData.total_night_sport = (int)jsonObject.get("total_night").getAsDouble();
                        GlobalData.total_afternight_sport = (int)jsonObject.get("total_afternight").getAsDouble();
                        GlobalData.total_sport = GlobalData.total_morning_sport
                                + GlobalData.total_aftermorning_sport
                                + GlobalData.total_noon_sport
                                + GlobalData.total_afternoon_sport
                                + GlobalData.total_night_sport
                                + GlobalData.total_afternight_sport;
                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        tv_total_sport.setText("今日已消耗："+GlobalData.total_sport+"大卡");
                        showBarChartSport(chart_sport, getBarData_sport());
                        initTotalCalory();
                    }
                });
    }
    private void showBarChartSport(final BarChart barChart, BarData barData) {
        chart_food.notifyDataSetChanged();
        chart_food.invalidate();
        barChart.setNoDataTextDescription("暂无数据");
        barChart.setData(barData); // 设置数据
        barChart.setDescription("");
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");
        barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果

        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(false);// 是否可以拖拽
        barChart.setScaleEnabled(false);// 是否可以缩放
        barChart.setPinchZoom(false);//y轴的值是否跟随图表变换缩放;如果禁止，y轴的值会跟随图表变换缩放
        barChart.setDrawValueAboveBar(true);//柱状图上面的数值显示在柱子上面还是柱子里面
        barChart.getXAxis().setDrawGridLines(false);//是否显示竖直标尺线
        barChart.getXAxis().setLabelsToSkip(0);//设置横坐标显示的间隔
        barChart.getXAxis().setDrawLabels(true);//是否显示X轴数值

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴的位置 默认在上方
        barChart.getAxisRight().setDrawLabels(false);//右侧是否显示Y轴数值
        barChart.getAxisRight().setEnabled(false);//是否显示最右侧竖线
        barChart.getAxisRight().setDrawAxisLine(true);
        barChart.getAxisLeft().setDrawAxisLine(false);

        barChart.setBackgroundColor(getResources().getColor(R.color.chartbackgroundClolor, null));
        barChart.setGridBackgroundColor(getResources().getColor(R.color.chartbackgroundClolor, null));

        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setTextSize(10f);
        barChart.getXAxis().setTextSize(10f);
        barChart.getAxisLeft().setTextColor(R.color.grey);
        barChart.getXAxis().setTextColor(R.color.grey);
        barChart.getAxisLeft().setGridColor(getResources().getColor(R.color.grid, null));

        barChart.animateXY(1000,1500);
    }
    private BarData getBarData_sport() {

        ArrayList<String> xValues = new ArrayList<String>();
        String []str_in_detail = {"早上", "上午", "中午", "下午", "晚上", "深夜"};
        for (int i = 0; i <6; i++) {
            xValues.add(str_in_detail[i]);
        }

        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(GlobalData.total_morning_sport, 0));
        yValues.add(new BarEntry(GlobalData.total_aftermorning_sport, 1));
        yValues.add(new BarEntry(GlobalData.total_noon_sport, 2));
        yValues.add(new BarEntry(GlobalData.total_afternoon_sport, 3));
        yValues.add(new BarEntry(GlobalData.total_night_sport, 4));
        yValues.add(new BarEntry(GlobalData.total_afternight_sport, 5));

        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setValueTextColor(Color.RED);
        //barDataSet.setBarSpacePercent(40);
        barDataSet.setBarSpacePercent(60f);

        barDataSet.setVisible(true);//是否显示柱状图柱子
        ArrayList<Integer> colors_bar = new ArrayList<Integer>();
        colors_bar.add(getResources().getColor(R.color.pink, null));
        barDataSet.setColors(colors_bar);
        barDataSet.setDrawValues(false);//是否显示柱子上面的数值


        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);
        barData.setValueTextSize(15);
        return barData;
    }

    //初始化搜索框
    void initActInput(){
        act_searchfooddetail.setThreshold(0); //设置输入一个字符就开始提示
        OkGo.<String>get(Urls.FoodCaloryServlet)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Type type = new TypeToken<List<FoodCalory>>(){}.getType();
                        GlobalData.foodCaloryList = new Gson().fromJson(response.body(),type);
                        for (int i = 0; i < GlobalData.foodCaloryList.size(); i++){
                            data.add(GlobalData.foodCaloryList.get(i).getFoodname());
                        }
                    }
                });

        //data = getResources().getStringArray(R.array.inputArray);  //从arrays.xml中获取字符串

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, data);
        act_searchfooddetail.setAdapter(adapter);
    }

}
