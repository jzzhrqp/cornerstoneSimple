package com.aoshuotec.iron.cornerstone.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.utils.Utils;


public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}





















































