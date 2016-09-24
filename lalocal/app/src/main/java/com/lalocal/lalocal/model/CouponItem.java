package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/9/13.
 */
public class CouponItem {


    /**
     * id : 97
     * name : 我有达⼈优惠券
     * expiredDateStr : 2016-11-14
     * discount : 500.0
     * status : 0
     * statusName : 未使⽤
     * minFee : 0.0
     * type : 1
     */

    private int id;
    private String name;
    private String expiredDateStr;
    private double discount;
    private int status;
    private String statusName;
    private double minFee;
    private int type;

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

    public String getExpiredDateStr() {
        return expiredDateStr;
    }

    public void setExpiredDateStr(String expiredDateStr) {
        this.expiredDateStr = expiredDateStr;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public double getMinFee() {
        return minFee;
    }

    public void setMinFee(double minFee) {
        this.minFee = minFee;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
