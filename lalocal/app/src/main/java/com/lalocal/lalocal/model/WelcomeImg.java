package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by xiaojw on 2016/8/15.
 */
public class WelcomeImg {


    /**
     * photo : http://media.lalocal.cn/201607291456253206099113538
     * second : 2
     * futureList : ["http://media.lalocal.cn/2016072914532819248253343696","http://media.lalocal.cn/2016072114535115018194145849","http://media.lalocal.cn/201607291447209374581697889","http://media.lalocal.cn/201607291448433722573879758","http://media.lalocal.cn/2016072914515011212251928287"]
     */

    private String photo;
    private int second;
    private List<String> futureList;
    private String targetUrl;
    private String targetType;
    private String targetId;
    private String targetName;

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public List<String> getFutureList() {
        return futureList;
    }

    public void setFutureList(List<String> futureList) {
        this.futureList = futureList;
    }
}
