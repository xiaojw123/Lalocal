package com.lalocal.lalocal.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.APPcofig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.MD5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
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

    public void resetPasword(String email, String vercode, String newpsw) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.RESET_PASSWORD);
        }
        ContentRequest request = new ContentRequest(Request.Method.PUT, APPcofig.RESET_PASSWORD_URL, response, response);
        request.setBodyParams(getResetPswParams(email, vercode, newpsw));
        requestQueue.add(request);

    }

    //发送验证码
    public void sendVerificationCode(String email) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.SEND_VERIFICATION_CODE);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, APPcofig.SEND_VERIFICATION_URL, response, response);
        request.setBodyParams(getVerParams(email));
        requestQueue.add(request);

    }

    //判断邮箱是否被注册过
    public void checkEmail(String email) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.CHECK_EMAIL);
            response.setEmail(email);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, APPcofig.CHECK_EMAIL_URL, response, response);
        request.setBodyParams(getCheckParams(email));
        requestQueue.add(request);
    }


    //登录
    public void login(final String email, final String password) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.LOGIN);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, APPcofig.LOGIN_URL, response, response);
        request.setBodyParams(getLoginParams(email, password));
        requestQueue.add(request);
    }

    //注册
    public void register(final String email, final String password, final String nickname) {
        if (callBack != null) {
            response = new ContentResponse(RequestCode.REGISTER);
            response.setUserInfo(email, password);
        }
        ContentRequest request = new ContentRequest(Request.Method.POST, APPcofig.REGISTER_URL, response, response);
        request.setBodyParams(getRegisterParams(email, password, nickname));
        requestQueue.add(request);
    }


    class ContentRequest extends StringRequest {
        private String body;

        public ContentRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            this(Method.GET, url, listener, errorListener);
        }

        public ContentRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return getHeaderParams();
        }

        public void setBodyParams(String body) {
            this.body = body;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return body.getBytes();
        }

        //request body type:json
        @Override
        public String getBodyContentType() {
            return CONTENT_TYPE;
        }

    }


    class ContentResponse implements Response.Listener<String>, Response.ErrorListener {

        private String email, psw;
        private int resultCode;

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
            }

        }

        private void responseResetPassword(String json) {
            AppLog.print("reset psw__" + json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                int code = jsonObject.optInt(ResultParams.RESULT_CODE, 1);
                String message = jsonObject.optString(ResultParams.MESSAGE);
                AppLog.print("responseResetPassword code____"+code);
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
                JSONObject jsonObject = new JSONObject(json);
                String resCode = jsonObject.optString(ResultParams.RESULT_CODE, "1");
                String message = jsonObject.optString(ResultParams.MESSAGE, "注册失败");
                callBack.onResigterComplete(resCode, message, email, psw);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        headers.put("APP_VERSION", APPcofig.getVersionName(context));
        headers.put("DEVICE", "android");
        headers.put("DEVICE_ID", CommonUtil.getUUID(context));
        return headers;
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
    }


}
