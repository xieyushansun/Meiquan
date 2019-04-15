package com.example.meiquan.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meiquan.Activity.AddInputActivity;
import com.example.meiquan.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TodayFragment extends Fragment {
    @BindView(R.id.chart_input) BarChart barCInput;
    @BindView(R.id.chart_output) BarChart barCOutput;
    @OnClick(R.id.btn_addin) void add(){
        startActivity(new Intent(getActivity(), AddInputActivity.class));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, null, false);
        ButterKnife.bind(this, view);
        showBarChart(barCInput, getBarData_in());



        return view;
    }

    private void showBarChart(final BarChart barChart, BarData barData) {

        barChart.setNoDataTextDescription("暂无数据");
        barChart.setData(barData); // 设置数据
        barChart.setDescription("");
        barChart.setDescriptionTextSize(15);
        barChart.setDrawBorders(false); //是否在折线图上添加边框
        barChart.setDescription("");
        barChart.setDescriptionColor(Color.RED);//数据的颜色
        barChart.setDescriptionTextSize(40);//数据字体大小
        barChart.setDrawBarShadow(false);//柱状图没有数据的部分是否显示阴影效果
        Legend legend = barChart.getLegend();
        legend.setTextSize(15);

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
        barChart.getXAxis().setDrawAxisLine(true);
        barChart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART);//设置比例图标的位置
        barChart.getLegend().setDirection(Legend.LegendDirection.RIGHT_TO_LEFT);//设置比例图标和文字之间的位置方向
        barChart.getLegend().setTextColor(Color.RED);
        barChart.getAxisLeft().setTextSize(15);
        barChart.getXAxis().setTextSize(15);

        //final String []str_in = {"0时", "4时", "8时", "12时", "16时", "20时", "24时",};
    }
    private BarData getBarData_in() {
        ArrayList<String> xValues = new ArrayList<String>();

        final String []str_in = {"0时", "", "","", "4时", "","", "", "8时","", "", "",
                "12时", "", "","", "16时", "","", "", "20时","", "", ""};
        for (int i = 0; i <24; i++)
        {
            xValues.add(str_in[i]);
        }
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();


        // y轴的数据集合
        BarDataSet barDataSet = new BarDataSet(yValues, "");
        barDataSet.setBarSpacePercent(80f);
        //barDataSet.setBarSpacePercent(40);
        barDataSet.setVisible(true);//是否显示柱状图柱子
        ArrayList<Integer> colors_bar = new ArrayList<Integer>();
        colors_bar.add(Color.rgb(97, 163, 201));
        barDataSet.setColors(colors_bar);
        barDataSet.setDrawValues(false);//是否显示柱子上面的数值

        ArrayList<BarDataSet> barDataSets = new ArrayList<BarDataSet>();
        barDataSets.add(barDataSet); // add the datasets

        BarData barData = new BarData(xValues, barDataSet);
        barData.setValueTextSize(15);

        return barData;
    }
}
