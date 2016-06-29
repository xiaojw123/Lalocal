package com.lalocal.lalocal.util;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.api.client.json.Json;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2016/6/27.
 */
public class ParisesNetwork {
    private Context mContext;
    private RequestQueue requestQueue;
    private int id;
    private int type;

    public ParisesNetwork(Context mContext, int targetId, int targetType) {
        this.mContext=mContext;
        this.id=id;
        this.type=type;
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(mContext);
        }
        getJsonResquest(id,type);
    }



    public JSONObject getJson(int id,int type){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("targetId",id);
            jsonObject.put("targetType", type);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  jsonObject;
    }

    public void getJsonResquest(int id,int type){

        JsonRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AppConfig.PRAISES, getJson(id,type),
                new Response.Listener() {
                    @Override
                    public void onResponse(Object o) {
                       if(onNetworkResponse!=null){
                          onNetworkResponse.networkResult(o);
                       }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(onNetworkResponse!=null){
                    onNetworkResponse.networkResult(error);
                }
            }
        }) {
            @Override
            public Map getHeaders() {
                HashMap headers = new HashMap();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("DEVICE_ID", CommonUtil.getUUID(mContext));

                return headers;
            }

            @Override
            public byte[] getBody() {
                return super.getBody();
            }
        };
        requestQueue.add(jsonRequest);
    }

   private OnNetworkResponse onNetworkResponse;
    public interface OnNetworkResponse{
        void networkResult(Object o);
    }
    public void networkResult( OnNetworkResponse onNetworkResponse){
        this.onNetworkResponse=onNetworkResponse;
    }
}
