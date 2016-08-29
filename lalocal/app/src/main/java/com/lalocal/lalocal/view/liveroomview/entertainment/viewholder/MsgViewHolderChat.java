package com.lalocal.lalocal.view.liveroomview.entertainment.viewholder;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.base.ui.TViewHolder;
import com.lalocal.lalocal.view.liveroomview.im.session.emoji.MoonUtil;
import com.netease.nimlib.sdk.chatroom.constant.MemberType;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;

/**
 * Created by hzxuwen on 2016/3/24.
 */
public class MsgViewHolderChat extends TViewHolder {
    private ChatRoomMessage message;

    private TextView bodyText;
    private TextView nameText;

    @Override
    protected int getResId() {
        return R.layout.message_item_text;
    }

    @Override
    protected void inflate() {
        bodyText = findView(R.id.nim_message_item_text_body);
        nameText = findView(R.id.message_item_name);
    }

    @Override
    protected void refresh(Object item) {
        message = (ChatRoomMessage) item;
        setNameTextView();
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
        }else{
            bodyText.setTextColor(Color.WHITE);
        }
        MoonUtil.identifyFaceExpression(DemoCache.getContext(), bodyText,content , ImageSpan.ALIGN_BOTTOM);
        bodyText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setNameTextView() {
        if (message.getMsgType() != MsgTypeEnum.notification) {
            // 聊天室中显示姓名
            if (message.getChatRoomMessageExtension() != null) {
                String senderNick = message.getChatRoomMessageExtension().getSenderNick();
                String name = DemoCache.getUserInfo().getName();
                if(senderNick!=null&&senderNick.equals(name)){
                    nameText.setText("我");
                }else{
                    nameText.setText(message.getChatRoomMessageExtension().getSenderNick());
                }

            } else {

                nameText.setText(DemoCache.getUserInfo() == null ? DemoCache.getAccount() : DemoCache.getUserInfo().getName());
                AppLog.i("TAG","MsgViewHolderChat:getAccount:"+DemoCache.getAccount());
                if(DemoCache.getUserInfo()!=null){
                    AppLog.i("TAG","MsgViewHolderChat:getUserInfo:"+DemoCache.getUserInfo().getName());
                }
            }

            if(message.getRemoteExtension() != null && message.getRemoteExtension().containsKey("type")) {
                MemberType type = MemberType.typeOfValue((Integer) message.getRemoteExtension().get("type"));
              /*  区别身份的颜色
              nameText.setTextColor(context.getResources().getColor(type == MemberType.CREATOR ?
                        R.color.color_yellow_FAEC55 : R.color.color_green_C2FF9A));*/
            }
      }
    }
}
