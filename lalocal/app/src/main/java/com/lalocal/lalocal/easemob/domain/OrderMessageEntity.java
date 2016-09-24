package com.lalocal.lalocal.easemob.domain;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 用户订单消息
 */
public class OrderMessageEntity {
    private String title;
    private String des;
    private String imgUrl;
    private String price;


    public OrderMessageEntity(String title,String des, String imgUrl, String price) {
        this.title = title;
        this.des=des;
        this.imgUrl = imgUrl;
        this.price = price;
    }


    public String getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDes(){
        return  des;
    }



    public JSONObject getJSONObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject jsonMsgType = new JSONObject();
            jsonObject.put("title", this.title);
            jsonObject.put("des",this.des);
            jsonObject.put("img_url", this.imgUrl);
            jsonObject.put("price", this.price);
            jsonMsgType.put("order", jsonObject);
            return jsonMsgType;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }





}
