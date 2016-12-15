package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/11.
 */
public class RankUserBean {
        private int id;
        private String nickName;
        private String email;
        private String avatar;
        private String description;
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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

        public String getAvatarOrigin() {
            return avatarOrigin;
        }

        public void setAvatarOrigin(String avatarOrigin) {
            this.avatarOrigin = avatarOrigin;
        }


}
