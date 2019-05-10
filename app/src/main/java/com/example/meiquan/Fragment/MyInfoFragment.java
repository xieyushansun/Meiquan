package com.example.meiquan.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.meiquan.Activity.AllMyFollows;
import com.example.meiquan.Activity.SystemSettingActivity;
import com.example.meiquan.Activity.personnalSettingActivity;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;
import com.example.meiquan.util.GlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.nanchen.compresshelper.CompressHelper;

import java.io.File;
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
import butterknife.OnItemClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MyInfoFragment extends Fragment implements AdapterView.OnItemClickListener{
    private final int IMAGE_PICKER = 100;

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
        initList();
        return view;
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
    @OnClick(R.id.tv_follow) void showfollow(){
        startActivity(new Intent(getActivity(), AllMyFollows.class));
    }
    @OnClick(R.id.circle_headImage) void changeHeadImage(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素

        Intent intent = new Intent(getContext(), ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                File headImage = CompressHelper.getDefault(getActivity()).compressToFile(new File(images.get(0).path));

                OkGo.<String>post(Urls.ChangeHeadImageServlet)
                        .params("HeadImage", headImage)
                        .params("phone", GlobalData.phone)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                if (response.body().compareTo("1") == 0){
                                    //circle_headImage
                                    Bitmap bt = BitmapFactory.decodeFile(images.get(0).path);
                                    circle_headImage.setImageBitmap(bt);
                                    circle_headImage.invalidate();
                                    ToastUtils.showShort("头像修改成功");
                                }else {
                                    ToastUtils.showShort("头像修改失败");
                                }

                            }
                        });

            } else {
                showToast("没有数据");
            }
        }
    }
    void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
