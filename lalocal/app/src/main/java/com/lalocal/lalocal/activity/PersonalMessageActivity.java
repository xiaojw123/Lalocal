package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.im.ChatActivity;
import com.lalocal.lalocal.im.LalocalHelperActivity;
import com.lalocal.lalocal.im.view.adapter.RecentContactAdapter;
import com.lalocal.lalocal.me.NotificationActivity;
import com.lalocal.lalocal.me.PraiseCommentActivity;
import com.lalocal.lalocal.model.RecentContactInfo;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.UserService;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by xiaojw on 2016/12/14.
 */

public class PersonalMessageActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
    @BindView(R.id.my_immessage_notification)
    FrameLayout notifyLayout;
    @BindView(R.id.my_immessage_praise_and_comment)
    FrameLayout praiseLayout;
    @BindView(R.id.my_immessage_user_list)
    RecyclerView myImmessageUserList;
    @BindView(R.id.my_immessage_assistant)
    RelativeLayout assistantLayout;
    RecentContactAdapter contactAdapter;
    List<RecentContactInfo> mItems = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_msg);
        ButterKnife.bind(this);
        initRecyclerView();
        //  创建观察者对象
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(requestCallbackWrapper);

    }

    private void initRecyclerView() {
        myImmessageUserList.setLayoutManager(new LinearLayoutManager(this));
        myImmessageUserList.setNestedScrollingEnabled(false);
        contactAdapter = new RecentContactAdapter(mItems);
        contactAdapter.setOnItemClickListener(this);
        myImmessageUserList.setAdapter(contactAdapter);
    }

    @OnClick({R.id.my_immessage_notification, R.id.my_immessage_praise_and_comment, R.id.my_immessage_assistant})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_immessage_notification:
                gotoNotification();
                break;
            case R.id.my_immessage_praise_and_comment:
                PraiseCommentActivity.start(this);
                break;
            case R.id.my_immessage_assistant:
                LalocalHelperActivity.start(this);
                break;
        }
    }

    private void gotoNotification() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivity(intent);
    }


    private RequestCallbackWrapper requestCallbackWrapper = new RequestCallbackWrapper<List<RecentContact>>() {
        @Override
        public void onResult(int code, List<RecentContact> recents, Throwable e) {
            // recents参数即为最近联系人列表（最近会话列表）
            updateRecentContacts(recents);
        }
    };

    private void updateRecentContacts(List<RecentContact> recents) {
        AppLog.print("requestCallbackWrapper onResult recents___len___" + recents.size());
        List<NimUserInfo> mUsers = NIMClient.getService(UserService.class).getAllUserInfo();
        for (NimUserInfo info : mUsers) {
            AppLog.print("xxUser:ccId__" + info.getAccount() + " name__" + info.getName() + ", avatar=" + info.getAvatar() + "");
        }


        final List<RecentContactInfo> infoList = new ArrayList<>();
        List<String> ssidlist = new ArrayList<>();
        Out:
        for (RecentContact contact : recents) {
            String ssid = contact.getContactId();
            for (RecentContactInfo curInfo : mItems) {
                if (ssid.equals(curInfo.getAccount())) {
                    curInfo.setContent(contact.getContent());
                    curInfo.setTime(contact.getTime());
                    curInfo.setUnReadCount(contact.getUnreadCount());
                    continue Out;//结束整个循环体
                }
            }
            RecentContactInfo info = new RecentContactInfo();
            info.setAccount(ssid);
            info.setContent(contact.getContent());
            info.setTime(contact.getTime());
            info.setUnReadCount(contact.getUnreadCount());
            infoList.add(info);
            ssidlist.add(ssid);
        }
        List<NimUserInfo> users = NIMClient.getService(UserService.class).getUserInfoList(ssidlist);
        int cacheSize = users != null && users.size() > 0 ? users.size() : 0;
        if (ssidlist.size() == cacheSize) {
            AppLog.print("从缓存获取recent contacts");
            //走缓存渠道
            resSetInfoList(infoList, users);

        } else {
            //走服务器渠道
            AppLog.print("从服务器获取recent contacts");
            NIMClient.getService(UserService.class).fetchUserInfo(ssidlist)
                    .setCallback(new RequestCallback<List<NimUserInfo>>() {

                        @Override
                        public void onSuccess(List<NimUserInfo> param) {
                            resSetInfoList(infoList, param);

                        }

                        @Override
                        public void onFailed(int code) {

                        }

                        @Override
                        public void onException(Throwable exception) {

                        }
                    });


        }
    }

    private void resSetInfoList(List<RecentContactInfo> infoList, List<NimUserInfo> users) {
        if (users!=null) {
            Out:
            for (NimUserInfo user : users) {
                String account = user.getAccount();
                if (TextUtils.isEmpty(account)) {
                    continue;
                }
                for (RecentContactInfo info : infoList) {
                    if (account.equals(info.getAccount())) {
                        info.setAvatar(user.getAvatar());
                        info.setNickName(user.getName());
                        continue Out;
                    }
                }

            }
        }
        if (infoList.size() > 0) {
            mItems.addAll(infoList);
        }
        contactAdapter.updateItems(mItems);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Object tag = view.getTag();
        if (tag != null) {
            RecentContactInfo info = (RecentContactInfo) tag;
            ChatActivity.start(this, info.getAccount(), info.getNickName(), info.getAvatar(),info.getTime());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);
        // 进入最近联系人列表界面，建议放在onResume中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.P2P);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);
        // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
        NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.P2P);
    }


    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    AppLog.print("messageObserver____onEvent___");
                    updateRecentContacts(messages);
                }
            };
}
