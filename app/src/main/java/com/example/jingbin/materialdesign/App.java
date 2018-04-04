package com.example.jingbin.materialdesign;

import android.app.Application;
import android.content.Context;

/**
 * @author dangxueyi
 * @description
 * @date 2017/12/29
 */

public class App extends Application {

    private static Context mContext;

    public static Context getInstance() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }
}
