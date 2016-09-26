package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2016/9/2.
 */
public class WalletContent implements Parcelable {

    /**
     * gold : 10
     * score : 17
     * signInFlag : false
     * couponNumb : 0
     * preExchangeGold : 0
     * scale : 22
     * firstMsg : 首次充值可获得额外40乐钻
     */

    private double gold;
    private long score;
    private boolean signInFlag;
    private int couponNumb;
    private int preExchangeGold;
    private int scale;
    private String firstMsg;

    public double getGold() {
        return gold;
    }

    public void setGold(double gold) {
        this.gold = gold;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isSignInFlag() {
        return signInFlag;
    }

    public void setSignInFlag(boolean signInFlag) {
        this.signInFlag = signInFlag;
    }

    public int getCouponNumb() {
        return couponNumb;
    }

    public void setCouponNumb(int couponNumb) {
        this.couponNumb = couponNumb;
    }

    public int getPreExchangeGold() {
        return preExchangeGold;
    }

    public void setPreExchangeGold(int preExchangeGold) {
        this.preExchangeGold = preExchangeGold;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFirstMsg() {
        return firstMsg;
    }

    public void setFirstMsg(String firstMsg) {
        this.firstMsg = firstMsg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.gold);
        dest.writeLong(this.score);
        dest.writeByte(this.signInFlag ? (byte) 1 : (byte) 0);
        dest.writeInt(this.couponNumb);
        dest.writeInt(this.preExchangeGold);
        dest.writeInt(this.scale);
        dest.writeString(this.firstMsg);
    }

    public WalletContent() {
    }

    protected WalletContent(Parcel in) {
        this.gold = in.readDouble();
        this.score = in.readLong();
        this.signInFlag = in.readByte() != 0;
        this.couponNumb = in.readInt();
        this.preExchangeGold = in.readInt();
        this.scale = in.readInt();
        this.firstMsg = in.readString();
    }

    public static final Parcelable.Creator<WalletContent> CREATOR = new Parcelable.Creator<WalletContent>() {
        @Override
        public WalletContent createFromParcel(Parcel source) {
            return new WalletContent(source);
        }

        @Override
        public WalletContent[] newArray(int size) {
            return new WalletContent[size];
        }
    };
}