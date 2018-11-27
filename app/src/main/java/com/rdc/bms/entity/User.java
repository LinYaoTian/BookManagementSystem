package com.rdc.bms.entity;


public class User {

    //姓名
    private String name;

    //登陆账号=借书证号
    private String userId;

    //密码
    private String password;

    //专业
    private String major;

    //性别
    private String sex;

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", major='" + major + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }

    public String getPassword() {
        return password == null ? "" : password;
    }

    public void setPassword(String password) {
        this.password = password == null ? "" : password;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getUserId() {
        return userId == null ? "" : userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? "" : userId;
    }

    public String getMajor() {
        return major == null ? "" : major;
    }

    public void setMajor(String major) {
        this.major = major == null ? "" : major;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? "" : sex;
    }
}
