package com.lalocal.lalocal.live.entertainment.model;

import com.lalocal.lalocal.model.LiveRowsBean;

import java.util.List;

/**
 * Created by android on 2016/10/10.
 */
public class LiveHomeListResp {
    private int returnCode;
    private String message;
    private long date;
    private Object errorCode;

    public List<LiveRowsBean> getResult() {
        return result;
    }

    public void setResult(List<LiveRowsBean> result) {
        this.result = result;
    }

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

    private List<LiveRowsBean> result;

}
