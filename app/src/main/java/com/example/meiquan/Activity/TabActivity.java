package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import butterknife.ButterKnife;

import android.os.Bundle;
import android.view.Window;

import com.example.meiquan.Fragment.AdviceFragment;
import com.example.meiquan.Fragment.NewsFragment;
import com.example.meiquan.Fragment.MyInfoFragment;
import com.example.meiquan.Fragment.StoreFragment;
import com.example.meiquan.Fragment.TodayFragment;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import java.util.ArrayList;
import java.util.List;

public class TabActivity extends AppCompatActivity {
    TabLayout mytab;
    ViewPager mViewPager;
    List<String> mTitle;
    List<Fragment> mFragment;



    //几个tab对应的Fragment对象
    TodayFragment todayFragment = new TodayFragment();
    AdviceFragment adviceFragment = new AdviceFragment();
    NewsFragment newFragment = new NewsFragment();
    StoreFragment storeFragment = new StoreFragment();
    MyInfoFragment myInfoFragment = new MyInfoFragment();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        getUserInfo();
        //initTab();

    }

    void getUserInfo(){
        OkGo.<String>post(Urls.GetUserInfoServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //如果登录成功，就获取用户其它资料
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        GlobalData.sex = jsonObject.get("sex").getAsString();
                        GlobalData.nickname = jsonObject.get("nickname").getAsString();
                        GlobalData.height = jsonObject.get("height").getAsInt();
                        GlobalData.weight = jsonObject.get("weight").getAsInt();
                        GlobalData.headimage_url = jsonObject.get("headimage_url").getAsString();
                        GlobalData.sportintensity = jsonObject.get("sportintensity").getAsDouble();
                        GlobalData.birthyear = jsonObject.get("birthyear").getAsInt();
                        GlobalData.birthmonth = jsonObject.get("birthmonth").getAsInt();
                        GlobalData.birthday = jsonObject.get("birthday").getAsInt();
                        GlobalData.province = jsonObject.get("province").getAsString();
                        GlobalData.city = jsonObject.get("city").getAsString();
                        GlobalData.type = jsonObject.get("type").getAsString();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        initTab();
                    }
                });

    }
    void initTab(){
        mytab =findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.viewpager);

        mTitle = new ArrayList<>();
        mTitle.add("今日");
        mTitle.add("建议");
        mTitle.add("动态");
        mTitle.add("商城");
        mTitle.add("我的");

        mFragment = new ArrayList<>();
        mFragment.add(todayFragment);
        mFragment.add(adviceFragment);
        mFragment.add(newFragment);
        mFragment.add(storeFragment);
        mFragment.add(myInfoFragment);

        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        mytab.setupWithViewPager(mViewPager);
        mytab.getTabAt(0).setIcon(R.drawable.todaytab_selector);
        mytab.getTabAt(1).setIcon(R.drawable.advicetab_selector);
        mytab.getTabAt(2).setIcon(R.drawable.newtab_selector);
        mytab.getTabAt(3).setIcon(R.drawable.storetab_selector);
        mytab.getTabAt(4).setIcon(R.drawable.mytab_selector);

        //mytab.removeAllTabs();
        //mViewPager.setOffscreenPageLimit(4); //预加载tab页
        mytab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                添加选中Tab的逻辑
                //int n = tab.getPosition(); //标号从0开始
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                添加未选中Tab的逻辑
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                再次选中tab的逻辑
            }
        });

    }



}
