package com.example.msg.model;

public class UserModel {
    private String userName;
    private int ban_count;
    private boolean is_male;
    private String user_id;
    private String user_phone;
    private int age;
    private int mileage;
    private int grade;
    //주소 추가해야함

    public boolean isIs_male() {
        return is_male;
    }

    public void setIs_male(boolean is_male) {
        this.is_male = is_male;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getBan_count() {
        return ban_count;
    }

    public void setBan_count(int ban_count) {
        this.ban_count = ban_count;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
