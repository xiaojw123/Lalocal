package com.lalocal.lalocal.im;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.BaseFragment;
import com.lalocal.lalocal.activity.fragment.PersonalMessageFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.LivePlayerBaseActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.live.entertainment.helper.MessageUpdateListener;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.FileSaveUtil;
import com.lalocal.lalocal.util.ImageCheckoutUtil;
import com.lalocal.lalocal.util.KeyboardUtil;
import com.lalocal.lalocal.util.PictureUtil;
import com.lalocal.lalocal.view.CustomFrameLayout;
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
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by xiaojw on 2016/12/21.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener, View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener, CustomTitleView.onBackBtnClickListener, XRecyclerView.LoadingListener {
<<<<<<< HEAD
    private static final int IMAGE_SIZE = 100 * 1024;
=======
    private static final int LOAD_DELAY_TIME = 1000;
    private static final int IMAGE_SIZE = 100 * 1024;// 300kb
>>>>>>> dev
    private static final int LOAD_MESSAGE_COUNT = 10;
    private static final int PEMISSION_CODE_SDCARD = 0x11;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    XRecyclerView mXRecyclerView;
    EditText mMsgEdit;
    CustomTitleView chatTv;
    TextView mMsgAddTv, mMsgSendTv;
    ImageView sendImg;
    MessageListAdapter msgAdapter;
    CustomFrameLayout moreOpLayout;
    Button cancelBtn, myselfBtn;
    List<IMMessage> mMessageList = new ArrayList<>();
    RelativeLayout rootView;
    int navigationBarHeight, lastHeight, opHieght;
    boolean isMoreAdd, isSoftKeyShow, isFirstload;
    String accId, nickName;
    int pageType;
    boolean hasCancel;
    boolean chatVisible = true;
    FragmentManager fm;
    PersonalMessageFragment lastFragment;
    LinearLayoutManager layoutManager;
    MessageUpdateListener unReadUpdateListener;
    LinearLayout opLLt;
    RelativeLayout opEditCotainer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        initParams();
        initView(view);
        //注册
        registerStatusObserve(true);
        return view;
    }

    public void setLastFragment(PersonalMessageFragment lastFragment) {
        this.lastFragment = lastFragment;
    }

    public void setUnReadUpdateListener(MessageUpdateListener unReadUpdateListener) {
        this.unReadUpdateListener = unReadUpdateListener;
    }

    public void setChatVisible(boolean chatVisible) {
        this.chatVisible = chatVisible;
    }

    @Override
    public void onDestroyView() {
        registerStatusObserve(false);
        super.onDestroyView();
    }

<<<<<<< HEAD
=======
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
>>>>>>> dev

    private void registerStatusObserve(boolean flag) {
        NIMClient.getService(MsgServiceObserve.class).observeMsgStatus(
                messageObserver, flag);
    }


    public void initParams() {
        navigationBarHeight = CommonUtil.getNavigationBarHeight(getActivity());
        opHieght = (int) getResources().getDimension(R.dimen.pop_more_message_layout_height) * 3;
        Bundle bundle = getArguments();
        if (bundle != null) {
            pageType = bundle.getInt(KeyParams.PAGE_TYPE, PageType.PAGE_DEFAULT);
            nickName = bundle.getString(KeyParams.NICKNAME);
            accId = bundle.getString(KeyParams.ACCID);
            hasCancel = bundle.getBoolean(KeyParams.HAST_CANCLE);
        }
    }

    public void updateView(Bundle bundle) {
        if (bundle != null) {
            nickName = bundle.getString(KeyParams.NICKNAME);
            accId = bundle.getString(KeyParams.ACCID);
        }
        chatTv.setTitle(nickName);
        limit = 10;
        mMessageList.clear();
        msgAdapter = new MessageListAdapter(mMessageList);
        mXRecyclerView.setAdapter(msgAdapter);
        loadDataFromLocal();
    }

    private void initView(View contentView) {
        opEditCotainer = (RelativeLayout) contentView.findViewById(R.id.chat_edit_cotainer);
        opLLt = (LinearLayout) contentView.findViewById(R.id.chat_op_layout);
        cancelBtn = (Button) contentView.findViewById(R.id.chat_cancel_btn);
        myselfBtn = (Button) contentView.findViewById(R.id.chat_myself_btn);
        chatTv = (CustomTitleView) contentView.findViewById(R.id.chat_ctv);
        rootView = (RelativeLayout) contentView.findViewById(R.id.activity_chat_root);
        moreOpLayout = (CustomFrameLayout) contentView.findViewById(R.id.chat_more_layout);
        sendImg = (ImageView) contentView.findViewById(R.id.chat_more_send_img);
        mMsgAddTv = (TextView) contentView.findViewById(R.id.chat_add_stv);
        mMsgEdit = (EditText) contentView.findViewById(R.id.chat_edit);
        mMsgSendTv = (TextView) contentView.findViewById(R.id.chat_send_tv);
        mXRecyclerView = (XRecyclerView) contentView.findViewById(R.id.chat_xrlv);
        mXRecyclerView.setLoadingMoreEnabled(false);
        mXRecyclerView.setLoadingListener(this);
        moreOpLayout.setOnViewHeplerListner(viewHelperListener);
        layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        mXRecyclerView.setLayoutManager(layoutManager);
        mXRecyclerView.setOnTouchListener(this);
        msgAdapter = new MessageListAdapter(mMessageList);
        mXRecyclerView.setAdapter(msgAdapter);
        cancelBtn.setOnClickListener(this);
        myselfBtn.setOnClickListener(this);
        mMsgSendTv.setOnClickListener(this);
        mMsgAddTv.setOnClickListener(this);
        sendImg.setOnClickListener(this);
        chatTv.setOnCustomClickLister(this);
        chatTv.setOnClickListener(this);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        chatTv.setTitle(nickName);
        if (pageType == PageType.PAGE_PLAY_BACK) {
            chatTv.setBackVisible(false);
            cancelBtn.setVisibility(View.VISIBLE);
            myselfBtn.setVisibility(View.GONE);
        } else {
            if (hasCancel) {
                cancelBtn.setVisibility(View.VISIBLE);
                myselfBtn.setVisibility(View.GONE);
            } else {
                cancelBtn.setVisibility(View.GONE);
                myselfBtn.setVisibility(View.VISIBLE);
            }
        }
        isFirstload = true;
        loadDataFromLocal();
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.chat_ctv:
                break;
            case R.id.chat_cancel_btn:
                if (getActivity() instanceof LivePlayerBaseActivity || getActivity() instanceof PlayBackActivity) {
                    if (isSoftKeyShow) {
                        KeyboardUtil.hidenSoftKey(mMsgEdit);
                    }
                    if (fm == null) {
                        fm = getFragmentManager();
                    }
                    if (closeFragmentClickListener != null) {
                        closeFragmentClickListener.closeClick();
                    }
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.hide(this);
                    if (lastFragment != null) {
                        ft.hide(lastFragment);
                    }
                    ft.commit();
                }
                break;
            case R.id.chat_myself_btn:
                Intent intent = new Intent(getActivity(), LiveHomePageActivity.class);

                intent.putExtra("userId", CommonUtil.getUserId(accId));
                getActivity().startActivity(intent);
                break;
            case R.id.chat_more_send_img:
                sendImg();
                break;
            case R.id.chat_add_stv:
                if (isSoftKeyShow) {
                    isMoreAdd = true;
                    KeyboardUtil.hidenSoftKey(mMsgEdit);
                } else {
                    if (moreOpLayout.getVisibility() != View.VISIBLE) {
                        moreOpLayout.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.chat_send_tv:
                String text = mMsgEdit.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(getActivity(), "不能发送空的内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                mMsgEdit.setText(null);
                sendText(accId, text);
                break;
        }


    }

    CloseFragmentClickListener closeFragmentClickListener;

    public void setOnCloseFragmentClickListener(CloseFragmentClickListener closeFragmentClickListener) {
        this.closeFragmentClickListener = closeFragmentClickListener;
    }


    public interface CloseFragmentClickListener {
        void closeClick();
    }


    private void sendImg() {
        int readCode = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int writeCode = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (readCode == PackageManager.PERMISSION_GRANTED && writeCode == PackageManager.PERMISSION_GRANTED) {
            searchPhotoBum();
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE});
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissions(String[] permissions) {
        requestPermissions(permissions, PEMISSION_CODE_SDCARD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PEMISSION_CODE_SDCARD) {
            int code = grantResults[0];
            if (code == PackageManager.PERMISSION_GRANTED) {
                searchPhotoBum();
            } else {
                Toast.makeText(getActivity(), getString(R.string.live_read_write_external_storage), Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void searchPhotoBum() {
        if (moreOpLayout.getVisibility() == View.VISIBLE) {
            moreOpLayout.setVisibility(View.GONE);
        }
        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.putExtra("crop", "true");
        intent.putExtra("scale", "true");
        intent.putExtra("scaleUpIfNeeded", true);
        intent.setType("image/*");
        startActivityForResult(intent,
                PHOTO_REQUEST_GALLERY);

    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (moreOpLayout.getVisibility() == View.VISIBLE) {
                moreOpLayout.setVisibility(View.GONE);
            }
            if (isSoftKeyShow) {
                KeyboardUtil.hidenSoftKey(mMsgEdit);
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                String path = FileSaveUtil.getPath(getActivity().getApplicationContext(), uri);
                File file = new File(path); // 图片文件路径
                if (file.exists()) {
                    int size = ImageCheckoutUtil.getImageSize(ImageCheckoutUtil.getLoacalBitmap(path));
                    if (size > IMAGE_SIZE) {
                        showDialog(path);
                    } else {
                        sendImg(accId, new File(path));
                    }
                } else {
                    CommonUtil.showToast(getActivity(), "该文件不存在", Toast.LENGTH_SHORT);
                }

            }
        }

    }

    private void showDialog(final String path) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String GalPicPath = getSavePicPath();
                    Bitmap bitmap = PictureUtil.compressSizeImage(path);
                    boolean isSave = FileSaveUtil.saveBitmap(
                            PictureUtil.reviewPicRotate(bitmap, GalPicPath),
                            GalPicPath);
                    File file = new File(GalPicPath);
                    if (file.exists() && isSave) {
                        sendImg(accId, file);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String getSavePicPath() throws IOException {
        String dir = FileSaveUtil.SD_CARD_PATH + "image_data/";
        FileSaveUtil.createSDDirectory(dir);
        String fileName = String.valueOf(System.currentTimeMillis() + ".png");
        return dir + fileName;
    }
    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        // r will be populated with the coordinates of your view that area still visible.
        rootView.getWindowVisibleDisplayFrame(r);
        int screenHeight = rootView.getRootView().getHeight();
        int height = screenHeight - r.bottom;
        if (height == lastHeight) {
            return;
        }
        if (height > navigationBarHeight) {
            isSoftKeyShow = true;
            if (moreOpLayout.getVisibility() == View.VISIBLE) {
                moreOpLayout.setVisibility(View.GONE);
            }
            mXRecyclerView.smoothScrollBy(0, height);
        } else {
            isSoftKeyShow = false;
            if (isMoreAdd) {
                isMoreAdd = false;
                if (moreOpLayout.getVisibility() != View.VISIBLE) {
                    moreOpLayout.setVisibility(View.VISIBLE);
                }
            }
//            mXRecyclerView.smoothScrollBy(0, -height);l
            mXRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    mXRecyclerView.scrollToPosition(getLastPosition());
                    layoutManager.scrollToPositionWithOffset(getLastPosition(),0);
                }
            },LOAD_DELAY_TIME);
        }
        lastHeight = height;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (chatVisible) {
            reigisterNimService(true);
        }
    }
    public void reigisterNimService(boolean flag) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, flag);
        if (flag) {
            // 进入聊天界面，建议放在onResume中
            NIMClient.getService(MsgService.class).setChattingAccount(accId, SessionTypeEnum.P2P);

//            if (unReadUpdateListener != null) {
//                AppLog.print("reigisterNimService chat fragment unReadUpdate");
//                unReadUpdateListener.onUnReadUpate();
//            }
        } else {
            // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.P2P);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        resetOpView();
        if (chatVisible) {
            reigisterNimService(false);
        }
    }


    private void loadDataFromLocal() {
        //本地查询
        NIMClient.getService(MsgService.class).queryMessageList(accId, SessionTypeEnum.P2P, 0, limit).setCallback(initCallback);
        //服务端查询
//        NIMClient.getService(MsgService.class).pullMessageHistory(anchor(), 10, true)
//                .setCallback(callback);
    }


    private void updateMessages(List<IMMessage> param) {
        if (!mMessageList.containsAll(param)) {
            mMessageList.addAll(param);
            msgAdapter.updateItems(mMessageList);
        }
    }

    private IMMessage anchor() {
        if (mMessageList.size() <= 0) {
            return ChatRoomMessageBuilder.createEmptyChatRoomMessage(accId, 0);
        } else {
            return mMessageList.get(0);
        }
    }

    private Observer<IMMessage> messageObserver = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage imMessage) {
<<<<<<< HEAD
=======
            MsgStatusEnum status = imMessage.getStatus();
            AppLog.print("消息发送结果status:" + status);
>>>>>>> dev
            updateChatMessage(imMessage);
        }
    };
    private Observer<List<IMMessage>> incomingMessageObserver =
            new Observer<List<IMMessage>>() {
                @Override
                public void onEvent(List<IMMessage> messages) {
                    // 处理新收到的消息，为了上传处理方便，SDK 保证参数 messages 全部来自同一个聊天对象。
                    for (IMMessage imMessage : messages) {
                        if (TextUtils.equals(accId, imMessage.getSessionId())) {
                            updateChatMessage(imMessage);
                        } else {
                            imMessage.setStatus(MsgStatusEnum.unread);
                        }
                    }
                }

            };

    public int getLastPosition() {
        return msgAdapter.getItemCount() - 1;
    }


    private void resetRefresh() {
        if (isRefresh) {
            isRefresh = false;
            mXRecyclerView.refreshComplete();
        }
    }

    public void resetOpView() {
        if (moreOpLayout != null && moreOpLayout.getVisibility() == View.VISIBLE) {
            moreOpLayout.setVisibility(View.GONE);
        }
        if (isSoftKeyShow) {
            KeyboardUtil.hidenSoftKey(mMsgEdit);
        }
    }


    @Override
    public void onBackClick() {
        if (!isAdded()) {
            return;
        }
        if (isSoftKeyShow) {
            KeyboardUtil.hidenSoftKey(mMsgEdit);
        }
        if (getActivity() instanceof LivePlayerBaseActivity) {
            if (fm == null) {
                fm = getFragmentManager();
            }
            FragmentTransaction ft = fm.beginTransaction();
            ft.hide(this);
            ft.show(lastFragment);
            ft.commit();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            reigisterNimService(true);
        } else {
            resetOpView();
            reigisterNimService(false);
        }
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        limit += 10;
        loadDataFromLocal();
    }

    @Override
    public void onLoadMore() {

    }

    public void sendImg(String sessionId, File file) {
        // 创建图片消息
        if (isSoftKeyShow) {
            KeyboardUtil.hidenSoftKey(mMsgEdit);
        }
        IMMessage message = MessageBuilder.createImageMessage(
                sessionId, // 聊天对象的 ID，如果是单聊，为用户帐号，如果是群聊，为群组 ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                file, file.getName());
//        updateChatMessage(message);
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
//        updateChatMessage(message);
        NIMClient.getService(MsgService.class).sendMessage(message, false);
    }


    private void updateChatMessage(IMMessage message) {
        if (!mMessageList.contains(message)) {
<<<<<<< HEAD
            mMessageList.add(message);
            msgAdapter.setItems(mMessageList);
            int lastPos = msgAdapter.getItemCount() - 1;
            if (lastPos <= 0) {
                msgAdapter.notifyDataSetChanged();
            } else {
                msgAdapter.notifyItemChanged(lastPos);
            }
            layoutManager.scrollToPositionWithOffset(lastPos, 0);
        } else {
            msgAdapter.updateItems(mMessageList);
=======
            AppLog.print("lastPosition:" + getLastPosition());
            mMessageList.add(message);
            if (mMessageList.size() == 1) {
                msgAdapter.updateItems(mMessageList);
            } else {
                msgAdapter.updateSingleItem(mMessageList);
            }
            mXRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    layoutManager.scrollToPositionWithOffset(getLastPosition(), 0);
                }
            }, 20);
>>>>>>> dev
        }
    }


    int limit = 10;
    boolean isRefresh;
    private CustomFrameLayout.ViewHelperListener viewHelperListener = new CustomFrameLayout.ViewHelperListener() {
        @Override
        public void onVisibilityChanged(final int visibility) {
            if (isSoftKeyShow) {
                return;
            }
            mXRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (visibility == View.VISIBLE) {
                        mXRecyclerView.smoothScrollBy(0, opHieght);
                    } else {
                        mXRecyclerView.scrollToPosition(msgAdapter.getItemCount() - 1);
                    }
                }
            }, LOAD_DELAY_TIME);
        }

    };
    private RequestCallback<List<IMMessage>> initCallback = new RequestCallback<List<IMMessage>>() {
        @Override
        public void onSuccess(List<IMMessage> param) {
            if (isRefresh) {
                isRefresh = false;
                mXRecyclerView.refreshComplete();
            }
            if (param != null && param.size() > 0) {
                Collections.reverse(param);
                mMessageList = param;
                msgAdapter.updateItems(mMessageList);
                if (isFirstload) {
                    isFirstload = false;
                    mXRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            layoutManager.scrollToPositionWithOffset(getLastPosition(), 0);
                        }
                    }, LOAD_DELAY_TIME);
                } else {
                    layoutManager.scrollToPosition(0);
                }
            }
        }

        @Override
        public void onFailed(int code) {
            resetRefresh();
        }

        @Override
        public void onException(Throwable exception) {
            resetRefresh();
        }
    };

}
