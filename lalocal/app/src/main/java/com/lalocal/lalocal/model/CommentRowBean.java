package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/12/19.
 */

public class CommentRowBean {

    /**
     * id : 509543
     * user : {"id":9826,"nickName":"条件状语从句X7","email":"ghnugyu@gy.com","avatar":"http://media.lalocal.cn/2016111518190711994878085929?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","avatarOrigin":"http://media.lalocal.cn/2016111518190711994878085929","sortValue":6702}
     * targetUser : null
     * content : 1111
     * dateTime : 2016-11-29
     * status : 1
     * sort : 0
     * partentComment : null
     */

    private int id;
    private LiveUserBean user;
    private LiveUserBean targetUser;
    private String content;
    private String dateTime;
    private int status;
    private int sort;
    private CommentRowBean partentComment;

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

    public CommentRowBean getPartentComment() {
        return partentComment;
    }

    public void setPartentComment(CommentRowBean partentComment) {
        this.partentComment = partentComment;
    }

//    public static class UserBean {
//        /**
//         * id : 9826
//         * nickName : 条件状语从句X7
//         * email : ghnugyu@gy.com
//         * avatar : http://media.lalocal.cn/2016111518190711994878085929?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
//         * description :
//         * avatarOrigin : http://media.lalocal.cn/2016111518190711994878085929
//         * sortValue : 6702
//         */
//
//        private int id;
//        private String nickName;
//        private String email;
//        private String avatar;
//        private String description;
//        private String avatarOrigin;
//        private int sortValue;
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getNickName() {
//            return nickName;
//        }
//
//        public void setNickName(String nickName) {
//            this.nickName = nickName;
//        }
//
//        public String getEmail() {
//            return email;
//        }
//
//        public void setEmail(String email) {
//            this.email = email;
//        }
//
//        public String getAvatar() {
//            return avatar;
//        }
//
//        public void setAvatar(String avatar) {
//            this.avatar = avatar;
//        }
//
//        public String getDescription() {
//            return description;
//        }
//
//        public void setDescription(String description) {
//            this.description = description;
//        }
//
//        public String getAvatarOrigin() {
//            return avatarOrigin;
//        }
//
//        public void setAvatarOrigin(String avatarOrigin) {
//            this.avatarOrigin = avatarOrigin;
//        }
//
//        public int getSortValue() {
//            return sortValue;
//        }
//
//        public void setSortValue(int sortValue) {
//            this.sortValue = sortValue;
//        }
//    }
}
