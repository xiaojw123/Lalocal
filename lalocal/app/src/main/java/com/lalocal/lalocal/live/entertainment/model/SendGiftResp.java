package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/9/11.
 */
public class SendGiftResp {

    private int returnCode;
    private String message;
    private long date;
    private ResultBean result;
    private Object errorCode;

    public int getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(int returnCode) {
        this.returnCode = returnCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public static class ResultBean {
        private int gold;
        private int score;
        private boolean signInFlag;
        private Object couponNumb;
        private Object preExchangeGold;
        private int scale;
        private double cashScale;
        private String firstMsg;

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public boolean isSignInFlag() {
            return signInFlag;
        }

        public void setSignInFlag(boolean signInFlag) {
            this.signInFlag = signInFlag;
        }

        public Object getCouponNumb() {
            return couponNumb;
        }

        public void setCouponNumb(Object couponNumb) {
            this.couponNumb = couponNumb;
        }

        public Object getPreExchangeGold() {
            return preExchangeGold;
        }

        public void setPreExchangeGold(Object preExchangeGold) {
            this.preExchangeGold = preExchangeGold;
        }

        public int getScale() {
            return scale;
        }

        public void setScale(int scale) {
            this.scale = scale;
        }

        public double getCashScale() {
            return cashScale;
        }

        public void setCashScale(double cashScale) {
            this.cashScale = cashScale;
        }

        public String getFirstMsg() {
            return firstMsg;
        }

        public void setFirstMsg(String firstMsg) {
            this.firstMsg = firstMsg;
        }
    }
}
