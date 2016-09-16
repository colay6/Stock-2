package com.chhd.stock.entity;

/**
 * Created by CWQ on 2016/7/10.
 */
public class Banner {

    private String title;
    private String imageUrl;

    public Banner(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
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

    @Override
    public String toString() {
        return title;
    }
}
