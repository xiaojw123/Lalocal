package com.lalocal.lalocal.model;

/**
 * Created by wangjie on 2016/9/25.
 */
public class Constants {

    //回放
    public static final int PLAY_BACK_TYPE_URL =20;

    // url
    public static final int TARGET_TYPE_URL = -1;
    // 用户
    public static final int TARGET_TYPE_USER = 0;
    // 文章
    public static final int TARGET_TYPE_ARTICLE = 1;
    // 产品
    public static final int TARGET_TYPE_PRODUCTION = 2;
    // 订单
    public static final int TARGET_TYPE_ORDER = 3;
    // 评论
    public static final int TARGET_TYPE_COMMENT = 4;
    // 评价
    public static final int TARGET_TYPE_APPRAISE = 5;
    // 标签
    public static final int TARGET_TYPE_TAG = 6;
    // 动态
    public static final int TARGET_TYPE_DYNAMICS = 7;
    // 画报
    public static final int TARGET_TYPE_BANNER = 8;
    // 线路
    public static final int TARGET_TYPE_ROUTE = 9;
    // 专题
    public static final int TARGET_TYPE_THEME = 10;
    // 广告
    public static final int TARGET_TYPE_ADVERTISING = 11;
    // 作者首页
    public static final int TARGET_TYPE_AUTHOR = 12;
    // 资讯
    public static final int TARGET_TYPE_INFOMATION = 13;
    // 分享会
    public static final int TARGET_TYPE_LIVE_SHOW = 14;
    // 视频直播频道
    public static final int TARGET_TYPE_CHANNEL = 15;
    // 我的粉丝页面
    public static final int TARGET_TYPE_FANS_PAGE = 16;

    // -专题类型
    // 0：综合
    public static final int THEME_NORMAL = 0;
    // 1：商品
    public static final int THEME_PRODUCT = 1;
    // 2：作者
    public static final int THEME_AUTHOR = 2;
    // 3：文章
    public static final int THEME_ARTICLE = 3;


    public static final String FLAG_ADD_PIC = "empty for adding pic";
    public static final int REQUEST_CODE_GALLERY = 0x01;
    public static final int PIC_MAX_QUANTITY = 5;

    public static final int SOLID_WIDTH = -1;
    public static final int SOLID_HEIGHT = -2;

    // -每日推荐参数
    // 每日推荐
    public static final int OPEN_APP_TO_SCAN = 0;
    // 主动拉取
    public static final int PULL_TO_SCAN = 1;

    // bundle传递的key
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_MASTER_NAME = "master_name";
    public static final String KEY_CHANNEL_ID = "channel_id";
    public static final String KEY_REPORT_FROM = "report_from";
    public static final String KEY_ARTICLE_ID = "article_id";

    // -举报类型
    // 主播举报
    public static final int REPORT_CHANNEL = 0;
    // 用户举报
    public static final int REPORT_USER = 1;

    // 直播首页分类 热门直播
    public static final int CATEGORY_HOT_LIVE = -999;
}
