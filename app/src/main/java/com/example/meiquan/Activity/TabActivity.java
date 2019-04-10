package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.meiquan.Fragment.MyInfoFragment;
import com.example.meiquan.R;
import com.google.android.material.tabs.TabLayout;

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

        mytab = (TabLayout) findViewById(R.id.tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mTitle = new ArrayList<>();
        mTitle.add("今日");
        mTitle.add("建议");
        mTitle.add("动态");
        mTitle.add("我的");

        mFragment = new ArrayList<>();
        mFragment.add(new MyInfoFragment());
        mFragment.add(new Fragment());
        mFragment.add(new Fragment());
        mFragment.add(new Fragment());

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

    }

}
