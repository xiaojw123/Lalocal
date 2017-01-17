package com.lalocal.lalocal.live.im.session.input;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.base.util.StringUtil;
import com.lalocal.lalocal.live.base.util.log.LogUtil;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.constant.MessageType;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.im.session.Container;
import com.lalocal.lalocal.live.im.session.actions.BaseAction;
import com.lalocal.lalocal.live.im.session.emoji.IEmoticonSelectedListener;
import com.lalocal.lalocal.live.im.session.emoji.MoonUtil;
import com.lalocal.lalocal.live.im.ui.barrage.BarrageView;
import com.lalocal.lalocal.live.im.ui.dialog.EasyAlertDialogHelper;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.SPCUtils;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMessage;
import com.netease.nimlib.sdk.media.record.AudioRecorder;
import com.netease.nimlib.sdk.media.record.IAudioRecordCallback;
import com.netease.nimlib.sdk.media.record.RecordType;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.CustomNotificationConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 底部文本编辑，表情，语音等模块
 *
 */
public class InputPanel implements IEmoticonSelectedListener, IAudioRecordCallback {

    private static final String TAG = "MsgSendLayout";

    private static final int SHOW_LAYOUT_DELAY = 200;
    public static final  String IS_SELSCTOR="isSelector";

    private Container container;
    private View view;
    private Handler uiHandler;

    protected View actionPanelBottomLayout; // 更多布局
    protected LinearLayout messageActivityBottomLayout;
    protected EditText messageEditText;// 文本消息编辑框

    protected FrameLayout textAudioSwitchLayout; // 切换文本，语音按钮布局



    protected View sendMessageButtonInInputBar;// 发送消息按钮

    protected View messageInputBar;

    // 表情


    // 语音
    protected AudioRecorder audioMessageHelper;
    private boolean started = false;
    private boolean cancelled = false;
    private boolean touched = false; // 是否按着
    private boolean isKeyboardShowed = true; // 是否显示键盘
    private Context mContext;
    private String creatorAccount;
    private  int userId;
    private String channelId;

    // state
    private boolean actionPanelBottomLayoutHasSetup = false;

    // adapter
    private List<BaseAction> actions;
    private InputConfig inputConfig;

    // data
    private long typingTime = 0;
    private ImageView barrageAndChat;
    private BarrageView barrageView;


    public InputPanel(Context mContext, Container container, View view, List<BaseAction> actions, InputConfig inputConfig,String creatorAccount,int userId, String channelId) {
        this.mContext=mContext;
        this.container = container;
        this.view = view;
        this.actions = actions;
        this.uiHandler = new Handler();
        this.inputConfig = inputConfig;
        this.creatorAccount=creatorAccount;
        this.userId=userId;
        this.channelId = channelId;
        init();
    }


    public InputPanel(Context mContext,Container container, View view, List<BaseAction> actions,String creatorAccount,int userId, String channelId) {
        this(mContext,container, view, actions, new InputConfig(),creatorAccount,userId, channelId);
    }

    public void onPause() {
        // 停止录音
        if (audioMessageHelper != null) {
            onEndAudioRecord(true);
        }
    }
    // 收起键盘
    public boolean collapse(boolean immediately) {
        boolean respond = (actionPanelBottomLayout != null && actionPanelBottomLayout.getVisibility() == View.VISIBLE);
        hideAllInputLayout(immediately);
        return respond;
    }

    private void init() {
        initViews();
        initInputBarListener();
        initTextEdit();
        restoreText(false);

        for (int i = 0; i < actions.size(); ++i) {
            actions.get(i).setIndex(i);
            actions.get(i).setContainer(container);
        }
    }

    public void reload(Container container, InputConfig inputConfig) {
        this.container = container;
        this.inputConfig = inputConfig;
        init();
    }

    private void initViews() {
        // input bar
        messageActivityBottomLayout = (LinearLayout) view.findViewById(R.id.messageActivityBottomLayout);
        messageInputBar = view.findViewById(R.id.textMessageLayout);
        barrageView = (BarrageView) view.findViewById(R.id.barrageView_test);
        barrageAndChat = (ImageView) view.findViewById(R.id.im_barrage_and_chat_iv);

        sendMessageButtonInInputBar = view.findViewById(R.id.buttonSendMessage);
        messageEditText = (EditText) view.findViewById(R.id.editTextMessage);
        // 表情

        // 显示录音按钮
        // 文本录音按钮切换布局
        textAudioSwitchLayout = (FrameLayout) view.findViewById(R.id.switchLayout);

    }

    private void initInputBarListener() {


        sendMessageButtonInInputBar.setOnClickListener(clickListener);

        barrageAndChat.setOnClickListener(clickListener);

    }

    private void initTextEdit() {
        messageEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        messageEditText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    switchToTextLayout(true);
                }
                return false;
            }
        });

        messageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //   messageEditText.setHint("");
                checkSendButtonEnable(messageEditText);
            }
        });

        messageEditText.addTextChangedListener(new TextWatcher() {
            private int start;
            private int count;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                this.start = start;
                this.count = count;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                checkSendButtonEnable(messageEditText);
                MoonUtil.replaceEmoticons(container.activity, s, start, count);
                int editEnd = messageEditText.getSelectionEnd();
                messageEditText.removeTextChangedListener(this);
                while (StringUtil.counterChars(s.toString()) > 5000 && editEnd > 0) {
                    s.delete(editEnd - 1, editEnd);
                    editEnd--;
                }
                messageEditText.setSelection(editEnd);
                messageEditText.addTextChangedListener(this);

                sendTypingCommand();
            }
        });
    }

    /**
     * 发送“正在输入”通知
     */
    private void sendTypingCommand() {
        if (container.account.equals(DemoCache.getAccount())) {
            return;
        }

        if (container.sessionType == SessionTypeEnum.Team || container.sessionType == SessionTypeEnum.ChatRoom) {
            return;
        }

        if (System.currentTimeMillis() - typingTime > 5000L) {
            typingTime = System.currentTimeMillis();
            CustomNotification command = new CustomNotification();
            command.setSessionId(container.account);
            command.setSessionType(container.sessionType);
            CustomNotificationConfig config = new CustomNotificationConfig();
            config.enablePush = false;
            config.enableUnreadCount = false;
            command.setConfig(config);

            JSONObject json = new JSONObject();
            json.put("id", "1");
            command.setContent(json.toString());

            NIMClient.getService(MsgService.class).sendCustomNotification(command);
        }
    }

    /**
     * ************************* 键盘布局切换 *******************************
     */

    boolean isSelector;
    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v == sendMessageButtonInInputBar) {
                boolean loginStatus = DemoCache.getLoginStatus();
                boolean loginChatRoomStatus = DemoCache.getLoginChatRoomStatus();
                if(loginChatRoomStatus&&loginStatus){
                    onTextMessageSendButtonPressed(messageEditText.getText().toString());
                }else {
                    Toast.makeText(mContext,"正在连接聊天系统，请稍后",Toast.LENGTH_SHORT).show();
                }

            } else if(v.getId()== R.id.im_barrage_and_chat_iv){
                isSelector = SPCUtils.getBoolean(mContext, IS_SELSCTOR);
                barrageAndChat.setSelected(!isSelector);
                if(isSelector){
                    SPCUtils.put(mContext,IS_SELSCTOR,false);
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_EDIT);
                }else {
                    SPCUtils.put(mContext,IS_SELSCTOR,true);
                    MobHelper.sendEevent(mContext, MobEvent.LIVE_ANCHOR_BARRAGE);
                }

                AppLog.i("TAG","isSelector:"+ (isSelector ==true?"开启": "关闭"));

            }
        }
    };

    //弹幕开关状态监听
    private  OnBarrageViewCheckStatusListener onBarrageViewCheckStatusListener;
    public interface OnBarrageViewCheckStatusListener {
        void getBarrageViewCheckStatus(boolean isCheck, String content);
    }

    public void setOnBarrageViewCheckStatusListener(OnBarrageViewCheckStatusListener onBarrageViewCheckStatusListener) {
        this.onBarrageViewCheckStatusListener = onBarrageViewCheckStatusListener;
    }

    // 点击edittext，切换键盘和更多布局
    public void switchToTextLayout(boolean needShowInput) {

        hideActionPanelLayout();
        messageEditText.setVisibility(View.VISIBLE);
        messageInputBar.setVisibility(View.VISIBLE);
        messageActivityBottomLayout.setVisibility(View.VISIBLE);
        if (needShowInput) {
            uiHandler.postDelayed(showTextRunnable, SHOW_LAYOUT_DELAY);
        } else {
            hideInputMethod();
        }
    }

    // 发送文本消息
    public void onTextMessageSendButtonPressed(String text ) {

        IMMessage textMessage;
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(container.activity, "不要输入空消息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (container.sessionType == SessionTypeEnum.ChatRoom&&creatorAccount!=null) {
                textMessage = ChatRoomMessageBuilder.createChatRoomTextMessage(container.account, text);
            ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(container.account, DemoCache.getAccount());
            Map<String, Object> ext = new HashMap<>();
                Boolean selectorStatus = SPCUtils.getBoolean(mContext, IS_SELSCTOR);
                if(selectorStatus){

                    int length = text.toString().length();
                    if(length>20){
                        Toast.makeText(container.activity,"弹幕做多发送20个字",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                        ext.put("type", chatRoomMember.getMemberType().getValue());
                        ext.put("style","1");
                        ext.put("level", LiveConstant.level);
                        ext.put("creatorAccount",creatorAccount);
                        ext.put("userId", UserHelper.getUserId(mContext));
                        ext.put("disableSendMsgUserId",UserHelper.getUserId(mContext));
                        ext.put("channelId", channelId);
                        textMessage.setRemoteExtension(ext);
                    }
                    if (container.proxy.sendMessage(textMessage, MessageType.barrage)) {
                        restoreText(true);
                    }
                }else {
                    if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
                        ext.put("type", chatRoomMember.getMemberType().getValue());
                        ext.put("style","0");
                        ext.put("level", LiveConstant.level);
                        ext.put("creatorAccount",creatorAccount);
                        ext.put("userId", UserHelper.getUserId(mContext));
                        ext.put("disableSendMsgUserId",UserHelper.getUserId(mContext));
                        ext.put("channelId", channelId);
                        textMessage.setRemoteExtension(ext);
                    }
                    if (container.proxy.sendMessage(textMessage,MessageType.text)) {
                        restoreText(true);
                    }
                }


            SPCUtils.put(mContext,IS_SELSCTOR,false);
            hideInputMethod();

        }

    }

    private void setMemberType(ChatRoomMessage message) {
        String content = message.getContent();
        String fromNick = message.getFromNick();
        Map<String, Object> ext = new HashMap<>();
        if(onBarrageViewCheckStatusListener!=null){
            onBarrageViewCheckStatusListener.getBarrageViewCheckStatus(isSelector,fromNick+":"+content);
        }
        ChatRoomMember chatRoomMember = ChatRoomMemberCache.getInstance().getChatRoomMember(container.account, DemoCache.getAccount());
        if (chatRoomMember != null && chatRoomMember.getMemberType() != null) {
            ext.put("type", chatRoomMember.getMemberType().getValue());
            ext.put("barrag",fromNick+":"+content);
            ext.put("style","1");
            message.setRemoteExtension(ext);
        }
    }

    // 切换成音频，收起键盘，按钮切换成键盘
    private void switchToAudioLayout() {
        messageEditText.setVisibility(View.GONE);
        hideInputMethod();

        hideActionPanelLayout();



    }

    // 点击“+”号按钮，切换更多布局和键盘
    public void toggleActionPanelLayout() {
        if (actionPanelBottomLayout == null || actionPanelBottomLayout.getVisibility() == View.GONE) {
            showActionPanelLayout();
        } else {
            hideActionPanelLayout();
        }
    }


    // 隐藏更多布局
    private void hideActionPanelLayout() {
        uiHandler.removeCallbacks(showMoreFuncRunnable);
        if (actionPanelBottomLayout != null) {
            actionPanelBottomLayout.setVisibility(View.GONE);
        }
    }

    // 隐藏键盘布局
    public void hideInputMethod() {
        isKeyboardShowed = false;
        barrageAndChat.setSelected(false);
        uiHandler.removeCallbacks(showTextRunnable);
        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(messageEditText.getWindowToken(), 0);
        messageEditText.clearFocus();


    }


    // 隐藏语音布局
    private void hideAudioLayout() {

        messageEditText.setVisibility(View.VISIBLE);


    }

    // 显示表情布局
    private void showEmojiLayout() {
        hideInputMethod();
        hideActionPanelLayout();
        hideAudioLayout();

        messageEditText.requestFocus();
        uiHandler.postDelayed(showEmojiRunnable, 200);


    }

    // 初始化更多布局
    private void addActionPanelLayout() {
        if (actionPanelBottomLayout == null) {
            View.inflate(container.activity, R.layout.nim_message_activity_actions_layout, messageActivityBottomLayout);
            actionPanelBottomLayout = view.findViewById(R.id.actionsLayout);
            actionPanelBottomLayoutHasSetup = false;
        }
        initActionPanelLayout();
    }

    // 显示键盘布局
    private void showInputMethod(EditText editTextMessage) {
        editTextMessage.requestFocus();
        //如果已经显示,则继续操作时不需要把光标定位到最后
        if (!isKeyboardShowed) {
            editTextMessage.setSelection(editTextMessage.getText().length());
            isKeyboardShowed = true;
        }

        InputMethodManager imm = (InputMethodManager) container.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextMessage, 0);
        container.proxy.onInputPanelExpand();
    }

    // 显示更多布局
    private void showActionPanelLayout() {
        addActionPanelLayout();

        hideInputMethod();

        uiHandler.postDelayed(showMoreFuncRunnable, SHOW_LAYOUT_DELAY);
        container.proxy.onInputPanelExpand();
    }

    // 初始化具体more layout中的项目
    private void initActionPanelLayout() {
        if (actionPanelBottomLayoutHasSetup) {
            return;
        }

        ActionsPanel.init(view, actions);
        actionPanelBottomLayoutHasSetup = true;
    }

    private Runnable showEmojiRunnable = new Runnable() {
        @Override
        public void run() {

        }
    };

    private Runnable showMoreFuncRunnable = new Runnable() {
        @Override
        public void run() {
            actionPanelBottomLayout.setVisibility(View.VISIBLE);
        }
    };

    private Runnable showTextRunnable = new Runnable() {
        @Override
        public void run() {
            showInputMethod(messageEditText);
        }
    };

    private void restoreText(boolean clearText) {
        if (clearText) {
            messageEditText.setText("");
        }

        checkSendButtonEnable(messageEditText);
    }

    /**
     * 显示发送或更多
     *
     * @param editText
     */
    private void checkSendButtonEnable(EditText editText) {
        String textMessage = editText.getText().toString();
        if (!TextUtils.isEmpty(StringUtil.removeBlanks(textMessage)) && editText.hasFocus()) {

            sendMessageButtonInInputBar.setVisibility(View.VISIBLE);
        } else if (inputConfig.isMoreFunctionShow) {
            sendMessageButtonInInputBar.setVisibility(View.GONE);
        }
    }

    /**
     * *************** IEmojiSelectedListener ***************
     */
    @Override
    public void onEmojiSelected(String key) {
        Editable mEditable = messageEditText.getText();
        if (key.equals("/DEL")) {
            messageEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
        } else {
            int start = messageEditText.getSelectionStart();
            int end = messageEditText.getSelectionEnd();
            start = (start < 0 ? 0 : start);
            end = (start < 0 ? 0 : end);
            mEditable.replace(start, end, key);
        }
    }

    private Runnable hideAllInputLayoutRunnable;

    @Override
    public void onStickerSelected(String category, String item) {
        Log.i("InputPanel", "onStickerSelected, category =" + category + ", sticker =" + item);
        // 贴图暂无实现
    }

    /**
     * 隐藏所有输入布局
     */
    private void hideAllInputLayout(boolean immediately) {
        if (hideAllInputLayoutRunnable == null) {
            hideAllInputLayoutRunnable = new Runnable() {

                @Override
                public void run() {
                    hideInputMethod();
                    hideActionPanelLayout();

                }
            };
        }
        long delay = immediately ? 0 : ViewConfiguration.getDoubleTapTimeout();
        uiHandler.postDelayed(hideAllInputLayoutRunnable, delay);
    }

    /**
     * ****************************** 语音 ***********************************
     */


    // 上滑取消录音判断
    private static boolean isCancelled(View view, MotionEvent event) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);

        if (event.getRawX() < location[0] || event.getRawX() > location[0] + view.getWidth()
                || event.getRawY() < location[1] - 40) {
            return true;
        }

        return false;
    }

    /**
     * 初始化AudioRecord
     */
    private void initAudioRecord() {
        if (audioMessageHelper == null) {
            audioMessageHelper = new AudioRecorder(container.activity, RecordType.AAC, AudioRecorder.DEFAULT_MAX_AUDIO_RECORD_TIME_SECOND, this);
        }
    }

    /**
     * 开始语音录制
     */
    private void onStartAudioRecord() {
        container.activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        started = audioMessageHelper.startRecord();
        cancelled = false;
        if (started == false) {
            Toast.makeText(container.activity, R.string.recording_init_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!touched) {
            return;
        }
        updateTimerTip(false); // 初始化语音动画状态
        playAudioRecordAnim();
    }

    /**
     * 结束语音录制
     *
     * @param cancel
     */
    private void onEndAudioRecord(boolean cancel) {
        container.activity.getWindow().setFlags(0, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        audioMessageHelper.completeRecord(cancel);

        stopAudioRecordAnim();
    }

    /**
     * 取消语音录制
     *
     * @param cancel
     */
    private void cancelAudioRecord(boolean cancel) {
        // reject
        if (!started) {
            return;
        }
        // no change
        if (cancelled == cancel) {
            return;
        }

        cancelled = cancel;
        updateTimerTip(cancel);
    }

    /**
     * 正在进行语音录制和取消语音录制，界面展示
     *
     * @param cancel
     */
    private void updateTimerTip(boolean cancel) {

    }

    /**
     * 开始语音录制动画
     */
    private void playAudioRecordAnim() {

    }

    /**
     * 结束语音录制动画
     */
    private void stopAudioRecordAnim() {

    }

    // 录音状态回调
    @Override
    public void onRecordReady() {

    }

    @Override
    public void onRecordStart(File audioFile, RecordType recordType) {

    }

    @Override
    public void onRecordSuccess(File audioFile, long audioLength, RecordType recordType) {
        IMMessage audioMessage = MessageBuilder.createAudioMessage(container.account, container.sessionType, audioFile, audioLength);
        container.proxy.sendMessage(audioMessage,MessageType.text);
    }

    @Override
    public void onRecordFail() {

    }

    @Override
    public void onRecordCancel() {

    }

    @Override
    public void onRecordReachedMaxTime(final int maxTime) {
        stopAudioRecordAnim();
        EasyAlertDialogHelper.createOkCancelDiolag(container.activity, "", container.activity.getString(R.string.recording_max_time), false, new EasyAlertDialogHelper.OnDialogActionListener() {
            @Override
            public void doCancelAction() {
            }

            @Override
            public void doOkAction() {
                audioMessageHelper.handleEndRecord(true, maxTime);
            }
        }).show();
    }

    public boolean isRecording() {
        return audioMessageHelper != null && audioMessageHelper.isRecording();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        int index = (requestCode << 16) >> 24;
        if (index != 0) {
            index--;
            if (index < 0 | index >= actions.size()) {
                LogUtil.d(TAG, "request code out of actions' range");
                return;
            }
            BaseAction action = actions.get(index);
            if (action != null) {
                action.onActivityResult(requestCode & 0xff, resultCode, data);
            }
        }
    }
}
