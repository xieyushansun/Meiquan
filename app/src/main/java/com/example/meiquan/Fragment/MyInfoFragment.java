package com.example.meiquan.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.meiquan.R;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyInfoFragment extends Fragment {
    @BindView(R.id.btn_test) Button btn_test;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        /*SimpleAdapter sim_adapter;
        List<Map<String, Object>> datalist;
        ListView listView_mainmenu;*/
        ButterKnife.bind(this, view);


        return view;
    }

    @OnClick (R.id.btn_test) void test(){
        showToast("cs");
    }
    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
