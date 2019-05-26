package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.goodiebag.horizontalpicker.HorizontalPicker;

import java.util.ArrayList;

import java.util.List;

public class FirstRegisterCompleteUserInfo_step1 extends AppCompatActivity {

    String sex = "女生"; //性别
    int birthyear = 1996; //出生年
    int birthmonth = 11; //出生月
    int birthday = 26; //出生日
    String type = "学生";

    @BindView(R.id.hpText) HorizontalPicker hpText;
    @BindView(R.id.wheel_birthyear) WheelView wheel_birthyear;
    @BindView(R.id.wheel_birthmonth) WheelView wheel_birthmonth;
    @BindView(R.id.wheel_birthday) WheelView wheel_birthday;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_first_register_complete_user_info_step1);
        ButterKnife.bind(this);
        /*
        sex picker
         */
        HorizontalPicker hpText = findViewById(R.id.hpText);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                sex=selected.getText();
            }
        };
        List<HorizontalPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new HorizontalPicker.TextItem("女生"));
        textItems.add(new HorizontalPicker.TextItem("男生"));
        hpText.setItems(textItems,0);
        hpText.setChangeListener(listener);
        /*
        type picker
         */
        HorizontalPicker hpText1 = findViewById(R.id.profession);
        HorizontalPicker.OnSelectionChangeListener listener1 = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                type=selected.getText();
            }
        };
        List<HorizontalPicker.PickerItem> textItems1 = new ArrayList<>();
        textItems1.add(new HorizontalPicker.TextItem("学生"));
        textItems1.add(new HorizontalPicker.TextItem("成人"));
        hpText1.setItems(textItems1,0);
        hpText1.setChangeListener(listener1);

        /*
        出生日期选择
         */
        wheel_birthyear.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                String text = view.getItem(newIndex).toString();
                birthyear = Integer.valueOf(text.replace("年", ""));
            }
        });
        wheel_birthmonth.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                String text = view.getItem(newIndex).toString();
                birthmonth = Integer.valueOf(text.replace("月", ""));
            }
        });
        wheel_birthday.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                String text = view.getItem(newIndex).toString();
                birthday = Integer.valueOf(text.replace("日", ""));
            }
        });
    }
    @OnClick (R.id.btn_nextstep) void nextStep(){
        /*Bundle bundle = new Bundle();
        bundle.putCharArray("sex", sex.toCharArray());
        bundle.putCharArray("type", type.toCharArray());
        bundle.putInt("birthyear", birthyear);
        bundle.putInt("birthmonth", birthmonth);
        bundle.putInt("birthday", birthday);

        Intent intent = new Intent(FirstRegisterCompleteUserInfo_step1.this, FirstRegisterCompleteUserInfo_step2.class);
        intent.putExtras(bundle);
        startActivity(intent);*/
        GlobalData.sex = sex;
        GlobalData.type = type;
        GlobalData.birthyear = birthyear;
        GlobalData.birthmonth = birthmonth;
        GlobalData.birthday = birthday;
        startActivity(new Intent(FirstRegisterCompleteUserInfo_step1.this, FirstRegisterCompleteUserInfo_step2.class));
        finish();
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
