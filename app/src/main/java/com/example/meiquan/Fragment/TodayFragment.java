package com.example.meiquan.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meiquan.Activity.AddFoodActivity;
import com.example.meiquan.Activity.AddSportActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodayFragment extends Fragment {
    @BindView(R.id.chart_food) BarChart chart_food;
    @BindView(R.id.chart_sport) BarChart chart_sport;
    @OnClick(R.id.btn_addsport) void addsport(){
        startActivity(new Intent(getActivity(), AddSportActivity.class));
    }
    @OnClick(R.id.btn_addfood) void addfood(){
        startActivity(new Intent(getActivity(), AddFoodActivity.class));
    }
    @OnClick(R.id.img_food_refresh) void food_refresh(){
        initTodayFoodDataFromSever();

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, null, false);
        ButterKnife.bind(this, view);
        initTodayFoodDataFromSever();


        return view;
    }


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
                    }
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        showBarChart(chart_food, getBarData_food());
                    }
                });
    }


    private void showBarChart(final BarChart barChart, BarData barData) {
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
    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
