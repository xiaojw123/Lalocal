package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by android on 2016/7/14.
 */
public class VersionResult {


    /**
     * forceFlag : true
     * apiUrl : http://api.lalocal.cn/api
     * checkUpdate : true
     * downloadUrl : http://media.lalocal.cn/app/lalocal_2_1_0.apk
     * msg : ["测试,第二条"]
     */

    private boolean forceFlag;
    private String apiUrl;
    private boolean checkUpdate;
    private String downloadUrl;
    private List<String> msg;

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

    public boolean isCheckUpdate() {
        return checkUpdate;
    }

    public void setCheckUpdate(boolean checkUpdate) {
        this.checkUpdate = checkUpdate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public List<String> getMsg() {
        return msg;
    }

    public void setMsg(List<String> msg) {
        this.msg = msg;
    }
}
