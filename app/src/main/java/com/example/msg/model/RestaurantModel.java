package com.example.msg.model;

public class RestaurantModel {
    private String restuser_id;
    private String restaurantName;
    private String restaurantPhone;
    private String profileImageUrl;
    private String restaurantAddress;
    private String description;
    private String pickupStartTime;
    private String pickupEndTime;
    private boolean approved;

    public String getRest_token() {
        return rest_token;
    }

    public void setRest_token(String rest_token) {
        this.rest_token = rest_token;
    }

    private String rest_token;

    public String getPickupStartTime() {
        return pickupStartTime;
    }

    public void setPickupStartTime(String pickupStartTime) {
        this.pickupStartTime = pickupStartTime;
    }

    public String getPickupEndTime() {
        return pickupEndTime;
    }

    public void setPickupEndTime(String pickupEndTime) {
        this.pickupEndTime = pickupEndTime;
    }

    public String getRestuser_id() {
        return restuser_id;
    }

    public void setRestuser_id(String restuser_id) {
        this.restuser_id = restuser_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantPhone() {
        return restaurantPhone;
    }

    public void setRestaurantPhone(String restaurantPhone) {
        this.restaurantPhone = restaurantPhone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getRestaurantAddress() {
        return restaurantAddress;
    }

    public void setRestaurantAddress(String restaurantAddress) {
        this.restaurantAddress = restaurantAddress;
    }
}