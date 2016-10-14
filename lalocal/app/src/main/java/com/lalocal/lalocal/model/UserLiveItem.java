package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xiaojw on 2016/10/12.
 */

public class UserLiveItem {

    /**
     * pageNumber : 1
     * totalPages : 1
     * pageSize : 10
     * totalRows : 2
     * rows : [{"id":1413,"startAt":"2016-10-10 18:29:28","endAt":"2016-10-10 18:30:51","totalScore":0,"onlineNumber":84,"videoUrl":"http://video.lalocal.cn/video/flv/201609281845.flv","direction":1,"photo":"http://7xpid3.com1.z0.glb.clouddn.com/2016092819221810034810166788","title":null,"channelId":196,"number":27,"user":{"id":9368,"nickName":"g郭黑化股份","email":"liuyang@mddtrip.cn","avatar":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","avatarOrigin":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933"},"address":null},{"id":1412,"startAt":"2016-10-10 18:27:50","endAt":"2016-10-10 18:29:23","totalScore":0,"onlineNumber":36,"videoUrl":"http://video.lalocal.cn/video/flv/201609281845.flv","direction":1,"photo":"http://7xpid3.com1.z0.glb.clouddn.com/2016092819221810034810166788","title":null,"channelId":196,"number":26,"user":{"id":9368,"nickName":"g郭黑化股份","email":"liuyang@mddtrip.cn","avatar":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","avatarOrigin":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933"},"address":null}]
     */

    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private int totalRows;
    /**
     * id : 1413
     * startAt : 2016-10-10 18:29:28
     * endAt : 2016-10-10 18:30:51
     * totalScore : 0
     * onlineNumber : 84
     * videoUrl : http://video.lalocal.cn/video/flv/201609281845.flv
     * direction : 1
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/2016092819221810034810166788
     * title : null
     * channelId : 196
     * number : 27
     * user : {"id":9368,"nickName":"g郭黑化股份","email":"liuyang@mddtrip.cn","avatar":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"","avatarOrigin":"http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933"}
     * address : null
     */

    private List<RowsBean> rows;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(int totalRows) {
        this.totalRows = totalRows;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean implements Parcelable {
        private int id;
        private String startAt;
        private String endAt;
        private String totalScore;
        private String onlineNumber;
        private String videoUrl;
        private int direction;
        private String photo;
        private String title;
        private int channelId;
        private int number;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        private String liveLen;
        private String date;

        public String getLiveLen() {
            return liveLen;
        }

        public void setLiveLen(String liveLen) {
            this.liveLen = liveLen;
        }

        /**

         * id : 9368
         * nickName : g郭黑化股份
         * email : liuyang@mddtrip.cn
         * avatar : http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
         * description :
         * avatarOrigin : http://7xpid3.com1.z0.glb.clouddn.com/201609232056121061299679933
         */

        private UserBean user;
        private String address;

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

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public String getOnlineNumber() {
            return onlineNumber;
        }

        public void setOnlineNumber(String onlineNumber) {
            this.onlineNumber = onlineNumber;
        }

        public String getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(String videoUrl) {
            this.videoUrl = videoUrl;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.onlineNumber);
            dest.writeString(this.photo);
            dest.writeString(this.title);
            dest.writeString(this.address);
            dest.writeString(this.liveLen);
            dest.writeString(this.date);
        }

        public RowsBean() {
        }

        protected RowsBean(Parcel in) {
            this.id = in.readInt();
            this.onlineNumber = in.readString();
            this.photo = in.readString();
            this.title = in.readString();
            this.address = in.readString();
            this.liveLen=in.readString();
            this.date=in.readString();
        }

        public static final Parcelable.Creator<RowsBean> CREATOR = new Parcelable.Creator<RowsBean>() {
            @Override
            public RowsBean createFromParcel(Parcel source) {
                return new RowsBean(source);
            }

            @Override
            public RowsBean[] newArray(int size) {
                return new RowsBean[size];
            }
        };
    }
}
