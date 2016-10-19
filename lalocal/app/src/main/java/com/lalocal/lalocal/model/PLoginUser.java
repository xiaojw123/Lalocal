package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/10/17.
 */

public class PLoginUser {


    /**
     * id : 9328
     * nickName : 65QXLKWF
     * email : null
     * avatar : http://media.lalocal.cn/2016051320515814900627825168?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description : null
     * avatarOrigin : http://media.lalocal.cn/2016051320515814900627825168
     * token : MjY1Yzk5ZTYtYTUwZi00MmM1LWJlMWQtNzY0ZmM4Yzg1ZWYy
     * status : -1
     * role : 0
     * orderNum : 0
     * imUserInfo : {"accId":"user_9328","token":"f943e6a5e8a8b3841fddd4fb47ef062a"}
     */

    private int id;
    private String nickName;
    private Object email;
    private String avatar;
    private Object description;
    private String avatarOrigin;
    private String token;
    private int status;
    private int role;
    private int orderNum;
    /**
     * accId : user_9328
     * token : f943e6a5e8a8b3841fddd4fb47ef062a
     */

    private ImUserInfoBean imUserInfo;

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

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getAvatarOrigin() {
        return avatarOrigin;
    }

    public void setAvatarOrigin(String avatarOrigin) {
        this.avatarOrigin = avatarOrigin;
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public ImUserInfoBean getImUserInfo() {
        return imUserInfo;
    }

    public void setImUserInfo(ImUserInfoBean imUserInfo) {
        this.imUserInfo = imUserInfo;
    }

    public static class ImUserInfoBean {
        private String accId;
        private String token;

        public String getAccId() {
            return accId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
