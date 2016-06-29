package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/6/28.
 */
public class BigPictureBean implements Parcelable {

    private String imgUrl;
    private String content;
    private String name;

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.content);
        dest.writeString(this.name);
    }

    public BigPictureBean() {
    }

    protected BigPictureBean(Parcel in) {
        this.imgUrl = in.readString();
        this.content = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<BigPictureBean> CREATOR = new Parcelable.Creator<BigPictureBean>() {
        @Override
        public BigPictureBean createFromParcel(Parcel source) {
            return new BigPictureBean(source);
        }

        @Override
        public BigPictureBean[] newArray(int size) {
            return new BigPictureBean[size];
        }
    };
}
