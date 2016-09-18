package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/7/19.
 */
public class LiveRowsBean implements Comparable<LiveRowsBean> {
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

    @Override
    public int compareTo(LiveRowsBean another) {
        return another.getOnlineUser() - this.getOnlineUser();
    }
}
