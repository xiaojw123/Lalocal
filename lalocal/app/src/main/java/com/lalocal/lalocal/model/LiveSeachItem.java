package com.lalocal.lalocal.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by xiaojw on 2016/9/9.
 */
public class LiveSeachItem implements Parcelable {
    private int pageNumber;
    private int totalPages;
    private int pageSize;
    private int totalRows;

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
        private String title;
        private String photo;
        /**
         * id : 9140
         * nickName : hhdjfjjfjjffj
         * email : 158764976515@qq.com
         * avatar : http://7xpid3.com1.z0.glb.clouddn.com/2016051320515814900627825168?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
         * description : null
         * avatarOrigin : http://7xpid3.com1.z0.glb.clouddn.com/2016051320515814900627825168
         */

        private UserBean user;
        private int onlineUser;
        private int status;
        private int type;
        private int style;
        private String cid;
        private String cname;

        public String getCname() {
            return cname;
        }

        public void setCname(String cname) {
            this.cname = cname;
        }

        private String pushUrl;
        private String pullUrl;
        private String hlsPullUrl;
        private Object annoucement;
        /**
         * url : https://dev.lalocal.cn/wechat/channel?id=128
         * title : 我美我帅我出门啊，你丑你宅家里蹲;我正在乐可旅行直播，快来围观呀~
         * desc : hhdjfjjfjjffj
         * img : http://7xpid3.com1.z0.glb.clouddn.com/201609081149556910886548284?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
         * type : 3
         */

        private ShareVOBean shareVO;
        private int roomId;
        private String createrAccId;
        private String address;
        private int number;
        private boolean challengeStatus;

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

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
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

        public ShareVOBean getShareVO() {
            return shareVO;
        }

        public void setShareVO(ShareVOBean shareVO) {
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String  address) {
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

        public static class UserBean implements Parcelable {
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

            public Object getDescription() {
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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.nickName);
                dest.writeString(this.email);
                dest.writeString(this.avatar);
                dest.writeString(this.description);
                dest.writeString(this.avatarOrigin);
            }

            public UserBean() {
            }

            protected UserBean(Parcel in) {
                this.id = in.readInt();
                this.nickName = in.readString();
                this.email = in.readString();
                this.avatar = in.readString();
                this.description = in.readString();
                this.avatarOrigin = in.readString();
            }

            public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
                @Override
                public UserBean createFromParcel(Parcel source) {
                    return new UserBean(source);
                }

                @Override
                public UserBean[] newArray(int size) {
                    return new UserBean[size];
                }
            };
        }

        public static class ShareVOBean implements Parcelable {
            private String url;
            private String title;
            private String desc;
            private String img;
            private int type;

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.url);
                dest.writeString(this.title);
                dest.writeString(this.desc);
                dest.writeString(this.img);
                dest.writeInt(this.type);
            }

            public ShareVOBean() {
            }

            protected ShareVOBean(Parcel in) {
                this.url = in.readString();
                this.title = in.readString();
                this.desc = in.readString();
                this.img = in.readString();
                this.type = in.readInt();
            }

            public static final Parcelable.Creator<ShareVOBean> CREATOR = new Parcelable.Creator<ShareVOBean>() {
                @Override
                public ShareVOBean createFromParcel(Parcel source) {
                    return new ShareVOBean(source);
                }

                @Override
                public ShareVOBean[] newArray(int size) {
                    return new ShareVOBean[size];
                }
            };
        }

        public RowsBean() {
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
        }

        protected RowsBean(Parcel in) {
            this.id = in.readInt();
            this.title = in.readString();
            this.photo = in.readString();
            this.user = in.readParcelable(UserBean.class.getClassLoader());
            this.onlineUser = in.readInt();
            this.status = in.readInt();
            this.type = in.readInt();
            this.style = in.readInt();
            this.cid = in.readString();
            this.cname = in.readString();
            this.pushUrl = in.readString();
            this.pullUrl = in.readString();
            this.hlsPullUrl = in.readString();
         //   this.annoucement = in.readParcelable(Object.class.getClassLoader());
            this.shareVO = in.readParcelable(ShareVOBean.class.getClassLoader());
            this.roomId = in.readInt();
            this.createrAccId = in.readString();
            this.address = in.readString();
            this.number = in.readInt();
            this.challengeStatus = in.readByte() != 0;
        }

        public static final Creator<RowsBean> CREATOR = new Creator<RowsBean>() {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.pageNumber);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.pageSize);
        dest.writeInt(this.totalRows);
        dest.writeTypedList(this.rows);
    }

    public LiveSeachItem() {
    }

    protected LiveSeachItem(Parcel in) {
        this.pageNumber = in.readInt();
        this.totalPages = in.readInt();
        this.pageSize = in.readInt();
        this.totalRows = in.readInt();
        this.rows = in.createTypedArrayList(RowsBean.CREATOR);
    }

    public static final Parcelable.Creator<LiveSeachItem> CREATOR = new Parcelable.Creator<LiveSeachItem>() {
        @Override
        public LiveSeachItem createFromParcel(Parcel source) {
            return new LiveSeachItem(source);
        }

        @Override
        public LiveSeachItem[] newArray(int size) {
            return new LiveSeachItem[size];
        }
    };
}
