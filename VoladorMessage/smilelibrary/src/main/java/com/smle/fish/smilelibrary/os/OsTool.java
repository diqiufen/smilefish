package com.smle.fish.smilelibrary.os;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.smle.fish.smilelibrary.io.EMimeType;

import java.io.File;
import java.io.IOException;

import androidx.core.content.FileProvider;

/**
 * @PACKAGE_NAME：mvpdemo.smile.com.mvpdemo.controller.origin
 * @user：yj
 * @date：2020/3/23
 * @版本：
 * @功能描述： 系统工具类  调用系统部分动作
 */
public class OsTool {

    /**
     * 发送拍照意图    (既然拍照 如果需要 记得更新媒体库
     * 拍照成功后能在媒体库第一时间看到拍摄的照片{@link #updateMedia(Context context, String filePath, String fileName, EMimeType mimeType)})
     *
     * @param activity
     * @param photoFile   创建的存储文件
     * @param requestCode 请求码
     * @throws IOException
     */
    public static void dispatchTakePictureIntent(Activity activity, File photoFile, int requestCode) throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            //第二个参数 应为authority 为了使权限证书唯一 所以一般使用包名  配置时需统一
            Uri photoURI = FileProvider.getUriForFile(activity, activity.getPackageName(), photoFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            activity.startActivityForResult(takePictureIntent, requestCode);
        } else {
            new Exception("相机被占用请稍后再试...");
        }
    }

    /**
     * 调用图库  和系统文件夹
     *
     * @param activity
     * @param requestCode
     */
    public static void dispatchSelectPictureIntent(Activity activity, int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, requestCode);
    }


    /**
     * 更新媒体库
     *
     * @param context
     * @param filePath 文件路径
     * @param fileName 显示的文件名称
     * @param mimeType 媒体类型 {@link EMimeType}
     */
    public static void updateMedia(Context context, String filePath, String fileName, EMimeType mimeType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.DATA, filePath);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, mimeType.getValue());
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis() + "");
        ContentResolver resolver = context.getContentResolver();
        resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }

    /**
     * 发送应用设置请求
     * @param context
     */
    public static void dispatchSettingsIntent(Context context) {
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }

}