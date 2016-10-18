package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/6/20.
 */
public class FavoriteItem {


    /**
     * targetType : 2
     * targetId : 267
     * targetName : 奥兰多迪士尼世界单乐园一日票（含迈阿密出发交通）
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/2016050316064915549796599206
     * author : null
     * price : 1258
     * commentNum : null
     * praiseNum : null
     * status : 0
     */

    private int targetType;
    private int targetId;
    private String targetName;
    private String subTitle;

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public void setReadNum(String readNum) {
        this.readNum = readNum;
    }

    public String getReadNum() {
        return readNum;

    }

    private String photo;
    private Author author;
    private double price;
    private String commentNum;
    private String praiseNum;
    private String readNum;
    private int status;

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

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(String commentNum) {
        this.commentNum = commentNum;
    }

    public String getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(String praiseNum) {
        this.praiseNum = praiseNum;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
