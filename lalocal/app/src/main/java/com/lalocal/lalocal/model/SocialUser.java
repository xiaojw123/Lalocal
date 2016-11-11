package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/11/8.
 */

public class SocialUser {

//    {
//        "nickName": “xb",
//        "avatar": “http://wx.qlogo.cn/mmopen/
//    ajNVdqHZLLC9o03icYicG8v35ZQNMXg6J3KJib2teDj1BiawfdWJyVHUibgFb8DrNiaN
//        s5QjqiaWUNPjibxoMcMxEZQsjw/0",
//        "sex": true,
//            "id": 34
//    }
    private String nickName;
    private String avatar;
    private boolean sex;
    private int id;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
