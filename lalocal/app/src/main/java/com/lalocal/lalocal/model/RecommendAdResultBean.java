package com.lalocal.lalocal.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/6/20.
 */
public class RecommendAdResultBean implements Parcelable {
    public String title;
    public String photo;
    public String url;
    public boolean publishFlag;
    public int sort;
    public String description;
    public int targetId;
    public int targetType;
    public SpecialShareVOBean shareVO;
    public int id;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeString(this.url);
        dest.writeByte(this.publishFlag ? (byte) 1 : (byte) 0);
        dest.writeInt(this.sort);
        dest.writeString(this.description);
        dest.writeInt(this.targetId);
        dest.writeInt(this.targetType);
        dest.writeParcelable(this.shareVO, flags);
        dest.writeInt(this.id);
    }
    public RecommendAdResultBean() {
    }
    protected RecommendAdResultBean(Parcel in) {
        this.title = in.readString();
        this.photo = in.readString();
        this.url = in.readString();
        this.publishFlag = in.readByte() != 0;
        this.sort = in.readInt();
        this.description = in.readString();
        this.targetId = in.readInt();
        this.targetType = in.readInt();
        this.shareVO = in.readParcelable(SpecialShareVOBean.class.getClassLoader());
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<RecommendAdResultBean> CREATOR = new Parcelable.Creator<RecommendAdResultBean>() {
        @Override
        public RecommendAdResultBean createFromParcel(Parcel source) {
            return new RecommendAdResultBean(source);
        }

        @Override
        public RecommendAdResultBean[] newArray(int size) {
            return new RecommendAdResultBean[size];
        }
    };
}
