package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/10/13.
 */

public class ChannelRecord {

    /**
     * id : 25
     * startAt : 2016-10-13 15:03:48
     * endAt : 2016-10-13 15:04:11
     * totalScore : 2070
     * onlineNumber : 0
     * title : null
     * channelId : 11
     * number : 59
     * giftScore : 72
     * challengeScore : 1998
     * timeScore : 0
     * giftRecords : [{"id":2,"photo":"http://media.lalocal.cn/Gift_TravellingCase@1.5x.png","numb":4},{"id":1,"photo":"http://media.lalocal.cn/Gift_Rose@1.5x.png","numb":4}]
     * challengeRecords : [{"id":6,"content":"直播吃翔2","score":1998,"time":"15:04","user":{"id":6,"nickName":"林小毛","email":"testlijian@mddtrip.cn","avatar":"http://media.lalocal.cn/201609221409188787543290701?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"这是一个好作者","avatarOrigin":"http://media.lalocal.cn/201609221409188787543290701"}}]
     */

    private int id;
    private String startAt;
    private String endAt;
    private long totalScore;
    private int onlineNumber;
    private String title;
    private int channelId;
    private int number;
    private long giftScore;
    private long challengeScore;
    private int timeScore;
    /**
     * id : 2
     * photo : http://media.lalocal.cn/Gift_TravellingCase@1.5x.png
     * numb : 4
     */

    private List<GiftRecordsBean> giftRecords;
    /**
     * id : 6
     * content : 直播吃翔2
     * score : 1998
     * time : 15:04
     * user : {"id":6,"nickName":"林小毛","email":"testlijian@mddtrip.cn","avatar":"http://media.lalocal.cn/201609221409188787543290701?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"这是一个好作者","avatarOrigin":"http://media.lalocal.cn/201609221409188787543290701"}
     */

    private List<ChallengeRecordsBean> challengeRecords;

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

    public long getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(long totalScore) {
        this.totalScore = totalScore;
    }

    public int getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(int onlineNumber) {
        this.onlineNumber = onlineNumber;
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

    public long getGiftScore() {
        return giftScore;
    }

    public void setGiftScore(long giftScore) {
        this.giftScore = giftScore;
    }

    public long getChallengeScore() {
        return challengeScore;
    }

    public void setChallengeScore(long challengeScore) {
        this.challengeScore = challengeScore;
    }

    public int getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(int timeScore) {
        this.timeScore = timeScore;
    }

    public List<GiftRecordsBean> getGiftRecords() {
        return giftRecords;
    }

    public void setGiftRecords(List<GiftRecordsBean> giftRecords) {
        this.giftRecords = giftRecords;
    }

    public List<ChallengeRecordsBean> getChallengeRecords() {
        return challengeRecords;
    }

    public void setChallengeRecords(List<ChallengeRecordsBean> challengeRecords) {
        this.challengeRecords = challengeRecords;
    }

    public static class GiftRecordsBean {
        private int id;
        private String photo;
        private String numb;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getNumb() {
            return numb;
        }

        public void setNumb(String numb) {
            this.numb = numb;
        }
    }

    public static class ChallengeRecordsBean {
        private int id;
        private String content;
        private long score;
        private String time;
        /**
         * id : 6
         * nickName : 林小毛
         * email : testlijian@mddtrip.cn
         * avatar : http://media.lalocal.cn/201609221409188787543290701?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
         * description : 这是一个好作者
         * avatarOrigin : http://media.lalocal.cn/201609221409188787543290701
         */

        private UserBean user;

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

        public long getScore() {
            return score;
        }

        public void setScore(long score) {
            this.score = score;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
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