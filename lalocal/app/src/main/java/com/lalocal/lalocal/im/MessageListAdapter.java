package com.lalocal.lalocal.im;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.TimeUtil;
import com.lalocal.lalocal.util.AppLog;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.List;

/**
 * Created by xiaojw on 2016/12/15.
 */

public class MessageListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<IMMessage> mMessageList;

    public MessageListAdapter(List<IMMessage> messageList) {
        mMessageList = messageList;
    }

    public void updateItems(List<IMMessage> messageList) {
        mMessageList = messageList;
        notifyDataSetChanged();
    }
    public void setItems(List<IMMessage> messageList){
        mMessageList = messageList;
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
        AppLog.print("onBindViewHolder__items_");
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
        AppLog.print("onBindViewHolder___position___" + position + ", content__" + imMessage.getContent());
        MsgDirectionEnum directEnum = imMessage.getDirect();
        ChatMsgHolder msgHolder = (ChatMsgHolder) holder;
        MsgTypeEnum msgType = imMessage.getMsgType();
        if (directEnum == MsgDirectionEnum.Out) {
            msgHolder.sendCotainer.setVisibility(View.VISIBLE);
            msgHolder.acceptCotainer.setVisibility(View.GONE);
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
//            if (imMessage.getStatus() == MsgStatusEnum.fail) {
//                AppLog.print("msg content___" + imMessage.getContent() + "__failed___");
//                msgHolder.sendAgainBtn.setTag(imMessage);
//                msgHolder.sendAgainBtn.setOnClickListener(sendAgainListener);
//                msgHolder.sendAgainBtn.setVisibility(View.VISIBLE);
//            } else {
//                msgHolder.sendAgainBtn.setVisibility(View.GONE);
//            }
        } else if (directEnum == MsgDirectionEnum.In) {
            msgHolder.sendCotainer.setVisibility(View.GONE);
            msgHolder.acceptCotainer.setVisibility(View.VISIBLE);
            if (msgType == MsgTypeEnum.text) {
                msgHolder.acceptTv.setVisibility(View.VISIBLE);
                msgHolder.acceptImg.setVisibility(View.GONE);
                msgHolder.acceptTv.setText(imMessage.getContent());
            } else if (msgType == MsgTypeEnum.image) {
                msgHolder.acceptImg.setVisibility(View.VISIBLE);
                msgHolder.acceptTv.setVisibility(View.GONE);
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


    }

    private void loadImg(IMMessage imMessage, ImageView img) {
        FileAttachment fileAttachment = (FileAttachment) imMessage.getAttachment();
        if (fileAttachment != null) {
            String imgUrl = fileAttachment.getPath();
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = fileAttachment.getThumbPath();
            }
            if (TextUtils.isEmpty(imgUrl)) {
                imgUrl = fileAttachment.getUrl();
            }
            Glide.with(mContext).load(imgUrl).transform(new CustomShapeTransformation(mContext, R.drawable.androidloading)).error(R.drawable.androidloading).into(img);
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
        return mMessageList != null && mMessageList.size() > 0 ? mMessageList.size() : 0;
    }

    private class ChatMsgHolder extends RecyclerView.ViewHolder {
        ImageView sendImg;
        TextView sendTv;
        TextView timeTv;
        ImageView acceptImg;
        TextView acceptTv;
//        Button sendAgainBtn;
        LinearLayout sendCotainer;
        FrameLayout acceptCotainer;

        private ChatMsgHolder(View itemView) {
            super(itemView);
            sendCotainer = (LinearLayout) itemView.findViewById(R.id.item_message_send_cotainer);
            acceptCotainer = (FrameLayout) itemView.findViewById(R.id.item_message_accept_cotainer);
            sendImg = (ImageView) itemView.findViewById(R.id.item_message_send_img);
            acceptImg = (ImageView) itemView.findViewById(R.id.item_message_accept_img);
            sendTv = (TextView) itemView.findViewById(R.id.item_message_send_tv);
            acceptTv = (TextView) itemView.findViewById(R.id.item_message_accept_tv);
            timeTv = (TextView) itemView.findViewById(R.id.item_message_time_tv);
//            sendAgainBtn = (Button) itemView.findViewById(R.id.item_message_sendagain_btn);
        }
    }

    private View.OnClickListener sendAgainListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if (tag != null) {
                IMMessage imMessage = (IMMessage) tag;
                resendMessage(imMessage);
            }
        }

    };

    private void resendMessage(IMMessage imMessage) {
        if (imMessage.getStatus() == MsgStatusEnum.fail) {
            int index = getItemIndex(imMessage.getUuid());
            if (index >= 0 && index < mMessageList.size()) {
                IMMessage item = mMessageList.get(index);
                item.setStatus(MsgStatusEnum.sending);
                AppLog.print("msgServie__content___" + item.getContent());
                NIMClient.getService(MsgService.class).sendMessage(item, true);
            }
        }
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < mMessageList.size(); i++) {
            IMMessage message = mMessageList.get(i);
            if (TextUtils.equals(uuid, message.getUuid())) {
                return i;
            }
        }
        return -1;

    }


}
