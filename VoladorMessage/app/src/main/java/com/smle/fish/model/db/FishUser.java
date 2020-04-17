package com.smle.fish.model.db;

import com.smle.fish.smilelibrary.db.DbBaseModel;

/**
 * @PACKAGE_NAME：com.smle.fish.model.db
 * @user：yj
 * @date：2020/4/9
 * @版本：
 * @功能描述：
 */
public class FishUser extends DbBaseModel {
    private String nickName = "";
    private String name = "noName";
    private String passWord = "";
    private String picture = "";
    private int age = 0;
    //1男0女
    private int sex = 0;
    //个性签名
    private String selfdomSignature = "";
    private String address = "";
    private String phone = "";
    private String ccc="";

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getSelfdomSignature() {
        return selfdomSignature;
    }

    public void setSelfdomSignature(String selfdomSignature) {
        this.selfdomSignature = selfdomSignature;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "FishUser{" +
                "id='" + id + '\'' +
                "nickName='" + nickName + '\'' +
                ", name='" + name + '\'' +
                ", passWord='" + passWord + '\'' +
                ", picture='" + picture + '\'' +
                ", age=" + age +
                ", sex=" + sex +
                ", selfdomSignature='" + selfdomSignature + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
