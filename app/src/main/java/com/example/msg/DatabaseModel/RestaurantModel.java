package com.example.msg.DatabaseModel;

public class RestaurantModel {
    public String res_id = null;
    public String res_name = null;
    public String res_phone;
    public String res_address = null;
    public String res_address_detail=null; //추가 0526
    public String res_imageURL = null;
    public String res_description = null;
    public String pickup_start_time;
    public String pickup_end_time;
    public boolean approved;
    public String res_token = null;
    public double res_latitude = -1;    //추가0526
    public double res_longitude = -1;   //추가0526
    public float res_rating = 0; //0529
    public int ratingCount = 1;
}
