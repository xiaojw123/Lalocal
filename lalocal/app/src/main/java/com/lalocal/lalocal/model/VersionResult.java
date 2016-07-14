package com.lalocal.lalocal.model;

/**
 * Created by android on 2016/7/14.
 */
public class VersionResult {
    private boolean forceFlag;
    private String apiUrl;

    public boolean isForceFlag() {
        return forceFlag;
    }

    public void setForceFlag(boolean forceFlag) {
        this.forceFlag = forceFlag;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
