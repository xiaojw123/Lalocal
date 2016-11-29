package com.lalocal.lalocal.live.im.session;

/**
 * Created by android on 2016/9/17.
 */
public class BarrageViewBean {
    private  String userId;
    private  String content;
    private String avator;
    private String senderName;


    public String getSenderName() {
        return senderName;
    }
    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
