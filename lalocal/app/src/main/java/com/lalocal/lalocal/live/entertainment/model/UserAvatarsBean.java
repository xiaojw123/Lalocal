package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/12/6.
 */
public class UserAvatarsBean {


        private boolean sex;
        private int id;
        private String nickName;
        private String avatar;
        private String description;
        private int role;
        private Object fansNum;
        private Object attentionNum;
        private AttentionVOBean attentionVO;
        private String avatarOrigin;
        private String accId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAvatarsBean that = (UserAvatarsBean) o;

        return id == that.id;

    }

    @Override
    public int hashCode() {
        return id;
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

        public Object getFansNum() {
            return fansNum;
        }

        public void setFansNum(Object fansNum) {
            this.fansNum = fansNum;
        }

        public Object getAttentionNum() {
            return attentionNum;
        }

        public void setAttentionNum(Object attentionNum) {
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

        public String getAccId() {
            return accId;
        }

        public void setAccId(String accId) {
            this.accId = accId;
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
