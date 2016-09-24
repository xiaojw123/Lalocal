package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/7/14.
 */
public class VersionInfo {
    private int returnCode;
    private String message;
    private long date;
    private VersionResult result;
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

    public VersionResult getResult() {
        return result;
    }

    public void setResult(VersionResult result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }
}
