package com.smle.fish.model.db;

/**
 * @PACKAGE_NAME：com.smle.fish.model.db
 * @user：yj
 * @date：2020/4/9
 * @版本：
 * @功能描述： 最近联系人
 */
public class LatelyContacts {

    private String nickname = "";
    private String name = "";
    private String passWord = "";
    private String picture = "";
    private int age = 0;
    //1男0女
    private int sex = 0;
    //个性签名
    private String selfdomSignature = "";
    private String address = "";
    private String phone = "";
    /**
     * 最近联系时间
     */
    private Long latelyTime = System.currentTimeMillis();

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public Long getLatelyTime() {
        return latelyTime;
    }

    public void setLatelyTime(Long latelyTime) {
        this.latelyTime = latelyTime;
    }
}
