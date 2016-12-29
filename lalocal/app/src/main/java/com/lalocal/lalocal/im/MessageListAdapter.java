package com.lalocal.lalocal.im;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.TimeUtil;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by xiaojw on 2016/12/15.
 */

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<IMMessage> mMessageList;
    LinearLayoutManager layoutManager;

    public MessageListAdapter(List<IMMessage> messageList) {
        mMessageList = messageList;
    }

    public void updateItems(List<IMMessage> messageList) {
        mMessageList = messageList;
        notifyDataSetChanged();
    }

    public void setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        LayoutInflater lInflater = LayoutInflater.from(mContext);
        View itemView = lInflater.inflate(R.layout.item_chat_message, parent, false);
        return new ChatMsgHolder(itemView);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.print("onBindViewHolder___position___" + position);
        if (mMessageList == null || mMessageList.size() < 1) {
            return;
        }
        final IMMessage imMessage = mMessageList.get(position);
        if (imMessage == null) {
            return;
        }
        IMMessage anchor = null;
        if (position > 0) {
            anchor = mMessageList.get(position - 1);
        }
        MsgDirectionEnum directEnum = imMessage.getDirect();
        ChatMsgHolder msgHolder = (ChatMsgHolder) holder;
        MsgTypeEnum msgType = imMessage.getMsgType();
        if (directEnum == MsgDirectionEnum.Out) {
            msgHolder.acceptTv.setVisibility(View.GONE);
            msgHolder.acceptImg.setVisibility(View.GONE);
            if (msgType == MsgTypeEnum.text) {
                msgHolder.sendTv.setVisibility(View.VISIBLE);
                msgHolder.sendImg.setVisibility(View.GONE);
                msgHolder.sendTv.setText(imMessage.getContent());
            } else if (msgType == MsgTypeEnum.image) {
                msgHolder.sendImg.setVisibility(View.VISIBLE);
                msgHolder.sendTv.setVisibility(View.GONE);
                loadImg(imMessage, msgHolder.sendImg);
                msgHolder.sendImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WatchMessagePictureActivity.start(mContext, imMessage);
                    }
                });
            }
        } else if (directEnum == MsgDirectionEnum.In) {
            msgHolder.sendTv.setVisibility(View.GONE);
            msgHolder.sendImg.setVisibility(View.GONE);
            if (msgType == MsgTypeEnum.text) {
                msgHolder.acceptTv.setVisibility(View.VISIBLE);
                msgHolder.acceptImg.setVisibility(View.GONE);
                msgHolder.acceptTv.setText(imMessage.getContent());
            } else if (msgType == MsgTypeEnum.image) {
                msgHolder.acceptImg.setVisibility(View.VISIBLE);
                msgHolder.acceptTv.setVisibility(View.GONE);
//                FileAttachment fileAttachment = (FileAttachment) imMessage.getAttachment();
//                AppLog.print("fileAttachMent__" + fileAttachment);
//                if (fileAttachment != null) {
//                    AppLog.print("url____" + fileAttachment.getUrl());
//                    Glide.with(mContext).load(fileAttachment.getUrl()).transform(new CustomShapeTransformation(mContext, R.drawable.androidloading)).into(msgHolder.acceptImg);
//                    String thumbPath = fileAttachment.getThumbPath();
//                    if (TextUtils.isEmpty(thumbPath)) {
//                        AppLog.print("");
//                        NIMClient.getService(MsgService.class).downloadAttachment(imMessage, true);
//                    }
//                }
                loadImg(imMessage, msgHolder.acceptImg);
                msgHolder.acceptImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WatchMessagePictureActivity.start(mContext, imMessage);
                    }
                });
            }

        }


        if (needShowTime(imMessage, anchor)) {
            msgHolder.timeTv.setVisibility(View.VISIBLE);
            msgHolder.timeTv.setText(TimeUtil.getTimeShowString(imMessage.getTime(), false));
        } else {
            msgHolder.timeTv.setVisibility(View.GONE);
        }
        if (position == getItemCount()-1) {
            AppLog.print("last size___");
            if (layoutManager != null) {
                AppLog.print("scoll___to pos_");
                layoutManager.scrollToPositionWithOffset(getItemCount() - 1, 0);
            }
        }


    }

    private void loadImg(IMMessage imMessage, ImageView img) {
        FileAttachment fileAttachment = (FileAttachment) imMessage.getAttachment();
        if (fileAttachment != null) {
            Glide.with(mContext).load(fileAttachment.getUrl()).transform(new CustomShapeTransformation(mContext, R.drawable.androidloading)).error(R.drawable.androidloading).into(img);
            String thumbPath = fileAttachment.getThumbPath();
            if (TextUtils.isEmpty(thumbPath)) {
                NIMClient.getService(MsgService.class).downloadAttachment(imMessage, true);
            }
        }
    }

    private boolean needShowTime(IMMessage message, IMMessage anchor) {
        if (anchor == null) {
            return true;
        } else {
            long time = anchor.getTime();
            long now = message.getTime();

            if (now - time == 0) {
                // 消息撤回时使用
                return false;
            } else if (now - time < (long) (5 * 60 * 1000)) {
                return false;
            } else {
                return true;
            }
        }
    }


    @Override
    public int getItemCount() {
        AppLog.print("getItemCount__" + mMessageList.size());
        return mMessageList != null && mMessageList.size() > 0 ? mMessageList.size() : 0;
    }

    private class ChatMsgHolder extends RecyclerView.ViewHolder {
        ImageView sendImg;
        TextView sendTv;
        TextView timeTv;
        ImageView acceptImg;
        TextView acceptTv;

        private ChatMsgHolder(View itemView) {
            super(itemView);
            sendImg = (ImageView) itemView.findViewById(R.id.item_message_send_img);
            acceptImg = (ImageView) itemView.findViewById(R.id.item_message_accept_img);
            sendTv = (TextView) itemView.findViewById(R.id.item_message_send_tv);
            acceptTv = (TextView) itemView.findViewById(R.id.item_message_accept_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_message_time_tv);
        }
    }

}
