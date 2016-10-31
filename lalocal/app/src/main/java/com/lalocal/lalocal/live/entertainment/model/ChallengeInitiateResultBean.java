package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/10/4.
 */
public class ChallengeInitiateResultBean {

        private int id;
        private String content;
        private int targetGold;
        private int nowGold;
        private int status;
        private int channelId;
        private CreaterBean creater;
        private ShareVOBean shareVO;
        private int score;
        private int roomId;
        private int remainTime;
        private Object userRank;
        private Object myRank;
        private Object successAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getTargetGold() {
            return targetGold;
        }

        public void setTargetGold(int targetGold) {
            this.targetGold = targetGold;
        }

        public int getNowGold() {
            return nowGold;
        }

        public void setNowGold(int nowGold) {
            this.nowGold = nowGold;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public CreaterBean getCreater() {
            return creater;
        }

        public void setCreater(CreaterBean creater) {
            this.creater = creater;
        }

        public ShareVOBean getShareVO() {
            return shareVO;
        }

        public void setShareVO(ShareVOBean shareVO) {
            this.shareVO = shareVO;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public int getRoomId() {
            return roomId;
        }

        public void setRoomId(int roomId) {
            this.roomId = roomId;
        }

        public int getRemainTime() {
            return remainTime;
        }

        public void setRemainTime(int remainTime) {
            this.remainTime = remainTime;
        }

        public Object getUserRank() {
            return userRank;
        }

        public void setUserRank(Object userRank) {
            this.userRank = userRank;
        }

        public Object getMyRank() {
            return myRank;
        }

        public void setMyRank(Object myRank) {
            this.myRank = myRank;
        }

        public Object getSuccessAt() {
            return successAt;
        }

        public void setSuccessAt(Object successAt) {
            this.successAt = successAt;
        }

        public static class CreaterBean {
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
        public static class ShareVOBean {
            private String url;
            private String title;
            private String desc;
            private String img;
            private int type;
            private int targetType;
            private int targetId;
            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getTargetType() {
                return targetType;
            }

            public void setTargetType(int targetType) {
                this.targetType = targetType;
            }

            public int getTargetId() {
                return targetId;
            }

            public void setTargetId(int targetId) {
                this.targetId = targetId;
            }
        }

}
