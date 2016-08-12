package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by lenovo on 2016/6/22.
 */
public class AppConfig {
    //用户协议-h5
    public static String USER_PROTOCOL_URL = "http://h5.lalocal.cn/static/userRole.html";
    //预定商品-h5
    public static String preOrderUrl = "http://dev.lalocal.cn/wechat/order_select?id=%1$s&USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";

    private static String baseUrl = "http://api.lalocal.cn/api";


    //登录接口
    public static String getLoginUrl() {
        return baseUrl + "/users/login";
    }

    //检测邮箱是否被注册过
    public static String getCheckMailUrl() {
        return baseUrl + "/users/checkEmail";
    }

    //发送验证码接口
    public static String getVerCodeSendUrl() {
        return baseUrl + "/system/sendEmail";
    }


    //忘记密码接口
    public static String getPasswordResetUrl() {

        return baseUrl + "/users/forgetPassword";
    }

    //我的收藏接口 GET_MY_FARORITE_ITEMS
    public static String getFavoriteItemsUrl() {
        return baseUrl + "/praises?";
    }

    //我的优惠券接口 GET_MY_COUPON_ITEMS
    public static String getCouponItemsUrl() {

        return baseUrl + "/coupons?";
    }

    //我的订单接口 GET_MY_ORDER_ITEMS
    public static String getOrderItemsUrl() {

        return baseUrl + "/orders";
    }

    //UPLOAD_HEDARE_URL
    public static String getHeaderUpdateUrl() {
        return baseUrl + "/users/avatar";
    }

    //MODIFY_USER_PROFILE_URL
    public static String getUserProfileModifyUrl() {

        return baseUrl + "/users/profile";
    }

    //GET_USER_PROFILE_URL
    public static String getUserProfileUrl() {

        return baseUrl + "/users/profile";
    }

    //BOUND_EMAIL_URL
    public static String getEmailBoundUrl() {

        return baseUrl + "/users/bindEmail";
    }

    //推荐接口RECOMMEND_URL
    public static String getRecommendUrl() {
        return baseUrl + "/themes?";

    }

    //推荐广告位接口 RECOMMEND_AD
    public static String getRecommendAD() {

        return baseUrl + "/advertising?type=0";
    }

    //专题详情接口"http://api.lalocal.cn/api/";SPECIAL_DETAILS_URL
    public static String getSepcailDetailUrl() {

        return baseUrl + "/themes/";
    }

    //产品详情接口 PRODUCTIONS_DETILS
    public static String getProductDetailsUrl() {

        return baseUrl + "/productions/";
    }

    //点赞接口PRAISES
    public static String getPraisesUrl() {

        return baseUrl + "/praises";
    }

    //取消赞CANCEL_PRAISES
    public static String getParisesCancelUrl() {

        return baseUrl + "/praises/";
    }

    //文章详情 ARTICLE_DETAILS
    public static String getArticleDetailsUrl() {

        return baseUrl + "/articles/";
    }

    //版本更新
    public static String VERSION_UPDATE = "http://api.lalocal.cn/api/" + "system/version?version=";
    public static String IN_LAND_PHONE = "400-017-8056";
    public static String FOREIGEN_PHONE = "(+86)0571-86808267";

    //开发环境:http://dev.lalocal.cn:8080/api
    //生产环境:http://api.lalocal.cn/api
    public static void setBaseUrl(String url) {
        AppLog.print("APP当前环境基准地址：" + url);
        baseUrl = url;
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


    public static String getRegisterUrl() {
        return baseUrl + "/users/register";
    }

    public static String getDestinationAreasUrl() {

        return baseUrl + "/system/areas";
    }

    public static String getDestinationCollectionsUrl() {
        return baseUrl + "/system/collections";
    }


    //获取热门搜索
    public static String getSearchHotUrl() {
        return baseUrl + "/tags/recommends";
    }

    //获取搜索标签
    public static String getSearchTagUrl(String name) {
        return baseUrl + "/tags?name=" + name;
    }

    //获取搜索
    public static String getSearchResultUrl(String name) {
        return baseUrl + "/tags/search?name=" + name;
    }

    //获取更多文章
    public static String getMoreArticleUrl(String name, int pageNumber, int pageSize) {
        return baseUrl + "/tags/search/articles?name=" + name + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //获取更多产品
    public static String getMoreProductUrl(String name, int pageNumber, int pageSize) {
        return baseUrl + "/tags/search/productions?name=" + name +
                "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }
    //获取更多路线

    public static String getMoreRouteUrl(String name, int pageNumber, int pageSize) {

        return baseUrl + "/tags/search/routes?name=" + name + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //⽬的地地区下⾯的路线列表
    public static String getRoutesUrl(int pageSize, int pageNub, int areaId) {
        return baseUrl + "/routes/pages?pageSize=" + pageSize + "&pageNumber=" + pageNub + "&areaId=" + areaId;
    }

    //热门路线列表
    public static String getHotRoutes(int areaId) {
        return baseUrl + "/routes/recommends?areaId=" + areaId;
    }

    //热门推荐产品列表
    public static String getHotProducts(int areadId) {
        return baseUrl + "/productions/recommends?areaId=" + areadId;
    }

    public static String getAreaProducts(int pageSize, int pageNub, int areaId, int type, int collectionsId) {
        String url = baseUrl + "/productions?pageSize=" + pageSize + "&pageNumber=" + pageNub + "&areaId=" + areaId;
        if (type == -1 && collectionsId != -1) {
            url += ("&collectionsId=" + collectionsId);
        } else if (type != -1 && collectionsId == -1) {
            url += ("&type=" + type);
        }
        return url;
    }

    public static String getRouteDetailsUrl(int id) {
        return baseUrl + "/routes/" + id;
    }

    public static String getPreOrderProductUrl(Context context, int id, int userid, String token) {
        String devcieId = CommonUtil.getUUID(context);
        String version = AppConfig.getVersionName(context);
        return String.format(preOrderUrl, id, userid, token, version, "android", devcieId);

    }
    public static  String  getPayUrl(){
        return  baseUrl+"/pay/charges";
    }


}
