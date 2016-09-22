package com.lalocal.lalocal.live.entertainment.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TViewHolder;
import com.lalocal.lalocal.live.base.util.MessageToGiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.im.session.emoji.MoonUtil;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hzxuwen on 2016/3/24.
 */
public class MsgViewHolderChat extends TViewHolder {
    private ChatRoomMessage message;

    private TextView bodyText;
    private TextView nameText;
    private ImageView itenImage;
    private String code;
    private LinearLayout messageItem;
    private String fromAccount;
    public static final String NIM_CHAT_MESSAGE_INFO="nimlivesenfmessage";
    @Override
    protected int getResId() {
        return R.layout.message_item_text;
    }

    @Override
    protected void inflate() {
        bodyText = findView(R.id.nim_message_item_text_body);
        nameText = findView(R.id.message_item_name);
        itenImage = findView(R.id.nim_message_item_iv);
        messageItem = findView(R.id.message_item_text_wang);
    }

    @Override
    protected void refresh(Object item) {

        String creatorAccount=null;
        String styles=null;
        String disableSendMsgNickName=null;
        String adminSendMsgNickName=null;
        String content=null;
        message = (ChatRoomMessage) item;
        fromAccount = message.getFromAccount();
        Map<String, Object> remoteExtension = message.getRemoteExtension();
        if (remoteExtension != null) {
            Iterator<Map.Entry<String, Object>> iterator = remoteExtension.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if("creatorAccount".equals(key)){
                    creatorAccount = value.toString();
                }
                if("style".equals(key)){
                  styles = value.toString();
                }
                if("disableSendMsgNickName".equals(key)){
                    disableSendMsgNickName=value.toString();
                }
                if("adminSendMsgNickName".equals(key)){
                    adminSendMsgNickName=value.toString();
                }
                if ("giftModel".equals(key)) {
                    String text = value.toString();
                    if (text != null && !"null".equals(text)) {
                        Map<String, Object> map = (Map<String, Object>) value;
                        Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                        while (mapItem.hasNext()) {
                            Map.Entry<String, Object> next1 = mapItem.next();
                            String key1 = next1.getKey();
                            Object value1 = next1.getValue();
                            if ("code".equals(key1)) {
                                code = value1.toString();
                            }
                        }
                    }
                }

            }
        }


        itenImage.setVisibility(View.GONE);
        switch (styles){
            case "0":
                content = message.getContent();
                bodyText.setTextColor(Color.WHITE);
                break;
            case "2"://点赞
                content="给主播点了个赞";
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
               /* itenImage.setVisibility(View.VISIBLE);
                itenImage.setImageResource(R.drawable.);*/
                break;
            case "6"://禁言
                content="禁言了"+disableSendMsgNickName;
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "7":
                content="解除了"+disableSendMsgNickName+"的禁言";
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "8":
                content="将"+adminSendMsgNickName+"授权为管理员";
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "9":
                setNameTextView("主播");
                content="取消了"+adminSendMsgNickName+"的管理员权限";
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "10":
                GiftBean messageToGiftBean = MessageToGiftBean.getMessageToGiftBean(message);
                // ("001".equals(code) ? "鲜花" : ("002".equals(code) ? "行李箱" : ("003".equals(code)?"飞机":"神秘礼物")));
                content="给主播送了";
                bodyText.setTextColor(Color.parseColor("#97d3e9"));
                itenImage.setVisibility(View.VISIBLE);
                DrawableUtils.displayImg(context,itenImage,messageToGiftBean.getGiftImage());
                break;
        }


        setNameTextView(creatorAccount);

        MoonUtil.identifyFaceExpression(DemoCache.getContext(), bodyText,content , ImageSpan.ALIGN_BASELINE);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());
    }



    public void setNameTextView(String creatorAccount) {
        if (message.getMsgType() != MsgTypeEnum.notification) {
            // 聊天室中显示姓名
            if (message.getChatRoomMessageExtension() != null) {
                String senderNick = message.getChatRoomMessageExtension().getSenderNick();
                String fromAccount = message.getFromAccount();
                AppLog.i("TAG","fromAccount"+fromAccount+"   creatorAccount:"+creatorAccount);
                String name = DemoCache.getUserInfo().getName();
                if(senderNick!=null&&senderNick.equals(name)){
                    nameText.setText("我");
                }else if(fromAccount.equals(creatorAccount)){
                    nameText.setText("主播");
                } else{
                    nameText.setText(message.getChatRoomMessageExtension().getSenderNick());
                }

            } else {
                nameText.setText(DemoCache.getUserInfo() == null ? DemoCache.getAccount() : DemoCache.getUserInfo().getName());

            }

            if(message.getRemoteExtension() != null) {
                String fromAccount = message.getFromAccount();

                AppLog.i("TAG","fromAccount"+fromAccount+"   creatorAccount:"+creatorAccount);
                String account = DemoCache.getUserInfo().getAccount();
                if(fromAccount!=null&&fromAccount.equals(account)){
                    nameText.setText("我");
                }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                    nameText.setText("主播");
                } else{
                    nameText.setText(message.getChatRoomMessageExtension().getSenderNick());
                }

            }
      }
    }
}
