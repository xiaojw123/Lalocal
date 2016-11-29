package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/11/22.
 */
public class RoomNotifyExt implements Comparable<RoomNotifyExt>{


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getSortValue() {
        return sortValue;
    }

    public void setSortValue(int sortValue) {
        this.sortValue = sortValue;
    }

    private String userId;

    private  String avatar;

    private  int sortValue;

    @Override
    public int compareTo(RoomNotifyExt another) {
        return another.getSortValue() - this.getSortValue();
    }

    @Override
    public String toString() {
        return "RoomNotifyExt{" +
                "userId='" + userId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", sortValue=" + sortValue +
                '}';
    }
}
