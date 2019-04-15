package com.example.meiquan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meiquan.Activity.SystemSettingActivity;
import com.example.meiquan.Activity.personnalSettingActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;

public class MyInfoFragment extends Fragment implements AdapterView.OnItemClickListener{
    @BindView(R.id.iv_headImage) ImageView iv_headImage; //头像
    @BindView(R.id.tv_nickname) TextView tv_nickname; //昵称
    @BindView(R.id.tv_phone) TextView tv_phone; //电话号码

    @BindView(R.id.lv_MyInfo) ListView lv_MyInfo; //列表

    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my_info, null, false);
        ButterKnife.bind(this, view);
        tv_nickname.setText(GlobalData.nickname);
        tv_phone.setText(GlobalData.phone);
        initList();
        return view;
    }

    void initList(){
        int[]item_id = {R.id.tv_item};
        String[]item_name={"text"};
        sim_adapter = new SimpleAdapter(getActivity(), getData(), R.layout.listitem_myinfo, item_name, item_id);
        lv_MyInfo.setAdapter(sim_adapter);
        lv_MyInfo.setOnItemClickListener(this);
    }
    private List<Map<String, Object>> getData() {
        datalist.clear();
        Map<String, Object>map1 = new HashMap<String, Object>();
        map1.put("text", "个人信息");
        datalist.add(map1);

        Map<String, Object>map2 = new HashMap<String, Object>();
        map2.put("text", "系统设置");
        datalist.add(map2);

        return datalist;
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position)
        {
            case 0:
            {
                showToast(""+position);
                startActivity(new Intent(getActivity(), personnalSettingActivity.class));break;
            }
            case 1:
            {
                showToast(""+position);
                startActivity(new Intent(getActivity(), SystemSettingActivity.class));break;
            }
        }
    }

    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
