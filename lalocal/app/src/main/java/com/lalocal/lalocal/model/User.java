package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2016/6/7.
 */
public class User implements Parcelable{
    int id=-1;
    String nickName;
    String email;
    String avatar;
    String token=null;
    int status=-1;
    ImUserInfo imUserInfo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ImUserInfo getImUserInfo() {
        return imUserInfo;
    }

    public void setImUserInfo(ImUserInfo imUserInfo) {
        this.imUserInfo = imUserInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nickName);
        dest.writeString(this.email);
        dest.writeString(this.avatar);
        dest.writeString(this.token);
        dest.writeInt(this.status);
        dest.writeParcelable(this.imUserInfo, flags);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readInt();
        this.nickName = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
        this.token = in.readString();
        this.status = in.readInt();
        this.imUserInfo = in.readParcelable(ImUserInfo.class.getClassLoader());
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}