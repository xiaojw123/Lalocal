package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by wangjie on 2016/9/19.
 */
public class RecommendListBean {

    /**
     * channelList : []
     * themeList : []
     * produList : []
     * produCN : 迷人又可爱的商品们
     * produEN : Amazing Products
     * channleCN : 不可思议的旅途现场
     * channleEN : Marvelous Travel Live
     * themeCN : 特立独行的专题世界
     * themeEN : Special Theme
     * travelCN : 一颗赛艇的旅行笔记
     * travelEN : Exciting Travel Diary
     */

    private String produCN;
    private String produEN;
    private String channleCN;
    private String channleEN;
    private String themeCN;
    private String themeEN;
    private String travelCN;
    private String travelEN;
    private List<LiveRowsBean> channelList;
    private List<RecommendRowsBean> themeList;
    private List<ProductDetailsResultBean> produList;

    public String getProduCN() {
        return produCN;
    }

    public void setProduCN(String produCN) {
        this.produCN = produCN;
    }

    public String getProduEN() {
        return produEN;
    }

    public void setProduEN(String produEN) {
        this.produEN = produEN;
    }

    public String getChannleCN() {
        return channleCN;
    }

    public void setChannleCN(String channleCN) {
        this.channleCN = channleCN;
    }

    public String getChannleEN() {
        return channleEN;
    }

    public void setChannleEN(String channleEN) {
        this.channleEN = channleEN;
    }

    public String getThemeCN() {
        return themeCN;
    }

    public void setThemeCN(String themeCN) {
        this.themeCN = themeCN;
    }

    public String getThemeEN() {
        return themeEN;
    }

    public void setThemeEN(String themeEN) {
        this.themeEN = themeEN;
    }

    public String getTravelCN() {
        return travelCN;
    }

    public void setTravelCN(String travelCN) {
        this.travelCN = travelCN;
    }

    public String getTravelEN() {
        return travelEN;
    }

    public void setTravelEN(String travelEN) {
        this.travelEN = travelEN;
    }

    public List<LiveRowsBean> getChannelList() {
        return channelList;
    }

    public void setChannelList(List<LiveRowsBean> channelList) {
        this.channelList = channelList;
    }

    public List<RecommendRowsBean> getThemeList() {
        return themeList;
    }

    public void setThemeList(List<RecommendRowsBean> themeList) {
        this.themeList = themeList;
    }

    public List<ProductDetailsResultBean> getProduList() {
        return produList;
    }

    public void setProduList(List<ProductDetailsResultBean> produList) {
        this.produList = produList;
    }
}
