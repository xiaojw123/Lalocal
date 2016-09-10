package com.lalocal.lalocal.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lalocal.lalocal.activity.RegisterActivity;
import com.lalocal.lalocal.help.ErrorMessage;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveDetailsDataResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveSeachItem;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.RouteDetail;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.SysConfigItem;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.model.WelcomeImg;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.MD5Util;
import com.lalocal.lalocal.view.adapter.AreaDetailAdapter;
import com.lalocal.lalocal.view.adapter.MoreAdpater;
import com.lalocal.lalocal.view.adapter.SearchResultAapter;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaojw on 2016/6/1.
 */
public class ContentLoader {
    public static final String CONTENT_TYPE = "application/json";
    private ICallBack callBack;
    RequestQueue requestQueue;
    ContentResponse response;
    Context context;

    public ContentLoader(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        this.context = context;


    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }

    //直播搜索
    public void searchLive(int pageNum, String nickName) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.SEARCH_LIVE);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.searchLiveUrl(10, pageNum, nickName), response, response);
        requestQueue.add(request);
    }

    //兑换乐钻
    public void exchargeGold(long socre) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.EXCHARGE_GOLD);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.exchargeGoldUrl(), response, response);
        request.setHeaderParams(getLoginHeaderParams());
        try {
            JSONObject jobj = new JSONObject();
            jobj.put("score", socre);
            request.setBodyParams(jobj.toString());
            requestQueue.add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    //充值乐钻
    public void chargeGold(String bodyJson) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CHARGE_GOLD);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.chargeGoldUrl(), response, response);
        request.setHeaderParams(getLoginHeaderParams());
        request.setBodyParams(bodyJson);
        requestQueue.add(request);
    }

    public void getRechargeProducts() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_RECHARGE_PRODUCT);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getRechargeProducts(), response, response);
        request.setHeaderParams(getLoginHeaderParams());
        requestQueue.add(request);
    }

    //获取积分日志
    public void getScoreLogs(int pageNum) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_SOCRE_LOGS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getScoreLogsUrl(10, pageNum), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    //获取乐钻日志
    public void getGoldLogs(int pageNum) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_GOLD_LOGS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getGoldLogsUrl(10, pageNum), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    public void getMyWallet() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MY_WALLET);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getMyWalletUrl(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    public void cancelOrder(int orderId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CANCEL_ORDER);
        }
        AppLog.print("cancelOrderUrl___" + AppConfig.getCancelOrderUrl(orderId));
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getCancelOrderUrl(orderId), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    public void delOrder(int orderId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.DEL_ORDER);
        }
        ContentRequest request = new ContentRequest(Request.Method.DELETE, AppConfig.getDelOrderUrl(orderId), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //获取系统配置
    public void getSystemConfigs() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_SYSTM_CONFIG);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getSystemConfigUrl(), response, response);
        requestQueue.add(request);
    }

    public void getWelcommenImgs() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_WELCOME_IMGS);
        }
        AppLog.print("startPage url_____" + AppConfig.getWelcommeImgs());
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getWelcommeImgs(), response, response);
        requestQueue.add(request);
    }


    public void payOrder(String json) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_PAY_RESULT);
        }
        AppLog.print("支付url___" + AppConfig.getPayUrl());
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getPayUrl(), response, response);
        request.setBodyParams(json);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    public void getRouteDetails(int id) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_ROUTE_DETAILS);
        }
        AppLog.print("request url____" + AppConfig.getRouteDetailsUrl(id));
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getRouteDetailsUrl(id), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);

    }


    public void getDestinationCollections() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_DESTINATION_COLLECTIONS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getDestinationCollectionsUrl(), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getSearchTag(String name) {
        name = name.replaceAll(" ", "");
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_SEARCH_TAG);
        }
        AppLog.print("searchTagUrl_____" + AppConfig.getSearchTagUrl(name));
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getSearchTagUrl(name), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getMoreAritleResult(String name, int pageNumber, int pageSize) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MORE_ARITLE);
            response.setSearchKey(name);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getMoreArticleUrl(name, pageNumber, pageSize), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getMoreProductResult(String name, int pageNumber, int pageSize) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MORE_PRODUCT);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getMoreProductUrl(name, pageNumber, pageSize), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getMoreRouteResult(String name, int pageNumber, int pageSize) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MORE_ROUTE);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getMoreRouteUrl(name, pageNumber, pageSize), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getSearchResult(String name) {
        name = name.replaceAll(" ", "");
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_SEARCH_RESULT);
            response.setSearchKey(name);
        }
        AppLog.print("AppConfig.getSearchResultUrl(name)___" + AppConfig.getSearchResultUrl(name));
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getSearchResultUrl(name), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getSearhHot() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_SEARCH_HOT);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getSearchHotUrl(), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getAreaProducts(int pageSize, int pageNub, int areaId, int type, int collectionId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_AREA_PRODUCTS);
            response.setType(type);
            response.setCollectionId(collectionId);
        }

        AppLog.print("areadProudcts_____url=" + AppConfig.getAreaProducts(pageSize, pageNub, areaId, type, collectionId));
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getAreaProducts(pageSize, pageNub, areaId, type, collectionId), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }


    //热门
    public void getHotProducts(int areaId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_HOT_PRODUCTS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getHotProducts(areaId), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);

    }

    //热门
    public void getHotRoutes(int areaId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_HOT_ROUTES);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getHotRoutes(areaId), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);

    }

    //攻略
    public void getDesAreaRoutes(int pageSize, int pageNub, int areaId, int type) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_DESTIANTION_AREA_ROUTES);
            response.setType(type);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getRoutesUrl(pageSize, pageNub, areaId), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);

    }

    public void getDestinationAreas() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_DESTINATION_AREAS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getDestinationAreasUrl(), response, response);
        request.setHeaderParams(getHeaderParams());
        requestQueue.add(request);
    }

    public void getOrderDetail(int id) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_ORDER_DETAIL);
        }

        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getOrderItemsUrl() + "/" + id, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    public void getMyOrder(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MY_ORDER);
        }
        AppLog.print("getOrderItemsUrl____" + AppConfig.getOrderItemsUrl() + "_____userid__" + userid + ", token__" + token);
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getOrderItemsUrl(), response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        requestQueue.add(request);

    }

    public void getMyCoupon(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MY_COUPON);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getCouponItemsUrl(), response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        requestQueue.add(request);
    }

    public void getMyFavorite(int userid, String token, int pageNumber, int pageSize) {
        //pageNumber=1&pageSize=10
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_FAVORITE_ITEMS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getFavoriteItemsUrl() + "pageNumber=" + pageNumber + "&pageSize=" + pageSize, response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        requestQueue.add(request);
    }


    public void getUserProfile(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getUserProfileUrl(), response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        requestQueue.add(request);
    }

    //修改的用户资料
    public void modifyUserProfile(String nickanme, int sex, String areaCode, String phone, int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.MODIFY_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getUserProfileModifyUrl(), response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        request.setBodyParams(getModifyUserProfileParams(nickanme, sex, areaCode, phone));
        requestQueue.add(request);
    }


    public void resetPasword(String email, String vercode, String newpsw) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RESET_PASSWORD);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getPasswordResetUrl(), response, response);
        request.setBodyParams(getResetPswParams(email, vercode, newpsw));
        requestQueue.add(request);

    }

    public void boundEmail(String email, int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.BOUDN_EMAIL);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getEmailBoundUrl(), response, response);
        request.setHeaderParams(getHeaderParams(userid, token));
        request.setBodyParams(getBoudEmailParams(email));
        requestQueue.add(request);
    }


    //发送验证码
    public void sendVerificationCode(String email, TextView textView) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.SEND_VERIFICATION_CODE);
            response.setResponseView(textView);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getVerCodeSendUrl(), response, response);
        request.setBodyParams(getVerParams(email));
        requestQueue.add(request);

    }

    //判断邮箱是否被注册过
    public void checkEmail(String email) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CHECK_EMAIL);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getCheckMailUrl(), response, response);
        request.setBodyParams(getCheckParams(email));
        requestQueue.add(request);
    }


    //登录
    public void login(final String email, final String password) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LOGIN);
            response.setUserInfo(email, password);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getLoginUrl(), response, response);
        request.setBodyParams(getLoginParams(email, password));


        requestQueue.add(request);
    }

    //注册
    public void register(final String email, final String password, final String nickname, Button regitsterBtn) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.REGISTER);
            response.setResponseView(regitsterBtn);
            response.setUserInfo(email, password);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getRegisterUrl(), response, response);
        request.setBodyParams(getRegisterParams(email, password, nickname));
        requestQueue.add(request);
    }

    //点赞
    public void specialPraise(int id, int type) {
        AppLog.print("specialPraise______" + id);
        if (callBack != null) {
            response = new ContentResponse(RequestCode.PARISES);
            response.setTargetId(id);
        }

        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getPraisesUrl(), response, response);
        request.setHeaderParams(getHeaderParams(-1, null));
        request.setBodyParams(getParisesParams(id, type));
        requestQueue.add(request);

    }

    //取消收藏
    public void cancelParises(Object praiseId, int targetId) {
        AppLog.print("cancelParises______" + praiseId);
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CANCEL_PARISES);
            response.setTargetId(targetId);
        }

        ContentRequest contentRequest = new ContentRequest(Request.Method.DELETE, AppConfig.getParisesCancelUrl() + praiseId, response, response);

        contentRequest.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));

        requestQueue.add(contentRequest);
    }

    //推荐
    public void recommentList(final int pageSize, final int pageNumber) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RECOMMEND);
            response.setRecommend(pageSize, pageNumber);
        }
        String getParameter = "pageSize=" + pageSize + "&pageNumber=" + pageNumber;
        ContentRequest request = new ContentRequest(AppConfig.getRecommendUrl() + getParameter, response, response);
        request.setHeaderParams(getHeaderParams(-1, null));
        requestQueue.add(request);

    }

    //直播列表
    public void liveList(final int pageSize, final int pageNumber) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_LIST);
        }
        String getParameter = "pageSize=" + pageSize + "&pageNumber=" + pageNumber;
        ContentRequest request = new ContentRequest(AppConfig.getLiveListUrl() + getParameter, response, response);
        request.setHeaderParams(getHeaderParams(-1, null));
        requestQueue.add(request);
    }

    //推荐直播列表
    public void liveRecommendList() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_RECOMMEND_LIST);
        }
        ContentRequest request = new ContentRequest(AppConfig.getLiveRecommendListUrl(), response, response);
        request.setHeaderParams(getHeaderParams(-1, null));
        requestQueue.add(request);
    }

    //直播详情
    public void liveDetails(final String skipId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_DETAILS);
        }
        ContentRequest request = new ContentRequest(AppConfig.getLiveDetails() + skipId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //创建直播间
    public void createLiveRoom() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CREATE_LIVE_ROOM);
        }

        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getCreateLiveRoom(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getCreateLiveRoom());
        requestQueue.add(request);
    }

    //修改直播
    public void alterLive(String title, String userId, String photo, String announcement, String longitude, String latitude) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.ALTER_LIVE_ROOM);
        }
        AppLog.i("TAG", "alterLive:修改直播333333333");
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getAlterLive() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getAlterLiveRoom(title, photo, announcement, longitude, latitude));
        requestQueue.add(request);
    }

    //上传直播封面
    public void alterLiveCover(String title, String userId, String photo, String announcement, String longitude, String latitude) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.ALTER_LIVE_COVER);
        }

        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getAlterLive() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getAlterLiveRoom(title, photo, announcement, longitude, latitude));
        requestQueue.add(request);
    }

    //上传在线人数
    public void getUserOnLine(String onLineUsers, int onlinecount) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_ON_LINE_COUNT);
        }

        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.getUserOnLine() + onLineUsers, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getUserOnLines(String.valueOf(onlinecount)));
        requestQueue.add(request);
    }

    //关闭直播间
    public void cancelLiveRoom(String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CANCEL_LIVE_ROOM);
        }
        ContentRequest request = new ContentRequest(Request.Method.DELETE, AppConfig.getCancelLive() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));

        requestQueue.add(request);
    }

    //直播礼物商城
    public void liveGiftStore() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_GIFT_STORE);
        }
        ContentRequest request = new ContentRequest(AppConfig.getGiftClassify(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //直播间管理员列表
    public void getLiveManagerList(String channelId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_MANAGER_LIST);
        }
        ContentRequest request = new ContentRequest(AppConfig.getLiveManagerList() + channelId + "/admins", response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //查看⽤户是不是管理员
    public void checkUserIdentity(String channelId, String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_CHECK_USER_IDENTITY);
        }

        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getCheckUserIdentity(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getUserIdentityStatus(userId, channelId));
        requestQueue.add(request);

    }

    //设置管理员
    public void liveAccreditManager(String channelId, String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_ACCREIDT_MANAGER);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getAccreditManager(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getUserIdentityStatus(userId, channelId));
        requestQueue.add(request);
    }

    //删除管理员
    public void cancelManagerAccredit(String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_CANCEL_MANAGET_ACCREIDT);
        }
        ContentRequest request = new ContentRequest(Request.Method.DELETE, AppConfig.getCancelManager() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);

    }

    //直播送礼物
    public  void liveSendGifts(String channelId, String toId, String toNickName, String giftId, String amount){
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_SEND_GIFTS);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getSendGifts(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        request.setBodyParams(getSendGiftsParams(channelId,toId,toNickName,giftId,amount));
        requestQueue.add(request);
    }

    //获取游客账号
    public void getTouristInfo() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_TOURIST);
        }
        ContentRequest request = new ContentRequest(AppConfig.getTourist(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //获取图片的上传的token
    public void getImgToken() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.IMG_TOKEN);
        }
        ContentRequest request = new ContentRequest(AppConfig.getImgToken(), response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //获取直播用户信息
    public void getLiveUserInfo(String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_USER_INFO);
        }
        ContentRequest request = new ContentRequest(AppConfig.getLiveUserInfo() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //直播添加关注
    public void getAddAttention(String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_ADD_ATTENTION);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.getAddAttention() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);

    }

    //取消关注
    public void getCancelAttention(String userId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_CANCEL_ATTENTION);
        }
        ContentRequest request = new ContentRequest(Request.Method.DELETE, AppConfig.getAddAttention() + userId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //查看粉丝和关注列表
    public void getAttentionOrFansList(String typeId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_FANS_OR_ATTENTION);
        }
        ContentRequest request = new ContentRequest(AppConfig.getAttentionOrFansList() + typeId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);

    }

    //搜索关注粉丝
    public void getSearchUser(String nickName) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LIVE_SEARCH_USER);
        }
        ContentRequest request = new ContentRequest(AppConfig.getSearchUser() + nickName, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }

    //推荐页广告位
    public void recommendAd() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RECOMMEND_AD);
        }
        ContentRequest request = new ContentRequest(AppConfig.getRecommendAD(), response, response);
        requestQueue.add(request);
    }

    //specialdetail
    public void specialDetail(String rowId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.SPECIAL_DETAIL);
        }


        ContentRequest request = new ContentRequest(AppConfig.getSepcailDetailUrl() + rowId, response, response);
        request.setHeaderParams(getHeaderParams(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    //产品详情
    public void productDetails(String targetId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.PRODUCT_DETAILS);
        }
        AppLog.print("获取产品详情———url———" + AppConfig.getProductDetailsUrl() + targetId);
        ContentRequest contentRequest = new ContentRequest(AppConfig.getProductDetailsUrl() + targetId, response, response);
        requestQueue.add(contentRequest);
    }


    //版本更新
    public void versionUpdate(String versionCode) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.VERSION_CODE);
        }
        ContentRequest contentRequest = new ContentRequest(AppConfig.VERSION_UPDATE + versionCode, response, response);
        contentRequest.setHeaderParams(getHeaderParams(-1, null));
        requestQueue.add(contentRequest);
    }

    public void articleDetails(String targetId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.ARTICLE_DETAILS);
        }

        ContentRequest contentRequest = new ContentRequest(AppConfig.getArticleDetailsUrl() + targetId, response, response);

        requestQueue.add(contentRequest);
    }

    class ContentRequest extends StringRequest {
        private String body;
        private Map<String, String> headerParams;
        private String bodyType;

        public ContentRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        @Override
        public void setRetryPolicy(RetryPolicy retryPolicy) {
            super.setRetryPolicy(new DefaultRetryPolicy(8000,//默认超时时间，应设置一个稍微大点儿的，例如本处的500000
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,//默认最大尝试次数
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        }


        public ContentRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        public void setHeaderParams(Map<String, String> headerParams) {
            this.headerParams = headerParams;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headerParams == null ? getHeaderParams() : headerParams;
        }

        public void setBodyParams(String body) {
            this.body = body;
        }


        @Override
        public byte[] getBody() throws AuthFailureError {
            return body != null ? body.getBytes() : super.getBody();
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        //request body type:json
        @Override
        public String getBodyContentType() {
            return bodyType != null ? bodyType : CONTENT_TYPE;
        }


        @Override
        protected HashMap<String, String> getParams()

                throws AuthFailureError {

            HashMap<String, String> hashMap = new HashMap<String, String>();

            hashMap.put("un", "852041173");

            hashMap.put("pw", "852041173abc");

            return hashMap;
        }
    }


    class ContentResponse implements Response.Listener<String>, Response.ErrorListener, CustomDialog.CustomDialogListener {
        private String email, psw;
        private int resultCode;
        private int pageSize, pageNumber;
        //responseView发送网络请求时，禁止在响应之前二次请求网络
        private View responseView;
        private String targetId;
        private String key;
        private int pageType;
        private int collectionId;

        public ContentResponse(int resultCode) {
            this.resultCode = resultCode;
        }

        public void setType(int type) {
            pageType = type;
        }

        public void setCollectionId(int collectionId) {
            this.collectionId = collectionId;
        }


        public void setSearchKey(String key) {
            this.key = key;
        }

        public void setUserInfo(String email, String psw) {
            this.email = email;
            this.psw = psw;
        }

        public void setTargetId(int targetId) {
            this.targetId = String.valueOf(targetId);
        }


        public void setEmail(String email) {
            this.email = email;
        }

        public void setResponseView(View responseView) {
            this.responseView = responseView;
        }

        public void setRecommend(int pageSize, int pageNumber) {
            this.pageSize = pageSize;
            this.pageNumber = pageNumber;

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            AppLog.print("volley error——————" + volleyError);
            if (responseView != null) {
                responseView.setEnabled(true);
            }
            String errorMsg = volleyError.toString();
            if (!ErrorMessage.AUTHOR_FIALED.equals(errorMsg) || !ErrorMessage.CLIENT_ERROR.equals(errorMsg)) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
            }
            callBack.onRequestFailed(volleyError);
        }

        @Override
        public void onResponse(String json) {
            if (responseView != null) {
                responseView.setEnabled(true);
            }
            JSONObject jsonObj;
            try {
                if (TextUtils.isEmpty(json)) {
                    return;
                }
                jsonObj = new JSONObject(json);
                int code = jsonObj.optInt(ResultParams.RESULT_CODE);
                String message = jsonObj.optString(ResultParams.MESSAGE);
                if (code != 0) {
                    callBack.onResponseFailed();
                    if (resultCode == RequestCode.LOGIN && "该邮箱未注册".equals(message)) {
                        CustomDialog dialog = new CustomDialog(context);
                        dialog.setMessage(message);
                        dialog.setCancelable(false);
                        dialog.setCancelBtn("取消", null);
                        dialog.setSurceBtn("去注册", this);
                        dialog.show();
                    } else {
                        CommonUtil.showPromptDialog(context, message, null);
                    }
                    return;
                }
                switch (resultCode) {
                    case RequestCode.SEARCH_LIVE:
                        AppLog.print("result search JSON___"+json);
                        responseSearchLive(jsonObj);
                        break;

                    case RequestCode.EXCHARGE_GOLD:
                        AppLog.print("respose_excharge_gold__" + json);
                        responseExchargeGold(jsonObj);
                        break;
                    case RequestCode.CHARGE_GOLD:
                        responseChargeGold(jsonObj);
                        break;

                    case RequestCode.GET_RECHARGE_PRODUCT:
                        responseGetRechargeProduct(jsonObj);
                        break;
                    case RequestCode.GET_SOCRE_LOGS:
                        responseGetScoreLogs(jsonObj);
                        break;
                    case RequestCode.GET_GOLD_LOGS:
                        responseGetScoreLogs(jsonObj);
                        break;

                    case RequestCode.GET_MY_WALLET:
                        AppLog.print("get my wallet__" + json);
                        responseGetMyWallet(jsonObj);
                        break;
                    case RequestCode.CANCEL_ORDER:
                        AppLog.print("CANCEL_ORDER___" + json);
                        callBack.onCancelSuccess();
                        break;
                    case RequestCode.DEL_ORDER:
                        AppLog.print("DEL_ORDER___" + json);
                        callBack.onDelSuccess();
                        break;
                    case RequestCode.GET_WELCOME_IMGS:
                        AppLog.print("res___" + json);
                        responseGetWelcomeImgs(jsonObj);
                        break;
                    case RequestCode.GET_SYSTM_CONFIG:
                        responseGetSystemConfig(jsonObj);
                        break;
                    case RequestCode.GET_PAY_RESULT:
                        JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
                        callBack.onGetPayResult(resultJobj.toString());
                        break;
                    case RequestCode.GET_ROUTE_DETAILS:
                        responseGetRouteDetails(jsonObj);
                        break;
                    case RequestCode.GET_AREA_PRODUCTS:
                        responseGetAreaDetailItems(jsonObj, AreaDetailAdapter.MODULE_TYPE_PRODUCT);
                        break;
                    case RequestCode.GET_DESTIANTION_AREA_ROUTES:
                        responseGetAreaDetailItems(jsonObj, AreaDetailAdapter.MODULE_TYPE_ROUTE);
                        break;
                    case RequestCode.GET_HOT_ROUTES:
                        AppLog.print("get hot routed___" + json);
                        responseGetHotItems(jsonObj, AreaDetailAdapter.MODULE_TYPE_ROUTE);
                        break;
                    case RequestCode.GET_HOT_PRODUCTS:
                        AppLog.print("get hot products___" + json);
                        responseGetHotItems(jsonObj, AreaDetailAdapter.MODULE_TYPE_PRODUCT);
                        break;
                    case RequestCode.GET_SEARCH_TAG:
                        responseGetSearchTag(jsonObj);
                        break;
                    case RequestCode.GET_MORE_ROUTE:
                        responsMoreItems(jsonObj, MoreAdpater.MODUEL_TYPE_ROUTE);
                        break;
                    case RequestCode.GET_MORE_PRODUCT:
                        responsMoreItems(jsonObj, MoreAdpater.MODUEL_TYPE_PRODUCT);
                        break;
                    case RequestCode.GET_MORE_ARITLE:
                        responsMoreItems(jsonObj, MoreAdpater.MODUEL_TYPE_ARTICLE);
                        break;

                    case RequestCode.GET_SEARCH_RESULT:
                        AppLog.print("get_search_result__" + json);
                        responseGetSearchResult(jsonObj);
                        break;

                    case RequestCode.GET_SEARCH_HOT:
                        responseGetSearchHot(jsonObj);
                        break;

                    case RequestCode.GET_DESTINATION_COLLECTIONS:
                        responseGetCollections(jsonObj);
                        break;

                    case RequestCode.GET_DESTINATION_AREAS:
                        responseGetDesAreas(jsonObj);
                        break;
                    case RequestCode.GET_ORDER_DETAIL:
                        AppLog.print("get order_detail___" + json);
                        responseGetOrderDetail(jsonObj);
                        break;
                    case RequestCode.GET_MY_ORDER:
                        AppLog.print("get my order____" + json);
                        responseGetMyOrderItems(jsonObj);
                        break;
                    case RequestCode.GET_FAVORITE_ITEMS:
                        responseGetFavoriteItems(jsonObj);
                        break;
                    case RequestCode.REGISTER:
                        responseRegister(jsonObj);
                        break;
                    case RequestCode.LOGIN:
                        responseLogin(jsonObj);
                        break;
                    case RequestCode.CHECK_EMAIL:
                        responseCheckMail();
                        break;
                    case RequestCode.SEND_VERIFICATION_CODE:
                        responseSendVerCode();
                        break;
                    case RequestCode.RESET_PASSWORD:
                        responseResetPassword();
                        break;
                    case RequestCode.MODIFY_USER_PROFILE:
                        responseModifyUserProfile(jsonObj);
                        break;

                    case RequestCode.GET_USER_PROFILE:
                        responseGetUserProfile(jsonObj);
                        break;
                    case RequestCode.BOUDN_EMAIL:
                        responseBoundEmail();
                        break;
                    case RequestCode.GET_MY_COUPON:
                        responseGetMyCoupon(jsonObj);
                        break;
                    case RequestCode.RECOMMEND:
                        AppLog.print("recommend____json__" + json);
                        responseRecommend(json);
                        break;
                    case RequestCode.RECOMMEND_AD:
                        AppLog.print("recommedn___ad__" + json);
                        responseRecommendAd(json);
                        break;
                    case RequestCode.SPECIAL_DETAIL:
                        responseSpecialDetail(json);
                        break;
                    case RequestCode.PRODUCT_DETAILS:
                        responseProductDetails(json);
                        break;
                    case RequestCode.CANCEL_PARISES:
                        responseCancelParises(json);
                        break;
                    case RequestCode.PARISES:
                        responseParises(json);
                        break;
                    case RequestCode.ARTICLE_DETAILS:
                        responseArticle(json);
                        break;
                    case RequestCode.VERSION_CODE:
                        responseVersion(json);
                        break;
                    case RequestCode.LIVE_LIST:
                        responseLiveList(json);
                        break;
                    case RequestCode.LIVE_RECOMMEND_LIST:
                        responseLiveRecommendList(json);
                        break;
                    case RequestCode.LIVE_DETAILS:
                        responseLiveDetails(json);
                        break;
                    case RequestCode.CREATE_LIVE_ROOM:
                        responseCreateLiveRoom(json);
                        break;
                    case RequestCode.CANCEL_LIVE_ROOM:
                        responseCancelLive(json);
                        break;
                    case RequestCode.ALTER_LIVE_ROOM:
                        responseAlterLiveRoom(json);
                        break;
                    case RequestCode.GET_TOURIST:
                        responseTourist(json);
                        break;
                    case RequestCode.IMG_TOKEN:
                        responseImgToken(json);
                        break;
                    case RequestCode.ALTER_LIVE_COVER:
                        responseAlterLiveCover(json);
                        break;
                    case RequestCode.LIVE_USER_INFO:
                        responseLiveUserInfo(json);
                        break;
                    case RequestCode.LIVE_ADD_ATTENTION:
                        responseAddAttention(json);
                        break;
                    case RequestCode.LIVE_CANCEL_ATTENTION:
                        responseCancelAttention(json);
                        break;
                    case RequestCode.LIVE_FANS_OR_ATTENTION:
                        responseFansOrAttention(json, false);
                        break;
                    case RequestCode.LIVE_SEARCH_USER:
                        responseFansOrAttention(json, true);
                        break;
                    case RequestCode.LIVE_ON_LINE_COUNT:
                        responseOnLinesCount(json);
                        break;
                    case RequestCode.LIVE_GIFT_STORE:
                        responseLiveGiftsStore(json);
                        break;
                    case RequestCode.LIVE_MANAGER_LIST:
                        responseLiveManagerList(json);
                        break;
                    case RequestCode.LIVE_CHECK_USER_IDENTITY:
                        responseUserIdentity(json);
                        break;
                    case RequestCode.LIVE_CANCEL_MANAGET_ACCREIDT:
                        responseCancelManager(json);
                        break;
                    case RequestCode.LIVE_ACCREIDT_MANAGER:
                        responseAccreditManager(json);
                        break;
                    case RequestCode.LIVE_SEND_GIFTS:
                        responseSendGifts(json);
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        private void responseSearchLive(JSONObject jsonObj) {
            Gson gson = new Gson();
            String json = jsonObj.optString(ResultParams.REULST);
            LiveSeachItem item = gson.fromJson(json, LiveSeachItem.class);
            callBack.onSearchLive(item);
        }

        private void responseExchargeGold(JSONObject jsonObj) {
            callBack.onExchargeGoldSuccess();
        }

        private void responseChargeGold(JSONObject jsonObj) {
            String result = jsonObj.optString(ResultParams.REULST);
            callBack.onChargeGold(result);
        }

        private void responseGetRechargeProduct(JSONObject jsonObj) {
            JSONArray resultJarray = jsonObj.optJSONArray(ResultParams.REULST);
            Gson gson = new Gson();
            List<RechargeItem> items = new ArrayList<>();
            for (int i = 0; i < resultJarray.length(); i++) {
                String itemJson = resultJarray.optString(i);
                RechargeItem item = gson.fromJson(itemJson, RechargeItem.class);
                items.add(item);
            }
            callBack.onGetRechargeProducts(items);
        }

        private void responseGetScoreLogs(JSONObject jsonObj) {
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            ConsumeRecord log = gson.fromJson(resultJobj.toString(), ConsumeRecord.class);
            callBack.onGetScoreLog(log);
        }


        private void responseGetMyWallet(JSONObject jsonObj) {
            AppLog.i("TAG","responseGetMyWallet:"+jsonObj.toString());
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            WalletContent content = gson.fromJson(resultJobj.toString(), WalletContent.class);
            callBack.onGetMyWallet(content);
        }

        private void responseGetSystemConfig(JSONObject jsonObj) {
            List<SysConfigItem> items = new ArrayList<>();
            Gson gson = new Gson();
            JSONArray resultJarray = jsonObj.optJSONArray(ResultParams.REULST);
            for (int i = 0; i < resultJarray.length(); i++) {
                JSONObject itemJobj = resultJarray.optJSONObject(i);
                SysConfigItem item = gson.fromJson(itemJobj.toString(), SysConfigItem.class);
                items.add(item);
            }
            callBack.onGetSysConfigs(items);
        }

        private void responseGetWelcomeImgs(JSONObject jsonObj) {
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            AppLog.print("resutlJoj__" + resultJobj.toString());
            WelcomeImg img = gson.fromJson(resultJobj.toString(), WelcomeImg.class);
            callBack.onGetWelcomeImgs(img);
        }


        private void responseGetRouteDetails(JSONObject jsonObj) {
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            RouteDetail routeDetail = gson.fromJson(resultJobj.toString(), RouteDetail.class);
            callBack.onGetRouteDetail(routeDetail);


        }

        private void responseGetAreaDetailItems(JSONObject jsonObj, int moduleType) {
            JSONObject resJobj = jsonObj.optJSONObject(ResultParams.REULST);
            int pageNumber = resJobj.optInt(ResultParams.PAGE_NUMBER);
            int toalPages = resJobj.optInt(ResultParams.TOTAL_PAGES);
            JSONArray itemsJarray = resJobj.optJSONArray(ResultParams.ROWS);
            List<SearchItem> items = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < itemsJarray.length(); i++) {
                JSONObject itemJobj = itemsJarray.optJSONObject(i);
                SearchItem item = null;
                switch (moduleType) {
                    case AreaDetailAdapter.MODULE_TYPE_PRODUCT:
                        item = gson.fromJson(itemJobj.toString(), ProductItem.class);
                        break;
                    case AreaDetailAdapter.MODULE_TYPE_ROUTE:
                        item = gson.fromJson(itemJobj.toString(), RouteItem.class);
                        break;

                }
                if (item != null) {
                    items.add(item);
                }
            }
            callBack.onGetAreaItems(pageNumber, toalPages, items, pageType, collectionId);


        }


        private void responsMoreItems(JSONObject jsonObj, int type) {
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            int pageNumber = resultJobj.optInt("pageNumber");
            int totalPages = resultJobj.optInt("totalPages");
            int totalRows = resultJobj.optInt("totalRows");
            JSONArray rowsJarray = resultJobj.optJSONArray("rows");
            List<SearchItem> items = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < rowsJarray.length(); i++) {
                JSONObject itemJobj = rowsJarray.optJSONObject(i);
                SearchItem item = null;
                switch (type) {
                    case MoreAdpater.MODUEL_TYPE_ARTICLE:
                        item = gson.fromJson(itemJobj.toString(), ArticleItem.class);
                        break;
                    case MoreAdpater.MODUEL_TYPE_PRODUCT:
                        item = gson.fromJson(itemJobj.toString(), ProductItem.class);
                        break;
                    case MoreAdpater.MODUEL_TYPE_ROUTE:
                        item = gson.fromJson(itemJobj.toString(), RouteItem.class);
                        break;
                }
                if (item != null) {
                    item.setModeltype(type);
                    items.add(item);
                }
            }
            callBack.onGetMoreItems(pageNumber, totalPages, items);

        }

        private void responseGetHotItems(JSONObject jsonObj, int type) {
            JSONArray itemsJarray = jsonObj.optJSONArray(ResultParams.REULST);
            List<SearchItem> items = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < itemsJarray.length(); i++) {
                JSONObject itemJobj = itemsJarray.optJSONObject(i);
                SearchItem item = null;
                switch (type) {
                    case AreaDetailAdapter.MODULE_TYPE_PRODUCT:
                        item = gson.fromJson(itemJobj.toString(), ProductItem.class);
                        break;
                    case AreaDetailAdapter.MODULE_TYPE_ROUTE:
                        item = gson.fromJson(itemJobj.toString(), RouteItem.class);
                        break;
                }
                if (item != null) {
                    items.add(item);
                }
            }
            callBack.onGetHotItems(items, type);
        }

        private void responseGetAreaRoutes(JSONObject jsonObj) {


        }

        private void responseGetSearchTag(JSONObject jsonObj) {
            JSONArray itemsArray = jsonObj.optJSONArray(ResultParams.REULST);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                String key = itemsArray.optString(i);
                items.add(key);
            }
            callBack.onGetSearchTag(items);
        }

        private void responseGetSearchResult(JSONObject jsonObj) {
            JSONObject resultJobj = jsonObj.optJSONObject(ResultParams.REULST);
            JSONObject articlesJobj = resultJobj.optJSONObject("articleList");
            JSONObject productsJobj = resultJobj.optJSONObject("productionList");
            JSONObject routesJobj = resultJobj.optJSONObject("routeList");
            JSONArray articlesJarray = articlesJobj.optJSONArray(ResultParams.REULST);
            JSONArray productsJarray = productsJobj.optJSONArray(ResultParams.REULST);
            JSONArray routesJarray = routesJobj.optJSONArray(ResultParams.REULST);
            int articlesTotalNumb = articlesJobj.optInt("totalNumb");
            int productsTotalNumb = productsJobj.optInt("totalNumb");
            int routesToalNumb = routesJobj.optInt("totalNumb");
            List<ArticleItem> articleItems = new ArrayList<>();
            List<ProductItem> productItems = new ArrayList<>();
            List<RouteItem> routeItems = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < articlesJarray.length(); i++) {
                JSONObject articleJobj = articlesJarray.optJSONObject(i);
                ArticleItem item = gson.fromJson(articleJobj.toString(), ArticleItem.class);
                item.setModeltype(SearchResultAapter.MODE_TYPE_ARTICLE);
                articleItems.add(item);
            }
            for (int i = 0; i < productsJarray.length(); i++) {
                JSONObject productJobj = productsJarray.optJSONObject(i);
                ProductItem item = gson.fromJson(productJobj.toString(), ProductItem.class);
                item.setModeltype(SearchResultAapter.MODE_TYPE_PRODUCT);
                productItems.add(item);
            }
            for (int i = 0; i < routesJarray.length(); i++) {
                JSONObject routeJobj = routesJarray.optJSONObject(i);
                RouteItem item = gson.fromJson(routeJobj.toString(), RouteItem.class);
                item.setModeltype(SearchResultAapter.MODE_TYPE_ROUTE);
                routeItems.add(item);
            }
            callBack.onGetSearchResult(key, articleItems, articlesTotalNumb, productItems, productsTotalNumb, routeItems, routesToalNumb);
        }

        private void responseGetSearchHot(JSONObject jsonObj) {
            JSONArray itemsArray = jsonObj.optJSONArray(ResultParams.REULST);
            List<String> items = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                String key = itemsArray.optString(i);
                items.add(key);
            }
            callBack.onGetSearchHot(items);
        }


        private void responseGetCollections(JSONObject jsonObj) {
            JSONArray jsonArray = jsonObj.optJSONArray(ResultParams.REULST);
            List<SiftModle> items = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject itemJobj = jsonArray.optJSONObject(i);
                SiftModle item = gson.fromJson(itemJobj.toString(), SiftModle.class);
                items.add(item);
            }
            callBack.onGetDestinationCollections(items);

        }

        private void responseGetDesAreas(JSONObject jsonObj) {
            JSONArray itemsJsArray = jsonObj.optJSONArray(ResultParams.REULST);
            List<AreaItem> items = new ArrayList<>();
            Gson gson = new Gson();
            for (int i = 0; i < itemsJsArray.length(); i++) {
                JSONObject itemJson = itemsJsArray.optJSONObject(i);
                AreaItem item = gson.fromJson(itemJson.toString(), AreaItem.class);
                items.add(item);
            }
            callBack.onGetDestinationAreas(items);
        }

        private void responseTourist(String json) {
            callBack.onTouristInfo(json);
        }

        private void responseGetOrderDetail(JSONObject jsonObj) {
            JSONObject resultJsObj = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            OrderDetail orderItem = gson.fromJson(resultJsObj.toString(), OrderDetail.class);
            callBack.onGetOrderDetail(orderItem);
        }

        private void responseGetMyOrderItems(JSONObject jsonObj) {
            JSONArray resutJsonArray = jsonObj.optJSONArray(ResultParams.REULST);
            Gson gson = new Gson();
            List<OrderItem> items = new ArrayList<>();
            for (int i = 0; i < resutJsonArray.length(); i++) {
                JSONObject itemJsonObj = resutJsonArray.optJSONObject(i);
                OrderItem orderItem = gson.fromJson(itemJsonObj.toString(), OrderItem.class);
                items.add(orderItem);
            }
            callBack.onGetOrderItem(items);


        }

        private void responseGetMyCoupon(JSONObject jsonObj) {
            JSONArray resultJsonArray = jsonObj.optJSONArray(ResultParams.REULST);
            Gson gson = new Gson();
            List<Coupon> coupons = new ArrayList<>();
            for (int i = 0; i < resultJsonArray.length(); i++) {
                JSONObject couponJsonObj = resultJsonArray.optJSONObject(i);
                Coupon coupon = gson.fromJson(couponJsonObj.toString(), Coupon.class);
                if (coupon.getStatus() == 1) {
                    continue;
                }
                coupons.add(coupon);
            }
            sortCoupon(coupons);
            callBack.onGetCounponItem(coupons);
        }


        private void sortCoupon(List<Coupon> coupons) {
            for (int i = 0; i < coupons.size() - 1; i++) {
                Coupon coupon1 = coupons.get(i);
                Coupon coupon2 = coupons.get(i + 1);
                int status1 = coupon1.getStatus();
                int status2 = coupon2.getStatus();//0未使用 2已过期
                int type1 = coupon1.getType();
                int type2 = coupon2.getType();//0普通优惠券 1作者优惠券
                String time1 = coupon1.getExpiredDateStr();
                String time2 = coupon2.getExpiredDateStr();//过期时间
                if (status1 == 2 && status2 == 0) {
                    coupons.set(i, coupon2);
                    coupons.set(i + 1, coupon1);
                } else if (status1 == status2) {
                    if (type1 == type2) {
                        int result = time1.compareTo(time2);
                        if (result == 1) {
                            coupons.set(i, coupon2);
                            coupons.set(i + 1, coupon1);
                        }
                    } else if (type1 == 0 && type2 == 1) {
                        coupons.set(i, coupon2);
                        coupons.set(i + 1, coupon1);
                    }
                }
            }
        }

        private void responseGetFavoriteItems(JSONObject jsonObj) throws JSONException {
            JSONObject resutJson = jsonObj.optJSONObject(ResultParams.REULST);
            int totalPages = resutJson.optInt("totalPages");
            int totalRows = resutJson.optInt("totalRows");
            int pageNumber = resutJson.optInt("pageNumber");
            JSONArray rows = resutJson.optJSONArray("rows");
            Gson gson = new Gson();
            List<FavoriteItem> items = new ArrayList<>();
            for (int i = 0; i < rows.length(); i++) {
                JSONObject rowJsonObj = rows.getJSONObject(i);
                FavoriteItem item = gson.fromJson(rowJsonObj.toString(), FavoriteItem.class);
                items.add(item);
            }
            callBack.onGetFavoriteItem(items, pageNumber, totalPages, totalRows);
        }

        private void responseBoundEmail() {
            callBack.onSendActivateEmmailComplete();

        }

        private void responseGetUserProfile(JSONObject jsonObj) {
            JSONObject resultJson = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            LoginUser user = gson.fromJson(resultJson.toString(), LoginUser.class);
            callBack.onGetUserProfile(user);
        }

        private void responseModifyUserProfile(JSONObject jsonObj) {
            JSONObject resultJson = jsonObj.optJSONObject(ResultParams.REULST);
            Gson gson = new Gson();
            LoginUser user = gson.fromJson(resultJson.toString(), LoginUser.class);
            callBack.onModifyUserProfile(user);
        }


        private void responseResetPassword() {
            callBack.onResetPasswordComplete();
        }

        private void responseSendVerCode() {
            callBack.onSendVerCode(email);
        }

        private void responseCheckMail() {

            callBack.onCheckEmail(email);
        }

        private void responseLogin(JSONObject jsonObject) {
            JSONObject resutJson = jsonObject.optJSONObject(ResultParams.REULST);
            User user = null;
            if (resutJson != null) {
                Gson gson = new Gson();
                user = gson.fromJson(resutJson.toString(), User.class);
            }
            AppLog.i("TAG", "responseLogin" + jsonObject.toString());
            Bundle bundle = new Bundle();
            bundle.putBoolean(KeyParams.IS_LOGIN, true);
            bundle.putString(KeyParams.EMAIL, email);
            bundle.putString(KeyParams.PASSWORD, psw);
            bundle.putInt(KeyParams.USERID, user.getId());
            bundle.putString(KeyParams.TOKEN, user.getToken());
            bundle.putString(KeyParams.AVATAR, user.getAvatar());
            bundle.putString(KeyParams.IM_CCID, user.getImUserInfo().getAccId());
            bundle.putString(KeyParams.IM_TOKEN, user.getImUserInfo().getToken());
            bundle.putString(KeyParams.NICKNAME, user.getNickName());
            UserHelper.saveLoginInfo(context, bundle);
            DemoCache.clear();
            AuthPreferences.clearUserInfo();
            AuthPreferences.saveUserAccount(user.getImUserInfo().getAccId());
            AuthPreferences.saveUserToken(user.getImUserInfo().getToken());
            loginIMServer(user.getImUserInfo().getAccId(), user.getImUserInfo().getToken());
            callBack.onLoginSucess(user);
        }

        private void loginIMServer(final String imccId, String imToken) {
            NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {

                @Override
                public void onSuccess(Object o) {
                    DemoCache.setAccount(imccId);
                    DemoCache.getRegUserInfo();
                    DemoCache.setLoginStatus(true);
                }

                @Override
                public void onFailed(int i) {
                    DemoCache.setLoginStatus(false);
                }

                @Override
                public void onException(Throwable throwable) {
                    DemoCache.setLoginStatus(false);
                }
            });
        }

        private void responseRegister(JSONObject jsonObject) {
            JSONObject jsonObj = jsonObject.optJSONObject(ResultParams.REULST);
            User user = null;
            if (jsonObj != null) {
                int userid = jsonObj.optInt("id");
                String token = jsonObj.optString("token");
                callBack.onResigterComplete(email, psw, userid, token);

            }

        }

        //点赞
        private void responseParises(String json) {

            if (!UserHelper.favorites.contains(targetId)) {
                UserHelper.favorites.add(targetId);
            }
            PariseResult pariseResult = new Gson().fromJson(json, PariseResult.class);
            callBack.onInputPariseResult(pariseResult);
        }

        //取消赞
        private void responseCancelParises(String json) {

            if (UserHelper.favorites.contains(targetId)) {
                UserHelper.favorites.remove(targetId);
            }
            PariseResult pariseResult = new Gson().fromJson(json, PariseResult.class);
            callBack.onPariseResult(pariseResult);
        }

        //产品详情
        private void responseProductDetails(String json) {
            AppLog.print("productDetails  json____" + json);

            ProductDetailsDataResp productDetailsDataResp = new Gson().fromJson(json, ProductDetailsDataResp.class);
            if (productDetailsDataResp != null) {
                callBack.onProductDetails(productDetailsDataResp);
            }

        }


        //推荐
        public void responseRecommend(String json) {
            RecommendDataResp recommendDataResp = new Gson().fromJson(json, RecommendDataResp.class);
            callBack.onRecommend(recommendDataResp);
        }

        //推荐广告
        public void responseRecommendAd(String json) {
            RecommendAdResp recommendAdResp = new Gson().fromJson(json, RecommendAdResp.class);
            callBack.onRecommendAd(recommendAdResp);

        }

        //直播列表
        private void responseLiveList(String json) {
            LiveListDataResp liveListDataResp = new Gson().fromJson(json, LiveListDataResp.class);
            if (liveListDataResp != null) {
                callBack.onLiveList(liveListDataResp);
            }

        }

        //推荐直播列表
        private void responseLiveRecommendList(String json) {
            LiveRecommendListDataResp liveRecommendListDataResp = new Gson().fromJson(json, LiveRecommendListDataResp.class);
            callBack.onLiveRecommendList(liveRecommendListDataResp);
        }

        //直播详情
        private void responseLiveDetails(String json) {

            LiveDetailsDataResp liveDetailsDataResp = new Gson().fromJson(json, LiveDetailsDataResp.class);
            callBack.onLiveDetails(liveDetailsDataResp);
        }

        //创建直播间
        private void responseCreateLiveRoom(String json) {

            CreateLiveRoomDataResp createLiveRoomDataResp = new Gson().fromJson(json, CreateLiveRoomDataResp.class);
            int id = createLiveRoomDataResp.getResult().getId();
            callBack.onCreateLiveRoom(createLiveRoomDataResp);
        }

        //修改直播间
        private void responseAlterLiveRoom(String json) {

            CreateLiveRoomDataResp createLiveRoomDataResp = new Gson().fromJson(json, CreateLiveRoomDataResp.class);
            callBack.onAlterLiveRoom(createLiveRoomDataResp);
        }

        //上传在线人数
        private void responseOnLinesCount(String json) {

        }

        //修改直播封面
        private void responseAlterLiveCover(String json) {
            CreateLiveRoomDataResp createLiveRoomDataResp = new Gson().fromJson(json, CreateLiveRoomDataResp.class);
            callBack.onAlterLiveCover(createLiveRoomDataResp);
        }


        //关闭直播间
        private void responseCancelLive(String json) {
            AppLog.i("TAG", "调用了关闭直播间的接口。。。。。。。。。。。。。。。。。。。。");
            CloseLiveBean closeLiveBean = new Gson().fromJson(json, CloseLiveBean.class);
            callBack.onCloseLive(closeLiveBean);
        }

        //直播礼物商城
        private void responseLiveGiftsStore(String json) {
            GiftDataResp giftDataResp = new Gson().fromJson(json, GiftDataResp.class);
            callBack.onGiftsStore(giftDataResp);
        }
        //直播送礼物
        private void responseSendGifts(String json) {
            AppLog.i("TAG","直播送礼物："+json);
         callBack.onSendGiftsBack(json);

        }
        //直播间管理员列表
        private void responseLiveManagerList(String json) {
            LiveManagerListResp liveManagerListResp = new Gson().fromJson(json, LiveManagerListResp.class);
            callBack.onManagerList(liveManagerListResp);

        }

        //查看用户是否为管理员
        private void responseUserIdentity(String json) {
            AppLog.i("TAG","查看用户是否为管理员:"+json);
            LiveManagerBean liveManagerBean = new Gson().fromJson(json, LiveManagerBean.class);
            callBack.onCheckManager(liveManagerBean);
        }

        //设置管理员
        private void responseAccreditManager(String json) {
            LiveManagerBean liveManagerBean = new Gson().fromJson(json, LiveManagerBean.class);
            callBack.onLiveManager(liveManagerBean);
        }

        //取消管理员
        private void responseCancelManager(String json) {
            AppLog.i("TAG", "responseCancelManager:" + json);
        }

        //上传图片token
        private void responseImgToken(String json) {
            ImgTokenBean imgTokenBean = new Gson().fromJson(json, ImgTokenBean.class);
            callBack.onImgToken(imgTokenBean);
        }

        //获取直播用户信息
        private void responseLiveUserInfo(String json) {
            LiveUserInfosDataResp liveUserInfosDataResp = new Gson().fromJson(json, LiveUserInfosDataResp.class);
            callBack.onLiveUserInfo(liveUserInfosDataResp);
        }

        //直播添加关注
        private void responseAddAttention(String json) {
            AppLog.i("TAG", "responseAddAttention" + json);
            LiveAttentionStatusBean liveAttentionStatusBean = new Gson().fromJson(json, LiveAttentionStatusBean.class);
            callBack.onLiveAttentionStatus(liveAttentionStatusBean);
        }

        //取消关注
        private void responseCancelAttention(String json) {
            LiveCancelAttention liveCancelAttention = new Gson().fromJson(json, LiveCancelAttention.class);
            callBack.onLiveCancelAttention(liveCancelAttention);

        }


        //获取粉丝或关注列表
        private void responseFansOrAttention(String json, boolean isSearch) {
            AppLog.i("TAG", "responseFansOrAttention:" + json);
            LiveFansOrAttentionResp liveFansOrAttentionResp = new Gson().fromJson(json, LiveFansOrAttentionResp.class);
            callBack.onLiveFansOrAttention(liveFansOrAttentionResp, isSearch);
        }
        /*//搜索关注粉丝
        private void responseSearchUser(String json) {
            AppLog.i("TAG","responseSearchUser:"+json);
            LiveFansOrAttentionResp liveFansOrAttentionResp = new Gson().fromJson(json, LiveFansOrAttentionResp.class);
            callBack.onSearchAttentionOrFans(liveFansOrAttentionResp);
        }*/

        //specialdetail
        public void responseSpecialDetail(String json) {
            AppLog.i("TAG", "responseSpecialDetail:" + json);
            SpectialDetailsResp spectialDetailsResp = new Gson().fromJson(json, SpectialDetailsResp.class);
            if (spectialDetailsResp != null) {
                callBack.onRecommendSpecial(spectialDetailsResp);
            }

        }

        private void responseArticle(String json) {
            ArticleDetailsResp articleDetailsResp = new Gson().fromJson(json, ArticleDetailsResp.class);
            if (articleDetailsResp.getReturnCode() == 0) {
                callBack.onArticleResult(articleDetailsResp);
            }
        }

        private void responseVersion(String json) {
            AppLog.i("TAG", "responseVersion:" + json);
            VersionInfo versionInfo = new Gson().fromJson(json, VersionInfo.class);
            if (versionInfo.getReturnCode() == 0) {
                callBack.onVersionResult(versionInfo);
            }
        }

        @Override
        public void onDialogClickListener() {
            Intent intent = new Intent(context, RegisterActivity.class);
            intent.putExtra(KeyParams.EMAIL, email);
            ((Activity) context).startActivityForResult(intent, 100);
        }
    }




    public String getModifyUserProfileParams(String nickname, int sex, String areaCode, String
            phone) {
        JSONObject jsonObj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(nickname)) {
                jsonObj.put("nickName", nickname);
            }
            if (sex == 1 || sex == 0) {
                boolean flag = (sex == 1);
                jsonObj.put("sex", flag);
            }
            if (!TextUtils.isEmpty(areaCode)) {
                jsonObj.put("areaCode", areaCode);
            }
            if (!TextUtils.isEmpty(phone)) {
                jsonObj.put("phone", phone);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public String getResetPswParams(String email, String vercode, String newpsw) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("email", email);
            jsonObj.put("code", vercode);
            jsonObj.put("password", MD5Util.getMD5String(newpsw));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }

    public String getBoudEmailParams(String email) {
        return getCheckParams(email);
    }

    public String getVerParams(String email) {
        return getCheckParams(email);

    }

    public String getCheckParams(String email) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();

    }

    public String getLoginParams(String email, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", MD5Util.getMD5String(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public String getRegisterParams(String email, String password, String nickname) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("password", MD5Util.getMD5String(password));
            jsonObject.put("nickName", nickname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    //点赞
    public String getParisesParams(int id, int type) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetId", id);
            jsonObject.put("targetType", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //创建直播间
    public String getCreateLiveRoom() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("style", 0);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //查看用户是否为管理员
    public String getUserIdentityStatus(String userID, String channelId) {
        AppLog.i("TAG", "getUserIdentityStatus" + "userID:" + userID + "channelId:" + channelId);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userId", Integer.parseInt(userID));
            jsonObject.put("channelId", Integer.parseInt(channelId));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    //直播送礼物
    public String getSendGiftsParams(String channelId,String toId,String toNickName,String giftId,String amount){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("channelId",channelId);
            jsonObject.put("toId",toId);
            jsonObject.put("toNickName",toNickName);
            jsonObject.put("giftId",giftId);
            jsonObject.put("amount",amount);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    //上传在线人数
    public String getUserOnLines(String onLinesUser) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("onlineUser", onLinesUser);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //修改直播
    private String getAlterLiveRoom(String title, String photo, String announcement, String longitude, String latitude) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", title);
            jsonObject.put("photo", photo);
            jsonObject.put("announcement", announcement);
            jsonObject.put("longitude", longitude);
            jsonObject.put("latitude", latitude);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public Map<String, String> getHeaderParams() {
        Map<String, String> headers = new HashMap<>();
        headers.put("APP_VERSION", AppConfig.getVersionName(context));
        headers.put("DEVICE", "android");
        headers.put("DEVICE_ID", CommonUtil.getUUID(context));
        headers.put("LATITUDE", "38.65777");
        headers.put("LONGITUDE", "104.08296");
        headers.put("DEVICE_WIDTH", DensityUtil.getWindowWidth((Activity) context) + "");
        headers.put("DEVICE_HEIGHT", DensityUtil.getWindowHeight((Activity) context) + "");
        AppLog.i("TAG", "getHeaderParams:" + "APP_VERSION=" + AppConfig.getVersionName(context) + "&" + "DEVICE=" + "android" + "&DEVICE_ID=" + CommonUtil.getUUID(context) +
                "&LATITUDE=38.65777&LONGITUDE=104.08296" + "&DEVICE_WIDTH=" + DensityUtil.getWindowWidth((Activity) context) + "" + "&DEVICE_HEIGHT="
                + DensityUtil.getWindowHeight((Activity) context) + "");
        return headers;
    }


    public Map<String, String> getHeaderParams(int userid, String token) {
        Map<String, String> map = getHeaderParams();
        if (userid != -1) {
            map.put("USER_ID", String.valueOf(userid));
        }
        if (!TextUtils.isEmpty(token)) {
            map.put("TOKEN", token);
        }
        AppLog.print("__USER_ID=" + String.valueOf(userid) + "\n__TOKEN=" + token);
        return map;

    }

    public Map<String, String> getLoginHeaderParams() {
        Map<String, String> map = getHeaderParams();
        map.put("USER_ID", String.valueOf(UserHelper.getUserId(context)));
        map.put("TOKEN", UserHelper.getToken(context));
        return map;

    }


    interface ResultParams {
        String RESULT_CODE = "returnCode";
        String MESSAGE = "message";
        String REULST = "result";
        String PAGE_NUMBER = "pageNumber";
        String TOTAL_PAGES = "totalPages";
        String ROWS = "rows";
    }

    interface RequestCode {
        int REGISTER = 100;
        int LOGIN = 101;
        int CHECK_EMAIL = 102;
        int SEND_VERIFICATION_CODE = 103;
        int RESET_PASSWORD = 104;
        int GET_FAVORITE_ITEMS = 105;
        int MODIFY_USER_PROFILE = 107;
        int GET_USER_PROFILE = 108;
        int BOUDN_EMAIL = 109;
        int GET_MY_COUPON = 110;
        int GET_MY_ORDER = 111;
        int GET_ORDER_DETAIL = 112;
        int GET_DESTINATION_AREAS = 113;
        int GET_DESTINATION_COLLECTIONS = 114;
        int GET_SEARCH_HOT = 116;
        int GET_SEARCH_RESULT = 117;
        int GET_SEARCH_TAG = 118;
        int GET_DESTIANTION_AREA_ROUTES = 119;
        int GET_HOT_ROUTES = 120;
        int GET_HOT_PRODUCTS = 121;
        int GET_AREA_PRODUCTS = 122;
        int GET_MORE_ARITLE = 123;
        int GET_MORE_PRODUCT = 124;
        int GET_MORE_ROUTE = 125;
        int GET_ROUTE_DETAILS = 126;
        int GET_PAY_RESULT = 127;
        int GET_WELCOME_IMGS = 128;
        int CANCEL_ORDER = 129;
        int DEL_ORDER = 130;
        int GET_SYSTM_CONFIG = 131;
        int GET_MY_WALLET = 132;
        int GET_GOLD_LOGS = 133;
        int GET_SOCRE_LOGS = 134;
        int GET_RECHARGE_PRODUCT = 135;
        int CHARGE_GOLD = 136;
        int EXCHARGE_GOLD = 137;
        int SEARCH_LIVE = 138;

        int RECOMMEND = 200;
        int RECOMMEND_AD = 201;
        int SPECIAL_DETAIL = 202;
        int PRODUCT_DETAILS = 203;
        int CANCEL_PARISES = 204;
        int PARISES = 205;

        int ARTICLE_DETAILS = 206;
        int VERSION_CODE = 207;
        int LIVE_LIST = 208;
        int LIVE_RECOMMEND_LIST = 209;
        int LIVE_DETAILS = 210;
        int CREATE_LIVE_ROOM = 211;
        int CANCEL_LIVE_ROOM = 212;
        int ALTER_LIVE_ROOM = 213;
        int GET_TOURIST = 214;
        int IMG_TOKEN = 215;
        int ALTER_LIVE_COVER = 216;
        int LIVE_USER_INFO = 217;
        int LIVE_ADD_ATTENTION = 218;
        int LIVE_CANCEL_ATTENTION = 219;
        int LIVE_FANS_OR_ATTENTION = 220;
        int LIVE_SEARCH_USER = 221;
        int LIVE_ON_LINE_COUNT = 222;
        int LIVE_GIFT_STORE = 223;
        int LIVE_MANAGER_LIST = 224;
        int LIVE_CHECK_USER_IDENTITY = 225;
        int LIVE_ACCREIDT_MANAGER = 226;
        int LIVE_CANCEL_MANAGET_ACCREIDT = 227;
        int LIVE_SEND_GIFTS=228;

    }

}