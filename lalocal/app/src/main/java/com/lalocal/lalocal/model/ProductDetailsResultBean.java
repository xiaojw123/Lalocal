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
    public List<ProductDetailsBean> details;
    public List<?> photoVOs;
}
