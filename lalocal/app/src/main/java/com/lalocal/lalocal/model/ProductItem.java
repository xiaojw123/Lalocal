package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/7/21.
 */
public class ProductItem extends SearchItem{

    /**
     * id : 4
     * title : 日本东京：东京铁塔、皇居前广场、浅草寺
     * description : 登上东京铁塔展望台，感受东京的繁华，体验日本传统茶道，感受日本传统文化。
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/2016021716582414059636448375
     * price : 484.0
     * status : 0
     * publishAt : 1457582114000
     * type : 1
     */

    private String description;
    private double price;
    private int status;
    private long publishAt;
    private int type;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getPublishAt() {
        return publishAt;
    }

    public void setPublishAt(long publishAt) {
        this.publishAt = publishAt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
