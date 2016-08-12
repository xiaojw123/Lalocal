package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by android on 2016/7/19.
 */
public class LiveListDataResp {
    private int returnCode;
    private String message;
    private long date;
    private LiveListResultBean result;
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

    public LiveListResultBean getResult() {
        return result;
    }

    public void setResult(LiveListResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

}
