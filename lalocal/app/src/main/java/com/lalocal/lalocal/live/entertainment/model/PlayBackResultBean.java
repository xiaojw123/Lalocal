package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;

import java.util.List;

/**
 * Created by android on 2016/12/14.
 */
public class PlayBackResultBean {
        private int id;
        private String startAt;
        private String endAt;
        private int totalScore;
        private int onlineNumber;
        private int direction;
        private String photo;
        private String title;
        private int channelId;
        private int number;
        private LiveUserBean user;
        private String address;
        private SpecialShareVOBean shareVO;
        private boolean praiseFlag;
        private Object praiseId;
        private String recommendTitle;
        private int praiseNum;
        private int shareNum;
        private String description;
        private int readNum;
        private int commentNum;
        private List<VideoListBean> videoList;
        private List<?> userAvatars;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getStartAt() {
            return startAt;
        }

        public void setStartAt(String startAt) {
            this.startAt = startAt;
        }

        public String getEndAt() {
            return endAt;
        }

        public void setEndAt(String endAt) {
            this.endAt = endAt;
        }

        public int getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(int totalScore) {
            this.totalScore = totalScore;
        }

        public int getOnlineNumber() {
            return onlineNumber;
        }

        public void setOnlineNumber(int onlineNumber) {
            this.onlineNumber = onlineNumber;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getChannelId() {
            return channelId;
        }

        public void setChannelId(int channelId) {
            this.channelId = channelId;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public LiveUserBean getUser() {
            return user;
        }

        public void setUser(LiveUserBean user) {
            this.user = user;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public SpecialShareVOBean getShareVO() {
            return shareVO;
        }

        public void setShareVO(SpecialShareVOBean shareVO) {
            this.shareVO = shareVO;
        }

        public boolean isPraiseFlag() {
            return praiseFlag;
        }

        public void setPraiseFlag(boolean praiseFlag) {
            this.praiseFlag = praiseFlag;
        }

        public Object getPraiseId() {
            return praiseId;
        }

        public void setPraiseId(Object praiseId) {
            this.praiseId = praiseId;
        }

        public String  getRecommendTitle() {
            return recommendTitle;
        }

        public void setRecommendTitle(String recommendTitle) {
            this.recommendTitle = recommendTitle;
        }

        public int getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(int praiseNum) {
            this.praiseNum = praiseNum;
        }

        public int getShareNum() {
            return shareNum;
        }

        public void setShareNum(int shareNum) {
            this.shareNum = shareNum;
        }

        public String  getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getReadNum() {
            return readNum;
        }

        public void setReadNum(int readNum) {
            this.readNum = readNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public List<VideoListBean> getVideoList() {
            return videoList;
        }

        public void setVideoList(List<VideoListBean> videoList) {
            this.videoList = videoList;
        }

        public List<?> getUserAvatars() {
            return userAvatars;
        }

        public void setUserAvatars(List<?> userAvatars) {
            this.userAvatars = userAvatars;
        }



        public static class VideoListBean {
            private int id;
            private int size;
            private double duration;
            private Object startTime;
            private Object endTime;
            private String url;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public double getDuration() {
                return duration;
            }

            public void setDuration(double duration) {
                this.duration = duration;
            }

            public Object getStartTime() {
                return startTime;
            }

            public void setStartTime(Object startTime) {
                this.startTime = startTime;
            }

            public Object getEndTime() {
                return endTime;
            }

            public void setEndTime(Object endTime) {
                this.endTime = endTime;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
}

