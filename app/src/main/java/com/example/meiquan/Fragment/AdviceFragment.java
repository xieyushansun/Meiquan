package com.example.meiquan.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meiquan.R;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.OnClick;

public class AdviceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_advice, null, false);

        HorizontalPicker hpText = view.findViewById(R.id.hpText);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
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
        textItems.add(new HorizontalPicker.TextItem("日"));

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

        hpText.setItems(textItems,w);
        hpText.setChangeListener(listener);

        return view;
    }
}
