package com.example.msg.recyclerView;

public class Products {
    private String title;
    private String imageUrl;
    private String uid;

    public Products(){  //필수인듯

    }

    public Products(String title, String imageUrl, String uid) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
