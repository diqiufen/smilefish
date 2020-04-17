package com.smle.fish.ui;

import android.os.Bundle;

import com.smle.fish.interfaces.InjectView;
import com.smle.fish.interfaces.WindowInterface;
import com.smle.fish.smilelibrary.util.InitTool;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

/**
 * @PACKAGE_NAME：com.smle.fish.ui
 * @user：yj
 * @date：2020/4/2
 * @版本：
 * @功能描述：
 */
public abstract class BaseActivity extends AppCompatActivity implements WindowInterface {

    protected String TAG = "BaseActivity";
    HasDefaultViewModelProviderFactory providerFactory;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        setContentView(getContentViewId());
        initView();
        init();
        getData();
    }

    protected void initView() {
        try {
            InitTool.init(this, InjectView.class, new InitTool.InitCallBack<InjectView>() {
                @Override
                public Object onCallBack(InjectView annotationObject) {
                    return findViewById(annotationObject.viewId());
                }
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
