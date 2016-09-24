package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/9/19.
 */
public class ThemeAuthorBean {

    /**
     * authorId : 2
     * authorName : 曹所以
     * authorAvatar : http://7xpid3.com1.z0.glb.clouddn.com/2016032219484115153988606115?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description : 无i圣诞节就对啦
     * qrCode :
     * publicTitle :
     * publicDescription :
     */

    private int authorId;
    private String authorName;
    private String authorAvatar;
    private String description;
    private String qrCode;
    private String publicTitle;
    private String publicDescription;

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        this.authorAvatar = authorAvatar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getPublicTitle() {
        return publicTitle;
    }

    public void setPublicTitle(String publicTitle) {
        this.publicTitle = publicTitle;
    }

    public String getPublicDescription() {
        return publicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
    }
}
