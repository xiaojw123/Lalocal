package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/10/5.
 */
public class ChallengeDetailsResp  {

    private int returnCode;
    private String message;
    private long date;
    private Object errorCode;
    private List<ResultBean> result;
    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int id;
        private String content;
        private int targetGold;
        private int nowGold;
        private int status;
        private int channelId;

        private CreaterBean creater;
        private Object shareVO;
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

        public Object getShareVO() {
            return shareVO;
        }

        public void setShareVO(Object shareVO) {
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
    }
}
