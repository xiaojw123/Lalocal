package com.lalocal.lalocal.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lenovo on 2016/6/21.
 */

public class SpecialToH5Bean implements Parcelable {
    private int targetType;
    private int targetId;
    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getTargetType() {

        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.targetType);
        dest.writeInt(this.targetId);
    }

    public SpecialToH5Bean() {
    }

    protected SpecialToH5Bean(Parcel in) {
        this.targetType = in.readInt();
        this.targetId = in.readInt();
    }

    public static final Parcelable.Creator<SpecialToH5Bean> CREATOR = new Parcelable.Creator<SpecialToH5Bean>() {
        @Override
        public SpecialToH5Bean createFromParcel(Parcel source) {
            return new SpecialToH5Bean(source);
        }

        @Override
        public SpecialToH5Bean[] newArray(int size) {
            return new SpecialToH5Bean[size];
        }
    };
}
