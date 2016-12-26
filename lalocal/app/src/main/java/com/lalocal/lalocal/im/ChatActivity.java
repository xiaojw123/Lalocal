package com.lalocal.lalocal.im;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.FileSaveUtil;
import com.lalocal.lalocal.util.ImageCheckoutUtil;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.util.PictureUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.AttachmentProgress;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChatActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;
    public static final String NIM_TO_ACCOUNT = "nim_to_account";
    public static final String TIME = "time";
    private static final String SERVICE_TYPE = "s_type";
    private static final int DEFAULT_TYPE = -3;
    RecyclerView mXRecyclerView;
    TextView mMsgAddTv;
    EditText mMsgEdit;
    TextView mMsgSendTv;
    MessageListAdapter msgAdapter;
    List<IMMessage> mMessageList = new ArrayList<>();
    FrameLayout moreOpLayout;
    CustomTitleView chatTv;
    ImageView sendImg;
    RelativeLayout rootView;
    int navigationBarHeight, lastHeight;
    boolean isMoreAdd, isSoftKeyShow;
    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
    String accId, nickName, avatar;
    int type;
    boolean isFirstload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
        initParams();
        type = getIntent().getIntExtra(SERVICE_TYPE, DEFAULT_TYPE);
        if (type != DEFAULT_TYPE) {
            setLoaderCallBack(new ChatCallBack());
//            mContentloader.getUsersService(type);
        } else {
            chatTv.setTitle(getNickName());
        }
        loadFromRemote();
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(
                sendObserver, true);
        // 如果发送的多媒体文件消息，还需要监听文件的上传进度。
        NIMClient.getService(MsgServiceObserve.class).observeAttachmentProgress(new Observer<AttachmentProgress>() {

            @Override
            public void onEvent(AttachmentProgress attachmentProgress) {


            }
        }, true);

    }


    private void initParams() {
        navigationBarHeight = CommonUtil.getNavigationBarHeight(this);
    }

    private void initView() {
        chatTv = (CustomTitleView) findViewById(R.id.chat_ctv);
        rootView = (RelativeLayout) findViewById(R.id.activity_chat_root);
        moreOpLayout = (FrameLayout) findViewById(R.id.chat_more_layout);
        sendImg = (ImageView) findViewById(R.id.chat_more_send_img);
        mMsgAddTv = (TextView) findViewById(R.id.chat_add_stv);
        mMsgEdit = (EditText) findViewById(R.id.chat_edit);
        mMsgSendTv = (TextView) findViewById(R.id.chat_send_tv);
        mXRecyclerView = (RecyclerView) findViewById(R.id.chat_xrlv);
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mXRecyclerView.setOnTouchListener(this);
        msgAdapter = new MessageListAdapter(mMessageList);
        mXRecyclerView.setAdapter(msgAdapter);
        mMsgSendTv.setOnClickListener(this);
        mMsgAddTv.setOnClickListener(this);
        sendImg.setOnClickListener(this);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    Observer<IMMessage> sendObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage imMessage) {
            MsgStatusEnum status = imMessage.getStatus();
            if (status == MsgStatusEnum.success) {
                mMsgEdit.setText(null);
                mMessageList.add(imMessage);
                msgAdapter.updateItems(mMessageList);
            }


        }
    };


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.chat_more_send_img:
                requestUserPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
                break;
            case R.id.chat_add_stv:
                if (isSoftKeyShow) {
                    isMoreAdd = true;
                    KeyboardUtil.hidenSoftKey(mMsgEdit);
                } else {
                    moreOpLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.chat_send_tv:
                String text = mMsgEdit.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(this, "不能发送空的内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendText(getAccount(), text);
                break;
        }


    }

    @OnMPermissionGranted(PERMISSION_STGAT_CODE)
    public void onPermissionGranted() {
        searchPhotoBum();
    }

    @OnMPermissionDenied(PERMISSION_STGAT_CODE)
    public void onPermissionDenied() {
        Toast.makeText(this, getString(R.string.live_read_write_external_storage), Toast.LENGTH_SHORT).show();
    }


    private void searchPhotoBum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);

    }


    public String getAccount() {
        if (type != DEFAULT_TYPE) {
            return accId;
        }
        return getIntent().getStringExtra(NIM_TO_ACCOUNT);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            moreOpLayout.setVisibility(View.GONE);
            if (isSoftKeyShow) {
                KeyboardUtil.hidenSoftKey(mMsgEdit);
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                String path = FileSaveUtil.getPath(getApplicationContext(), uri);
                File file = new File(path); // 图片文件路径
                if (file.exists()) {
                    int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(path));
                    if (size > IMAGE_SIZE) {
                        showDialog(path);
                    } else {
                        sendImg(getAccount(), new File(path));
                    }
                } else {
                    CommonUtil.showToast(this, "该文件不存在", Toast.LENGTH_SHORT);
                }

            }
        }

    }

    private void showDialog(final String path) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                // // TODO Auto-generated method stub
                try {
                    String GalPicPath = getSavePicPath();
                    Bitmap bitmap = PictureUtil.compressSizeImage(path);
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, GalPicPath),
                            GalPicPath);
                    File file = new File(GalPicPath);
                    if (file.exists() && isSave) {
                        sendImg(getAccount(), file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private String getSavePicPath() {
        final String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        try {
            FileSaveUtil.createSDDirectory(dir);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        return dir + fileName;
    }

    public void sendImg(String sessionId, File file) {
        // 创建图片消息
        IMMessage message = MessageBuilder.createImageMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                file, // 图片文件对象
                null // 文件显示名字，如果第三方 APP 不关注，可以为 null
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);

    }

    private void sendText(String sessionId, String text) {
        if (isSoftKeyShow) {
            KeyboardUtil.hidenSoftKey(mMsgEdit);
        }
        // 创建文本消息
        IMMessage message = MessageBuilder.createTextMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                text);
// 发送消息。如果需要关心发送结果，可设置回调函数。发送完成时，会收到回调。如果失败，会有具体的错误码。
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }


    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still visible.
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getRootView().getHeight();
        int height = screenHeight - r.bottom;
        Log.i("xjw", "onGlobalLayout screenHeight___" + screenHeight + ", bottom___" + r.bottom + ", top__" + r.top);
        //1920  1812  1011
        if (height == lastHeight) {
            return;
        }
        if (height > navigationBarHeight) {
            isSoftKeyShow = true;
            if (moreOpLayout.getVisibility() == View.VISIBLE) {
                moreOpLayout.setVisibility(View.GONE);
            }
        } else {
            isSoftKeyShow = false;
            if (isMoreAdd) {
                isMoreAdd = false;
                if (moreOpLayout.getVisibility() != View.VISIBLE) {
                    moreOpLayout.setVisibility(View.VISIBLE);
                }
            }
        }
        lastHeight = height;
    }


    public static void start(Context context, String accid, String nickName, String avatar, long time) {
        Intent imIntent = new Intent(context, ChatActivity.class);
        imIntent.putExtra(ChatActivity.NIM_TO_ACCOUNT, accid);
        imIntent.putExtra(KeyParams.NICKNAME, nickName);
        imIntent.putExtra(KeyParams.AVATAR, avatar);
        imIntent.putExtra(TIME, time);
        context.startActivity(imIntent);
    }

    public static void startUserService(Context context, int type) {
        Intent imIntent = new Intent(context, ChatActivity.class);
        imIntent.putExtra(SERVICE_TYPE, type);
        context.startActivity(imIntent);
    }

    class ChatCallBack extends ICallBack {
        @Override
        public void onGetUser(JSONObject resultJobj) {
            nickName = resultJobj.optString("nickName");
            avatar = resultJobj.optString("avatar");
            accId = resultJobj.optString("accId");
            chatTv.setTitle(nickName);
        }
    }

    public String getNickName() {
        if (type != DEFAULT_TYPE) {
            return nickName;
        }
        return getIntent().getStringExtra(KeyParams.NICKNAME);
    }

    public String getAvatar() {
        if (type != DEFAULT_TYPE) {
            return avatar;
        }
        return getIntent().getStringExtra(KeyParams.AVATAR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        // 进入聊天界面，建议放在onResume中
        NIMClient.getService(MsgService.class).setChattingAccount(getAccount(), SessionTypeEnum.P2P);


    }

    @Override
    protected void onPause() {
        super.onPause();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None);
    }


    Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                    AppLog.print("incomingMessageObserver___len:" + messages.size());
                    updateMessages(messages);
                }

            };


    private void loadFromRemote() {
        NIMClient.getService(MsgService.class).pullMessageHistory(anchor(), LOAD_MESSAGE_COUNT, true)
                .setCallback(callback);
    }

    private RequestCallback<List<IMMessage>> callback = new RequestCallback<List<IMMessage>>() {
        @Override
        public void onSuccess(List<IMMessage> param) {
            Collections.reverse(param);
            if (isFirstload && mMessageList.size() > 0) {
                for (IMMessage message : param) {
                    for (IMMessage item : mMessageList) {
                        if (item.isTheSame(message)) {
                            mMessageList.remove(item);
                        }
                    }
                }
            }
            updateMessages(param);
        }

        @Override
        public void onFailed(int code) {

        }

        @Override
        public void onException(Throwable exception) {

        }
    };

    private void updateMessages(List<IMMessage> param) {
        mMessageList.addAll(param);
        msgAdapter.updateItems(mMessageList);
        int len = mMessageList.size();
        if (len > 0) {
            mXRecyclerView.scrollToPosition(len - 1);
        }
    }

    private IMMessage anchor() {
        AppLog.print("anchor time___" + getTime());
        return ChatRoomMessageBuilder.createEmptyChatRoomMessage(getAccount(), 0);
    }

    private static final int LOAD_MESSAGE_COUNT = 20;

    public long getTime() {
        return getIntent().getLongExtra(TIME, 0);
    }
}
