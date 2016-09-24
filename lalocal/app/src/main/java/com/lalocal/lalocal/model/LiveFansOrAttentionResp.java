package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/8/7.
 */
public class LiveFansOrAttentionResp {
    private int returnCode;
    private String message;
    private long date;
    private LiveFansOrAttentionResultBean result;
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

    public LiveFansOrAttentionResultBean getResult() {
        return result;
    }

    public void setResult(LiveFansOrAttentionResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

}
