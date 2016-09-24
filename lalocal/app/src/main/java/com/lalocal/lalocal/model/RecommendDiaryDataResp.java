package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/13.
 */
public class RecommendDiaryDataResp {

    private String title;
    private String subTitle;
    private List<RecommendDiaryBean> diaryList;

    public RecommendDiaryDataResp() {
    }

    public RecommendDiaryDataResp(String title, String subTitle, List<RecommendDiaryBean> diaryList) {
        this.title = title;
        this.subTitle = subTitle;
        this.diaryList = diaryList;
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

    public List<RecommendDiaryBean> getDiaryList() {
        return diaryList;
    }

    public void setDiaryList(List<RecommendDiaryBean> diaryList) {
        this.diaryList = diaryList;
    }
}
