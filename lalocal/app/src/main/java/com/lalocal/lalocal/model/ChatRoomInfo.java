package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by android on 2016/7/24.
 */
public class ChatRoomInfo implements Parcelable {
    private String pullUrl;
    private String  roomId;

    public String getPullUrl() {
        return pullUrl;
    }

    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pullUrl);
        dest.writeString(this.roomId);
    }

    public ChatRoomInfo() {
    }

    protected ChatRoomInfo(Parcel in) {
        this.pullUrl = in.readString();
        this.roomId = in.readString();
    }

    public static final Parcelable.Creator<ChatRoomInfo> CREATOR = new Parcelable.Creator<ChatRoomInfo>() {
        @Override
        public ChatRoomInfo createFromParcel(Parcel source) {
            return new ChatRoomInfo(source);
        }

        @Override
        public ChatRoomInfo[] newArray(int size) {
            return new ChatRoomInfo[size];
        }
    };
}
