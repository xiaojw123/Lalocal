package com.lalocal.lalocal.live.entertainment.module;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.ui.TAdapterDelegate;
import com.lalocal.lalocal.live.base.ui.TViewHolder;
import com.lalocal.lalocal.live.entertainment.ui.MessageListViewEx;
import com.lalocal.lalocal.live.entertainment.viewholder.ChatRoomMsgViewHolderFactory;
import com.lalocal.lalocal.live.im.config.UserPreferences;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.live.im.session.adapter.MsgAdapter;
import com.lalocal.lalocal.live.im.session.viewholder.MsgViewHolderBase;
import com.lalocal.lalocal.live.im.ui.dialog.EasyAlertDialog;
import com.lalocal.lalocal.live.im.ui.dialog.EasyAlertDialogHelper;
import com.lalocal.lalocal.live.im.ui.listview.AutoRefreshListView;
import com.lalocal.lalocal.live.im.ui.listview.ListViewUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.ChatRoomService;
import com.netease.nimlib.sdk.chatroom.ChatRoomServiceObserver;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.msg.attachment.AudioAttachment;
import com.netease.nimlib.sdk.msg.attachment.FileAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 聊天室消息收发模块
 * Created by huangjun on 2016/1/27.
 */
public class ChatRoomMsgListPanel implements TAdapterDelegate {
    private static final int MESSAGE_CAPACITY = 500;
    public static final String NIM_CHAT_MESSAGE_INFO="nimlivesenfmessage";
    // container
    private Container container;
    private View rootView;
    private Handler uiHandler;

    // message list view
    private MessageListViewEx messageListView;
    private LinkedList<IMMessage> items;
    private MsgAdapter adapter;
    private String content;
    private   Context context;
    public ChatRoomMsgListPanel(Container container, View rootView, String content, Context context) {
        this.container = container;
        this.rootView = rootView;
        this.content=content;
        this.context=context;
        init();
    }

    public void onResume() {
        setEarPhoneMode(UserPreferences.isEarPhoneModeEnable());
    }

    public void onPause() {

    }

    public void onDestroy() {
        registerObservers(false);
    }

    public boolean onBackPressed() {
        uiHandler.removeCallbacks(null);

        return false;
    }

    private void init() {
        initListView();
        this.uiHandler = new Handler();
        registerObservers(true);
    }


    private void initListView() {
        items = new LinkedList<>();
        adapter = new MsgAdapter(container.activity, items, this);
        adapter.setEventListener(new MsgItemEventListener());

        messageListView = (MessageListViewEx) rootView.findViewById(R.id.messageListView);
       View view= View.inflate(container.activity, R.layout.chat_head_items,null);
       TextView headInfos= (TextView) view.findViewById(R.id.chat_info_head);

        headInfos.setText("公告： "+content);
        messageListView.addHeaderView(view);
        messageListView.requestDisallowInterceptTouchEvent(true);

        messageListView.setMode(AutoRefreshListView.Mode.START);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            messageListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        // adapter
        messageListView.setAdapter(adapter);

        messageListView.setListViewEventListener(new MessageListViewEx.OnListViewEventListener() {
            @Override
            public void onListViewStartScroll() {
                container.proxy.shouldCollapseInputPanel();
            }
        });
    }





    private OnChatRoomMessageItemClickListener onChatRoomMessageItemClickListener;

    public interface OnChatRoomMessageItemClickListener {
       void onMessageListItem(String userId);
    }

    public void setOnChatRoomMessageItemClickListener(OnChatRoomMessageItemClickListener onChatRoomMessageItemClickListener) {
        this.onChatRoomMessageItemClickListener = onChatRoomMessageItemClickListener;
    }


    // 刷新消息列表
    public void refreshMessageList() {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void scrollToBottom() {
        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ListViewUtil.scrollToBottom(messageListView);
            }
        }, 200);
    }

    public void onIncomingMessage(List<ChatRoomMessage> messages) {
        boolean needScrollToBottom = ListViewUtil.isLastMessageVisible(messageListView);
        boolean needRefresh = false;
        List<IMMessage> addedListItems = new ArrayList<>(messages.size());
        for (IMMessage message : messages) {
            if (message.getMsgType() != MsgTypeEnum.text) {
                continue;
            }
            if (isMyMessage(message)) {
                saveMessage(message, false);
                addedListItems.add(message);
                needRefresh = true;
            }
        }
        if (needRefresh) {
            adapter.notifyDataSetChanged();
        }

        // incoming messages tip
        IMMessage lastMsg = messages.get(messages.size() - 1);
        if (isMyMessage(lastMsg) && needScrollToBottom) {
            ListViewUtil.scrollToBottom(messageListView);
        }
    }

    // 发送消息后，更新本地消息列表
    public void onMsgSend(IMMessage message) {
        saveMessage(message, false);
        List<IMMessage> addedListItems = new ArrayList<>(1);
        addedListItems.add(message);
        adapter.notifyDataSetChanged();
        ListViewUtil.scrollToBottom(messageListView);
    }

    //显示本地消息
    public  void onSendLocalMsg(IMMessage message){

    }
    private void saveMessage(final List<IMMessage> messageList, boolean addFirst) {
        if (messageList == null || messageList.isEmpty()) {
            return;
        }

        for (IMMessage msg : messageList) {
            saveMessage(msg, addFirst);
        }
    }

    public void saveMessage(final IMMessage message, boolean addFirst) {
        if (message == null) {
            return;
        }

        if (items.size() >= MESSAGE_CAPACITY) {
            items.poll();
        }

        if (addFirst) {
            items.add(0, message);
        } else {
            items.add(message);
        }
    }

    /**
     * *************** implements TAdapterDelegate ***************
     */
    @Override
    public int getViewTypeCount() {
        return ChatRoomMsgViewHolderFactory.getViewTypeCount();
    }

    @Override
    public Class<? extends TViewHolder> viewHolderAtPosition(int position) {
        return ChatRoomMsgViewHolderFactory.getViewHolderByType(items.get(position));
    }

    @Override
    public boolean enabled(int position) {
        return true;
    }


    /**
     * *************** MessageLoader ***************
     */
    private class MessageLoader implements AutoRefreshListView.OnRefreshListener {

        private static final int LOAD_MESSAGE_COUNT = 10;

        private IMMessage anchor;

        private boolean firstLoad = true;

        public MessageLoader() {
            anchor = null;
            loadFromLocal();
        }

        private RequestCallback<List<ChatRoomMessage>> callback = new RequestCallbackWrapper<List<ChatRoomMessage>>() {
            @Override
            public void onResult(int code, List<ChatRoomMessage> messages, Throwable exception) {
                if (messages != null) {
                    onMessageLoaded(messages);
                } else {
                    messageListView.onRefreshComplete(LOAD_MESSAGE_COUNT, LOAD_MESSAGE_COUNT, false);
                }
            }
        };

        private void loadFromLocal() {
            messageListView.onRefreshStart(AutoRefreshListView.Mode.START);
            NIMClient.getService(ChatRoomService.class).pullMessageHistory(container.account, anchor().getTime(), LOAD_MESSAGE_COUNT)
                    .setCallback(callback);
        }

        private IMMessage anchor() {
            if (items.size() == 0) {
                return (anchor == null ? ChatRoomMessageBuilder.createEmptyChatRoomMessage(container.account, 0) : anchor);
            } else {
                return items.get(0);
            }
        }

        /**
         * 历史消息加载处理
         */
        private void onMessageLoaded(List<ChatRoomMessage> messages) {
            int count = messages.size();

            if (items.size() > 0) {
                // 在第一次加载的过程中又收到了新消息，做一下去重
                for (IMMessage message : messages) {
                    for (IMMessage item : items) {
                        if (item.isTheSame(message)) {
                            items.remove(item);
                            break;
                        }
                    }
                }
            }

            List<IMMessage> result = new ArrayList<>();
            for (IMMessage message : messages) {
                result.add(message);
            }
            saveMessage(result, true);

            // 如果是第一次加载，updateShowTimeItem返回的就是lastShowTimeItem
            if (firstLoad) {
                ListViewUtil.scrollToBottom(messageListView);
            }

            refreshMessageList();
            messageListView.onRefreshComplete(count, LOAD_MESSAGE_COUNT, true);

            firstLoad = false;
        }

        /**
         * *************** OnRefreshListener ***************
         */
        @Override
        public void onRefreshFromStart() {
            loadFromLocal();
        }

        @Override
        public void onRefreshFromEnd() {
        }
    }


    /**
     * ************************* 观察者 ********************************
     */

    private void registerObservers(boolean register) {
        ChatRoomServiceObserver service = NIMClient.getService(ChatRoomServiceObserver.class);
        service.observeMsgStatus(messageStatusObserver, register);
        service.observeAttachmentProgress(attachmentProgressObserver, register);
    }

    /**
     * 消息状态变化观察者
     */
    Observer<ChatRoomMessage> messageStatusObserver = new Observer<ChatRoomMessage>() {
        @Override
        public void onEvent(ChatRoomMessage message) {
            if (isMyMessage(message)) {
                onMessageStatusChange(message);
            }
        }
    };

    /**
     * 消息附件上传/下载进度观察者
     */
    Observer<AttachmentProgress> attachmentProgressObserver = new Observer<AttachmentProgress>() {
        @Override
        public void onEvent(AttachmentProgress progress) {
            onAttachmentProgressChange(progress);
        }
    };

    private void onMessageStatusChange(IMMessage message) {
        int index = getItemIndex(message.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            item.setStatus(message.getStatus());
            item.setAttachStatus(message.getAttachStatus());
            if ( item.getAttachment() instanceof AudioAttachment) {
                item.setAttachment(message.getAttachment());
            }
            refreshViewHolderByIndex(index);
        }
    }

    private void onAttachmentProgressChange(AttachmentProgress progress) {
        int index = getItemIndex(progress.getUuid());
        if (index >= 0 && index < items.size()) {
            IMMessage item = items.get(index);
            float value = (float) progress.getTransferred() / (float) progress.getTotal();
            adapter.putProgress(item, value);
            refreshViewHolderByIndex(index);
        }
    }

    public boolean isMyMessage(IMMessage message) {
        return message.getSessionType() == container.sessionType
                && message.getSessionId() != null
                && message.getSessionId().equals(container.account);
    }

    /**
     * 刷新单条消息
     *
     * @param index
     */
    private void refreshViewHolderByIndex(final int index) {
        container.activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (index < 0) {
                    return;
                }

                Object tag = ListViewUtil.getViewHolderByIndex(messageListView, index);
                if (tag instanceof MsgViewHolderBase) {
                    MsgViewHolderBase viewHolder = (MsgViewHolderBase) tag;

                    viewHolder.refreshCurrentItem();
                }
            }
        });
    }

    private int getItemIndex(String uuid) {
        for (int i = 0; i < items.size(); i++) {
            IMMessage message = items.get(i);
            if (TextUtils.equals(message.getUuid(), uuid)) {
                return i;
            }
        }

        return -1;
    }

    private class MsgItemEventListener implements MsgAdapter.ViewHolderEventListener {



        @Override
        public void onFailedBtnClick(IMMessage message) {
            if (message.getDirect() == MsgDirectionEnum.Out) {
                // 发出的消息，如果是发送失败，直接重发，否则有可能是漫游到的多媒体消息，但文件下载
                if (message.getStatus() == MsgStatusEnum.fail) {
                    resendMessage(message); // 重发
                } else {
                    if (message.getAttachment() instanceof FileAttachment) {
                        FileAttachment attachment = (FileAttachment) message.getAttachment();
                        if (TextUtils.isEmpty(attachment.getPath())
                                && TextUtils.isEmpty(attachment.getThumbPath())) {
                            showReDownloadConfirmDlg(message);
                        }
                    } else {
                        resendMessage(message);
                    }
                }
            } else {
                showReDownloadConfirmDlg(message);
            }
        }


        @Override
        public void itemClickListener(IMMessage itemMessage) {
        //    Toast.makeText(context,"itemMessage"+itemMessage.getFromAccount(),Toast.LENGTH_SHORT).show();

         /*   Intent intent = new Intent();
            intent.setAction(NIM_CHAT_MESSAGE_INFO);
            intent.putExtra("msg", itemMessage.getFromAccount());
            context.sendBroadcast(intent);*/

        }

        @Override
        public boolean onViewHolderLongClick(View clickView, View viewHolderView, IMMessage item) {

            return true;
        }

        // 重新下载(对话框提示)
        private void showReDownloadConfirmDlg(final IMMessage message) {
            EasyAlertDialogHelper.OnDialogActionListener listener = new EasyAlertDialogHelper.OnDialogActionListener() {

                @Override
                public void doCancelAction() {
                }

                @Override
                public void doOkAction() {
                    // 正常情况收到消息后附件会自动下载。如果下载失败，可调用该接口重新下载
                    if (message.getAttachment() != null && message.getAttachment() instanceof FileAttachment)
                        NIMClient.getService(ChatRoomService.class).downloadAttachment((ChatRoomMessage) message, true);
                }
            };

            final EasyAlertDialog dialog = EasyAlertDialogHelper.createOkCancelDiolag(container.activity, null,
                    container.activity.getString(R.string.repeat_download_message), true, listener);
            dialog.show();
        }

        // 重发消息到服务器
        private void resendMessage(IMMessage message) {
            // 重置状态为unsent
            int index = getItemIndex(message.getUuid());
            if (index >= 0 && index < items.size()) {
                IMMessage item = items.get(index);
                item.setStatus(MsgStatusEnum.sending);
                refreshViewHolderByIndex(index);
            }

            NIMClient.getService(ChatRoomService.class).sendMessage((ChatRoomMessage) message, true);
        }

    }

    private void setEarPhoneMode(boolean earPhoneMode) {
        UserPreferences.setEarPhoneModeEnable(earPhoneMode);
//        MessageAudioControl.getInstance(container.activity).setEarPhoneModeEnable(earPhoneMode);
    }
}
