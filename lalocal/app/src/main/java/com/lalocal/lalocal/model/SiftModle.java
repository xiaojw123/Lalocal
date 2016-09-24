package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/7/22.
 */
public class SiftModle {

    /**
     * id : 6
     * name : 景点门票
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/collections/ticket@3x.png
     * photoPre : http://7xpid3.com1.z0.glb.clouddn.com/collections/ticket@3x.png
     */

    private int id;
    private String name;
    private String photo;
    private String photoPre;

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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhotoPre() {
        return photoPre;
    }

    public void setPhotoPre(String photoPre) {
        this.photoPre = photoPre;
    }
}

