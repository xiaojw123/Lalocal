package com.lalocal.lalocal.live.entertainment.viewholder;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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
import com.lalocal.lalocal.live.entertainment.ui.CustomNewUserInforDialog;
import com.lalocal.lalocal.live.entertainment.ui.VerticalImageSpan;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import java.util.Iterator;
import java.util.Map;

import static com.lalocal.lalocal.R.drawable.live_im_gift_item_bg;
import static com.lalocal.lalocal.R.drawable.live_im_master_bg;
import static com.lalocal.lalocal.R.drawable.live_master_im_item_bg;

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
    private VerticalImageSpan span;
    private Drawable drawable;


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
        String  textColor="#99190f00";
        disableSendMsgUserId=null;
        adminSendMsgUserId=null;
        nameText.setCompoundDrawables(null,null,null,null);
        message = (ChatRoomMessage) item;
        fromAccount = message.getFromAccount();
        messageItem.setBackgroundResource(live_master_im_item_bg);
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
                    AppLog.i("TAG","disableSendMsgUserId："+disableSendMsgUserId);
                    userId1=disableSendMsgUserId;
                }else if(adminSendMsgUserId!=null&&!adminSendMsgUserId.equals("null")){
                    userId1=adminSendMsgUserId;
                }else{
                    userId1=userId;
                }
                AppLog.i("TAG","获取用户id:"+userId1+"    :"+UserHelper.getUserId(context)+"channelId:"+channelId+"     :"+LivePlayerBaseActivity.CHANNELID_ID);
                if(UserHelper.isLogined(context)&&userId1!=null){
                    if(LiveConstant.isUnDestory){
                        CustomNewUserInforDialog dialog = new CustomNewUserInforDialog(context, container,userId1, LivePlayerBaseActivity.CHANNELID_ID, LiveConstant.ROLE, false,creatorAccount, LiveConstant.ROOM_ID);
                        dialog.show();
                    }
                }else {
                    if(!UserHelper.isLogined(context)){
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

            }
        });




        switch (styles){
            case "0":
                itemContent = message.getContent();
                break;
            case "1":
                itemContent=message.getContent();
                break;
            case "2"://点赞
                itemContent="给主播点了赞";
                break;
            case "6"://禁言
                itemContent=context.getString(R.string.ban)+disableSendMsgNickName;
                messageItem.setBackgroundResource(live_im_master_bg);
                break;
            case "7":
                itemContent=context.getString(R.string.relieve)+disableSendMsgNickName+context.getString(R.string.de_ban);
                messageItem.setBackgroundResource(live_im_master_bg);
                break;
            case "8":
                itemContent= context.getString(R.string.jiang)+adminSendMsgNickName+context.getString(R.string.setting_manage);
                messageItem.setBackgroundResource(live_im_master_bg);
                break;
            case "9":
                itemContent=context.getString(R.string.cancel_le)+adminSendMsgNickName+context.getString(R.string.de_manager);
                messageItem.setBackgroundResource(live_im_master_bg);
                break;
            case "10":
                final   GiftBean messageToGiftBean = MessageToBean.getMessageToGiftBean(message);
                itemContent=context.getString(R.string.send_gift_)+giftCount+context.getString(R.string.ge)+giftName;
                textColor="#ffffff";
                messageItem.setBackgroundResource(live_im_gift_item_bg);
                messageItem.setPadding(0,DensityUtil.dip2px(context,8),0,DensityUtil.dip2px(context,8));
                setNameTextView(creatorAccount,itemContent,textColor,styles,messageToGiftBean);
                return;
            case "12":
                itemContent= message.getContent();
                break;
        }
        AppLog.i("TAG","creatorAccount:"+creatorAccount+"    itemContent:"+itemContent);
        setNameTextView(creatorAccount,itemContent,textColor,styles,null);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void textviewImageContent(final String text, final String textColor, final GiftBean messageToGiftBean, final TextView tv){
            AppLog.i("TAG","礼物消息内容:"+text);
            String[] textContent = text.split(",.,");
            String nameText = textContent[0]+" ";
            String contentText = " "+textContent[1];
            String substring = nameText.substring(0, nameText.length() - 1);
            String str=substring+contentText+"!";
            int length = substring.length();
            Bitmap bitmap = DrawableUtils.loadingBitMap(context,messageToGiftBean.getGiftImage());
        if(bitmap==null){
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
                case LiveConstant.UMBRELLA:
                    drawable=context.getResources().getDrawable(R.drawable.gift_umbrella_48);
                    break;
            }

            int i = DensityUtil.dip2px(context, 25);
            drawable.setBounds(0,0,i, i);
            span=new VerticalImageSpan(drawable);
        }else {
            int i = DensityUtil.dip2px(context, 60);
            Bitmap small = DensityUtil.small(bitmap,i,i);
            span = new VerticalImageSpan(small);
        }
            final SpannableStringBuilder style=new SpannableStringBuilder(str);
            style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.live_im_item_name_color)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(new ForegroundColorSpan(Color.parseColor(textColor)), length+1, str.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            style.setSpan(span,str.length()-1,str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(style==null||style.length()==0){
                tv.setText(textviewSetContent(text,textColor));
            }else {
                tv.setText(style);
            }
    }

    private SpannableStringBuilder textviewSetContent(String text, String textColor) {
        String[] textContent = text.split(",.,");
        String nameText = textContent[0] + " ";
        String contentText = " " + textContent[1];
        String substring = nameText.substring(0, nameText.length() - 1);
        String str = substring + contentText;
        int length = substring.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.live_im_item_name_color)), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor(textColor)), length + 1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return style;
    }

    public void setNameTextView(String creatorAccount,String itemContent,String textColor,String styles,GiftBean messageToGiftBean) {
        String contentItem=null;
        if (message.getMsgType() != MsgTypeEnum.notification) {
            if(message.getRemoteExtension() != null) {
                if(styles.equals("101")||styles.equals("100")){
                    contentItem=context.getString(R.string.master_aplit)+itemContent;
                    messageItem.setBackgroundResource(live_im_gift_item_bg);
                }else {
                    String fromAccount = message.getFromAccount();
                    String account = DemoCache.getAccount();
                    if(fromAccount!=null&&fromAccount.equals(account)){
                        contentItem=context.getString(R.string.me_split)+itemContent;
                    }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                        contentItem=context.getString(R.string.master_aplit)+itemContent;
                        messageItem.setBackgroundResource(live_im_master_bg);
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
