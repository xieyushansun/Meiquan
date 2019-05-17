package com.example.meiquan.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.example.meiquan.GlobalData;
import com.example.meiquan.R;
import com.example.meiquan.Urls;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.smarttop.library.bean.City;
import com.smarttop.library.bean.County;
import com.smarttop.library.bean.Province;
import com.smarttop.library.bean.Street;
import com.smarttop.library.utils.LogUtil;
import com.smarttop.library.widget.AddressSelector;
import com.smarttop.library.widget.BottomDialog;
import com.smarttop.library.widget.OnAddressSelectedListener;

import java.text.DecimalFormat;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddOrderActivity extends AppCompatActivity implements OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener {

    @BindView(R.id.img_buy_commodity) ImageView img_buy_commodity;
    @BindView(R.id.tv_buy_commodityname) TextView tv_buy_commodityname;
    @BindView(R.id.tv_buy_commodityprice) TextView tv_buy_commodityprice;
    @BindView(R.id.tv_address) TextView tv_address;
    @BindView(R.id.tv_buynumber) TextView tv_buynumber;
    @BindView(R.id.tv_totalmoney) TextView tv_totalmoney;
    @BindView(R.id.ed_detailaddress) EditText ed_detailaddress;
    @BindView(R.id.ed_contackphone) EditText ed_contackphone;



    String provinceCode;
    String cityCode;
    String countyCode;
    String streetCode;
    int provincePosition;
    int cityPosition;
    int countyPosition;
    int streetPosition;
    BottomDialog dialog;

    String commodity_name;
    String commodity_price;
    String commodity_imageurl;
    String id_commodity;
    double d_price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        commodity_name = intent.getStringExtra("commodity_name");
        commodity_price = intent.getStringExtra("commodity_price");
        commodity_imageurl = intent.getStringExtra("commodity_imageurl");
        id_commodity = intent.getStringExtra("id_commodity");

        tv_buy_commodityname.setText(commodity_name);
        tv_buy_commodityprice.setText(commodity_price);
        d_price = Double.valueOf(commodity_price);
        tv_totalmoney.setText("应付总金额："+commodity_price+" 元");
        Glide.with(this).load(Urls.HOST+commodity_imageurl).into(img_buy_commodity);

        initDialog();

    }

    void initDialog(){
        dialog = new BottomDialog(this);
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.setSelectorAreaPositionListener(this);
    }

    @OnClick(R.id.btn_back) void back(){
        finish();
    }

    @OnClick(R.id.tv_chooseaddress) void chooseAddress(){
        dialog.show();
    }
    @OnClick(R.id.btn_buy) void buy(){
        String address = tv_address.getText().toString();
        String detailaddress = ed_detailaddress.getText().toString();
        String acceptaddress = address+detailaddress;

        String totalmoney = tv_totalmoney.getText().toString();
        totalmoney = totalmoney.replace("应付总金额：", "").replace(" 元","" );
        String contackphone = ed_contackphone.getText().toString();

        if (address.compareTo("请选择收货地") == 0){
            ToastUtils.showShort("请选择收货地");
            return;
        }
        if (detailaddress.isEmpty()){
            ToastUtils.showShort("请输入详细地址");
            return;
        }
        if (contackphone.isEmpty()){
            ToastUtils.showShort("请输入联系电话");
            return;
        }

        OkGo.<String>post(Urls.AddBuyRecordServlet)
                .params("userphone", GlobalData.phone)
                .params("commodityid", id_commodity)
                .params("acceptaddress", acceptaddress)
                .params("buynumber", getCurrentNumber())
                .params("totalmoney", totalmoney)
                .params("contackphone", contackphone)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        if (response.body().compareTo("1") == 0){
                            ToastUtils.showShort("购买成功！");
                        }else {
                            ToastUtils.showShort("购买失败！");
                        }

                    }
                });
    }

    int getCurrentNumber(){
        int n = Integer.valueOf(tv_buynumber.getText().toString());
        return n;
    }
    @OnClick(R.id.img_more) void morenumber(){
        tv_buynumber.setText(String.valueOf(getCurrentNumber()+1));
        double total = d_price*getCurrentNumber();
        DecimalFormat df = new DecimalFormat("#.00");
        tv_totalmoney.setText("应付总金额："+df.format(total)+" 元");
    }
    @OnClick(R.id.img_less) void lessnumber(){
        if (getCurrentNumber() <= 1){
            ToastUtils.showShort("不能再减少了哟！");
        }else {
            tv_buynumber.setText(String.valueOf(getCurrentNumber()-1));
            double total = d_price*getCurrentNumber();
            DecimalFormat df = new DecimalFormat("#.00");
            tv_totalmoney.setText("应付总金额："+df.format(total)+" 元");
        }
    }

    @Override
    public void onAddressSelected(Province province, City city, County county, Street street) {
        provinceCode = (province == null ? "" : province.code);
        cityCode = (city == null ? "" : city.code);
        countyCode = (county == null ? "" : county.code);
        streetCode = (street == null ? "" : street.code);
        LogUtil.d("数据", "省份id=" + provinceCode);
        LogUtil.d("数据", "城市id=" + cityCode);
        LogUtil.d("数据", "乡镇id=" + countyCode);
        LogUtil.d("数据", "街道id=" + streetCode);
        String s = (province == null ? "" : province.name) + (city == null ? "" : city.name) + (county == null ? "" : county.name) +
                (street == null ? "" : street.name);
        tv_address.setText(s);
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void dialogclose() {
        if(dialog!=null){
            dialog.dismiss();
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        this.provincePosition = provincePosition;
        this.cityPosition = cityPosition;
        this.countyPosition = countyPosition;
        this.streetPosition = streetPosition;
        LogUtil.d("数据", "省份位置=" + provincePosition);
        LogUtil.d("数据", "城市位置=" + cityPosition);
        LogUtil.d("数据", "乡镇位置=" + countyPosition);
        LogUtil.d("数据", "街道位置=" + streetPosition);
    }
}
