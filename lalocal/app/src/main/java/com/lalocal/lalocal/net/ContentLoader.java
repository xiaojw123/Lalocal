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
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.MD5Util;
import com.lalocal.lalocal.view.dialog.CustomDialog;

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

    public void getOrderDetail(int id) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_ORDER_DETAIL);
        }

        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getOrderItemsUrl() + "/" + id, response, response);

        request.setHeaderParams(getHeaderParamsWithUserId(UserHelper.getUserId(context), UserHelper.getToken(context)));

        requestQueue.add(request);
    }

    public void getMyOrder(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MY_ORDER);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getOrderItemsUrl(), response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
        requestQueue.add(request);

    }

    public void getMyCoupon(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_MY_COUPON);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getCouponItemsUrl(), response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
        requestQueue.add(request);
    }

    public void getMyFavorite(int userid, String token, int pageNumber, int pageSize) {
        //pageNumber=1&pageSize=10
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_FAVORITE_ITEMS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getFavoriteItemsUrl() + "pageNumber=" + pageNumber + "&pageSize=" + pageSize, response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
        requestQueue.add(request);
    }


    public void getUserProfile(int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.getUserProfileUrl(), response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
        requestQueue.add(request);
    }

    //修改的用户资料
    public void modifyUserProfile(String nickanme, int sex, String areaCode, String phone, int userid, String token) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.MODIFY_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT,AppConfig.getUserProfileModifyUrl(), response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
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
        request.setHeaderParams(getHeaderParamsWithUserId(userid, token));
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
        request.setHeaderParams(getHeaderParamsWithUserId(-1, null));

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

        ContentRequest contentRequest = new ContentRequest(Request.Method.DELETE,AppConfig.getParisesCancelUrl() + praiseId, response, response);

        contentRequest.setHeaderParams(getHeaderParamsWithUserId(UserHelper.getUserId(context), UserHelper.getToken(context)));

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
        request.setHeaderParams(getHeaderParamsWithUserId(-1, null));
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

        AppLog.i("TAG","specialDetail:"+AppConfig.getSepcailDetailUrl());
        ContentRequest request = new ContentRequest(AppConfig.getSepcailDetailUrl()+ rowId, response, response);
        request.setHeaderParams(getHeaderParamsWithUserId(UserHelper.getUserId(context), UserHelper.getToken(context)));
        requestQueue.add(request);
    }


    //产品详情
    public void productDetails(String targetId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.PRODUCT_DETAILS);
        }
        ContentRequest contentRequest = new ContentRequest(AppConfig.getProductDetailsUrl() + targetId, response, response);
        requestQueue.add(contentRequest);
    }
    //版本更新
    public void versionUpdate(String versionCode){
        if (callBack != null) {
            response = new ContentResponse(RequestCode.VERSION_CODE);
        }
        ContentRequest contentRequest = new ContentRequest(AppConfig.VERSION_UPDATE+versionCode, response, response);
        contentRequest.setHeaderParams(getHeaderParamsWithUserId(-1, null));
        requestQueue.add(contentRequest);
    }

    public void articleDetails(String targetId) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.ARTICLE_DETAILS);
        }

        ContentRequest contentRequest = new ContentRequest(AppConfig.getArticleDetailsUrl() +targetId, response, response);

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

        public ContentResponse(int resultCode) {
            this.resultCode = resultCode;
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
            callBack.onRequestFailed();
            if (responseView != null) {
                responseView.setEnabled(true);
            }
            CommonUtil.showToast(context, "网络请求异常", Toast.LENGTH_LONG);
        }

        @Override
        public void onResponse(String json) {
            AppLog.print("onResponse__");
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
                    callBack.onRequestFailed();
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
                    case RequestCode.GET_ORDER_DETAIL:
                        responseGetOrderDetail(jsonObj);
                        break;
                    case RequestCode.GET_MY_ORDER:
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
                        AppLog.print("get user proflie json___"+json);
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
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
            AppLog.print("checkMail___");
            callBack.onCheckEmail(email);
        }

        private void responseLogin(JSONObject jsonObject) {
            JSONObject resutJson = jsonObject.optJSONObject(ResultParams.REULST);
            User user = null;
            if (resutJson != null) {
                Gson gson = new Gson();
                user = gson.fromJson(resutJson.toString(), User.class);
            }
            callBack.onLoginSucess(user);
            Bundle bundle = new Bundle();
            bundle.putBoolean(KeyParams.IS_LOGIN, true);
            bundle.putString(KeyParams.EMAIL, email);
            bundle.putString(KeyParams.PASSWORD, psw);
            bundle.putInt(KeyParams.USERID, user.getId());
            bundle.putString(KeyParams.TOKEN, user.getToken());
            UserHelper.saveLoginInfo(context, bundle);
        }

        private void responseRegister(JSONObject jsonObject) {
            JSONObject jsonObj = jsonObject.optJSONObject(ResultParams.REULST);
            if (jsonObj != null) {
                int userid = jsonObj.optInt("id");
                String token = jsonObj.optString("token");
                callBack.onResigterComplete(email, psw, userid, token);
            }

        }

        //点赞
        private void responseParises(String json) {
            AppLog.print("TAG" + "responseParises" + json);
            if (!UserHelper.favorites.contains(targetId)) {
                UserHelper.favorites.add(targetId);
            }
            PariseResult pariseResult = new Gson().fromJson(json, PariseResult.class);
            callBack.onInputPariseResult(pariseResult);
        }

        //取消赞
        private void responseCancelParises(String json) {
            AppLog.print("TAG" + "responseCancelParises" + json);
            if (UserHelper.favorites.contains(targetId)) {
                UserHelper.favorites.remove(targetId);
            }
            PariseResult pariseResult = new Gson().fromJson(json, PariseResult.class);
            callBack.onPariseResult(pariseResult);
        }

        //产品详情
        private void responseProductDetails(String json) {
            AppLog.i("TAG", "responseProductDetails:" + json);
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

        //specialdetail
        public void responseSpecialDetail(String json) {
            AppLog.i("TAG", "responseSpecialDetail:"+json);
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
            AppLog.i("TAG","responseVersion:"+json);
            VersionInfo versionInfo = new Gson().fromJson(json, VersionInfo.class);
            if(versionInfo.getReturnCode()==0){
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


    public Map<String, String> getHeaderParams() {
        Map<String, String> headers = new HashMap<>();
        headers.put("APP_VERSION", AppConfig.getVersionName(context));
        headers.put("DEVICE", "android");
        headers.put("DEVICE_ID", CommonUtil.getUUID(context));
        return headers;
    }


    public Map<String, String> getHeaderParamsWithUserId(int userid, String token) {
        Map<String, String> map = getHeaderParams();
        if (userid != -1) {
            map.put("USER_ID", String.valueOf(userid));
        }
        if (!TextUtils.isEmpty(token)) {
            map.put("TOKEN", token);
        }
        return map;

    }

    interface ResultParams {
        String RESULT_CODE = "returnCode";
        String MESSAGE = "message";
        String REULST = "result";
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
        int RECOMMEND = 200;
        int RECOMMEND_AD = 201;
        int SPECIAL_DETAIL = 202;
        int PRODUCT_DETAILS = 203;
        int CANCEL_PARISES = 204;
        int PARISES = 205;

        int ARTICLE_DETAILS=206;
        int VERSION_CODE=207;


    }

}