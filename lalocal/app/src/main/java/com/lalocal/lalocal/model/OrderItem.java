package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/24.
 */
public class OrderItem {


    /**
     * id : 54
     * orderNumb : 1463969762663
     * status : 0
     * fee : 374.0
     * originFee : 374.0
     * name : ⽇本东京迪⼠尼度假区⼀⽇门票
     * photo : http://7xpid3.com1.z0.glb.clouddn.com/201602151846301491192720928
     * orderPayList : [{"name":"长者（65~99岁）","unit":374,"amount":1}]
     */

    private int id;
    private String orderNumb;
    private int status;
    private double fee;
    private double originFee;
    private String name;
    private String photo;
    /**
     * name : 长者（65~99岁）
     * unit : 374.0
     * amount : 1
     */

    private List<OrderPay> orderPayList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderNumb() {
        return orderNumb;
    }

    public void setOrderNumb(String orderNumb) {
        this.orderNumb = orderNumb;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getOriginFee() {
        return originFee;
    }

    public void setOriginFee(double originFee) {
        this.originFee = originFee;
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

    public List<OrderPay> getOrderPayList() {
        return orderPayList;
    }

    public void setOrderPayList(List<OrderPay> orderPayList) {
        this.orderPayList = orderPayList;
    }

    public static class OrderPay {
        private String name;
        private double unit;
        private int amount;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getUnit() {
            return unit;
        }

        public void setUnit(double unit) {
            this.unit = unit;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
