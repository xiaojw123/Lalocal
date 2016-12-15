package com.lalocal.lalocal.live.entertainment.viewholder;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TViewHolder;
import com.lalocal.lalocal.live.base.util.MessageToBean;
import com.lalocal.lalocal.live.entertainment.activity.LivePlayerBaseActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomUserInfoDialog;
import com.lalocal.lalocal.live.entertainment.ui.VerticalImageSpan;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
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
    private TextView nameText;

    private String code;
    private LinearLayout messageItem;
    private String fromAccount;
    private LinearLayout itemLayout;
    String creatorAccount=null;
    private String userId;
    private String channelId;
    private Container container;
    private String disableSendMsgUserId;
    private String adminSendMsgUserId;


    @Override
    protected int getResId() {
        return R.layout.message_item_text;
    }
    @Override
    protected void inflate() {
        nameText = findView(R.id.message_item_name);
        messageItem = findView(R.id.message_item_text_item);
        itemLayout = findView(R.id.message_item_text_wang);
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
        adminSendMsgUserId=null;
        nameText.setCompoundDrawables(null,null,null,null);
        message = (ChatRoomMessage) item;
        fromAccount = message.getFromAccount();
        messageItem.setBackgroundResource(R.drawable.live_master_im_item_bg);
        messageItem.setPadding(0,DensityUtil.dip2px(context,8),0,DensityUtil.dip2px(context,8));
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

                if ("disableSendMsgUserId".equals(key)) {
                    disableSendMsgUserId = value.toString();
                }
                if("adminSendMsgUserId".equals(key)){
                    adminSendMsgUserId = value.toString();
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

        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId1=null;
                if(container==null){
                    container = new Container(((LivePlayerBaseActivity)context), LiveConstant.ROOM_ID, SessionTypeEnum.ChatRoom, ((LivePlayerBaseActivity)context));
                }
                if(disableSendMsgUserId!=null&&!disableSendMsgUserId.equals("null")){
                    userId1=disableSendMsgUserId;
                }else if(adminSendMsgUserId!=null&&!adminSendMsgUserId.equals("null")){
                    userId1=adminSendMsgUserId;
                }else{
                    userId1=userId;
                }
                if(UserHelper.isLogined(context)){
                    if(LiveConstant.isUnDestory){
                        CustomUserInfoDialog dialog = new CustomUserInfoDialog(context, container,userId1, channelId, LiveConstant.ROLE, false,creatorAccount, LiveConstant.ROOM_ID);
                        dialog.show();
                    }
                }else {
                    final CustomChatDialog customDialog = new CustomChatDialog(context);
                    customDialog.setContent(context.getString(R.string.live_login_hint));
                    customDialog.setCancelable(false);
                    customDialog.setCancelBtn(context.getString(R.string.live_canncel), null);
                    customDialog.setSurceBtn(context.getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            DemoCache.setLoginStatus(false);
                            LLoginActivity.startForResult(context, 701);
                        }
                    });
                    customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            LiveConstant.USER_INFO_FIRST_CLICK = true;
                        }
                    });
                    customDialog.show();
                }

            }
        });




        switch (styles){
            case "0":
                itemContent = message.getContent();
                textColor="#99190f00";
                break;
            case "1":
                itemContent=message.getContent();
                textColor="#99190f00";
                break;
            case "2"://点赞
                itemContent="给主播点了个赞";
                textColor="#99190f00";
                break;
            case "6"://禁言
                itemContent="禁言了"+disableSendMsgNickName;
                textColor="#99190f00";
                messageItem.setBackgroundResource(R.drawable.live_im_master_bg);
                break;
            case "7":
                itemContent= "解除了"+disableSendMsgNickName+"的禁言";
                textColor="#99190f00";
                messageItem.setBackgroundResource(R.drawable.live_im_master_bg);
                break;
            case "8":
                itemContent= "将"+adminSendMsgNickName+"授权为管理员";
                messageItem.setBackgroundResource(R.drawable.live_im_master_bg);
                textColor="#99190f00";
                break;
            case "9":
                itemContent="取消了"+adminSendMsgNickName+"的管理员权限";
                textColor="#99190f00";
                messageItem.setBackgroundResource(R.drawable.live_im_master_bg);
                break;
            case "10":
              final   GiftBean messageToGiftBean = MessageToBean.getMessageToGiftBean(message);
                itemContent="给主播送了"+giftCount+"个"+giftName;
                textColor="#ffffff";
                messageItem.setBackgroundResource(R.drawable.live_im_gift_item_bg);
                messageItem.setPadding(0,DensityUtil.dip2px(context,8),0,DensityUtil.dip2px(context,8));
                setNameTextView(creatorAccount,itemContent,textColor,styles,messageToGiftBean);
                return;
            case "12":
                itemContent= message.getContent();
                textColor="#99190f00";
                break;
        }
        AppLog.i("TAG","creatorAccount:"+creatorAccount+"    itemContent:"+itemContent);
        setNameTextView(creatorAccount,itemContent,textColor,styles,null);
    }

    private void textviewImageContent(final String text,final String textColor,final GiftBean messageToGiftBean,final TextView tv){
        Drawable drawable =null;
        switch (messageToGiftBean.getCode()){
            case LiveConstant.ROSE:
               drawable=context.getResources().getDrawable(R.drawable.gift_rose_48);
            break;
            case LiveConstant.GLASSES:
                drawable=context.getResources().getDrawable(R.drawable.gift_glasses_48);
             break;
            case LiveConstant.PLAN:
                drawable=context.getResources().getDrawable(R.drawable.gift_plane_48);
                break;
            case LiveConstant.FACE:
                drawable=context.getResources().getDrawable(R.drawable.gift_face_48);
                break;
            case LiveConstant.TRAVELLINGCASE:
                drawable=context.getResources().getDrawable(R.drawable.gift_travellingcase_48);
                break;
            case LiveConstant.STAR:
                drawable=context.getResources().getDrawable(R.drawable.gift_start_48);
                break;
            case LiveConstant.FRUIT:
                drawable=context.getResources().getDrawable(R.drawable.gift_fruit_48);
                break;
            case LiveConstant.WATER:
                drawable=context.getResources().getDrawable(R.drawable.gift_water_48);
                break;
            case LiveConstant.SUN_CREAM:
                drawable=context.getResources().getDrawable(R.drawable.gift_sun_cream_48);
                break;
        }
        AppLog.i("TAG","礼物条目内容："+(drawable==null?"为空":"不为空")+text+"   ||||:"+messageToGiftBean.getGiftName());
        int i = DensityUtil.dip2px(context, 25);
        drawable.setBounds(0,0,i, i);
        String[] textContent = text.split(",.,");
        String nameText = textContent[0];
        String contentText = textContent[1];
        String substring = nameText.substring(0, nameText.length() - 1);
        String str=substring+contentText+"!";
        int length = substring.length();
        VerticalImageSpan span= new VerticalImageSpan(drawable);

        final SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.live_im_item_name_color)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor(textColor)), length+1, str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(span,str.length()-1,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(style);
    }

    private SpannableStringBuilder textviewSetContent(String text, String textColor) {
        String[] textContent = text.split(",.,");
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


    public void setNameTextView(String creatorAccount,String itemContent,String textColor,String styles,GiftBean messageToGiftBean) {
        String contentItem=null;
        if (message.getMsgType() != MsgTypeEnum.notification) {
            if(message.getRemoteExtension() != null) {
                if(styles.equals("101")||styles.equals("100")){
                    contentItem="主播 :  ,.,  "+itemContent;
                    messageItem.setBackgroundResource(R.drawable.live_im_gift_item_bg);
                }else {
                    String fromAccount = message.getFromAccount();
                    String account = DemoCache.getAccount();
                    if(fromAccount!=null&&fromAccount.equals(account)){
                        contentItem="我 :  ,.,  "+itemContent;
                    }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                        contentItem="主播 :  ,.,  "+itemContent;
                        messageItem.setBackgroundResource(R.drawable.live_im_master_bg);
                        textColor="#99190f00";
                    } else{
                        contentItem=message.getChatRoomMessageExtension().getSenderNick()+" :  ,.,  "+itemContent;
                    }
                }
            }
            if( messageToGiftBean==null){
                nameText.setText(textviewSetContent(contentItem,textColor));
            }else {
                textviewImageContent(contentItem,textColor,messageToGiftBean,nameText);
            }

        }
    }
}
