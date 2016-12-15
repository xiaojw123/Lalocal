package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/11/17.
 */

public class RecommendationsBean {

    /**
     * id : 30
     * title : 啊快来咯哦
     * targetId : 3511
     * address : LALOCAL神秘之地
     * type : 1
     * photo : http://media.lalocal.cn/2016110714263218518017807781
     * user : {"id":8746,"nickName":"杜瑶歌曲","email":"duyaoyao@mddtrip.cn","avatar":"http://media.lalocal.cn/2016072917572116882175413670?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200","description":"我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我们的确如此，我","avatarOrigin":"http://media.lalocal.cn/2016072917572116882175413670","sortValue":168644}
     */

    private int id;
    private String title;
    private int targetId;
    private String address;
    private int type;
    private String photo;
    private LiveUserBean user;

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

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
}
