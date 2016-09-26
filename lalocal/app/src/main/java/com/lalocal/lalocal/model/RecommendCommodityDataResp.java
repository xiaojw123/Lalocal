package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/13.
 */
public class RecommendCommodityDataResp {
    private String title;
    private String subTitle;
    private List<RecommendCommodityBean> commodityList;

    public RecommendCommodityDataResp() {
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

    public List<RecommendCommodityBean> getComodityList() {
        return commodityList;
    }

    public void setComodityList(List<RecommendCommodityBean> commodityList) {
        this.commodityList = commodityList;
    }
}
