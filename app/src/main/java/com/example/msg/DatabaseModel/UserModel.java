package com.example.msg.DatabaseModel;

public class UserModel {
    public String user_id = null;
    public String user_name = null;
    public int age = -1;
    public boolean is_male = false;
    public String user_address = null;
    public String user_address_detail=null; //추가 0526
    public String user_phone = null;
    public String email = null;
    public int mileage = -1;
    public int ban_count = -1;
    public int user_grade = -1;
    public boolean sanction;
    public double latitude = -1;    //추가0526
    public double longitude = -1; //추가0526
    public float user_rating = 0; //0529
    public int ratingCount = 1; //0529
}
