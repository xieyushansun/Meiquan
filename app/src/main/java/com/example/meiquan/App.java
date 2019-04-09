package com.example.meiquan;

import android.app.Application;

import com.lzy.okgo.OkGo;

public class App extends Application {


    /**
     * 应用刚创建时，进行一些初始化
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化 OkGo
        initOkGo();
    }

    /**
     * 初始化 OkGo
     */
    void initOkGo() {
        OkGo.getInstance().init(this);
    }
}
