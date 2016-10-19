package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/7/19.
 */
public class LiveRowsBean implements Comparable<LiveRowsBean>,Parcelable {
    private int id;
    private String title;
    private String photo;
    private LiveUserBean user;
    private int onlineUser;
    private int status;
    private int type;
    private int style;
    private String cid;
    private String cname;
    private String pushUrl;
    private String pullUrl;
    private String hlsPullUrl;
    private Object annoucement;
    private SpecialShareVOBean shareVO;
    private int roomId;
    private String createrAccId;
    private String address;
    private int number;
    private boolean challengeStatus;
    private String startAt;
    private String endAt;
    private int totalScore;
    private int onlineNumber;
    private List<VideoListBean> videoList;
    private int direction;
    private int channelId;

    public String getLiveLen() {
        return liveLen;
    }

    public void setLiveLen(String liveLen) {
        this.liveLen = liveLen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String liveLen;
    private String date;

    public List<VideoListBean> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<VideoListBean> videoList) {
        this.videoList = videoList;
    }



    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
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





    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isChallengeStatus() {
        return challengeStatus;
    }

    public void setChallengeStatus(boolean challengeStatus) {
        this.challengeStatus = challengeStatus;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public LiveUserBean getUser() {
        return user;
    }

    public void setUser(LiveUserBean user) {
        this.user = user;
    }

    public int getOnlineUser() {
        return onlineUser;
    }

    public void setOnlineUser(int onlineUser) {
        this.onlineUser = onlineUser;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getPushUrl() {
        return pushUrl;
    }

    public void setPushUrl(String pushUrl) {
        this.pushUrl = pushUrl;
    }

    public String getPullUrl() {
        return pullUrl;
    }

    public void setPullUrl(String pullUrl) {
        this.pullUrl = pullUrl;
    }

    public String getHlsPullUrl() {
        return hlsPullUrl;
    }

    public void setHlsPullUrl(String hlsPullUrl) {
        this.hlsPullUrl = hlsPullUrl;
    }

    public Object getAnnoucement() {
        return annoucement;
    }

    public void setAnnoucement(Object annoucement) {
        this.annoucement = annoucement;
    }

    public SpecialShareVOBean getShareVO() {
        return shareVO;
    }

    public void setShareVO(SpecialShareVOBean shareVO) {
        this.shareVO = shareVO;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getCreaterAccId() {
        return createrAccId;
    }

    public void setCreaterAccId(String createrAccId) {
        this.createrAccId = createrAccId;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
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

    @Override
    public int compareTo(LiveRowsBean another) {
        return another.getOnlineUser() - this.getOnlineUser();
    }

    public LiveRowsBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.photo);
        dest.writeParcelable(this.user, flags);
        dest.writeInt(this.onlineUser);
        dest.writeInt(this.status);
        dest.writeInt(this.type);
        dest.writeInt(this.style);
        dest.writeString(this.cid);
        dest.writeString(this.cname);
        dest.writeString(this.pushUrl);
        dest.writeString(this.pullUrl);
        dest.writeString(this.hlsPullUrl);

        dest.writeParcelable(this.shareVO, flags);
        dest.writeInt(this.roomId);
        dest.writeString(this.createrAccId);
        dest.writeString(this.address);
        dest.writeInt(this.number);
        dest.writeByte(this.challengeStatus ? (byte) 1 : (byte) 0);
        dest.writeString(this.startAt);
        dest.writeString(this.endAt);
        dest.writeInt(this.totalScore);
        dest.writeInt(this.onlineNumber);
        dest.writeList(this.videoList);
        dest.writeInt(this.direction);
        dest.writeInt(this.channelId);
        dest.writeString(this.liveLen);
        dest.writeString(this.date);
    }

    protected LiveRowsBean(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.photo = in.readString();
        this.user = in.readParcelable(LiveUserBean.class.getClassLoader());
        this.onlineUser = in.readInt();
        this.status = in.readInt();
        this.type = in.readInt();
        this.style = in.readInt();
        this.cid = in.readString();
        this.cname = in.readString();
        this.pushUrl = in.readString();
        this.pullUrl = in.readString();
        this.hlsPullUrl = in.readString();

        this.shareVO = in.readParcelable(SpecialShareVOBean.class.getClassLoader());
        this.roomId = in.readInt();
        this.createrAccId = in.readString();
        this.address = in.readString();
        this.number = in.readInt();
        this.challengeStatus = in.readByte() != 0;
        this.startAt = in.readString();
        this.endAt = in.readString();
        this.totalScore = in.readInt();
        this.onlineNumber = in.readInt();
        this.videoList = new ArrayList<VideoListBean>();
        in.readList(this.videoList, VideoListBean.class.getClassLoader());
        this.direction = in.readInt();
        this.channelId = in.readInt();
        this.liveLen = in.readString();
        this.date = in.readString();
    }

    public static final Creator<LiveRowsBean> CREATOR = new Creator<LiveRowsBean>() {
        @Override
        public LiveRowsBean createFromParcel(Parcel source) {
            return new LiveRowsBean(source);
        }

        @Override
        public LiveRowsBean[] newArray(int size) {
            return new LiveRowsBean[size];
        }
    };
}
