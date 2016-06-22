package com.lalocal.lalocal.service;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;


import com.lalocal.lalocal.model.SpectialDetailsResp;

import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.MD5Util;

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
public class ContentService {
    public static final String CONTENT_TYPE = "application/json";
    private ICallBack callBack;
    RequestQueue requestQueue;
    ContentResponse response;
    Context context;

    public ContentService(Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        this.context = context;
    }

    public void setCallBack(ICallBack callBack) {
        this.callBack = callBack;
    }

    public void getMyFavorite(int userid, String token, int pageNumber, int pageSize) {
        //pageNumber=1&pageSize=10
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_FAVORITE_ITEMS);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.GET_MY_FARORITE_ITEMS + "pageNumber=" + pageNumber + "&pageSize=" + pageSize, response, response);
        request.setHeaderParams(getLoginedHeaderParams(userid, token));
        requestQueue.add(request);
    }


    public void getUserProfile(int userid, String token) {
        AppLog.print("userid___" + userid + ", token___" + token);
        if (callBack != null) {
            response = new ContentResponse(RequestCode.GET_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.GET, AppConfig.GET_USER_PROFILE_URL, response, response);
        request.setHeaderParams(getLoginedHeaderParams(userid, token));
        requestQueue.add(request);
    }

    //修改的用户资料
    public void modifyUserProfile(String nickanme, int sex, String areaCode, String phone, int userid, String token) {
        AppLog.print("modifyUserProfile____token_" + token);
        if (callBack != null) {
            response = new ContentResponse(RequestCode.MODIFY_USER_PROFILE);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.MODIFY_USER_PROFILE_URL, response, response);
        request.setHeaderParams(getLoginedHeaderParams(userid, token));
        request.setBodyParams(getModifyUserProfileParams(nickanme, sex, areaCode, phone));
        requestQueue.add(request);
    }


    private String getUploadBodyParmas(String photo) {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("avatar", photo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObj.toString();
    }


    public void resetPasword(String email, String vercode, String newpsw) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RESET_PASSWORD);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.RESET_PASSWORD_URL, response, response);
        request.setBodyParams(getResetPswParams(email, vercode, newpsw));
        requestQueue.add(request);

    }

    public void boundEmail(String email, int userid, String token) {
        AppLog.print("bound____email__" + email);
        if (callBack != null) {
            response = new ContentResponse(RequestCode.BOUDN_EMAIL);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, AppConfig.BOUND_EMAIL_URL, response, response);
        request.setHeaderParams(getLoginedHeaderParams(userid, token));
        request.setBodyParams(getBoudEmailParams(email));
        requestQueue.add(request);
    }


    //发送验证码
    public void sendVerificationCode(String email) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.SEND_VERIFICATION_CODE);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.SEND_VERIFICATION_URL, response, response);
        request.setBodyParams(getVerParams(email));
        requestQueue.add(request);

    }

    //判断邮箱是否被注册过
    public void checkEmail(String email) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CHECK_EMAIL);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.CHECK_EMAIL_URL, response, response);
        request.setBodyParams(getCheckParams(email));
        requestQueue.add(request);
    }


    //登录
    public void login(final String email, final String password) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LOGIN);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.LOGIN_URL, response, response);
        request.setBodyParams(getLoginParams(email, password));
        requestQueue.add(request);
    }

    //注册
    public void register(final String email, final String password, final String nickname) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.REGISTER);
            response.setUserInfo(email, password);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, AppConfig.REGISTER_URL, response, response);
        request.setBodyParams(getRegisterParams(email, password, nickname));
        requestQueue.add(request);
    }

    //推荐
    public void recommentList(final int pageSize, final int pageNumber) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RECOMMEND);
            response.setRecommend(pageSize, pageNumber);
        }
        String getParameter = "pageSize=" + pageSize + "&pageNumber=" + pageNumber;
        ContentRequest request = new ContentRequest(AppConfig.RECOMMEND_URL + getParameter, response, response);
        requestQueue.add(request);

    }

    //推荐页广告位
    public void recommendAd() {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RECOMMEND_AD);
        }

        ContentRequest request = new ContentRequest(AppConfig.RECOMMEND_AD, response, response);
        requestQueue.add(request);
    }
    //specialdetail
    public void specialDetail(String rowId){
        if(callBack!=null){
            response=new ContentResponse(RequestCode.SPECIAL_DETAIL);
        }
        ContentRequest request = new ContentRequest(AppConfig.SPECIAL_DETAILS_URL+rowId,response,response);
        requestQueue.add(request);
    }

    //产品详情
    public  void productDetails(String targetId){
        if(callBack !=null){
            response=new ContentResponse(RequestCode.PRODUCT_DETAILS);
        }
        ContentRequest contentRequest = new ContentRequest(AppConfig.PRODUCTIONS_DETILS + targetId, response, response);
        requestQueue.add(contentRequest);
    }

    class ContentRequest extends StringRequest {
        private String body;
        private Map<String, String> headerParams;
        private byte[] bodyParams;
        private String bodyType;

        public ContentRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
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


        public void setBodyParams(byte[] bodyParams) {
            this.bodyParams = bodyParams;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return bodyParams != null ? bodyParams : body.getBytes();
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        //request body type:json
        @Override
        public String getBodyContentType() {
            return bodyType != null ? bodyType : CONTENT_TYPE;
        }

    }


    class ContentResponse implements Response.Listener<String>, Response.ErrorListener {

        private String email, psw;
        private int resultCode;
        private int pageSize, pageNumber;

        public ContentResponse(int resultCode) {
            this.resultCode = resultCode;
        }

        public void setUserInfo(String email, String psw) {
            this.email = email;
            this.psw = psw;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setRecommend(int pageSize, int pageNumber) {
            this.pageSize = pageSize;
            this.pageNumber = pageNumber;

        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            AppLog.print("volley error——————" + volleyError);
            if (callBack != null) {
                callBack.onRequestFailed(volleyError.getMessage());
            }

        }

        @Override
        public void onResponse(String json) {
            switch (resultCode) {
                case RequestCode.GET_FAVORITE_ITEMS:
                    responseGetFavoriteItems(json);
                    break;
                case RequestCode.REGISTER:
                    responseRegister(json);
                    break;
                case RequestCode.LOGIN:
                    responseLogin(json);
                    break;
                case RequestCode.CHECK_EMAIL:
                    responseCheckMail(json);
                    break;
                case RequestCode.SEND_VERIFICATION_CODE:
                    responseSendVerCode(json);
                    break;
                case RequestCode.RESET_PASSWORD:
                    responseResetPassword(json);
                    break;
                case RequestCode.UPLOAD_HEADER:
                    responseUploadHeader(json);
                    break;
                case RequestCode.MODIFY_USER_PROFILE:
                    responseModifyUserProfile(json);
                    break;

                case RequestCode.GET_USER_PROFILE:
                    responseGetUserProfile(json);
                    break;
                case RequestCode.BOUDN_EMAIL:
                    responseBoundEmail(json);
                    break;

                case RequestCode.RECOMMEND:
                    responseRecommend(json);
                    break;
                case RequestCode.RECOMMEND_AD:
                    responseRecommendAd(json);
                    break;
                case RequestCode.SPECIAL_DETAIL:
                    responseSpecialDetail(json);
                    break;
                case RequestCode.PRODUCT_DETAILS:
                    responseProductDetails(json);
                    break;
            }

        }

        private void responseGetFavoriteItems(String json) {
            AppLog.print("responseGetFavorite___" + json);
            try {
                JSONObject jsonObj = new JSONObject(json);
                int code = jsonObj.optInt(ResultParams.RESULT_CODE);
                if (code == 0) {
                    JSONObject resutJson = jsonObj.optJSONObject(ResultParams.REULST);
                    int totalPages=jsonObj.optInt("totalPages");
                    int totalRows=jsonObj.optInt("totalRows");
                    JSONArray rows = resutJson.optJSONArray("rows");
                    Gson gson = new Gson();
                    List<FavoriteItem> items = new ArrayList<>();
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject rowJsonObj = rows.getJSONObject(i);
                        FavoriteItem item = gson.fromJson(rowJsonObj.toString(), FavoriteItem.class);
                        items.add(item);
                    }
                    callBack.onGetFavoriteItem(items,totalPages,totalRows);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void responseBoundEmail(String json) {
            AppLog.print("boundEmail____" + json);
            try {
                JSONObject jsonObj = new JSONObject(json);
                int code = jsonObj.optInt(ResultParams.RESULT_CODE);
                String message = jsonObj.optString(ResultParams.MESSAGE);
                callBack.onSendActivateEmmailComplete(code, message);
            } catch (JSONException e) {
                e.printStackTrace();


            }

        }

        private void responseGetUserProfile(String json) {
            AppLog.print("getUserprofile___" + json);
            try {
                JSONObject jsonObj = new JSONObject(json);
                int code = jsonObj.optInt(ResultParams.RESULT_CODE);
                JSONObject resultJson = jsonObj.optJSONObject(ResultParams.REULST);
                Gson gson = new Gson();
                LoginUser user = gson.fromJson(resultJson.toString(), LoginUser.class);
                callBack.onGetUserProfile(code, user);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void responseModifyUserProfile(String json) {
            AppLog.print("modifyuserprofile____" + json);

            try {
                JSONObject jsonObj = new JSONObject(json);
                int code = jsonObj.optInt(ResultParams.RESULT_CODE);
                JSONObject resultJson = jsonObj.optJSONObject(ResultParams.REULST);
                Gson gson = new Gson();
                LoginUser user = gson.fromJson(resultJson.toString(), LoginUser.class);
                callBack.onModifyUserProfile(code, user);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void responseUploadHeader(String json) {
            AppLog.print("responseUploadHeader json___" + json);

        }


        private void responseResetPassword(String json) {
            AppLog.print("reset psw__" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = jsonObject.optInt(ResultParams.RESULT_CODE, 1);
                String message = jsonObject.optString(ResultParams.MESSAGE);
                AppLog.print("responseResetPassword code____" + code);
                callBack.onResetPasswordComplete(code, message);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void responseSendVerCode(String json) {
            AppLog.print("sendVerCode json___" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = jsonObject.optInt(ResultParams.RESULT_CODE, -1);
                callBack.onSendVerCode(code, email);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void responseCheckMail(String json) {
            AppLog.print("CheckEmail  json__" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String reutrnCode = jsonObject.optString(ResultParams.RESULT_CODE);
                JSONObject resultJson = jsonObject.optJSONObject(ResultParams.REULST);
                if ("0".equals(reutrnCode) && resultJson != null) {
                    callBack.onCheckEmail(true, email);
                } else {
                    callBack.onCheckEmail(false, email);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

        private void responseLogin(String json) {
            AppLog.print("login Result___s" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String returnCode = jsonObject.optString(ResultParams.RESULT_CODE);
                String message = jsonObject.optString(ResultParams.MESSAGE);
                JSONObject resutJson = jsonObject.optJSONObject(ResultParams.REULST);
                User user = null;
                if (resutJson != null) {
                    Gson gson = new Gson();
                    user = gson.fromJson(resutJson.toString(), User.class);
                }
                callBack.onLoginSucess(returnCode, message, user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void responseRegister(String json) {
            try {
                AppLog.print("responseRegister___" + json);
                JSONObject jsonObject = new JSONObject(json);
                String resCode = jsonObject.optString(ResultParams.RESULT_CODE, "1");
                String message = jsonObject.optString(ResultParams.MESSAGE, "注册失败");
                JSONObject jsonObj = jsonObject.optJSONObject(ResultParams.REULST);
                int userid = jsonObj.optInt("id");
                String token = jsonObj.optString("token");
                callBack.onResigterComplete(resCode, message, email, psw, userid, token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //产品详情
    private void responseProductDetails(String json) {
        
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
    public void responseSpecialDetail(String json){
        SpectialDetailsResp spectialDetailsResp = new Gson().fromJson(json, SpectialDetailsResp.class);
        if(spectialDetailsResp!=null){
            callBack.onRecommendSpecial(spectialDetailsResp);
        }

    }

    public String getModifyUserProfileParams(String nickname, int sex, String areaCode, String phone) {
        JSONObject jsonObj = new JSONObject();
        try {
            if (!TextUtils.isEmpty(nickname)) {
                AppLog.print("nickname___");
                jsonObj.put("nickName", nickname);
            }
            if (sex == 1 || sex == 0) {
                boolean flag = sex == 1 ? true : false;
                AppLog.print("sex___" + flag);
                jsonObj.put("sex", flag);
            }
            if (!TextUtils.isEmpty(areaCode)) {
                jsonObj.put("areaCode", areaCode);
            }
            if (!TextUtils.isEmpty(phone)) {
                AppLog.print("phone___");
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


    public Map<String, String> getHeaderParams() {
        Map<String, String> headers = new HashMap<>();
        headers.put("APP_VERSION", AppConfig.getVersionName(context));
        headers.put("DEVICE", "android");
        headers.put("DEVICE_ID", CommonUtil.getUUID(context));
        AppLog.print("verision__" + AppConfig.getVersionName(context) + ", device_id__" + CommonUtil.getUUID(context));
        return headers;
    }


    public Map<String, String> getLoginedHeaderParams(int userid, String token) {
        Map<String, String> map = getHeaderParams();
        if (userid != -1) {
            map.put("USER_ID", String.valueOf(userid));
        }
        if (!TextUtils.isEmpty(token)) {
            map.put("TOKEN", token);
        }
        AppLog.print("userid__" + userid + ", token___" + token);
        return map;

    }


    interface ResultParams {
        public static final String RESULT_CODE = "returnCode";
        public static final String MESSAGE = "message";
        public static final String REULST = "result";
    }

    interface RequestCode {
        public static final int REGISTER = 100;
        public static final int LOGIN = 101;
        public static final int CHECK_EMAIL = 102;
        public static final int SEND_VERIFICATION_CODE = 103;
        public static final int RESET_PASSWORD = 104;
        public static final int GET_FAVORITE_ITEMS = 105;
        public static final int UPLOAD_HEADER = 106;
        public static final int MODIFY_USER_PROFILE = 107;
        public static final int GET_USER_PROFILE = 108;
        public static final int BOUDN_EMAIL = 109;


        public static final int RECOMMEND=200;
        public static final int RECOMMEND_AD=201;
        public static final int SPECIAL_DETAIL=202;
        public static final int PRODUCT_DETAILS=203;




    }


}
