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
        int challengeStatus=-1;
        String challengeContent=null;
        message = (ChatRoomMessage) item;
        fromAccount = message.getFromAccount();
        messageItem.setBackgroundResource(0);
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
          /*      if("challengeModel".equals(key)){
                    String model = value.toString();
                    if(model!=null&&!"null".equals(model)){
                        Map<String, Object> map = (Map<String, Object>) value;
                        Iterator<Map.Entry<String, Object>> mapItem = map.entrySet().iterator();
                        while (mapItem.hasNext()){
                            Map.Entry<String, Object> next1 = mapItem.next();
                            String key1 = next1.getKey();
                            Object value1 = next1.getValue();
                            if("content".equals(key1)){
                                challengeContent = value1.toString();

                            }
                            if("targetgold".equals(key1)) {
                                challengeContent = challengeContent + value1.toString();
                            }
                            if("status".equals(key1)){
                                challengeStatus=(int)value1;
                            }
                        }
                    }
                }*/

            }
        }


        itenImage.setVisibility(View.GONE);
        switch (styles){
            case "0":
                itemContent = message.getContent();
                textColor="#ffffff";

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
    /*        case "4":
                if(challengeStatus==0){

                }
                switch (challengeStatus){
                    case 0:
                        itemContent="发起挑战  "+challengeContent;
                        textColor="#ffaa2a";
                        messageItem.setBackgroundResource(R.drawable.task_message_lv);
                        break;
                    case 1:
                        itemContent="开始挑战任务";
                        textColor="#ffe400";
                        messageItem.setBackgroundResource(R.drawable.task_massage_lv_yellow);
                        break;
                    case 2:
                        itemContent="完成挑战任务";
                        textColor="#ffe400";
                        messageItem.setBackgroundResource(R.drawable.task_massage_lv_yellow);
                        break;
                    case 3:
                        break;
                    case 4:
                        itemContent="放弃挑战，所有金额返还!";
                        textColor="#ffe400";
                        messageItem.setBackgroundResource(R.drawable.task_massage_lv_yellow);
                        break;

                }

                break;*/
        }


        setNameTextView(creatorAccount,itemContent,textColor,styles);

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

    public void setNameTextView(String creatorAccount,String itemContent,String textColor,String styles) {
        String contentItem=null;
        if (message.getMsgType() != MsgTypeEnum.notification) {
            if(message.getRemoteExtension() != null) {

                if(styles.equals("101")||styles.equals("100")){
                    contentItem="主播  :  "+itemContent;
                }else {
                    String fromAccount = message.getFromAccount();
                    String account = DemoCache.getUserInfo().getAccount();
                    if(fromAccount!=null&&fromAccount.equals(account)){
                        contentItem="我  :  "+itemContent;
                    }else if(fromAccount!=null&&fromAccount.equals(creatorAccount)){
                        contentItem="主播  :  "+itemContent;
                    } else{
                        contentItem=message.getChatRoomMessageExtension().getSenderNick()+"  :  "+itemContent;
                    }
                }

            }
            nameText.setText(textviewSetContent(contentItem,textColor));
        }
    }
}
