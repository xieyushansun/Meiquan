package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.meiquan.Fragment.MyInfoFragment;
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

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        OkGo.<String>post(Urls.GetUserInfoServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //如果登录成功，就获取用户其它资料
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response.body(), JsonObject.class);
                        GlobalData.nickname = jsonObject.get("nickname").getAsString();
                        GlobalData.height = jsonObject.get("height").getAsInt();
                        GlobalData.weight = jsonObject.get("weight").getAsInt();
                        //showToast("phone="+GlobalData.phone+"nickname"+GlobalData.nickname+
                              //  "height="+GlobalData.height+"weight="+GlobalData.weight);
                    }
                });
        initTab();

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
        //mViewPager.setOffscreenPageLimit(4);

    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    void initTab(){
        mytab = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTitle = new ArrayList<>();
        mTitle.add("今日");
        mTitle.add("建议");
        mTitle.add("动态");
        mTitle.add("我的");

        mFragment = new ArrayList<>();
        mFragment.add(new TodayFragment());
        mFragment.add(new Fragment());
        mFragment.add(new Fragment());
        mFragment.add(new MyInfoFragment());
    }
}
