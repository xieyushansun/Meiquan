package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllMyFollows extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @BindView(R.id.lv_allmyfollows) ListView lv_allmyfollows;
    ImageView img_addnewfriend;
    List<JsonObject> AllMyFollowList;

    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_all_my_follows);
        ButterKnife.bind(this);
        img_addnewfriend = findViewById(R.id.img_addnewfriend);
        img_addnewfriend.setOnClickListener(this);
        loadallmyfollow();
    }

    void loadallmyfollow(){
        OkGo.<String>post(Urls.GetAllMyFollowsServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        AllMyFollowList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        initList();
                    }
                });
    }
    @OnClick(R.id.btn_back) void back(){
        finish();
    }


    void initList(){
        int[]item_id = {R.id.tv_nickname, R.id.tv_phone};
        String[]item_name={"nickname", "phone"};
        sim_adapter = new SimpleAdapter(AllMyFollows.this, getData(), R.layout.listitem_allmyfollows, item_name, item_id);
        lv_allmyfollows.setAdapter(sim_adapter);
        lv_allmyfollows.setOnItemClickListener(this);
    }
    private List<Map<String, Object>> getData() {
        datalist.clear();
        for (int i = 0; i < AllMyFollowList.size(); i++){
            Map<String, Object>map = new HashMap<String, Object>();
            map.put("nickname", AllMyFollowList.get(i).get("nickname").getAsString());
            map.put("phone", AllMyFollowList.get(i).get("followphone").getAsString());
            datalist.add(map);
        }
        return datalist;
    }

    @Override
    public void onClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(AllMyFollows.this);
        view = View.inflate(AllMyFollows.this, R.layout.addnewfriend_dialog, null);

        builder.setView(view);
        builder.setCancelable(true);

        EditText ed_followphone = view.findViewById(R.id.ed_followphone);
        Button btn_cancel=view.findViewById(R.id.btn_cancel); //取消按钮
        Button btn_comfirm=view.findViewById(R.id.btn_confirm); //确定按钮
        ImageView img_close = view.findViewById(R.id.img_close);  //关闭对话框按钮

        final AlertDialog dialog = builder.create();
        dialog.show();
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ed_followphone.getText().toString().isEmpty()){
                    showToast("请输入手机号");
                    return;
                }
                OkGo.<String>post(Urls.AddFriendServlet)
                        .params("phone", GlobalData.phone)
                        .params("followphone", ed_followphone.getText().toString())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.body().compareTo("-1") == 0){
                                    showToast("该用户不存在");
                                }else if (response.body().compareTo("1") == 0)
                                {
                                    showToast("关注成功");
                                }else {
                                    showToast("关注失败");
                                }
                            }

                            @Override
                            public void onFinish() {
                                super.onFinish();
                                loadallmyfollow();
                            }
                        });
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();  //关闭对话框
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //关注者列表被点击事件
    }
}
