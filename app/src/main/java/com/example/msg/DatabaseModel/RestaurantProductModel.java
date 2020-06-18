package com.example.msg.DatabaseModel;

import java.io.Serializable;

public class RestaurantProductModel implements Serializable {
    public String rproduct_id = null;
    public String res_id = null;
    public String title = null;
    public String p_imageURL = null;
    public String p_description = null;
    public String categoryBig = null;
    public String categorySmall = null;
    public int quality = -1;
    public String quantity = null;
    public String expiration_date = null;
    public int cost = -1;
    public boolean fast = false;
    public int stock = -1;
    public int completed = -1;
    public double longitude = -1;
    public double latitude = -1;
    public int saleDateYear = -1;
    public int saleDateMonth = -1;
    public int saleDateDay   = -1;

    public RestaurantProductModel() {

    }

    public RestaurantProductModel(String rproduct_id, String res_id, String title, String p_imageURL, String p_description
            ,String categoryBig, String categorySmall, int quality, String quantity, String expiration_date, int cost, boolean fast
            ,int stock, int completed, double longitude, double latitude, int saleDateYear,int saleDateMonth, int saleDateDay  ) {
        this.rproduct_id = rproduct_id;
        this.res_id = res_id;
        this.title = title;
        this.p_imageURL = p_imageURL;
        this.p_description = p_description;
        this.categoryBig = categoryBig;
        this.categorySmall = categorySmall;
        this.quality = quality;
        this.quantity = quantity;
        this.expiration_date = expiration_date;
        this.cost = cost;
        this.fast = fast;
        this.stock = stock;
        this.completed = completed;
        this.longitude = longitude;
        this.latitude =latitude;
        this.saleDateYear=saleDateYear;
        this.saleDateMonth=saleDateMonth;
        this.saleDateDay=saleDateDay;
    }

}
