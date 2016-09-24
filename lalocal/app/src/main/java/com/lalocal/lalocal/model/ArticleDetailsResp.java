package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/7/11.
 */
public class ArticleDetailsResp {
    private int returnCode;
    private String message;
    private long date;
    private ArticleDetailsResultBean result;
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

    public ArticleDetailsResultBean getResult() {
        return result;
    }

    public void setResult(ArticleDetailsResultBean result) {
        this.result = result;
    }

    public Object getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Object errorCode) {
        this.errorCode = errorCode;
    }

}
