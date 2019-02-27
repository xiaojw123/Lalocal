package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by lenovo on 2016/6/22.
 */
public class ProductDetailsResultBean {
    public int id;
    public String title;
    public String description;
    public String photo;
    public double price;
    public int status;
    public long publishAt;
    public int type;
    public double longitude;
    public double latitude;
    public String address;
    public int sailNum;
    public Object score;
    public boolean praiseFlag;
    public Object praiseId;
    public String url;
    public SpecialShareVOBean shareVO;
    public Object appraiseVO;

//    "departureTime": "8am",
//            "departurePoint": "Your hotel in Seoul city",
//            "departureRemark": "Hotel pickups commence prior to this time, you must contact the local service provider to verify your exact pickup time.Times are subject to change due to local traffic conditions.",
//            "duration": "9 hours",
//            "returnDetails": "Return to your hotel",
//            "orderUrl": "https://dev.lalocal.cn/wechat/order_select?id=853",
//            "videoUrl": null
//
    public String departureTime;
    public String departurePoint;
    public String departureRemark;
    public String duration;
    public String returnDetails;
    public String orderUrl;
    public String videoUrl;
    public List<ProductDetailsBean> details;
    public List<PhotosVosBean> photoVOs;
    public String h5Url;

}
