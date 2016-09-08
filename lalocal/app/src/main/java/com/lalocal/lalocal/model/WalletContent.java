package com.lalocal.lalocal.model;

/**
 * Created by xiaojw on 2016/9/2.
 */
public class WalletContent {

    /**
     * gold : 10
     * score : 17
     * signInFlag : false
     * couponNumb : 0
     * preExchangeGold : 0
     * scale : 22
     * firstMsg : 首次充值可获得额外40乐钻
     */

    private long gold;
    private long score;
    private boolean signInFlag;
    private int couponNumb;
    private int preExchangeGold;
    private int scale;
    private String firstMsg;

    public long getGold() {
        return gold;
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public boolean isSignInFlag() {
        return signInFlag;
    }

    public void setSignInFlag(boolean signInFlag) {
        this.signInFlag = signInFlag;
    }

    public int getCouponNumb() {
        return couponNumb;
    }

    public void setCouponNumb(int couponNumb) {
        this.couponNumb = couponNumb;
    }

    public int getPreExchangeGold() {
        return preExchangeGold;
    }

    public void setPreExchangeGold(int preExchangeGold) {
        this.preExchangeGold = preExchangeGold;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFirstMsg() {
        return firstMsg;
    }

    public void setFirstMsg(String firstMsg) {
        this.firstMsg = firstMsg;
    }
}
