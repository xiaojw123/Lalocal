package com.lalocal.lalocal.live.entertainment.viewholder;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TViewHolder;
import com.lalocal.lalocal.live.base.util.MessageToBean;
import com.lalocal.lalocal.live.entertainment.activity.LivePlayerBaseActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomUserInfoDialog;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by hzxuwen on 2016/3/24.
 */
public class MsgViewHolderChat extends TViewHolder{
    private ChatRoomMessage message;

    private TextView bodyText;
    private TextView nameText;
    private ImageView itenImage;
    private String code;
    private LinearLayout messageItem;
    private String fromAccount;
    private LinearLayout itemLayout;
    String creatorAccount=null;
    private String userId;
    private String channelId;
    private Container container;
    private String disableSendMsgUserId;
    private String adminSendMsgImUserId;

    @Override
    protected int getResId() {
        return R.layout.message_item_text;
    }
    @Override
    protected void inflate() {
        bodyText = findView(R.id.nim_message_item_text_body);
        nameText = findView(R.id.message_item_name);
        itenImage = findView(R.id.nim_message_item_iv);
        messageItem = findView(R.id.message_item_text_item);
        itemLayout = findView(R.id.message_item_text_wang);
        if(container==null){
            container = new Container(((LivePlayerBaseActivity)context), LiveConstant.ROOM_ID, SessionTypeEnum.ChatRoom, ((LivePlayerBaseActivity)context));
        }

    }

    @Override
    protected void refresh(Object item) {
        String itemContent=null;
        String styles=null;
        String disableSendMsgNickName=null;
        String adminSendMsgNickName=null;
        String giftCount=null;
        String giftName=null;
        String textColor=null;
        disableSendMsgUserId=null;
        adminSendMsgImUserId=null;
        message = (ChatRoomMessage) item;
        fromAccount = message.getFromAccount();
        if (LiveConstant.ROLE==0){
            messageItem.setBackgroundResource(0);//用户端
        }else{//主播端
            messageItem.setBackgroundResource(R.drawable.live_master_im_item_bg);
            messageItem.setPadding(0,DensityUtil.dip2px(context,8),0,DensityUtil.dip2px(context,8));
        }
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
                if("userId".equals(key)){
                    userId = value.toString();
                }
                if("channelId".equals(key)){
                    channelId = value.toString();
                }

                if("disableSendMsgNickName".equals(key)){
                    disableSendMsgNickName=value.toString();
                }
                if ("disableSendMsgUserId".equals(key)) {
                    disableSendMsgUserId = value.toString();
                }
                if("adminSendMsgImUserId".equals(key)){
                    adminSendMsgImUserId = value.toString();
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
                            if("giftCount".equals(key1)){
                                giftCount= value1.toString();
                            }
                            if("giftName".equals(key1)){
                                giftName = value1.toString();
                            }

                        }
                    }
                }
            }
        }

        itenImage.setVisibility(View.GONE);
        switch (styles){
            case "0":
                itemContent = message.getContent();
                if(LiveConstant.ROLE==0){
                    textColor="#ffffff";
                }else{
                    textColor="#190f00";
                }


                break;
            case "2"://点赞
                itemContent="给主播点了个赞";
                textColor="#97d3e9";

                break;
            case "6"://禁言
                itemContent="禁言了"+disableSendMsgNickName;
                textColor="#97d3e9";

                break;
            case "7":
                itemContent="解除了"+disableSendMsgNickName+"的禁言";
                textColor="#97d3e9";
                break;
            case "8":
                itemContent="将"+adminSendMsgNickName+"授权为管理员";
                textColor="#97d3e9";
                break;
            case "9":
                itemContent="取消了"+adminSendMsgNickName+"的管理员权限";
                textColor="#97d3e9";
                break;
            case "10":
                GiftBean messageToGiftBean = MessageToBean.getMessageToGiftBean(message);
                itemContent="给主播送了"+giftCount+"个"+giftName;
                textColor="#97d3e9";
                itenImage.setVisibility(View.VISIBLE);
                messageItem.setBackgroundResource(R.drawable.live_im_gift_item_bg);
                messageItem.setPadding(0,DensityUtil.dip2px(context,8),0,DensityUtil.dip2px(context,8));
                AppLog.i("TAG","获取礼物URL:"+messageToGiftBean.getGiftImage());
                DrawableUtils.displayImg(context,itenImage,messageToGiftBean.getGiftImage());
                break;
            case "13":
                itemContent="分享了直播!";
                textColor="#ffffff";
                break;
            case "100":
                itemContent = "离开了";
                textColor="#ffffff";
                break;
            case "101":
                itemContent = "回来了";
                textColor="#ffffff";
                break;

        }

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId1=null;
                if(adminSendMsgImUserId!=null){
                    userId1=adminSendMsgImUserId;
                }else if(disableSendMsgUserId!=null){
                    userId1=disableSendMsgUserId;
                }else{
                    userId1=userId;
                }

                CustomUserInfoDialog dialog = new CustomUserInfoDialog(context, container,userId, channelId, LiveConstant.ROLE, false,creatorAccount, LiveConstant.ROOM_ID);
                dialog.show();
            }
        });

        AppLog.i("TAG","creatorAccount:"+creatorAccount+"    itemContent:"+itemContent);
        setNameTextView(creatorAccount,itemContent,textColor,styles);

     }

    private SpannableStringBuilder textviewSetContent(String text,String textColor) {
        String[] textContent = text.split(":");
        String nameText = textContent[0];
        String contentText = textContent[1];
        String substring = nameText.substring(0, nameText.length() - 1);
        String str=substring+contentText;
        int length = substring.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.live_im_item_name_color)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor(textColor)), length+1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  style;
    }

    public void setNameTextView(String creatorAccount,String itemContent,String textColor,String styles) {
        String contentItem=null;
        if (message.getMsgType() != MsgTypeEnum.notification) {
            if(message.getRemoteExtension() != null) {
                if(styles.equals("101")||styles.equals("100")){
                    contentItem="主播  :  "+itemContent;
                    messageItem.setBackgroundResource(R.drawable.live_im_gift_item_bg);
                }else {
                    String fromAccount = message.getFromAccount();
                    AppLog.i("TAG","我发消息的账号:"+fromAccount);
                    String account = DemoCache.getAccount();
                    if(fromAccount!=null&&fromAccount.equals(account)){
                        contentItem="我  :  "+itemContent;
                    }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                        contentItem="主播  :  "+itemContent;
                    } else{
                        contentItem=message.getChatRoomMessageExtension().getSenderNick()+"  :  "+itemContent;
                    }
                }
            }
            AppLog.i("TAG","检测用户发消息内容:"+contentItem);
            nameText.setText(textviewSetContent(contentItem,textColor));
        }
    }
}
