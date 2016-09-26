package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 2016/7/19.
 */
public  class LiveUserBean implements Parcelable {
    private int id;
    private String nickName;
    private String email;
    private String avatar;
    private String description;
    private String avatarOrigin;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAvatarOrigin() {
        return avatarOrigin;
    }

    public void setAvatarOrigin(String avatarOrigin) {
        this.avatarOrigin = avatarOrigin;
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
        dest.writeString(this.description);
        dest.writeString(this.avatarOrigin);
    }

    public LiveUserBean() {
    }

    protected LiveUserBean(Parcel in) {
        this.id = in.readInt();
        this.nickName = in.readString();
        this.email = in.readString();
        this.avatar = in.readString();
        this.description = in.readString();
        this.avatarOrigin = in.readString();
    }

    public static final Parcelable.Creator<LiveUserBean> CREATOR = new Parcelable.Creator<LiveUserBean>() {
        @Override
        public LiveUserBean createFromParcel(Parcel source) {
            return new LiveUserBean(source);
        }

        @Override
        public LiveUserBean[] newArray(int size) {
            return new LiveUserBean[size];
        }
    };
}
