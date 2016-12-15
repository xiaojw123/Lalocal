package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/13.
 */
public class RecommendSpecialDataResp {
    private String title;
    private String subTitle;
    private List<RecommendSpecialBean> specialList;

    public RecommendSpecialDataResp() {
    }

    public RecommendSpecialDataResp(String title, String subTitle, List<RecommendSpecialBean> specialList) {
        this.title = title;
        this.subTitle = subTitle;
        this.specialList = specialList;
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

    public List<RecommendSpecialBean> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(List<RecommendSpecialBean> specialList) {
        this.specialList = specialList;
    }
}
