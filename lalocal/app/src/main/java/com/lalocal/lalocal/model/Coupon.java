package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/6/23.
 */
public class Coupon {

    /**
     * id : 132
     * name : 发给作者第二批
     * expiredDateStr : 2016-08-10
     * discount : 500.0
     * status : 1
     * statusName : 已使用
     * minFee : null
     * type : 1
     */

    private int id;
    private String name;
    private String expiredDateStr;
    private double discount;
    private int status;
    private String statusName;
    private String minFee;
    private int type;//优惠券类型,0普通优惠券 1作者优惠券

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

    public String getMinFee() {
        return minFee;
    }

    public void setMinFee(String minFee) {
        this.minFee = minFee;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
