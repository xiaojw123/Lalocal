package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/9/19.
 */
public class RecommendListDataResp {

    /**
     * returnCode : 0
     * message : success
     * date : 1474252465023
     * result : null
     * errorCode : 0
     */

    private int returnCode;
    private String message;
    private long date;
    private RecommendListBean result;
    private int errorCode;

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

    public RecommendListBean getResult() {
        return result;
    }

    public void setResult(RecommendListBean result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
