package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2016/9/5.
 */
public class RechargeItem implements Parcelable {


    /**
     * id : 4
     * name : 6元=60乐钻
     * fee : 6
     * value : 60
     */

    private int id;
    private String name;
    private double fee;
    private int value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeDouble(this.fee);
        dest.writeInt(this.value);
    }

    public RechargeItem() {
    }

    protected RechargeItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.fee = in.readDouble();
        this.value = in.readInt();
    }

    public static final Parcelable.Creator<RechargeItem> CREATOR = new Parcelable.Creator<RechargeItem>() {
        @Override
        public RechargeItem createFromParcel(Parcel source) {
            return new RechargeItem(source);
        }

        @Override
        public RechargeItem[] newArray(int size) {
            return new RechargeItem[size];
        }
    };
}
