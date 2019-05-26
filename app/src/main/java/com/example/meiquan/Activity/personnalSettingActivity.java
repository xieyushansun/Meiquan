package com.example.meiquan.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
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

public class personnalSettingActivity extends AppCompatActivity {
    @BindView(R.id.circle_headImage) CircleImageView circle_headImage;
    @BindView(R.id.tv_nickname) TextView tv_nickname;
    @BindView(R.id.tv_phone) TextView tv_phone;
    @BindView(R.id.tv_sex) TextView tv_sex;
    @BindView(R.id.tv_height) TextView tv_height;
    @BindView(R.id.tv_weight) TextView tv_weight;
    @BindView(R.id.tv_hometown) TextView tv_hometown;
    @BindView(R.id.tv_birth) TextView tv_birth;
    @BindView(R.id.tv_type) TextView tv_type;
    @BindView(R.id.tv_sportintensity) TextView tv_sportintensity;



    private final int IMAGE_PICKER = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_personnal_setting);
        ButterKnife.bind(this);

        initInfo();


    }
    void showHeadImage(){
        Glide.with(this).load(GlobalData.headimage_url).into(circle_headImage);
    }
    void initInfo(){
        OkGo.<String>post(Urls.GetUserHeadImageServlet)
                .params("phone", GlobalData.phone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //加载头像
                        if(!response.body().isEmpty()){
                            GlobalData.headimage_url = Urls.HOST + response.body();
                            showHeadImage();
                        }
                    }
                });
        tv_nickname.setText(GlobalData.nickname);
        tv_phone.setText(GlobalData.phone);
        tv_sex.setText(GlobalData.sex);
        tv_height.setText(""+GlobalData.height);
        tv_weight.setText(""+GlobalData.weight);
        tv_hometown.setText(""+GlobalData.province+GlobalData.city);
        tv_birth.setText(""+GlobalData.getAge());
        tv_type.setText(GlobalData.type);

        if (GlobalData.sportintensity == 1.2){
            tv_sportintensity.setText("长时间坐在办公室、教室，很少运动或是完全没有运动的人");
        }else if (GlobalData.sportintensity == 1.3){
            tv_sportintensity.setText("偶尔会运动或散步、逛街、到郊外踏青，每周大约少量运动1-3次的人");
        }else if (GlobalData.sportintensity == 1.5){
            tv_sportintensity.setText("有持续运动的习惯，或是会上健身房，每周大约运动3-5次的人");
        }else if (GlobalData.sportintensity == 1.7){
            tv_sportintensity.setText("热爱运动，每周运动6-7次，或是工作量相当大的人");
        }else {
            tv_sportintensity.setText("工作或生活作息需要大量劳动，相当消耗能量的人");
        }

    }
    @OnClick(R.id.btn_back) void close(){
        finish();
    }
    @OnClick(R.id.btn_changepassword) void changepassword(){
        startActivity(new Intent(personnalSettingActivity.this, ChangePasswordActivity.class));
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

        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, IMAGE_PICKER);
    }

    @OnClick(R.id.tv_resetinfo) void resetInfo(){
        GlobalData.flag_firstregisterOrresetInfo = 1; //1为重置个人信息标志
        startActivity(new Intent(personnalSettingActivity.this, FirstRegisterCompleteUserInfo_step1.class));
        finish();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);

                File headImage = CompressHelper.getDefault(this).compressToFile(new File(images.get(0).path));

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
                ToastUtils.showShort("没有数据");
            }
        }
    }
}
