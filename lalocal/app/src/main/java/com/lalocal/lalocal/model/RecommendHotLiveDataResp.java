package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/13.
 */
public class RecommendHotLiveDataResp {

    private String title;
    private String subTitle;
    private List<LiveRowsBean> hotLiveList;

    public RecommendHotLiveDataResp() {

    }

    public RecommendHotLiveDataResp(String title, String subTitle, List<LiveRowsBean> hotLiveList) {
        this.title = title;
        this.subTitle = subTitle;
        this.hotLiveList = hotLiveList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<LiveRowsBean> getHotLiveList() {
        return hotLiveList;
    }

    public void setHotLiveList(List<LiveRowsBean> hotLiveList) {
        this.hotLiveList = hotLiveList;
    }
}
