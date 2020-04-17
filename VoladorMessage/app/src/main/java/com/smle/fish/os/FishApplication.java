package com.smle.fish.os;

import android.app.Application;
import android.content.Context;

import com.smle.fish.db.FishDatabase;

/**
 * @PACKAGE_NAME：com.smle.fish.os
 * @user：yj
 * @date：2020/4/15
 * @版本：
 * @功能描述：
 */
public class FishApplication extends Application {

    private Context context = this;

    @Override
    public void onCreate() {
        super.onCreate();
        FishDatabase.getInstance(context).init();
    }
}
