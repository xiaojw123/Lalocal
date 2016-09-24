package com.lalocal.lalocal.model;

import java.util.List;

/**
 * Created by lenovo on 2016/6/17.
 */
public class SpectialDataResultBean {
    public int id;
    public String name;
    public int type;
    public String publishAt;
    public String photo;
    public SpecialAuthorBean author;
    public String description;
    public SpecialBannerBean banner;
    public String url;
    public SpecialShareVOBean shareVO;
    public boolean praiseFlag;
    public Object praiseId;
    public List<SpecialGroupsBean> groups;
}
