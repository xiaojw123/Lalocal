package com.lalocal.lalocal.model;



/**
 * Created by lenovo on 2016/6/17.
 */
public class RecommendDataResp {
    private int returnCode;
    private String message;
    private long date;
    private RecommendResultBean result;
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
    public RecommendResultBean getResult() {
        return result;
    }

    public void setResult(RecommendResultBean result) {
        this.result = result;
    }
    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }
}
