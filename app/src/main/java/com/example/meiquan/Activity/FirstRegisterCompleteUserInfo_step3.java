package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.entity.City;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yiguo.adressselectorlib.AddressSelector;
import com.yiguo.adressselectorlib.CityInterface;
import com.yiguo.adressselectorlib.OnItemClickListener;

import java.util.ArrayList;

public class FirstRegisterCompleteUserInfo_step3 extends AppCompatActivity {

    private ArrayList<City> provices = new ArrayList<>();
    private ArrayList<City> cities = new ArrayList<>();
    String provinceName = "";
    String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_first_register_complete_user_info_step3);
        ButterKnife.bind(this);
        String[] arrays1 = getResources().getStringArray(R.array.province);
        for(int i = 0; i < arrays1.length; i++){
            City city = new City();
            city.setName(arrays1[i]);
            provices.add(city);
        }

        AddressSelector addressSelector = findViewById(R.id.address);
        addressSelector.setTabAmount(2);
        addressSelector.setListTextNormalColor(getResources().getColor(R.color.grey, null));
        addressSelector.setListTextSelectedColor(getResources().getColor(R.color.colorPrimary, null));

        addressSelector.setCities(provices);

        addressSelector.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void itemClick(AddressSelector addressSelector, CityInterface city, int tabPosition) {
                if (tabPosition == 1) {
                    cityName = city.getCityName();
                    return;
                }

                String province = city.getCityName();
                provinceName = province;
                String[] arrays2 = arrays2= getResources().getStringArray(R.array.北京);
                switch (province){
                    case "北京": arrays2= getResources().getStringArray(R.array.北京);break;
                    case "天津": arrays2 = getResources().getStringArray(R.array.天津);break;
                    case "河北": arrays2 = getResources().getStringArray(R.array.河北);break;
                    case "山西": arrays2 = getResources().getStringArray(R.array.山西);break;
                    case "内蒙古": arrays2 = getResources().getStringArray(R.array.内蒙古);break;
                    case "辽宁": arrays2 = getResources().getStringArray(R.array.辽宁);break;
                    case "吉林": arrays2 = getResources().getStringArray(R.array.吉林);break;
                    case "黑龙江": arrays2 = getResources().getStringArray(R.array.黑龙江);break;
                    case "上海": arrays2 = getResources().getStringArray(R.array.上海);break;
                    case "江苏": arrays2 = getResources().getStringArray(R.array.江苏);break;
                    case "浙江": arrays2 = getResources().getStringArray(R.array.浙江);break;
                    case "安徽": arrays2 = getResources().getStringArray(R.array.安徽);break;
                    case "福建": arrays2 = getResources().getStringArray(R.array.福建);break;
                    case "江西": arrays2 = getResources().getStringArray(R.array.江西);break;
                    case "山东": arrays2 = getResources().getStringArray(R.array.山东);break;
                    case "河南": arrays2 = getResources().getStringArray(R.array.河南);break;
                    case "湖北": arrays2 = getResources().getStringArray(R.array.湖北);break;
                    case "湖南": arrays2 = getResources().getStringArray(R.array.湖南);break;
                    case "广东": arrays2 = getResources().getStringArray(R.array.广东);break;
                    case "广西": arrays2 = getResources().getStringArray(R.array.广西);break;
                    case "海南": arrays2 = getResources().getStringArray(R.array.海南);break;
                    case "重庆": arrays2 = getResources().getStringArray(R.array.重庆);break;
                    case "四川": arrays2 = getResources().getStringArray(R.array.四川);break;
                    case "贵州": arrays2 = getResources().getStringArray(R.array.贵州);break;
                    case "云南": arrays2 = getResources().getStringArray(R.array.云南);break;
                    case "西藏": arrays2 = getResources().getStringArray(R.array.西藏);break;
                    case "陕西": arrays2 = getResources().getStringArray(R.array.陕西);break;
                    case "甘肃": arrays2 = getResources().getStringArray(R.array.甘肃);break;
                    case "青海": arrays2 = getResources().getStringArray(R.array.青海);break;
                    case "宁夏": arrays2 = getResources().getStringArray(R.array.宁夏);break;
                    case "新疆": arrays2 = getResources().getStringArray(R.array.新疆);break;
                    case "台湾": arrays2 = getResources().getStringArray(R.array.台湾);break;
                    case "香港": arrays2 = getResources().getStringArray(R.array.香港);break;
                    case "澳门": arrays2 = getResources().getStringArray(R.array.澳门);break;
                    default:break;
                }

                cities.clear();

                for(int i = 0; i < arrays2.length; i++){
                    City city1 = new City();
                    city1.setName(arrays2[i]);
                    cities.add(city1);
                }
                addressSelector.setCities(cities);
            }
        });
        addressSelector.setOnTabSelectedListener(new AddressSelector.OnTabSelectedListener() {
            @Override
            public void onTabSelected(AddressSelector addressSelector, AddressSelector.Tab tab) {
                switch (tab.getIndex()){
                    case 0:
                        addressSelector.setCities(provices);
                        break;
                    case 1:
                        addressSelector.setCities(cities);
                        break;
                }
            }

            @Override
            public void onTabReselected(AddressSelector addressSelector, AddressSelector.Tab tab) {

            }
        });

    }
    @OnClick(R.id.btn_nextstep) void nextstep(){
        if (provinceName.isEmpty() || cityName.isEmpty()){
            return;
        }
        GlobalData.province = provinceName;
        GlobalData.city = cityName;
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
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().compareTo("1") == 0){
                            showToast("信息完善成功");
                            startActivity(new Intent(FirstRegisterCompleteUserInfo_step3.this, TabActivity.class));
                            finish();
                        }else {
                            showToast("信息完成失败");
                        }

                    }
                });
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
