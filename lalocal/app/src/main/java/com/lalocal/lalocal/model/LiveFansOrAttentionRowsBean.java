package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/8/7.
 */
public class LiveFansOrAttentionRowsBean {


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

        public static class AttentionVOBean {
            private int status;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
}
