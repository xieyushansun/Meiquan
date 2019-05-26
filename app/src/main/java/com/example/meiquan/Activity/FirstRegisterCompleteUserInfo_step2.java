package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;

public class FirstRegisterCompleteUserInfo_step2 extends AppCompatActivity {
    @BindView(R.id.ruler_picker_height) RulerValuePicker ruler_picker_height;
    @BindView(R.id.ruler_picker_weight) RulerValuePicker ruler_picker_weight;
    @BindView(R.id.tv_showheight) TextView tv_showheight;
    @BindView(R.id.tv_showweight) TextView tv_showweight;
    @BindView(R.id.ed_nickname) EditText ed_nickname;
    int height = 160;
    int weight = 50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_first_register_complete_user_info_step2);
        ButterKnife.bind(this);
        /*
        heigth picker
         */

        ruler_picker_height.setMinMaxValue(100, 230);
        ruler_picker_height.setIndicatorIntervalDistance(30);
        ruler_picker_height.setIndicatorWidth(2);
        ruler_picker_height.setTextSize(16);
        ruler_picker_height.selectValue(160 /* Initial value */);
        ruler_picker_height.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {

            }
            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                height = selectedValue;
                tv_showheight.setText(selectedValue+"cm");
            }
        });
        /*
        weight picker
         */

        ruler_picker_weight.setMinMaxValue(30, 200);
        ruler_picker_weight.setIndicatorIntervalDistance(30);
        ruler_picker_weight.setIndicatorWidth(2);
        ruler_picker_weight.setTextSize(16);
        ruler_picker_weight.selectValue(50 /* Initial value */);
        ruler_picker_weight.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {

            }
            @Override
            public void onIntermediateValueChange(final int selectedValue) {
                weight = selectedValue;
                tv_showweight.setText(selectedValue+"kg");
            }
        });
    }
    @OnClick(R.id.btn_nextstep) void nextstep(){
        GlobalData.weight = weight;
        GlobalData.height = height;
        String nickname = ed_nickname.getText().toString();
        if (nickname.isEmpty()){
            GlobalData.nickname = "MeiQuan";
        }else
        {
            GlobalData.nickname = nickname;
        }

        startActivity(new Intent(FirstRegisterCompleteUserInfo_step2.this, FirstRegisterCompleteUserInfo_step3.class));
        finish();
    }
}
