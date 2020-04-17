package com.smle.fish.model;

import com.smle.fish.smilelibrary.db.DbBaseModel;

/**
 * @PACKAGE_NAME：com.smle.fish.model
 * @user：yj
 * @date：2020/4/9
 * @版本：
 * @功能描述：
 */
public class TestModelA extends DbBaseModel {
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
