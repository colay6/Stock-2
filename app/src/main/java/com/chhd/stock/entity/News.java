package com.chhd.stock.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by CWQ on 2016/7/28.
 */
public class News implements Parcelable {

    private String title;

    private String url;

    private String img;

    private String author;

    private int time;

    public News() {
    }

    public News(String title, String url, String img, String author, int time) {
        this.title = title;
        this.url = url;
        this.img = img;
        this.author = author;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", img='" + img + '\'' +
                ", author='" + author + '\'' +
                ", time=" + time +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.img);
        dest.writeString(this.author);
        dest.writeInt(this.time);
    }

    protected News(Parcel in) {
        this.title = in.readString();
        this.url = in.readString();
        this.img = in.readString();
        this.author = in.readString();
        this.time = in.readInt();
    }

    public static final Parcelable.Creator<News> CREATOR = new Parcelable.Creator<News>() {
        @Override
        public News createFromParcel(Parcel source) {
            return new News(source);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };
}
