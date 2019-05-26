package com.example.meiquan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meiquan.Activity.AllMyFollowsActivity;
import com.example.meiquan.Activity.MainActivity;
import com.example.meiquan.Activity.personnalSettingActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoFragment extends Fragment implements AdapterView.OnItemClickListener{


    @BindView(R.id.circle_headImage) CircleImageView circle_headImage; //头像
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
        getHeadImage();

        initList();
        return view;
    }
    void getHeadImage(){
        OkGo.<String>post(Urls.GetUserHeadImageServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                         //加载头像
                        if(!response.body().isEmpty()){
                            GlobalData.headimage_url = Urls.HOST + response.body();
                            showHeadImage(GlobalData.headimage_url);
                        }
                    }
                });
    }
    private void showHeadImage(String url) {
        Glide.with(getContext()).load(url).into(circle_headImage);
    }

    void initList(){
        int[]item_id = {R.id.img_item, R.id.tv_item};
        String[]item_name={"img", "text"};
        sim_adapter = new SimpleAdapter(getActivity(), getData(), R.layout.listitem_myinfo, item_name, item_id);
        lv_MyInfo.setAdapter(sim_adapter);
        lv_MyInfo.setOnItemClickListener(this);
    }
    private List<Map<String, Object>> getData() {
        datalist.clear();
        Map<String, Object>map1 = new HashMap<String, Object>();
        map1.put("text", "个人信息");
        map1.put("img", R.drawable.myinfo);
        datalist.add(map1);

        Map<String, Object>map2 = new HashMap<String, Object>();
        map2.put("text", "系统设置");
        map2.put("img", R.drawable.systemsetting);
        datalist.add(map2);

        Map<String, Object>map3 = new HashMap<String, Object>();
        map3.put("text", "关于我们");
        map3.put("img", R.drawable.aboutus);
        datalist.add(map3);

        return datalist;
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0: startActivityForResult(new Intent(getActivity(), personnalSettingActivity.class), 120);break;
        }
    }
    @OnClick(R.id.tv_follow) void showfollow(){
        startActivity(new Intent(getActivity(), AllMyFollowsActivity.class));
    }
    @OnClick(R.id.btn_logout) void logout(){
        startActivity(new Intent(getActivity(), MainActivity.class
        ));
    }
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120) {
            getHeadImage();
        }
    }
}
