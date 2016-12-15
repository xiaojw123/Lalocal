package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by android on 2016/10/4.
 */
public class ChallengeInitiateDataResp {
    private int returnCode;
    private String message;
    private long date;
    private ChallengeInitiateResultBean result;
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

    public ChallengeInitiateResultBean getResult() {
        return result;
    }

    public void setResult(ChallengeInitiateResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }


}
