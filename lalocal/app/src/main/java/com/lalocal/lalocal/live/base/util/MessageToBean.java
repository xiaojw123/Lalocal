package com.lalocal.lalocal.live.base.util;

import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by android on 2016/9/17.
 */
public class MessageToBean {

    public static GiftBean getMessageToGiftBean(ChatRoomMessage message){
        GiftBean giftBean = new GiftBean();
        giftBean.setFromAccount(message.getFromAccount());
        Map<String, Object> remoteExtension = message.getRemoteExtension();
        if (remoteExtension != null) {
            Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next1 = iterator.next();
                String key1 = next1.getKey();
                Object value1 = next1.getValue();
                if (key1.equals("giftModel")) {
                    Map<String, Object> map = (Map<String, Object>) value1;
                    Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                    while (mapItem.hasNext()) {
                        Map.Entry<String, Object> next = mapItem.next();
                        String key = next.getKey();
                        Object value = next.getValue();
                        switch (key) {
                            case "headImage":
                                giftBean.setHeadImage(value.toString());
                                break;
                            case "giftImage":
                                giftBean.setGiftImage(value.toString());
                                break;
                            case "giftName":
                                giftBean.setGiftName(value.toString());
                                break;
                            case "giftCount":
                                giftBean.setGiftCount(Integer.parseInt(value.toString()));
                                break;
                            case "code":
                                giftBean.setCode(value.toString());
                                break;
                            case "userName":
                                giftBean.setUserName(value.toString());
                                break;
                            case "userId":
                                giftBean.setUserId(value.toString());
                                break;
                        }
                    }
                }
            }
        }
        return giftBean;
    }

    public static ChallengeDetailsResp.ResultBean getMessageToChallengeBean(ChatRoomMessage message){
        ChallengeDetailsResp.ResultBean resultBean=new  ChallengeDetailsResp.ResultBean();
        Map<String, Object> remoteExtension = message.getRemoteExtension();
        if (remoteExtension != null) {
            Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key= next.getKey();
                Object value= next.getValue();
                if("challengeModel".equals(key)){
                    String model = value.toString();
                    if(model!=null&&!"null".equals(model)){
                        Map<String, Object> map = (Map<String, Object>) value;
                        Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                        while (mapItem.hasNext()){
                            Map.Entry<String, Object> next1 = mapItem.next();
                            String key1 = next1.getKey();
                            Object value1 = next1.getValue();
                            if("content".equals(key1)){

                            }
                            if("targetgold".equals(key1)) {

                            }
                            if("status".equals(key1)){
                                resultBean.setStatus((int)value1);
                            }
                        }
                    }


                }
            }
        }

        return  resultBean;
    }

}
