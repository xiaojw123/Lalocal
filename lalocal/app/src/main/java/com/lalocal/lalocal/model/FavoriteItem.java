package com.lalocal.lalocal.model;

import android.text.TextUtils;

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

    public void setReadNum(double readNum) {
        this.readNum = readNum;
    }

    public double getReadNum() {
        return readNum;

    }

    private String photo;
    private Author author;
    private double price;
    private String commentNum;
    private String praiseNum;
    private double readNum;
    private int status;
    private String address;
    private String startAt;

    public String getAddress() {
        if (TextUtils.isEmpty(address)) {
            return "乐可奇妙之旅";
        }
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStartAt() {
        return startAt;
    }

    public void setStartAt(String startAt) {
        this.startAt = startAt;
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
