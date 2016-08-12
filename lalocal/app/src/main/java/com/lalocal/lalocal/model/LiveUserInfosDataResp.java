package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/8/5.
 */
public class LiveUserInfosDataResp {

    private int returnCode;
    private String message;
    private long date;
    private LiveUserInfoResultBean result;
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

    public LiveUserInfoResultBean getResult() {
        return result;
    }

    public void setResult(LiveUserInfoResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }



}
