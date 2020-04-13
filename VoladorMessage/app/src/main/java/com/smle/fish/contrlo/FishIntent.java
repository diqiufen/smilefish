package com.smle.fish.contrlo;

import android.app.Activity;
import android.content.Intent;

import com.smle.fish.ui.home.MainActivity;
import com.smle.fish.ui.home.MainActivity2;

/**
 * @PACKAGE_NAME：com.smle.fish.contrlo
 * @user：yj
 * @date：2020/4/7
 * @版本：
 * @功能描述：
 */
public class FishIntent {

    public static void startMainActivity(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }
}
