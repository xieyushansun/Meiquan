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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.entity.FoodCalory;
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

public class AddFoodActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @BindView(R.id.act_input) AutoCompleteTextView act_input;
    List<String> data =new ArrayList<String>();
    @BindView(R.id.lv_foodlist) ListView lv_foodlist;
    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
    String foodName;
    String foodWhen = "早上";
    int intake = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_add_food);
        ButterKnife.bind(this);
        lv_foodlist.setOnItemClickListener(this);
        initActInput();
        act_input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*TextView tv_type = view.findViewById(R.id.tv_type);
                foodName = tv_type.getText().toString();  //获取点击列表的数据*/

                foodName = parent.getItemAtPosition(position).toString(); //获取点击内容

                final AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);
                view = View.inflate(AddFoodActivity.this, R.layout.intakechoosedialog, null);

                builder.setView(view);
                builder.setCancelable(true);

                Button btn_cancel=view.findViewById(R.id.btn_cancel); //取消按钮
                Button btn_comfirm=view.findViewById(R.id.btn_confirm); //确定按钮
                ImageView img_close = view.findViewById(R.id.img_close);  //关闭对话框按钮


                WheelView wheel_when = view.findViewById(R.id.wheel_when);
                wheel_when.setOnWheelChangedListener(new OnWheelChangedListener() {
                    @Override
                    public void onChanged(WheelView view, int oldIndex, int newIndex) {
                        foodWhen = view.getItem(newIndex).toString();  //饮食的时间
                    }
                });
                //取消或确定按钮监听事件处理
                final EditText ed_intake= view.findViewById(R.id.ed_intake);//输入内容
                btn_comfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(getApplicationContext(), input_edt.getText().toString(),Toast.LENGTH_LONG).show();
                        if (ed_intake.getText().toString().isEmpty()){
                            ToastUtils.showShort("请输入食物用量");
                            return;
                        }
                        intake = Integer.valueOf(ed_intake.getText().toString());  //获取食用量
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
    }
    void initList(){
        int[]item_id = {R.id.tv_type, R.id.tv_hot};
        String[]item_name={"type", "hot"};
        sim_adapter = new SimpleAdapter(this, getData(), R.layout.listitem_addinput_food, item_name, item_id);
        lv_foodlist.setAdapter(sim_adapter);
    }
    private List<Map<String, Object>> getData() {
        /*获取数据库数据，加载到列表。*/
        datalist.clear();
        for (int i = 0; i < GlobalData.foodCaloryList.size(); i++){
            Map<String, Object>map = new HashMap<String, Object>();
            map.put("type", GlobalData.foodCaloryList.get(i).getFoodname());
            map.put("hot", GlobalData.foodCaloryList.get(i).getCalory()+"大卡/100g");
            datalist.add(map);
        }
        return datalist;
    }
    void initActInput(){
        act_input.setThreshold(0); //设置输入一个字符就开始提示
        OkGo.<String>get(Urls.FoodCaloryServlet)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Type type = new TypeToken<List<FoodCalory>>(){}.getType();
                        GlobalData.foodCaloryList = new Gson().fromJson(response.body(),type);
                        for (int i = 0; i < GlobalData.foodCaloryList.size(); i++){
                            data.add(GlobalData.foodCaloryList.get(i).getFoodname());
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
                new ArrayAdapter<String>(AddFoodActivity.this, android.R.layout.simple_dropdown_item_1line, data);
        act_input.setAdapter(adapter);
    }
    @OnClick(R.id.im_close) void clearEditText(){
        act_input.setText("");
    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        TextView tv_type = view.findViewById(R.id.tv_type);
        foodName = tv_type.getText().toString();  //获取点击列表的数据

        final AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodActivity.this);
        view = View.inflate(AddFoodActivity.this, R.layout.intakechoosedialog, null);
        builder.setView(view);
        builder.setCancelable(true);


        Button btn_cancel=view.findViewById(R.id.btn_cancel); //取消按钮
        Button btn_comfirm=view.findViewById(R.id.btn_confirm); //确定按钮
        ImageView img_close = view.findViewById(R.id.img_close);  //关闭对话框按钮


        WheelView wheel_when = view.findViewById(R.id.wheel_when);
        wheel_when.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                foodWhen = view.getItem(newIndex).toString();  //饮食的时间
            }
        });
        //取消或确定按钮监听事件处理
        final EditText ed_intake= view.findViewById(R.id.ed_intake);//输入内容
        btn_comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), input_edt.getText().toString(),Toast.LENGTH_LONG).show();
                if (ed_intake.getText().toString().isEmpty()){
                    ToastUtils.showShort("请输入食物用量");
                    return;
                }
                intake = Integer.valueOf(ed_intake.getText().toString());  //获取食用量
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

    void submitData(){
        OkGo.<String>post(Urls.FoodRecordServlet)
                .params("phone", GlobalData.phone)
                .params("foodname", foodName)
                .params("intake", intake)
                .params("foodwhen", foodWhen)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //获取返回的信息 1或者0
                        if (response.body().compareTo("1") == 0){
                            ToastUtils.showShort("您已成功添加");
                        }else{
                            ToastUtils.showShort("添加失败，请稍后重试");
                        }
                    }
                });
    }
}
