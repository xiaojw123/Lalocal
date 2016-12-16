package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.LiveUserBean;

/**
 * Created by android on 2016/12/14.
 */
public class PlayBackReviewRowsBean {

        private int id;
        private LiveUserBean user;
        private LiveUserBean targetUser;
        private String content;
        private String dateTime;
        private int status;
        private int sort;
        private PlayBackReviewRowsBean partentComment;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public LiveUserBean getUser() {
            return user;
        }

        public void setUser(LiveUserBean user) {
            this.user = user;
        }

        public LiveUserBean getTargetUser() {
            return targetUser;
        }

        public void setTargetUser(LiveUserBean targetUser) {
            this.targetUser = targetUser;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public PlayBackReviewRowsBean getPartentComment() {
            return partentComment;
        }

        public void setPartentComment(PlayBackReviewRowsBean partentComment) {
            this.partentComment = partentComment;
        }
}
