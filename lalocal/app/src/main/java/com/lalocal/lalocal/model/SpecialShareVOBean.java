package com.lalocal.lalocal.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/6/17.
 */
public class SpecialShareVOBean implements Parcelable {
    private  String url;
    private String title;
    private String desc;
    private String img;
    private int type;
    private Bitmap bitmap;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeString(this.title);
        dest.writeString(this.desc);
        dest.writeString(this.img);
        dest.writeInt(this.type);
        dest.writeParcelable(this.bitmap, flags);
    }

    public SpecialShareVOBean() {
    }

    protected SpecialShareVOBean(Parcel in) {
        this.url = in.readString();
        this.title = in.readString();
        this.desc = in.readString();
        this.img = in.readString();
        this.type = in.readInt();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Parcelable.Creator<SpecialShareVOBean> CREATOR = new Parcelable.Creator<SpecialShareVOBean>() {
        @Override
        public SpecialShareVOBean createFromParcel(Parcel source) {
            return new SpecialShareVOBean(source);
        }

        @Override
        public SpecialShareVOBean[] newArray(int size) {
            return new SpecialShareVOBean[size];
        }
    };
}
