package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/10/19.
 */
public class TestData {

    /**
     * id : 1643
     * startAt : 2016-10-18 13:58:44
     * endAt : 2016-10-18 14:18:16
     * totalScore : 18
     * onlineNumber : 846
     * direction : 0
     * photo : http://media.lalocal.cn/2016090723133116877528144
     * title : 很长别哭KKKKKK不ill1爱HK啦啦
     * channelId : 24
     * number : 2
     * user : {"id":8753,"nickName":"李响2","email":"lixiang@mddtrip.cn","avatar":"http://media.lalocal.cn/2016090723133116877528144?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":null,"avatarOrigin":"http://media.lalocal.cn/2016090723133116877528144"}
     * address : 乐可奇妙之旅
     * videoList : [{"id":48,"size":143216751,"duration":1122.21,"startTime":"2016-10-18 13:59:30","endTime":"2016-10-18 14:18:47","url":"http://vid-11812.vod.chinanetcenter.broadcastapp.agoraio.cn/live-dev-24-2--20161018135930.mp4"}]
     * shareVO : {"url":"https://dev.lalocal.cn/wechatrecording?id=1643","title":"","desc":"李响2正在乐可旅行直播很长别哭KKKKKK不ill1爱HK啦啦","img":"http://media.lalocal.cn/icon.jpg?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","type":3,"targetType":20,"targetId":1643}
     */

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
    /**
     * id : 8753
     * nickName : 李响2
     * email : lixiang@mddtrip.cn
     * avatar : http://media.lalocal.cn/2016090723133116877528144?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description : null
     * avatarOrigin : http://media.lalocal.cn/2016090723133116877528144
     */

    private UserBean user;
    private String address;
    /**
     * url : https://dev.lalocal.cn/wechatrecording?id=1643
     * title :
     * desc : 李响2正在乐可旅行直播很长别哭KKKKKK不ill1爱HK啦啦
     * img : http://media.lalocal.cn/icon.jpg?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * type : 3
     * targetType : 20
     * targetId : 1643
     */

    private ShareVOBean shareVO;
    /**
     * id : 48
     * size : 143216751
     * duration : 1122.21
     * startTime : 2016-10-18 13:59:30
     * endTime : 2016-10-18 14:18:47
     * url : http://vid-11812.vod.chinanetcenter.broadcastapp.agoraio.cn/live-dev-24-2--20161018135930.mp4
     */

    private List<VideoListBean> videoList;

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

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ShareVOBean getShareVO() {
        return shareVO;
    }

    public void setShareVO(ShareVOBean shareVO) {
        this.shareVO = shareVO;
    }

    public List<VideoListBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListBean> videoList) {
        this.videoList = videoList;
    }

    public static class UserBean {
        private int id;
        private String nickName;
        private String email;
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

    public static class VideoListBean {
        private int id;
        private int size;
        private double duration;
        private String startTime;
        private String endTime;
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
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
