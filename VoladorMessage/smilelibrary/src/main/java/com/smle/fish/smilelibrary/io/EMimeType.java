package com.smle.fish.smilelibrary.io;

/**
 * @PACKAGE_NAME：mvpdemo.smile.com.mvpdemo
 * @user：yj
 * @date：2020/3/23
 * @版本：
 * @功能描述： 常用的 简易的 Mime类型 类
 */
public enum EMimeType {

    TEXT("text/plain"),
    THML("text/html"),
    JPG("image/jpeg"),
    PNG("image/png"),
    VIDEO("video/mpeg"),
    PDF("application/pdf");


    private String value = "";

    EMimeType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
