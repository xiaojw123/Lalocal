package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/6.
 */
public class LiveManagerListBean {

        private int id;
        private String nickName;
        private Object email;
        private String avatar;
        private Object description;
        private String avatarOrigin;

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

}
