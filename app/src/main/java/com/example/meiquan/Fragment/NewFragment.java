package com.example.meiquan.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.GsonUtils;
import com.example.meiquan.Activity.AllMyFollows;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.google.gson.JsonObject;
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
import es.dmoral.toasty.Toasty;
import me.shaohui.bottomdialog.BottomDialog;

public class NewFragment extends Fragment implements AdapterView.OnItemClickListener {
    BottomDialog bottomDialog;
    @BindView(R.id.lv_new) ListView lv_new;
    SimpleAdapter sim_adapter;
    List<Map<String, Object>> datalist = new ArrayList<Map<String, Object>>();
    List<JsonObject> newList;
    @OnClick (R.id.profile_image) void test(){
        loadnew();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //设置状态栏无色
        /*if (Build.VERSION.SDK_INT >= 21){
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }*/
        View view = inflater.inflate(R.layout.fragment_follow, null, false);
        ButterKnife.bind(this, view);



        return view;
    }
    void loadnew(){
        OkGo.<String>post(Urls.GetNewsServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        newList = GsonUtils.fromJson(response.body(), GsonUtils.getListType(JsonObject.class));
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        initList();
                    }
                });
    }
    void initList(){
        int[]item_id = {R.id.tv_phone, R.id.tv_content, R.id.tv_sendtime};
        String[]item_name={"phone", "content", "sendtime"};
        sim_adapter = new SimpleAdapter(getContext(), getData(), R.layout.listitem_news, item_name, item_id);
        lv_new.setAdapter(sim_adapter);
        lv_new.setOnItemClickListener(this);
    }
    private List<Map<String, Object>> getData() {
        datalist.clear();
        for (int i = 0; i < newList.size(); i++){
            Map<String, Object>map = new HashMap<String, Object>();
            map.put("phone", newList.get(i).get("phone").getAsString());
            map.put("content", newList.get(i).get("content").getAsString());
            map.put("sendtime", newList.get(i).get("sendtime").getAsString());
            datalist.add(map);
        }
        return datalist;
    }

    @OnClick(R.id.img_addmynews) void addNews(){
        //showToast("添加新闻");
        bottomDialog = BottomDialog.create(getActivity().getSupportFragmentManager())
                .setViewListener(new BottomDialog.ViewListener() {
                    @Override
                    public void bindView(View v) {
                        TextView tv_sendnews = v.findViewById(R.id.tv_sendnews);
                        final EditText ed_message = v.findViewById(R.id.ed_message);
                        tv_sendnews.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String content = ed_message.getText().toString();
                                //bottomDialog.dismiss();
                                OkGo.<String>post(Urls.AddUserNewsServlet)
                                        .params("phone", GlobalData.phone)
                                        .params("content", content)
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onSuccess(Response<String> response) {
                                                if (response.body().compareTo("1") == 0){
                                                    showToast("发送成功");
                                                }
                                                else{
                                                    Toasty.info(getContext(), "发送失败", Toast.LENGTH_SHORT, true).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
                bottomDialog.setLayoutRes(R.layout.remark_bottom_dialog);
                bottomDialog.setDimAmount(0.1f);
                bottomDialog.show();
    }
    void showToast(String msg) {
        Toasty.success(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
    /*void closedialog(){
        bottomDialog.dismiss();
    }*/
}
