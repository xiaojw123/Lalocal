package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/9/26.
 */

public class JsCoupon {


    /**
     * status : 0
     * minFee : 0
     * statusName : 未使用
     * discount : 8998.99
     * expiredDateStr : 2016-12-29
     * type : 1
     * name : 旺仔
     * couponId : 152
     */

    private int status;
    private int minFee;
    private String statusName;
    private double discount;
    private String expiredDateStr;
    private int type;
    private String name;
    private String couponId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getMinFee() {
        return minFee;
    }

    public void setMinFee(int minFee) {
        this.minFee = minFee;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getExpiredDateStr() {
        return expiredDateStr;
    }

    public void setExpiredDateStr(String expiredDateStr) {
        this.expiredDateStr = expiredDateStr;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }
}
