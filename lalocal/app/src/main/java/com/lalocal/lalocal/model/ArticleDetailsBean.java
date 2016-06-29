package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/6/27.
 */
public class ArticleDetailsBean implements Parcelable {
    private int praiseNum;
    private int readNum;
    private int targetId;
    private String targetName;
    private String phone;
    private int praises;

    public int getPraises() {
        return praises;
    }

    public void setPraises(int praises) {
        this.praises = praises;
    }

    private int targetType;

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getReadNum() {
        return readNum;
    }

    public void setReadNum(int readNum) {
        this.readNum = readNum;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArticleDetailsBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.praiseNum);
        dest.writeInt(this.readNum);
        dest.writeInt(this.targetId);
        dest.writeString(this.targetName);
        dest.writeString(this.phone);
        dest.writeInt(this.praises);
        dest.writeInt(this.targetType);
    }

    protected ArticleDetailsBean(Parcel in) {
        this.praiseNum = in.readInt();
        this.readNum = in.readInt();
        this.targetId = in.readInt();
        this.targetName = in.readString();
        this.phone = in.readString();
        this.praises = in.readInt();
        this.targetType = in.readInt();
    }

    public static final Creator<ArticleDetailsBean> CREATOR = new Creator<ArticleDetailsBean>() {
        @Override
        public ArticleDetailsBean createFromParcel(Parcel source) {
            return new ArticleDetailsBean(source);
        }

        @Override
        public ArticleDetailsBean[] newArray(int size) {
            return new ArticleDetailsBean[size];
        }
    };
}
