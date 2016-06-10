package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2016/6/7.
 */
public class User implements Parcelable{
    int userid;
    String nickName;
    String email;
    String avatar;
    String token;
    int status=-1;

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getUserid() {

        return userid;
    }

    public String getNickName() {
        return nickName;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getToken() {
        return token;
    }

    public int getStatus() {
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userid);
        dest.writeString(nickName);
        dest.writeString(email);
        dest.writeString(avatar);
        dest.writeString(token);
        dest.writeInt(status);
    }

    public User(Parcel in){
        userid = in.readInt();
        nickName = in.readString();
        email = in.readString();
        avatar = in.readString();
        token = in.readString();
        status = in.readInt();
    }

}
