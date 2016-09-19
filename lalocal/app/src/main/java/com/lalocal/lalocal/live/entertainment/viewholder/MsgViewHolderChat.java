package com.lalocal.lalocal.live.entertainment.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.ui.TViewHolder;
import com.lalocal.lalocal.live.im.session.emoji.MoonUtil;
import com.lalocal.lalocal.util.AppLog;
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

    @Override
    protected int getResId() {
        return R.layout.message_item_text;
    }

    @Override
    protected void inflate() {
        bodyText = findView(R.id.nim_message_item_text_body);
        nameText = findView(R.id.message_item_name);
        itenImage = findView(R.id.nim_message_item_iv);
    }

    @Override
    protected void refresh(Object item) {
        String creatorAccount=null;
        message = (ChatRoomMessage) item;
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
            }
        }
        setNameTextView(creatorAccount);
        String content = message.getContent();
        if("点赞".equals(content.trim())){
            content="给主播点了个赞";
            bodyText.setTextColor(Color.parseColor("#97d3e9"));
        }

        if("点赞2".equals(content.trim())){
            content="给主播点了个赞";
            bodyText.setTextColor(Color.parseColor("#97d3e9"));
        }else if("给主播点了个赞".equals(content.trim())){
            bodyText.setTextColor(Color.parseColor("#97d3e9"));
        }else if(message.getContent().indexOf("禁言了")!=-1||message.getContent().indexOf("解除了")!=-1||message.getContent().indexOf("的管理员权限")!=-1||message.getContent().indexOf("为管理员")!=-1){
            bodyText.setTextColor(Color.parseColor("#97d3e9"));
        } else{
            bodyText.setTextColor(Color.WHITE);
        }
        MoonUtil.identifyFaceExpression(DemoCache.getContext(), bodyText,content , ImageSpan.ALIGN_BOTTOM);
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
