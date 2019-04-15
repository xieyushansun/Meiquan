package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddInputActivity extends AppCompatActivity {
    @BindView(R.id.lv_detail) ListView lv_detail; //列表
    @BindView(R.id.act_input) AutoCompleteTextView act_input;

    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
    private static String[] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_input);
        ButterKnife.bind(this);
        initActInput();
        initList();
        act_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //parent.getItemAtPosition(position).toString(); //获取点击内容

            }
        });
    }

    void initActInput(){
        act_input.setThreshold(0); //设置输入一个字符就开始提示

        data = getResources().getStringArray(R.array.inputArray);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(AddInputActivity.this, android.R.layout.simple_dropdown_item_1line, data);
        act_input.setAdapter(adapter);
    }
    void initList(){
        int[]item_id = {R.id.tv_type, R.id.tv_hot};
        String[]item_name={"type", "hot"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.listitem_addinput, item_name, item_id);
        lv_detail.setAdapter(sim_adapter);

    }
    private List<Map<String, Object>> getData() {
        /*
            获取数据库数据，加载到列表。
        */

        datalist.clear();
        Map<String, Object>map1 = new HashMap<String, Object>();
        map1.put("type", "骑车");
        map1.put("hot", "2小时*110大卡/小时");
        datalist.add(map1);

        Map<String, Object>map2 = new HashMap<String, Object>();
        map2.put("type", "逛街");
        map2.put("hot", "0.5小时*190大卡/小时");
        datalist.add(map2);

        return datalist;
    }
    //确认添加该项活动
    @OnClick (R.id.btn_submit) void submitAdd(){
        /*OkGo.<String>post(Urls.LoginServlet)
                .params("activename", )
                .params("type", )*/
        //showToast(act_input.getText().toString());
    }

    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
