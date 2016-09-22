package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by lenovo on 2016/6/22.
 */
public class AppConfig {

  // private static String baseUrl = "http://api.lalocal.cn/api/";
    private static String baseUrl = "http://dev.lalocal.cn:8080/api/";
    private static String sUserRuleUrl = "http://h5.lalocal.cn/static/userRole.html";

    public static String getWelcommeImgs() {
        return baseUrl + "system/welcomeImgs";
    }

    public static String getSystemConfigUrl() {

        return baseUrl + "system/configs";
    }

    public static void setUserRuleUrl(String userRuleUrl) {
        sUserRuleUrl = userRuleUrl;
    }

    public static String getUserRuleUrl() {
        return sUserRuleUrl;
    }

    //登录接口
    public static String getLoginUrl() {
        return baseUrl + "users/login";
    }

    //检测邮箱是否被注册过
    public static String getCheckMailUrl() {
        return baseUrl + "users/checkEmail";
    }

    //发送验证码接口
    public static String getVerCodeSendUrl() {
        return baseUrl + "system/sendEmail";
    }

    //忘记密码接口
    public static String getPasswordResetUrl() {
        return baseUrl + "users/forgetPassword";
    }

    //我的收藏接口 GET_MY_FARORITE_ITEMS
    public static String getFavoriteItemsUrl() {
        return baseUrl + "praises?";
    }

    //我的优惠券接口 GET_MY_COUPON_ITEMS
    public static String getCouponItemsUrl() {
        return baseUrl + "coupons?";
    }

    //我的订单接口 GET_MY_ORDER_ITEMS
    public static String getOrderItemsUrl() {

        return baseUrl + "orders";
    }

    //UPLOAD_HEDARE_URL
    public static String getHeaderUpdateUrl() {
        return baseUrl + "users/avatar";
    }

    //MODIFY_USER_PROFILE_URL
    public static String getUserProfileModifyUrl() {

        return baseUrl + "users/profile";
    }

    //GET_USER_PROFILE_URL
    public static String getUserProfileUrl() {

        return baseUrl + "users/profile";
    }

    //BOUND_EMAIL_URL
    public static String getEmailBoundUrl() {

        return baseUrl + "users/bindEmail";
    }

    //推荐接口RECOMMEND_URL
    public static String getRecommendUrl() {
        return baseUrl + "themes?";

    }

    //推荐广告位接口 RECOMMEND_AD
    public static String getRecommendAD() {

        return baseUrl + "advertising?type=0";
    }

    //专题详情接口"http://api.lalocal.cn/api/";SPECIAL_DETAILS_URL
    public static String getSepcailDetailUrl() {

        return baseUrl + "themes/";
    }

    //产品详情接口 PRODUCTIONS_DETILS
    public static String getProductDetailsUrl() {

        return baseUrl + "productions/";
    }

    //点赞接口PRAISES
    public static String getPraisesUrl() {

        return baseUrl + "praises";
    }

    //取消赞CANCEL_PRAISES
    public static String getParisesCancelUrl() {

        return baseUrl + "praises/";
    }

    //文章详情 ARTICLE_DETAILS
    public static String getArticleDetailsUrl() {

        return baseUrl + "articles/";
    }


    //直播列表
    public static String getLiveListUrl() {
        return baseUrl + "channels?";
    }

    //推荐直播列表
    public static String getLiveRecommendListUrl() {
        return baseUrl + "channels/recommends";
    }

    //直播详情 http://dev.lalocal.cn:8080/api/channels/2
    public static String getLiveDetails() {
        return baseUrl + "channels/";
    }

    //创建直播间  http://api.lalocal.cn/api/channels
    public static String getCreateLiveRoom() {
        return baseUrl + "channels";
    }

    //修改直播
    public static String getAlterLive() {
        return baseUrl + "channels/";
    }

    //关闭直播
    public static String getCancelLive() {
        return baseUrl + "channels/";
    }

    //游客账号 http://dev.lalocal.cn:8080/api/system/im/tourist
    public static String getTourist() {
        return baseUrl + "system/im/tourist";
    }

    //获取图片上传的token   http://dev.lalocal.cn:8080/api/system/imgs/token
    public static String getImgToken() {
        return baseUrl + "system/imgs/token";
    }

    //上传在线人数http://dev.lalocal.cn:8080/api/channels/2/onlineUsers
    public static String getUserOnLine() {
        return baseUrl + "channels/";
    }

    //获取直播用户信息  http://dev.lalocal.cn:8080/api/users/userInfos/112
    public static final String getLiveUserInfo() {
        return baseUrl + "users/userInfos/";
    }

    //直播添加关注接口http://dev.lalocal.cn:8080/api/users/attentions/2
    public static final String getAddAttention() {
        return baseUrl + "users/attentions/";
    }

    //查看⽤户关注/粉丝列  http://dev.lalocal.cn:8080/api/users/attentions?type=0&userId=8386
    public static final String getAttentionOrFansList() {
        return baseUrl + "users/attentions?";
    }

    //搜索关注粉丝http://dev.lalocal.cn:8080/api/users?pageSize=10&pageNumber=1&nickName=%E6%9D%8E
    public static final String getSearchUser() {
        return baseUrl + "users?";
    }

    //直播礼物商城http://dev.lalocal.cn:8080/api/c
    public static final String getGiftClassify() {
        return baseUrl + "gifts";
    }

    //直播间管理员列表
    public static final String getLiveManagerList() {
        return baseUrl + "channels/";
    }

    //查看用户是否为管理员
    public static final String getCheckUserIdentity() {
        return baseUrl + "channels/admins/check";
    }

    //设置管理员
    public static final String getAccreditManager() {
        return baseUrl + "channels/admins";
    }

    //删除管理员
    public static final String getCancelManager() {
        return baseUrl + "channels/admins/";
    }

    //直播送礼物 http://dev.lalocal.cn:8080/api/gifts
    public static final String getSendGifts() {
        return baseUrl + "gifts";
    }
    //礼物排行榜http://dev.lalocal.cn:8080/api/
    public static final String getGiftRanks(){
        return  baseUrl+"channels/";
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    //版本更新
    public static String VERSION_UPDATE = "http://api.lalocal.cn/api/system/version?version=";
    public static String IN_LAND_PHONE = "400-017-8056";
    public static String FOREIGEN_PHONE = "(+86)0571-86808267";

    //开发环境:http://dev.lalocal.cn:8080/api
    //生产环境:http://api.lalocal.cn/api
    public static void setBaseUrl(String url) {
        AppLog.print("APP当前环境基准地址：" + url);
        baseUrl = url + "/";
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "0.0.0";
    }

    public static String getPackageName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "com.lalocal.lalocal";

    }


    public static String getRegisterUrl() {
        return baseUrl + "users/register";
    }

    public static String getDestinationAreasUrl() {

        return baseUrl + "system/areas";
    }

    public static String getDestinationCollectionsUrl() {
        return baseUrl + "system/collections";
    }


    //获取热门搜索
    public static String getSearchHotUrl() {
        return baseUrl + "tags/recommends";
    }

    //获取搜索标签
    public static String getSearchTagUrl(String name) {
        return baseUrl + "tags?name=" + name;
    }

    //获取搜索
    public static String getSearchResultUrl(String name) {
        return baseUrl + "tags/search?name=" + name;
    }

    //获取更多文章
    public static String getMoreArticleUrl(String name, int pageNumber, int pageSize) {
        return baseUrl + "tags/search/articles?name=" + name + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //获取更多产品
    public static String getMoreProductUrl(String name, int pageNumber, int pageSize) {
        return baseUrl + "tags/search/productions?name=" + name +
                "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }
    //获取更多路线

    public static String getMoreRouteUrl(String name, int pageNumber, int pageSize) {

        return baseUrl + "tags/search/routes?name=" + name + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //⽬的地地区下⾯的路线列表
    public static String getRoutesUrl(int pageSize, int pageNub, int areaId) {
        return baseUrl + "routes/pages?pageSize=" + pageSize + "&pageNumber=" + pageNub + "&areaId=" + areaId;
    }

    //热门路线列表
    public static String getHotRoutes(int areaId) {
        return baseUrl + "routes/recommends?areaId=" + areaId;
    }

    //热门推荐产品列表
    public static String getHotProducts(int areadId) {
        return baseUrl + "productions/recommends?areaId=" + areadId;
    }

    public static String getAreaProducts(int pageSize, int pageNub, int areaId, int type, int collectionsId) {
        String url = baseUrl + "productions?pageSize=" + pageSize + "&pageNumber=" + pageNub + "&areaId=" + areaId;
        if (type == -1 && collectionsId != -1) {
            url += ("&collectionsId=" + collectionsId);
        } else if (type != -1 && collectionsId == -1) {
            url += ("&type=" + type);
        }
        return url;
    }

    public static String getRouteDetailsUrl(int id) {
        return baseUrl + "routes/" + id;
    }


    public static String getPayUrl() {
        return baseUrl + "pay/charges";
    }

    //取消订单
    public static String getCancelOrderUrl(int orderId) {
        return baseUrl + "orders/" + orderId + "/cancel";
    }

    //删除订单
    public static String getDelOrderUrl(int orderId) {
        return baseUrl + "orders/" + orderId;
    }

    public static String getMyWalletUrl() {
        return baseUrl + "users/purse";
    }

    //乐钻日志
    public static String getGoldLogsUrl(int pageSize, int pageNum) {
        return baseUrl + "users/gold/logs?pageSize=" + pageSize + "&pageNumber=" + pageNum;
    }

    //积分日志
    public static String getScoreLogsUrl(int pageSize, int pageNum) {

        return baseUrl + "users/score/logs?pageSize=" + pageSize + "&pageNumber=" + pageNum;
    }

    public static String getRechargeProducts() {
        return baseUrl + "iapPruducts";

    }

    //充值乐钻
    public static String chargeGoldUrl() {

        return baseUrl + "iaplogs/charges";
    }

    public static String exchargeGoldUrl() {

        return baseUrl + "users/score/exchange";
    }

    //直播搜索
    public static String searchLiveUrl(int pageSize, int pageNum, String nickName) {
        return baseUrl + "channels?pageSize=" + pageSize + "&pageNumber=" + pageNum + "&nickName=" + nickName;
    }
    //兑换优惠券
    public static String exchargeCouponUrl(){
      return  baseUrl+"codes";
    }



}
