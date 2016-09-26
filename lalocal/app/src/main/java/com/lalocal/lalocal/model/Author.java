package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/6/21.
 */
public class Author {

    /**
     * authorId : 19
     * authorName : 木头人
     * authorAvatar : http://7xpid3.com1.z0.glb.clouddn.com/2016051320515814900627825168?imageMogr2/auto-orient/strip/thumbnail/!200x200r/gravity/Center/crop/200x200
     * description :
     * qrCode : null
     * publicTitle : null
     * publicDescription : null
     */

    private int authorId;
    private String authorName;
    private String authorAvatar;
    private String description;
    private String publicDescription;

    private String qrCode;
    private String publicTitle;

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

    public Object getPublicDescription() {
        return publicDescription;
    }

    public void setPublicDescription(String publicDescription) {
        this.publicDescription = publicDescription;
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
}
