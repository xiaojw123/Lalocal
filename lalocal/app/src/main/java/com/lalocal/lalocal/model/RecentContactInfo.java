package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/12/19.
 */

public class RecentContactInfo {
    private String avatar;
    private int unReadCount;
    private String nickName;
    private String content;
    private long time;
    private String account;
    public RecentContactInfo(){

    }

    public RecentContactInfo(String account,String avatar, int unReadCount, String nickName, String content, long time) {
        this.account=account;
        this.avatar = avatar;
        this.unReadCount = unReadCount;
        this.nickName = nickName;
        this.content = content;
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(int unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
