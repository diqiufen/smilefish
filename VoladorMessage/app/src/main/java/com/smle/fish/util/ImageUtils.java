package com.smle.fish.util;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * @PACKAGE_NAME：com.smle.fish.util
 * @user：yj
 * @date：2020/4/7
 * @版本：
 * @功能描述：
 */
public class ImageUtils {

    /**
     * 设置饱和度:0为纯黑白，饱和度为0；1为饱和度为100，即原图；
     * @param imageView
     * @param saturation
     */
    public static void setSaturation(ImageView imageView, float saturation) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);
        ColorMatrixColorFilter grayColorFilter = new ColorMatrixColorFilter(cm);
        imageView.setColorFilter(grayColorFilter);
    }
}
