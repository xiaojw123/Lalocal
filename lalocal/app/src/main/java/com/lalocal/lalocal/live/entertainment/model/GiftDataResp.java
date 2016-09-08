package com.lalocal.lalocal.live.entertainment.model;

import java.util.List;

/**
 * Created by android on 2016/9/1.
 */
public class GiftDataResp {

    private int returnCode;
    private String message;
    private long date;
    private Object errorCode;
    private List<GiftDataResultBean> result;

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

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

    public List<GiftDataResultBean> getResult() {
        return result;
    }

    public void setResult(List<GiftDataResultBean> result) {
        this.result = result;
    }


}
