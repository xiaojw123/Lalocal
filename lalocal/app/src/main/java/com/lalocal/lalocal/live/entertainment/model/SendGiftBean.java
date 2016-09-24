package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/9.
 */
public class SendGiftBean {
    private int type;

    private String headImage;
    private String giftImage;
    private String giftName;
    private String giftCount;
    private int userId;
    private String code;
    private String userName;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public String getGiftImage() {
        return giftImage;
    }

    public void setGiftImage(String giftImage) {
        this.giftImage = giftImage;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public String getGiftCount() {
        return giftCount;
    }

    public void setGiftCount(String giftCount) {
        this.giftCount = giftCount;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
