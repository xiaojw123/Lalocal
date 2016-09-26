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
import com.lalocal.lalocal.live.base.util.MessageToGiftBean;
import com.lalocal.lalocal.live.entertainment.model.GiftBean;
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
        String itemContent=null;
        String creatorAccount=null;
        String styles=null;
        String disableSendMsgNickName=null;
        String adminSendMsgNickName=null;
        String giftCount=null;
        String content=null;
        String giftName=null;
        String textColor=null;

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
                textColor="#ffffff";
                //  bodyText.setTextColor(Color.WHITE);
                break;
            case "2"://点赞
                itemContent="给主播点了个赞";
                textColor="#97d3e9";
                //  bodyText.setTextColor(Color.parseColor("#97d3e9"));
               /* itenImage.setVisibility(View.VISIBLE);
                itenImage.setImageResource(R.drawable.);*/
                break;
            case "6"://禁言
                itemContent="禁言了"+disableSendMsgNickName;
                textColor="#97d3e9";
                //  bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "7":
                itemContent="解除了"+disableSendMsgNickName+"的禁言";
                textColor="#97d3e9";
                //  bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "8":
                itemContent="将"+adminSendMsgNickName+"授权为管理员";
                textColor="#97d3e9";
                //  bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "9":

                itemContent="取消了"+adminSendMsgNickName+"的管理员权限";
                textColor="#97d3e9";
                // setNameTextView("主播",);
                //   bodyText.setTextColor(Color.parseColor("#97d3e9"));
                break;
            case "10":
                GiftBean messageToGiftBean = MessageToGiftBean.getMessageToGiftBean(message);
                itemContent="给主播送了"+giftCount+"个"+giftName;
                textColor="#97d3e9";
                //   bodyText.setTextColor(Color.parseColor("#97d3e9"));
                itenImage.setVisibility(View.VISIBLE);
                DrawableUtils.displayImg(context,itenImage,messageToGiftBean.getGiftImage());
                break;
        }


        setNameTextView(creatorAccount,itemContent,textColor);

        //   MoonUtil.identifyFaceExpression(DemoCache.getContext(), bodyText,content , ImageSpan.ALIGN_BASELINE);
        //   bodyText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private SpannableStringBuilder textviewSetContent(String text,String textColor) {
        String[] textContent = text.split(":");
        String nameText = textContent[0];
        String contentText = textContent[1];
        String substring = nameText.substring(0, nameText.length() - 1);
        String str=substring+contentText;
        int length = substring.length();
        SpannableStringBuilder style=new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.parseColor("#97d3e9")), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new ForegroundColorSpan(Color.parseColor(textColor)), length+1, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  style;
    }

    public void setNameTextView(String creatorAccount,String itemContent,String textColor) {
        String contentItem=null;
        if (message.getMsgType() != MsgTypeEnum.notification) {
            // 聊天室中显示姓名
  /*          if (message.getChatRoomMessageExtension() != null) {
                String senderNick = message.getChatRoomMessageExtension().getSenderNick();
                String fromAccount = message.getFromAccount();
                AppLog.i("TAG","fromAccount"+fromAccount+"   creatorAccount:"+creatorAccount);
                String name = DemoCache.getUserInfo().getName();
                if(senderNick!=null&&senderNick.equals(name)){
                    contentItem="我  :"+itemContent;
                  //  nameText.setText("我");
                }else if(fromAccount.equals(creatorAccount)){
                    nameText.setText("主播");
                    contentItem="我  :"+itemContent;
                } else{
                    contentItem=message.getChatRoomMessageExtension().getSenderNick()+" :"+itemContent;
                   // nameText.setText(message.getChatRoomMessageExtension().getSenderNick());
                }

            } else {
              //  nameText.setText(DemoCache.getUserInfo() == null ? DemoCache.getAccount() : DemoCache.getUserInfo().getName());
                contentItem=( DemoCache.getUserInfo() == null ? DemoCache.getAccount() : DemoCache.getUserInfo().getName())+":"+itemContent;
            }*/

            //    nameText.setText(textviewSetContent(contentItem));
            if(message.getRemoteExtension() != null) {
                String fromAccount = message.getFromAccount();

                //   AppLog.i("TAG","fromAccount"+fromAccount+"   creatorAccount:"+creatorAccount);
                String account = DemoCache.getUserInfo().getAccount();
                if(fromAccount!=null&&fromAccount.equals(account)){
                    contentItem="我  :  "+itemContent;
                    //   nameText.setText("我");
                }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                    contentItem="主播  :  "+itemContent;
                    //  nameText.setText("主播");
                } else{
                    contentItem=message.getChatRoomMessageExtension().getSenderNick()+"  :  "+itemContent;
                    // nameText.setText(message.getChatRoomMessageExtension().getSenderNick());
                }

            }
            nameText.setText(textviewSetContent(contentItem,textColor));
        }
    }
}
