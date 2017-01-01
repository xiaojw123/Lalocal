package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/18.
 */
public class AreaItem {


    public String getYitu8Url() {
        return yitu8Url;
    }

    public void setYitu8Url(String yitu8Url) {
        this.yitu8Url = yitu8Url;
    }

    /**
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/201604150930291471703259569
     * name : 日韩
     * status : 0
     * id : 0
     * sonList : []
     */

    private String yitu8Url;
    private String photo;
    private String name;
    private int status;
    private int id;
    private List<?> sonList;

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<?> getSonList() {
        return sonList;
    }

    public void setSonList(List<?> sonList) {
        this.sonList = sonList;
    }
}
