package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 2016/7/27.
 */
public class ImUserInfo implements Parcelable {

    private String token;
    private String accId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.token);
        dest.writeString(this.accId);
    }

    public ImUserInfo() {
    }

    protected ImUserInfo(Parcel in) {
        this.token = in.readString();
        this.accId = in.readString();
    }

    public static final Creator<ImUserInfo> CREATOR = new Creator<ImUserInfo>() {
        @Override
        public ImUserInfo createFromParcel(Parcel source) {
            return new ImUserInfo(source);
        }

        @Override
        public ImUserInfo[] newArray(int size) {
            return new ImUserInfo[size];
        }
    };
}
