package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by android on 2016/7/19.
 */
public class LiveRecommendListDataResp {
    private int returnCode;
    private String message;
    private long date;
    private Object errorCode;
    private List<LiveRowsBean> result;
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

    public List<LiveRowsBean> getResult() {
        return result;
    }

    public void setResult(List<LiveRowsBean> result) {
        this.result = result;
    }
}
