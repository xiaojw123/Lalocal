package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/8/5.
 */
public class LiveUserInfoResultBean {
    private boolean sex;
    private int id;
    private String nickName;
    private String avatar;
    private String description;
    private int role;
    private int fansNum;
    private int attentionNum;

    public String getAccId() {
        return accId;
    }

    public void setAccId(String accId) {
        this.accId = accId;
    }

    private String accId;


    private AttentionVOBean attentionVO;
    private String avatarOrigin;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getFansNum() {
        return fansNum;
    }

    public void setFansNum(int fansNum) {
        this.fansNum = fansNum;
    }

    public int getAttentionNum() {
        return attentionNum;
    }

    public void setAttentionNum(int attentionNum) {
        this.attentionNum = attentionNum;
    }

    public AttentionVOBean getAttentionVO() {
        return attentionVO;
    }

    public void setAttentionVO(AttentionVOBean attentionVO) {
        this.attentionVO = attentionVO;
    }

    public String getAvatarOrigin() {
        return avatarOrigin;
    }

    public void setAvatarOrigin(String avatarOrigin) {
        this.avatarOrigin = avatarOrigin;
    }

    public static class AttentionVOBean {
        private Object status;

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }
    }

}
