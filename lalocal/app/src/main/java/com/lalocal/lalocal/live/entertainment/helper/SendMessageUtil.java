package com.lalocal.lalocal.live.entertainment.helper;

import com.lalocal.lalocal.live.entertainment.model.LiveMessage;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by android on 2016/9/20.
 */
public class SendMessageUtil {
    public  static IMMessage sendMessage(String account,String messageContent,String roomId,String userAccount,LiveMessage liveMessage){

        IMMessage message = ChatRoomMessageBuilder.createChatRoomTextMessage(account,messageContent);
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(roomId,userAccount);
        Map<String, Object> ext = new HashMap<>();
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext.put("style", liveMessage.getStyle());
            ext.put("disableSendMsgUserId",liveMessage.getDisableSendMsgUserId());
            ext.put("disableSendMsgNickName",liveMessage.getDisableSendMsgNickName());
            ext.put("adminSendMsgUserId",liveMessage.getAdminSendMsgUserId());
            ext.put("adminSendMsgNickName",liveMessage.getAdminSendMsgNickName());
            ext.put("adminSendMsgImUserId",liveMessage.getAdminSendMsgImUserId());
            ext.put("userId",liveMessage.getUserId());
            ext.put("creatorAccount",liveMessage.getCreatorAccount());
            ext.put("onlineNum",liveMessage.getOnlineNum());

            if(liveMessage.getGiftModel()!=null){
                Map<String, Object> ext1 = new HashMap<>();
                ext1.put("headImage",liveMessage.getGiftModel().getHeadImage());
                ext1.put("giftImage",liveMessage.getGiftModel().getGiftImage());
                ext1.put("giftName", liveMessage.getGiftModel().getGiftName());
                ext1.put("giftCount",String.valueOf(liveMessage.getGiftModel().getGiftCount()));
                ext1.put("userId",liveMessage.getGiftModel().getUserId());
                ext1.put("code",liveMessage.getGiftModel().getCode());
                ext1.put("userName",liveMessage.getGiftModel().getUserName());
                ext.put("giftModel", ext1);
            }

            message.setRemoteExtension(ext);
        }


        return message;
    }
}
