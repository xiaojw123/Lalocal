package com.lalocal.lalocal.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.lalocal.lalocal.help.UserHelper;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lenovo on 2016/6/22.
 */
public class AppConfig {

/*    1.获取我的推送标签

            get

    http://dev.lalocal.cn/api/users/pushTags

    应用启动的时候调用（登陆状态），将返回的结果注册到友盟平台（tag）

    相应：
    数组String
    */

    /*2. 上报日志接口
    *method:post
    * params:log
    * ur:http://dev.lalocal.cn/api/system/logs/app
    * */
    public static String UTF8 = "UTF-8";

    //用户协议-h5
    public static String USER_PROTOCOL_URL = "http://h5.lalocal.cn/static/userRole.html";
    //预定商品-h5
    public static String preOrderUrl = "http://dev.lalocal.cn/wechat/order_select?id=%1$s&USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";
    //       private static String baseUrl = "http://api.lalocal.cn/api/";
    private static String baseUrl = "http://dev.lalocal.cn:8080/api/";
    private static String sUserRuleUrl = "http://h5.lalocal.cn/static/userRole.html";
<<<<<<< HEAD
    public static final String BOOK_URL_FORMART = "%1$s?USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";

    //获取h5 url地址
    public static String getH5Url(Context context, String baseH5Url) {
        if (!TextUtils.isEmpty(baseH5Url) && !baseH5Url.contains("?")) {
=======
    public static final String BOOK_URL_FORMART_1 = "%1$s?USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";
    public static final String BOOK_URL_FORMART_2 = "%1$s&USER_ID=%2$s&TOKEN=%3$s&APP_VERSION=%4$s&DEVICE=%5$s&DEVICE_ID=%6$s";

    //获取h5 url地址
    public static String getH5Url(Context context, String baseH5Url) {
        if (!TextUtils.isEmpty(baseH5Url)) {
>>>>>>> dev
            if (UserHelper.isLogined(context)) {
                int userId = UserHelper.getUserId(context);
                String token = UserHelper.getToken(context);
                String device = CommonUtil.getDevice();
                String devcieId = CommonUtil.getUUID(context);
                String version = AppConfig.getVersionName(context);
<<<<<<< HEAD
                return String.format(AppConfig.BOOK_URL_FORMART, baseH5Url, userId, token, version, device, devcieId);
            }
        }
        return baseH5Url;

=======
                if (baseH5Url.contains("?")) {
                    int len = baseH5Url.length();
                    int firstIndex = baseH5Url.indexOf("?");
                    String keyParams = baseH5Url.substring(firstIndex, len);
                    if (!keyParams.contains("USER_ID")) {
                        return String.format(AppConfig.BOOK_URL_FORMART_2, baseH5Url, userId, token, version, device, devcieId);
                    }
                } else {
                    return String.format(AppConfig.BOOK_URL_FORMART_1, baseH5Url, userId, token, version, device, devcieId);
                }
            }
        }
        return baseH5Url;
>>>>>>> dev
    }

    public static String getWelcommeImgs() {
        return baseUrl + "system/welcomeImgs";
    }

    public static String getSystemConfigUrl() {
        return baseUrl + "system/configs";
    }

    // 文章列表
    public static String getArticleListUrl() {
        return baseUrl + "articles?";
    }

    // 首页推荐接口 包含：直播列表、专题列表、商品列表
    public static String getIndexRecommendListUrl() {
        return baseUrl + "index";
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

    public static String getAvaileCouponItemUrl(String productionId) {

        return baseUrl + "coupons/available?productionId=" + productionId;
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

    public static String getUserProfileUrl() {

        return baseUrl + "users/profile";
    }

    //BOUND_EMAIL_URL
    public static String getEmailBoundUrl() {

        return baseUrl + "users/bindEmail";
    }

    //用户端离开直播间
    public static final String getUserLeaveRoom(String channels) {
        return baseUrl + "channels/" + channels + "/leave";
    }

    //获取直播间头像  http://dev.lalocal.cn/api/channels/14/stats?number=7&isMaster=false
    public static final String getLiveRoomAvatar(String roomId, int number, boolean isMaster) {
        return baseUrl + "channels/" + roomId + "/stats?number=" + number + "&isMaster=" + isMaster;
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
    public static final String getGiftRanks() {
        return baseUrl + "channels/";
    }

    //分享统计
    public static final String getShareStatistics() {
        return baseUrl + "system/share";
    }

    //上传在线人数 http://dev.lalocal.cn:8080/api/system/numbs?number=1&channelId=11
    public static final String getOnLineUserCount(int number, String channelId) {
        return baseUrl + "system/numbs?number=" + number + "&channelId=" + channelId;
    }

    //发起挑战 http://dev.lalocal.cn:8080/api/challenges
    public static final String getChallageInitiate() {
        return baseUrl + "challenges";
    }

    //挑战列表 http://dev.lalocal.cn:8080/api/challenges?channelId=1&status=0 getChallengeList
    public static final String getChallengeList() {
        return baseUrl + "challenges?channelId=";
    }

    //主播操作挑战 http://dev.lalocal.cn:8080/api/challenges/6/status
    public static final String getLiveChallengeStatus(int challengeId) {
        return baseUrl + "challenges/" + challengeId + "/status";
    }

    //挑战详情
    public static final String getChallengeDetails() {
        return baseUrl + "challenges";
    }

    //直播地区列表
    public static final String getLiveArea() {
        return baseUrl + "system/areas?channelFlag=true";
    }

    //直播首页 http://dev.lalocal.cn:8080/api/channels/index?area=2&attentionFlag=
    public static final String getLiveHotList(String areaId, String attentionFlag) {
        return baseUrl + "channels/index?area=" + areaId + "&attentionFlag=" + attentionFlag;
    }

    //历史直播http://dev.lalocal.cn:8080/api/channels/historys?area=2&pageNumber=2
    public static final String getPlayBackLive(String areaId, int pageNumber, String attentionFlag) {
        //  return baseUrl+(areaId==null?("channels/historys?area=&pageNumber="+pageNumber):("channels/historys?area="+areaId+"&pageNumber="+pageNumber));
        return baseUrl + "channels/historys?area=" + areaId + "&pageNumber=" + pageNumber + "&attentionFlag=" + attentionFlag;
    }
    //获取上传短视频的token  http://dev.lalocal.cn/api/system/videos/token?postfix=mp4
    public static final String getShortVideoTkoen(){
        return  baseUrl+"system/videos/token?postfix=mp4";
    }
    //上传短视频 http://dev.lalocal.cn/api/channels/historys
    public static final String getShortVideoPost(){
        return baseUrl+"channels/historys";
    }

    //历史直播详情 http://dev.lalocal.cn:8080/api/channels/historys/1
    public static final String getPlayBackLiveDetails(int id) {
        return baseUrl + "channels/historys/" + id;
    }

    //修改历史回放 http://dev.lalocal.cn/api/channels/historys/1
    public static final String getAlterPlayBack(int historyId) {
        return baseUrl + "channels/historys/" + historyId;
    }

    //回放消息展示
    public static final String getPlayBackMsg(String historyId) {
        return baseUrl + "channels/historys/" + historyId + "/msgs";
    }

    //历史直播评论  comments?targetId=5317&targetType=20&pageSize=10&pageNumber=1
    public static final String getPlayBackReview(String targetId, int targetType, int pageSize, int pageNumber) {
        return baseUrl + "comments?targetId=" + targetId + "&targetType=" + targetType + "&pageSize=" + pageSize + "&pageNumber=" + pageNumber;
    }

    //上报日志 http://dev.lalocal.cn/api/system/logs/app
    public static final String uploadLogs() {
        return baseUrl + "system/logs/app";
    }

    //禁言
    public static final String getMute() {
        return baseUrl + "channels/mute";
    }

    //永久禁言
    public static final String getPerpetualMute() {
        return baseUrl + "users/block/";
    }

    //关闭直播间
    public static final String getCloseRoom() {
        return baseUrl + "channels/close";
    }

    //发送消息
    public static final String sendMessage() {
        return baseUrl + "system/msgs/validate";
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

    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
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
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "tags?name=" + encodeName;
    }

    //获取搜索
    public static String getSearchResultUrl(String name) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "tags/search?name=" + encodeName;
    }

    //获取更多文章
    public static String getMoreArticleUrl(String name, int pageNumber, int pageSize) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "tags/search/articles?name=" + encodeName + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }

    //获取更多产品
    public static String getMoreProductUrl(String name, int pageNumber, int pageSize) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "tags/search/productions?name=" + encodeName +
                "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
    }
    //获取更多路线

    public static String getMoreRouteUrl(String name, int pageNumber, int pageSize) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "tags/search/routes?name=" + encodeName + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;
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

    public static String getYiTu8City(int id) {
        return baseUrl + "yitu8/+" + id + "/city";
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

    //招商充值接口
    public static String chargeCmbGoldUrl() {

        return baseUrl + "iaplogs/charges/cmb";
    }

    public static String getPayStatus(String payNo) {
        return String.format(baseUrl + "iaplogs/%1$s/status", payNo);

    }


    public static String exchargeGoldUrl() {

        return baseUrl + "users/score/exchange";
    }

    //直播搜索
    public static String searchLiveUrl(int pageSize, int pageNum, String nickName) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(nickName, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return baseUrl + "channels?pageSize=" + pageSize + "&pageNumber=" + pageNum + "&nickName=" + encodeName;
    }

    //兑换优惠券
    public static String exchargeCouponUrl() {
        return baseUrl + "codes/check";
//
    }

    //用户历史直播
    public static String getUserLiveUrl(int userid, int pageNum) {
        String url = baseUrl + "channels/users/" + userid + "/historys?pageNumber=" + pageNum + "&pageSize=10";
        AppLog.i("hs", "url is " + url);
        return url;
    }

    //用户明细
    public static String getChannelRecords(int id) {

        return baseUrl + "channels/records/" + id;
    }

    //发送短信验证码
    public static String getSMSVerCode() {

        return baseUrl + "users/phone/code";
    }

    //手机登录
    public static String getPhoneLoginUrl() {
        return baseUrl + "users/phone/login";
    }

    //手机注册
    public static String getPhoneRegisterUrl() {
        return baseUrl + "users/phone/user";
    }


    // 用户当前直播
    public static String getUserCurLive(int userid) {
        String url = baseUrl + "channels/users/" + userid + "/detail";
        AppLog.i("ussr", "the getUserCurLive url is " + url);
        return url;
    }

    // 获取用户文章列表
    public static String getUserArticles(int userid, int pageNum) {
        String url = baseUrl + "articles/author?authorId=" + userid + "&pageSize=10&pageNumber=" + pageNum;
        AppLog.i("ussr", "the getUserArticles url is " + url);
        return url;
    }

    //三方登录url
    public static String getSocialLoginUrl() {
        return baseUrl + "users/social/login";
    }

    //三方注册
    public static String getSocialReigsterUrl() {
        return baseUrl + "users/social/register";
    }

    //三方绑定
    public static String getSocialBindurl() {
        return baseUrl + "users/social/bind";
    }

    //三方账号列表
    public static String getUsersSocialUrl() {
        return baseUrl + "users/social";
    }

    //解除绑定
    public static String getUnBindSocialUrl(int uid) {
        return baseUrl + "users/social/" + uid;
    }

    //绑定手机号
    public static String getBindPhoneUrl() {
//        http://dev.lalocal.cn:8080/api/users/phone/bind
        return baseUrl + "users/phone/bind";
    }

    public static String getPushTagUrl() {
        return baseUrl + "users/pushTags";
    }

    public static String getCmbPayUrl(int orderId) {
        return baseUrl + "pay/cmb/pay/" + orderId;
    }

    /* 招行支付命令
    * 生产环境：https://netpay.cmbchina.com/netpayment/BaseHttp.dll?PrePayEUserP
    * 测试环境：http://61.144.248.29:801/netpayment/BaseHttp.dll?PrePayEUserP
    *
    * */
    public static String getCmbPayCommand(String branchId, String cono, String billNo, String amount, String date,
                                          int expireTimeSpan, String merchantUrl, String merchantPara, String merchantCode,
                                          String merchantRetUrl, String merchantRetPara) {
//        return String.format("http://61.144.248.29:801/netpayment/BaseHttp.dll?PrePayEUserP?" +
//                        "BranchID=%1$s&CoNo=%2$s&BillNo=%3$s&Amount=%4$s&Date=%5$s&ExpireTimeSpan=%6$s&MerchantUrl=%7$s" +
//                        "&MerchantPara=%8$s&MerchantCode=%9$s&MerchantRetUrl=%10$s&MerchantRetPara=%11$s"
//                , branchId, cono, billNo, amount, date,
//                expireTimeSpan, merchantUrl, merchantPara, merchantCode,
//                merchantRetUrl, merchantRetPara);
        return String.format("https://netpay.cmbchina.com/netpayment/BaseHttp.dll?PrePayEUserP?" +
                        "BranchID=%1$s&CoNo=%2$s&BillNo=%3$s&Amount=%4$s&Date=%5$s&ExpireTimeSpan=%6$s&MerchantUrl=%7$s" +
                        "&MerchantPara=%8$s&MerchantCode=%9$s&MerchantRetUrl=%10$s&MerchantRetPara=%11$s"
                , branchId, cono, billNo, amount, date,
                expireTimeSpan, merchantUrl, merchantPara, merchantCode,
                merchantRetUrl, merchantRetPara);

    }

    public static String getOrderStatusUrl(int orderId) {
        return String.format(baseUrl + "orders/%1$s/status", orderId);
    }

    public static String getPushLogsUrl(String dateTime) {
        if (dateTime == null) {
            dateTime = "";
        }
        return baseUrl + "pushLogs?dateTime=" + dateTime;
    }

    public static String getGLiveSearchUrl(String name, int pageNum, int pageSize) {
        return baseUrl + "tags/search/channels?name=" + encodeString(name) +
                "&pageNumber=" + pageNum + "&pageSize=" + pageSize;
    }


    //回放接口
    public static String getGPlayBackSearchUrl(String name, int pageNum, int pageSize) {
        return baseUrl + "tags/search/channel/historys?name=" + encodeString(name) + "&pageNumber=" + pageNum + "&pageSize=" + pageSize;
    }


    public static String getGSepicalSearchUrl(String name, int pageNum, int pageSize) {
        return baseUrl + "tags/search/themes?name=" + encodeString(name) + "&pageNumber=" + pageNum + "&pageSize=" + pageSize;
    }

    public static String getGUserSearchUrl(int pageSize, int pageNum, String nickName) {
        return baseUrl + "users?pageSize=" + pageSize + "&pageNumber=" + pageNum + "&nickName=" + encodeString(nickName);
    }

    public static String encodeString(String name) {
        String encodeName = "";
        try {
            encodeName = URLEncoder.encode(name, UTF8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeName;

    }

    public static String getMessageCount() {
        return baseUrl + "dynamics/amount";
    }

    /**
     * 首页我的关注接口
     *
     * @return
     */
    public static String getHomeAttention() {
        return baseUrl + "channels/index/attention";
    }

    /**
     * 获取每日推荐
     *
     * @return
     */
    public static String getDailyRecommendations(int type) {
        AppLog.i("dailyy", baseUrl + "dailyRecommendations?type=" + type);
        return baseUrl + "dailyRecommendations?type=" + type;
    }

    /**
     * 直播间举报，主播端
     *
     * @return
     */
    public static String getChannelReport() {
        AppLog.i("qn", "channel report url is " + baseUrl + "channels/report");
        return baseUrl + "channels/report";
    }

    /**
     * 举报：用户端
     *
     * @return
     */
    public static String getReport() {
        AppLog.i("qn", "report url is " + baseUrl + "reports");
        return baseUrl + "reports";
    }

    /**
     * 2.2版本直播首页接口
     *
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @param dateTime
     * @return
     */
    public static String getChannelIndexTotal(String pageNum, String pageSize, String categoryId, String dateTime) {
        String url = baseUrl + "channels/index/total?pageNumber=" + pageNum + "&pageSize=" +
                pageSize + "&categoryId=" + categoryId + "&dateTime=" + dateTime;
        AppLog.i("fghd", "getChannelIndexTotlal url is " + url);
        return url;
    }

    /**
     * 获取文章评论
     *
     * @param articleId 文章编号
     * @return "comments?targetId="+targetId+"&targetType="+targetType+"&pageSize="+pageSize+"&pageNumber="+pageNumber;
     */
    public static String getArticleComments(int articleId, int pageNum) {
        String url = baseUrl + "comments?targetId=" + articleId + "&targetType=1&pageSize=10&pageNumber=" + pageNum;
        //   String url = baseUrl + "comments?targetId=" + articleId + "&targetType=1";
        AppLog.i("articleComments", "the url is " + url);
        return url;
    }

    /**
     * 获取发表评论的url
     *
     * @return
     */
    public static String getSendingCommentsUrl() {
        return baseUrl + "comments";
    }

    /**
     * 获取删除评论的url
     *
     * @param commentId
     * @return
     */
    public static String getDeletingCommentUrl(int commentId) {
        String url = baseUrl + "comments/" + commentId;
        AppLog.i("deleteComments", "url is " + url);
        return url;
    }

    public static String getUsersServiceUrl() {
        return baseUrl + "users/service";
    }

    public static String getUsersServiceUrl(int type) {
        return baseUrl + "users/" + type + "/service ";
    }

    public static String getPraiseCommentUrl(int pageNum) {
        return baseUrl + "dynamics?pageSize=10&pageNumber=" + pageNum;
    }
}
