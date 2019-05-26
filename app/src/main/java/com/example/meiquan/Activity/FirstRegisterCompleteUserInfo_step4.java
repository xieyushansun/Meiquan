package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.blankj.utilcode.util.ToastUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.goodiebag.horizontalpicker.HorizontalPicker;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

public class FirstRegisterCompleteUserInfo_step4 extends AppCompatActivity {
    @BindView(R.id.hpText) HorizontalPicker hpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_first_register_complete_user_info_step4);
        ButterKnife.bind(this);

        HorizontalPicker hpText = findViewById(R.id.hpText);
        HorizontalPicker.OnSelectionChangeListener listener = new HorizontalPicker.OnSelectionChangeListener() {
            @Override
            public void onItemSelect(HorizontalPicker picker, int index) {
                HorizontalPicker.PickerItem selected = picker.getSelectedItem();
                switch (selected.getText()){
                    case "A":GlobalData.sportintensity = 1.2;break;
                    case "B":GlobalData.sportintensity = 1.3;break;
                    case "C":GlobalData.sportintensity = 1.5;break;
                    case "D":GlobalData.sportintensity = 1.7;break;
                    case "E":GlobalData.sportintensity = 1.9;break;
                    default:break;
                }
            }
        };
        List<HorizontalPicker.PickerItem> textItems = new ArrayList<>();
        textItems.add(new HorizontalPicker.TextItem("A"));
        textItems.add(new HorizontalPicker.TextItem("B"));
        textItems.add(new HorizontalPicker.TextItem("C"));
        textItems.add(new HorizontalPicker.TextItem("D"));
        textItems.add(new HorizontalPicker.TextItem("E"));
        hpText.setItems(textItems,0);
        hpText.setChangeListener(listener);

    }
    @OnClick(R.id.btn_finish) void submit(){
        OkGo.<String> post(Urls.FirstRegisterServlet)
                .params("phone", GlobalData.phone)
                .params("sex", GlobalData.sex)
                .params("birthyear", GlobalData.birthyear)
                .params("birthmonth", GlobalData.birthmonth)
                .params("birthday", GlobalData.birthday)
                .params("type", GlobalData.type)
                .params("province", GlobalData.province)
                .params("city", GlobalData.city)
                .params("height", GlobalData.height)
                .params("weight", GlobalData.weight)
                .params("sportintensity", GlobalData.sportintensity)
                .params("nickname", GlobalData.nickname)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().compareTo("1") == 0){
                            ToastUtils.showShort("信息完善成功");
                            if (GlobalData.flag_firstregisterOrresetInfo == 0){
                                startActivity(new Intent(FirstRegisterCompleteUserInfo_step4.this, TabActivity.class));
                                finish();
                            }else
                            {
                                startActivity(new Intent(FirstRegisterCompleteUserInfo_step4.this, personnalSettingActivity.class));
                                GlobalData.flag_firstregisterOrresetInfo = 0;
                                finish();
                            }
                            finish();
                        }else {
                            ToastUtils.showShort("信息完成失败");
                        }

                    }
                });

    }
}
