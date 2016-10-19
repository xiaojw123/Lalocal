package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/10/17.
 */
public class LiveRowsDataResp {

    /**
     * returnCode : 0
     * message : success
     * date : 1476671964261
     * result : null
     * errorCode : 0
     */

    private int returnCode;
    private String message;
    private long date;
    private LiveRowsBean result;
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

    public LiveRowsBean getResult() {
        return result;
    }

    public void setResult(LiveRowsBean result) {
        this.result = result;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
