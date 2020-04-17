package com.smle.fish.smilelibrary.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.smle.fish.smilelibrary.os.OsTool;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;

/**
 * @author yj
 * @PACKAGE_NAME：com.smle.fish.smilelibrary.activity
 * @user：yj
 * @date：2020/4/17
 * @版本：
 * @功能描述：
 */
public class RequestPermissionActivity extends AppCompatActivity {

    private String TAG = getClass().getName();
    private Uri uri;
    HasDefaultViewModelProviderFactory providerFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        //
    }


    protected void requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "0000000000000");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d(TAG, "222222222222");
                showRequestPermissions();
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "333333333333333");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                }
            }
        } else {
            //有权限
            Log.d(TAG, "66666666666666");
        }
    }

    protected void showRequestPermissions() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(String.format("由于需要某某权限"))
                .setCancelable(false)
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        OsTool.dispatchSettingsIntent(RequestPermissionActivity.this);
                    }
                })
                .setNegativeButton("不了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).create();
        alertDialog.show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "44444444444");
        requestPermissions();
    }


//    class OpenFileMode {
//        static final String openFileModeR = "R";
//        static final String openFileModeW = "W";
//        static final String openFileModeRW = "RW";
//    }
}