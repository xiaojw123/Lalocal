package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class SearchItem {
    private int id;
    private String title;
    private String photo;
    private int modeltype;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getModeltype() {
        return modeltype;
    }

    public void setModeltype(int modeltype) {
        this.modeltype = modeltype;
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

}
