package com.example.meiquan.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meiquan.Activity.AddFoodActivity;
import com.example.meiquan.Activity.AddSportActivity;
import com.example.meiquan.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class TodayFragment extends Fragment {
    @BindView(R.id.chart_food) BarChart barCInput;
    @OnClick(R.id.btn_addsport) void addsport(){
        startActivity(new Intent(getActivity(), AddSportActivity.class));
    }
    @OnClick(R.id.btn_addfood) void addfood(){
        startActivity(new Intent(getActivity(), AddFoodActivity.class));
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, null, false);
        ButterKnife.bind(this, view);
        showBarChart(barCInput, getBarData_food());

        return view;
    }

    private void showBarChart(final BarChart barChart, BarData barData) {

        barChart.setNoDataTextDescription("暂无数据");
        barChart.setData(barData); // 设置数据
        barChart.setDescription("");
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");
        barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果

        barChart.setTouchEnabled(true); // 设置是否可以触摸
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
    }

    private BarData getBarData_food() {

        ArrayList<String> xValues = new ArrayList<String>();
        String []str_in_detail = {"早上", "上午", "中午", "下午", "晚上", "深夜"};
        for (int i = 0; i <6; i++)
        {
            xValues.add(str_in_detail[i]);
        }



        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        yValues.add(new BarEntry(1, 0));
        yValues.add(new BarEntry(2, 1));
        yValues.add(new BarEntry(3, 2));
        yValues.add(new BarEntry(4, 3));
        yValues.add(new BarEntry(5, 4));
        yValues.add(new BarEntry(6, 5));

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
}
