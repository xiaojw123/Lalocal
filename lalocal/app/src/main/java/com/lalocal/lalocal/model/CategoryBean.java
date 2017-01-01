package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/12/5.
 */

public class CategoryBean {

    /**
     * id : 1
     * name : 美食
     * publishFlag : true
     * photo : http://media.lalocal.cn/2016021716275916280553191126
     * prePhoto : null
     * sort : 1
     */

    private int id;
    private String name;
    private boolean publishFlag;
    private String photo;
    private String prePhoto;
    private int sort;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPublishFlag() {
        return publishFlag;
    }

    public void setPublishFlag(boolean publishFlag) {
        this.publishFlag = publishFlag;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPrePhoto() {
        return prePhoto;
    }

    public void setPrePhoto(String prePhoto) {
        this.prePhoto = prePhoto;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
