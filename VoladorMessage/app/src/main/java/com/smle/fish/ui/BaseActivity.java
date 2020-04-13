package com.smle.fish.ui;

import android.os.Bundle;

import com.smle.fish.db.Database;
import com.smle.fish.interfaces.WindowInterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @PACKAGE_NAME：com.smle.fish.ui
 * @user：yj
 * @date：2020/4/2
 * @版本：
 * @功能描述：
 */
public abstract class BaseActivity extends AppCompatActivity implements WindowInterface {

    protected String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        setContentView(getContentViewId());
        init();
        getData();
    }
}
