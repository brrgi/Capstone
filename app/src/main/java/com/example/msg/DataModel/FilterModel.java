package com.example.msg.DataModel;


import java.io.Serializable;

public class FilterModel implements Serializable {
    private String filterType = "distance";
    private String category = null;
    private double range = 500;
    private int price = 500;
    private boolean searchHighQuality = true;
    private boolean searchMidQuality = true;
    private boolean searchLowQuality = true;

    public void setFilterTypeDistance() {
        filterType = "distance";
    }

    public void setFilterTypePrice() {
        filterType = "price";
    }

    public void setFilterTypeStock() {
        filterType = "stock";
    }

    public String getFilterType() {
        return filterType;
    }

    public double getRange() {
        return range;
    }

    public void setRange(double range) {
        this.range = range;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isSearchHighQuality() {
        return searchHighQuality;
    }

    public void setSearchHighQuality(boolean searchHighQuality) {
        this.searchHighQuality = searchHighQuality;
    }

    public boolean isSearchMidQuality() {
        return searchMidQuality;
    }

    public void setSearchMidQuality(boolean searchMidQuality) {
        this.searchMidQuality = searchMidQuality;
    }

    public boolean isSearchLowQuality() {
        return searchLowQuality;
    }

    public void setSearchLowQuality(boolean searchLowQuality) {
        this.searchLowQuality = searchLowQuality;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
