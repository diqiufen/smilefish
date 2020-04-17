package com.smle.fish.smilelibrary.io;

/**
 * @PACKAGE_NAME：mvpdemo.smile.com.mvpdemo.controller
 * @user：yj
 * @date：2020/3/20
 * @版本：
 * @功能描述： 限制 特定文件存入特定文件夹
 */
public enum EFolder {

    /**
     * 图片 文件夹
     */
    DIRECTORY_PICTURES("Pictures"),
    /**
     * 电影 文件夹
     */
    DIRECTORY_MOVIES("Movies"),
    /**
     * 下载 文件夹
     */
    DIRECTORY_DOWNLOADS("Download"),
    /**
     * 公共 文件夹
     */
    DIRECTORY_DCIM("DCIM"),
    /**
     * 文档 文件夹
     */
    DIRECTORY_DOCUMENTS("Documents"),
    /**
     * 截屏 文件夹
     */
    DIRECTORY_SCREENSHOTS("Screenshots"),
    /**
     * 音频 文件夹
     */
    DIRECTORY_AUDIOBOOKS("Audiobooks"),

    /**
     * 警报 文件夹
     */
    DIRECTORY_ALARMS("Alarms"),
    /**
     * 音乐 文件夹
     */
    DIRECTORY_MUSIC("Music"),
    /**
     * 多媒体 文件夹
     */
    DIRECTORY_PODCASTS("Podcasts"),
    /**
     * 铃声 文件夹
     */
    DIRECTORY_RINGTONES("Ringtones"),
    /**
     * 通知    铃声 文件夹
     */
    DIRECTORY_NOTIFICATIONS("Notifications");

    private String value = "";

    EFolder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }


}
