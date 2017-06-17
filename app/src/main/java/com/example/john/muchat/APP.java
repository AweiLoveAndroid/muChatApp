package com.example.john.muchat;

import com.example.common.app.Application;
import com.example.factory.Factory;
import com.igexin.sdk.PushManager;

/**
 * Created by John on 2017/6/1.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //调用Factory进行初始化
        Factory.setup();
        //推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
