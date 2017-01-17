package com.lalocal.lalocal.live.entertainment.model;

/**
 * Created by ${WCJ} on 2017/1/11.
 */
public class ShortVideoTokenBean {
    private String filename;
    private String token;
    private String url;
    private int limitSize;

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLimitSize() {
        return limitSize;
    }

    public void setLimitSize(int limitSize) {
        this.limitSize = limitSize;
    }
}
