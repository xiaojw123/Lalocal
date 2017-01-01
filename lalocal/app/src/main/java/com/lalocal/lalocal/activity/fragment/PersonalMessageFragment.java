package com.lalocal.lalocal.activity.fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.im.ChatFragment;
import com.lalocal.lalocal.im.LalocalHelperActivity;
import com.lalocal.lalocal.im.NimChatActivity;
import com.lalocal.lalocal.im.view.adapter.RecentContactAdapter;
import com.lalocal.lalocal.live.entertainment.activity.LivePlayerBaseActivity;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by xiaojw on 2016/12/24.
 */

public class PersonalMessageFragment extends BaseFragment implements OnItemClickListener, View.OnClickListener {

    FrameLayout titleLayout;
    Button cancelBtn;
    RecyclerView personalMessageRlv;
    RelativeLayout assistantLayout;
    RecentContactAdapter contactAdapter;
    List<RecentContactInfo> mItems = new ArrayList<>();
    ChatFragment chatFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_personal_msg, container, false);
        initView(contentView);
        return contentView;
    }

    public void setNextFragment(ChatFragment chatFragment) {
        this.chatFragment = chatFragment;
    }

    private void initView(View view) {
        titleLayout = (FrameLayout) view.findViewById(R.id.fragment_personal_msg_title);
        cancelBtn = (Button) view.findViewById(R.id.chat_cancel_btn);
        personalMessageRlv = (RecyclerView) view.findViewById(R.id.personal_msg_rlv);
        assistantLayout = (RelativeLayout) view.findViewById(R.id.personal_msg_assistant);
        cancelBtn.setOnClickListener(this);
        assistantLayout.setOnClickListener(this);
        personalMessageRlv.setLayoutManager(new LinearLayoutManager(getActivity()));
        personalMessageRlv.setNestedScrollingEnabled(false);
        contactAdapter = new RecentContactAdapter(mItems);
        contactAdapter.setOnItemClickListener(this);
        personalMessageRlv.setAdapter(contactAdapter);
        Bundle bundle = getArguments();
        if (bundle != null) {
            boolean hasTitle = bundle.getBoolean(KeyParams.HAST_TITLE);
            if (hasTitle) {
                titleLayout.setVisibility(View.VISIBLE);
            }
        }
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
        final List<RecentContactInfo> infoList = new ArrayList<>();
        List<String> ssidlist = new ArrayList<>();
//        Out:
//        for (RecentContact contact : recents) {
//            String ssid = contact.getContactId();
//            for (RecentContactInfo curInfo : mItems) {
//                if (ssid.equals(curInfo.getAccount())) {
//                    curInfo.setContent(contact.getContent());
//                    curInfo.setTime(contact.getTime());
//                    curInfo.setUnReadCount(contact.getUnreadCount());
//                    continue Out;//结束整个循环体
//                }
//            }
//            RecentContactInfo info = new RecentContactInfo();
//            info.setAccount(ssid);
//            info.setContent(contact.getContent());
//            info.setTime(contact.getTime());
//            info.setUnReadCount(contact.getUnreadCount());
//            infoList.add(info);
//            ssidlist.add(ssid);
//        }
        int len = recents.size();
        Out:
        for (int i = 0; i < len; i++) {
            RecentContact contact = recents.get(i);
            String ssid = contact.getContactId();
            for (RecentContactInfo curInfo : mItems) {
                if (ssid.equals(curInfo.getAccount())) {
                    curInfo.setContent(contact.getContent());
                    curInfo.setTime(contact.getTime());
                    curInfo.setUnReadCount(contact.getUnreadCount());
                    continue Out;//继续下一次外层循环
                }
            }
            RecentContactInfo info = new RecentContactInfo();
//            long currentTime = contact.getTime();
//            int nextIndex = i + 1;
//            if (nextIndex < len) {
//                RecentContact nextContct = recents.get(nextIndex);
//                if (nextContct != null) {
//                    long nextTime = nextContct.getTime();
//                    if (currentTime < nextTime) {
//                        info.setAccount(nextContct.getContactId());
//                        info.setContent(nextContct.getContent());
//                        info.setTime(nextTime);
//                        info.setUnReadCount(nextContct.getUnreadCount());
//                        infoList.add(nextIndex, info);
//                        ssidlist.add(nextIndex, ssid);
//                        continue;
//                    }
//                }
//            }
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
        if (users != null) {
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
        Collections.sort(mItems, desContactsComparator);
        contactAdapter.updateItems(mItems);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Object tag = view.getTag();
        if (tag != null) {
            RecentContactInfo info = (RecentContactInfo) tag;
            Bundle bundle = new Bundle();
            bundle.putString(KeyParams.ACCID, info.getAccount());
            bundle.putString(KeyParams.NICKNAME, info.getNickName());
            if (getActivity() instanceof LivePlayerBaseActivity) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                chatFragment.updateView(bundle);
                ft.hide(this);
                ft.show(chatFragment);
                ft.commit();
                return;
            }
            NimChatActivity.start(getActivity(), bundle);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.print("personalMessage__onResume___");
        registerNimService(true);
    }

    private void queryContacts() {
        NIMClient.getService(MsgService.class).queryRecentContacts()
                .setCallback(requestCallbackWrapper);
    }

    public void registerNimService(boolean flag) {
        //  注册/注销观察者
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, flag);
        if (flag) {
            // 进入最近联系人列表界面，建议放在onResume中
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.P2P);
            queryContacts();
        } else {
            // 退出聊天界面或离开最近联系人列表界面，建议放在onPause中
            NIMClient.getService(MsgService.class).setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.P2P);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.print("personalMessage__onPause___");
        registerNimService(false);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.chat_cancel_btn:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.hide(this);
                ft.hide(chatFragment);
                ft.commit();
                break;
            case R.id.personal_msg_assistant:
                LalocalHelperActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.print("onHiddenChanged————————hiden___" + hidden);
        if (!hidden) {
            registerNimService(true);
        } else {
            registerNimService(false);
        }

    }

    private Comparator<RecentContactInfo> desContactsComparator = new Comparator<RecentContactInfo>() {

        @Override
        public int compare(RecentContactInfo o1, RecentContactInfo o2) {
            return (int) (o2.getTime() - o1.getTime());
        }
    };
}
