package com.example.meiquan.Activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.AlertDialog;
import android.app.TimePickerDialog;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.entity.SportCalory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddSportActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.lv_sportlist) ListView lv_sportlist; //列表
    @BindView(R.id.act_input) AutoCompleteTextView act_input;

    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
    List<String> data =new ArrayList<String>();


    int SportHour = 0;
    int SportMinute = 0;
    String sportName;
    String sportWhen = "早上";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_add_sport);
        ButterKnife.bind(this);
        initActInput();
        //initList();
        act_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*TextView tv_type = (TextView) view.findViewById(R.id.tv_type);
                sportName = tv_type.getText().toString();*/

                sportName = parent.getItemAtPosition(position).toString(); //获取点击内容
                //showToast(parent.getItemAtPosition(position).toString());
                final AlertDialog.Builder builder = new AlertDialog.Builder(AddSportActivity.this);
                view = View.inflate(AddSportActivity.this, R.layout.timedurationchoosedialog, null);



                WheelView wheel_hour = view.findViewById(R.id.wheel_hour);
                WheelView wheel_minute = view.findViewById(R.id.wheel_minute);
                WheelView wheel_when = view.findViewById(R.id.wheel_when);
                wheel_hour.setOnWheelChangedListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView view, int oldIndex, int newIndex) {
                        String text = view.getItem(newIndex).toString();
                        SportHour = Integer.valueOf(text.replace("小时", ""));
                    }
                });
                wheel_minute.setOnWheelChangedListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView view, int oldIndex, int newIndex) {
                        String text = view.getItem(newIndex).toString();
                        SportMinute = Integer.valueOf(text.replace("分钟", ""));
                    }
                });
                wheel_when.setOnWheelChangedListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView view, int oldIndex, int newIndex) {
                        sportWhen = view.getItem(newIndex).toString();  //运动的时间
                    }
                });
                builder.setView(view);
                builder.setCancelable(true);
                TextView title= (TextView) view
                        .findViewById(R.id.title);//设置标题
                //final EditText input_edt= (EditText) view.findViewById(R.id.dialog_edit);//输入内容
                Button btn_cancel=view.findViewById(R.id.btn_cancel);//取消按钮
                Button btn_comfirm=view.findViewById(R.id.btn_confirm);//确定按钮
                ImageView img_close = view.findViewById(R.id.img_close); //关闭对话框按钮
                //取消或确定按钮监听事件处理
                btn_comfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (SportHour == 0 && SportMinute == 0){
                            showToast("请输入运动时长");
                            return;
                        }
                        submitData();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
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
        });
        lv_sportlist.setOnItemClickListener(this);
    }
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv_type = view.findViewById(R.id.tv_type);
        sportName = tv_type.getText().toString();
        //sportName = parent.getItemAtPosition(position).toString(); //获取点击内容
        final AlertDialog.Builder builder = new AlertDialog.Builder(AddSportActivity.this);
        view = View.inflate(AddSportActivity.this, R.layout.timedurationchoosedialog, null);

        WheelView wheel_hour = view.findViewById(R.id.wheel_hour);
        WheelView wheel_minute = view.findViewById(R.id.wheel_minute);
        WheelView wheel_when = view.findViewById(R.id.wheel_when);
        wheel_hour.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                String text = view.getItem(newIndex).toString();
                SportHour = Integer.valueOf(text.replace("小时", ""));
            }
        });
        wheel_minute.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                String text = view.getItem(newIndex).toString();
                SportMinute = Integer.valueOf(text.replace("分钟", ""));
            }
        });
        wheel_when.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                sportWhen = view.getItem(newIndex).toString();  //运动的时间
            }
        });
        builder.setView(view);
        builder.setCancelable(true);

        //final EditText input_edt= (EditText) view.findViewById(R.id.dialog_edit);//输入内容
        Button btn_cancel=(Button)view.findViewById(R.id.btn_cancel);//取消按钮
        Button btn_comfirm=(Button)view.findViewById(R.id.btn_confirm);//确定按钮
        ImageView img_close = view.findViewById(R.id.img_close); //关闭对话框按钮
        //取消或确定按钮监听事件处理
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SportHour == 0 && SportMinute == 0){
                    showToast("请输入运动时长");
                    return;
                }
                submitData();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();
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
    void initActInput(){
        act_input.setThreshold(0); //设置输入一个字符就开始提示
        OkGo.<String>get(Urls.SportCaloryServlet)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Type type = new TypeToken<List<SportCalory>>(){}.getType();

                        GlobalData.sportCaloryList = new Gson().fromJson(response.body(),type);
                        for (int i = 0; i < GlobalData.sportCaloryList.size(); i++){
                            data.add(GlobalData.sportCaloryList.get(i).getSportname());
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        initList();
                    }
                });
        //data = getResources().getStringArray(R.array.inputArray);  //从arrays.xml中获取字符串

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(AddSportActivity.this, android.R.layout.simple_dropdown_item_1line, data);
        act_input.setAdapter(adapter);
    }
    void initList(){
        int[]item_id = {R.id.tv_type, R.id.tv_hot};
        String[]item_name={"type", "hot"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.listitem_addinput_sport, item_name, item_id);
        lv_sportlist.setAdapter(sim_adapter);
    }


    private List<Map<String, Object>> getData() {
        /*
            获取数据库数据，加载到列表。
        */
        datalist.clear();
        for (int i = 0; i < GlobalData.sportCaloryList.size(); i++){
            Map<String, Object>map = new HashMap<String, Object>();
            map.put("type", GlobalData.sportCaloryList.get(i).getSportname());
            map.put("hot", GlobalData.sportCaloryList.get(i).getCalory()+"大卡/小时");
            datalist.add(map);
        }
        return datalist;
    }
    //清除编辑框中的数据
    @OnClick (R.id.im_close) void clearEditText(){
        act_input.setText("");
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
    void submitData(){
        OkGo.<String>post(Urls.SportRecordServlet)
                .params("phone", GlobalData.phone)
                .params("sportname", sportName)
                .params("sporthour", ""+SportHour)
                .params("sportminute", ""+SportMinute)
                .params("sportwhen", sportWhen)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //获取返回的信息 1或者0
                        if (response.body().compareTo("1") == 0){
                            showToast("您已成功添加");
                        }else{
                            showToast("添加失败，请稍后重试");
                        }
                    }
                });
    }
    void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

















