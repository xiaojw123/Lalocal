package com.lalocal.lalocal.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.EmptActivity;
import com.lalocal.lalocal.activity.HomeActivity;
import com.lalocal.lalocal.activity.MyArticleActivity;
import com.lalocal.lalocal.activity.MyFavoriteActivity;
import com.lalocal.lalocal.activity.MyLiveActivity;
import com.lalocal.lalocal.activity.MyOrderActivity;
import com.lalocal.lalocal.activity.MyWalletActivity;
import com.lalocal.lalocal.activity.PersonalMessageActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveAttentionOrFansActivity;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.MeItem;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.ArcImageView;
import com.lalocal.lalocal.view.ReboundScrollView;
import com.lalocal.lalocal.view.ShapeTextView;
import com.lalocal.lalocal.view.adapter.MeItemAdapter;
import com.lalocal.lalocal.view.decoration.DividerGridItemDecoration;
import com.lalocal.lalocal.view.listener.OnItemClickListener;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.model.RecentContact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by xiaojw on 2016/6/3.
 */
public class
MeFragment extends BaseFragment {
    public static final int UPDAE_MY_WALLET = 0x01323;
    public static final String USER = "user";
    public static final int LOGIN_OK = 102;
    public static final int UN_LOGIN_OK = 103;
    public static final int UPDATE_ME_DATA = 301;
    public static final int IM_LOGIN_OK = 105;
    OnMeFragmentListener fragmentListener;
    @BindView(R.id.home_me_fans_tab)
    LinearLayout homeMeFansTab;
    @BindView(R.id.home_me_atten_tab)
    LinearLayout homeMeFlowTab;
    @BindView(R.id.home_me_fans_num)
    TextView homeMeFansNum;
    @BindView(R.id.home_me_follow_num)
    TextView homeMeFollowNum;
    @BindView(R.id.home_me_username)
    TextView username_tv;
    @BindView(R.id.home_me_verified)
    TextView verified_tv;
    @BindView(R.id.home_me_headportrait_img)
    ArcImageView headImg;
    @BindView(R.id.home_me_login_prompt)
    TextView loginPrompt;
    @BindView(R.id.home_me_author_tag)
    ImageView authorTag;
    @BindView(R.id.home_me_userprofile_container)
    ViewGroup loginLayout;
    @BindView(R.id.fragment_me_unlogin_layout)
    ViewGroup unLoginLayout;
    @BindView(R.id.fragment_me_login_stv)
    ShapeTextView loginStv;
    @BindView(R.id.home_me_personal_info)
    LinearLayout perInfoCotainer;
    @BindString(R.string.login_prompt)
    String loginPrmotText;
    @BindString(R.string.default_description)
    String defaultDecription;
    @BindView(R.id.fragment_me_rlv)
    RecyclerView itemRlv;
    @BindView(R.id.fragment_me_rsv)
    ReboundScrollView reboundScrollView;
    MeItemAdapter itemAdapter;
    int commUnReadCount;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AppLog.print("meFragment_onAttach(Activity)___");
        fragmentListener = (OnMeFragmentListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AppLog.print("meFragment_onCreateView____");
        View view = inflater.inflate(R.layout.fragment_me_new, container, false);
        ButterKnife.bind(this, view);
        if (itemAdapter == null) {
            itemAdapter = new MeItemAdapter(getMeItems(false));
            itemAdapter.setOnItemClickListener(recyclerClickListener);
        }
//        itemRlv.setLayoutManager(new FullyGridLayoutManager(getActivity(), 3));
        itemRlv.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        itemRlv.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        itemRlv.setNestedScrollingEnabled(false);
        itemRlv.setAdapter(itemAdapter);
        setLoaderCallBack(new MeCallBack());
        return view;
    }

    @NonNull
    private List<MeItem> getMeItems(boolean hasArticle) {
        List<MeItem> items = new ArrayList<>();
        MeItem item1 = new MeItem(MeItemAdapter.ITEM_MY_LIVE, "我的直播", R.drawable.mylive_ic);
        MeItem item2 = new MeItem(MeItemAdapter.ITEM_MY_MESSAGE, "私信", R.drawable.mymassage_ic);
        MeItem item3 = new MeItem(MeItemAdapter.ITEM_MY_FAVOR, "我的收藏", R.drawable.mycollect_ic);
        MeItem item4 = new MeItem(MeItemAdapter.ITEM_MY_WALLET, "我的钱包", R.drawable.mywallet_ic);
        MeItem item5 = new MeItem(MeItemAdapter.ITEM_MY_ORDER, "我的订单", R.drawable.myorder_ic);
        MeItem item6 = new MeItem(MeItemAdapter.ITEM_MY_FRIEND, "邀请好友", R.drawable.invitemyfriends_ic);
        MeItem item8 = new MeItem(MeItemAdapter.ITEM_MY_SETTING, "设置", R.drawable.mysetting_ic);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        items.add(item4);
        items.add(item5);
        items.add(item6);
        if (hasArticle) {
            MeItem item7 = new MeItem(MeItemAdapter.ITEM_MY_ARTICLE, "我的文章", R.drawable.myartical_ic);
            items.add(item7);
        }
        items.add(item8);
        return items;
    }

    //初始化登录状态
    private void initLogin() {
        if (UserHelper.isLogined(getActivity())) {
            mContentloader.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
        } else {
            updateFragmentView(false, null);
        }
    }


    //从其他页面切换到我的页面
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.print("meFragment_onHiddenChanged____" + hidden);
        if (!hidden) {
            registerMsgServicesObser(true);
            initLogin();
        } else {
            registerMsgServicesObser(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLog.print("meFragment____onStart");
        initLogin();
    }

    private void gotoSettingPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivityForResult(intent, 101);
    }

    private void gotoLoginPage() {
        Intent intent = new Intent(getActivity(), LLoginActivity.class);
        startActivityForResult(intent, KeyParams.REQUEST_CODE);
    }

    //更新UI
    private void updateFragmentView(boolean isLogined, User user) {
        if (isLogined && user != null) {//更新登录状态UI
            //显示个人页登录布局
            if (loginLayout.getVisibility() != View.VISIBLE) {
                reboundScrollView.smoothScrollTo(0, 0);
                loginLayout.setVisibility(View.VISIBLE);
                unLoginLayout.setVisibility(View.GONE);
            }
            loginLayout.requestFocus();
            //设置个人介绍
            String description = user.getDescription();
            if (TextUtils.isEmpty(description)) {
                loginPrompt.setText(defaultDecription);
            } else {
                loginPrompt.setText(description);
            }
            //设置昵称
            username_tv.setText(user.getNickName());
            //设置用户角色
            int role = user.getRole();
            if (role == 1) {//专栏作者
                verified_tv.setVisibility(View.GONE);
                showArticleTag();
            } else {//普通用户
                verified_tv.setVisibility(View.VISIBLE);
                //设置邮箱状态
                int status = user.getStatus();
                switch (status) {
                    case 0://已验证
                        verified_tv.setActivated(true);
                        verified_tv.setText(getResourceString(R.string.verified));
                        break;
                    case -1://未验证
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResourceString(R.string.no_verified));
                        break;
                    case 1://拉黑
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResourceString(R.string.user_black));
                        break;
                    case 2://禁用
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResourceString(R.string.user_forbiden));
                        break;
                }
                hidenArticeTag();
            }
            String email = user.getEmail();
            if (TextUtils.isEmpty(email)) {
                verified_tv.setText("未绑定");
            }
            //设置用户头像
            String avatar = user.getAvatarOrigin();
            if (!TextUtils.isEmpty(avatar)) {
                DrawableUtils.displayImg(getActivity(), headImg, avatar);
            }
            //关注、粉丝、消息个数
            homeMeFollowNum.setText(user.getAttentionNum());
            homeMeFansNum.setText(user.getFansNum());
            mContentloader.getMessageCount();
        } else {//更新未登录状态UI
            if (unLoginLayout.getVisibility() != View.VISIBLE) {
                reboundScrollView.smoothScrollTo(0, 0);
                unLoginLayout.setVisibility(View.VISIBLE);
                loginLayout.setVisibility(View.GONE);
            }
            unLoginLayout.requestFocus();
            //关闭未读消息标记
            updateMessageCount(isLogined, 0);
        }

    }

    public String getResourceString(int id) {
        if (isAdded()) {
            return getString(id);
        }
        return null;
    }

    //显示认证作家标记
    public void showArticleTag() {
        authorTag.setVisibility(View.VISIBLE);
        itemAdapter.updateItems(getMeItems(true));
    }

    //隐藏认证作家标记
    public void hidenArticeTag() {
        authorTag.setVisibility(View.GONE);
        itemAdapter.updateItems(getMeItems(false));
    }

    private OnItemClickListener recyclerClickListener = new OnItemClickListener() {
        @Override
        public void onItemClickListener(View view, int position) {
            Object tagObj = view.getTag();
            if (tagObj != null) {
                MeItem item = (MeItem) tagObj;
                switch (item.getId()) {
                    case MeItemAdapter.ITEM_MY_LIVE://我的直播
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_LIVE);
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(MyLiveActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_MESSAGE://我的消息
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_NOTICE);
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(PersonalMessageActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_FAVOR://我的收藏
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_COLLECTION);
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(MyFavoriteActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_WALLET://我的钱包
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_WALLET);
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(MyWalletActivity.class);
                        } else {
                            gotoLoginPage();
                        }

                        break;
                    case MeItemAdapter.ITEM_MY_ORDER://我的订单
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_ORDER);
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(MyOrderActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_FRIEND://邀请好友
                        MobHelper.sendEevent(getActivity(), MobEvent.MY_FIND);
                        //TODO:待开发
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(EmptActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_ARTICLE://我的文章
                        if (UserHelper.isLogined(getActivity())) {
                            gotoMyItemPage(MyArticleActivity.class);
                        } else {
                            gotoLoginPage();
                        }
                        break;
                    case MeItemAdapter.ITEM_MY_SETTING://设置
                        gotoSettingPage();
                        break;


                }


            }


        }
    };

    @OnClick({R.id.home_me_personal_info, R.id.fragment_me_login_stv, R.id.home_me_fans_tab, R.id.home_me_atten_tab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_me_login_stv://登录页面跳转
                MobHelper.sendEevent(getActivity(), MobEvent.USER_LOGIN_BUTTON);
                gotoLoginPage();
                break;
            case R.id.home_me_personal_info://个人信息页面跳转
                MobHelper.sendEevent(getActivity(), MobEvent.MY_AVATAR);
                gotoEditPage();
                break;
            case R.id.home_me_fans_tab://粉丝页面跳转
                MobHelper.sendEevent(getActivity(), MobEvent.MY_FANS);
                if (UserHelper.isLogined(getActivity())) {
                    gotoLiveUserPage("1");
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_atten_tab://关注页面跳转
                MobHelper.sendEevent(getActivity(), MobEvent.MY_ATTENTION);
                if (UserHelper.isLogined(getActivity())) {
                    gotoLiveUserPage("0");

                } else {
                    gotoLoginPage();
                }
                break;
        }
    }

    private void gotoEditPage() {
        Intent intent = new Intent(getActivity(), AccountEidt1Activity.class);
        intent.putExtra(KeyParams.USERID, UserHelper.getUserId(getActivity()));
        intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(getActivity()));
        startActivityForResult(intent, 100);
    }

    private void gotoMyItemPage(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    private void gotoLiveUserPage(String liveType) {
        Intent intent = new Intent(getActivity(), LiveAttentionOrFansActivity.class);
        intent.putExtra("liveType", liveType);
        intent.putExtra("userId", String.valueOf(UserHelper.getUserId(getActivity())));
        startActivity(intent);
    }


    class MeCallBack extends ICallBack {

        @Override
        public void onGetMessageCount(int msgCount) {
            commUnReadCount = msgCount;
            updateMessageCount(true, msgCount);
        }

        @Override
        public void onGetUserProfile(LoginUser user) {
            updateFragmentView(true, user);
            String sex = user.isSex() ? getResources().getString(R.string.man) : getResources().getString(R.string.woman);
            Map<String, Object> params = new HashMap<>();
            params.put(MobHelper.UMADParamas.USER_ID,user.getId());
            params.put(MobHelper.UMADParamas.NAME, user.getNickName());
            params.put(MobHelper.UMADParamas.SEX, sex);
            MobHelper.registerSuperProperty(getActivity(),params);
        }

    }

    private void updateMessageCount(boolean isLogin, int msgCount) {
        if (isLogin) {
            msgCount += NIMClient.getService(MsgService.class).getTotalUnreadCount();
            if (msgCount > 0) {
                if (isAdded()) {
                    if (((HomeActivity) getActivity()).unReadTv.getVisibility() != View.VISIBLE) {
                        ((HomeActivity) getActivity()).unReadTv.setVisibility(View.VISIBLE);
                    } else {
                        ((HomeActivity) getActivity()).unReadTv.setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        if (itemAdapter != null) {
            List<MeItem> items = itemAdapter.getItems();
            MeItem item = items.get(1);
            if (item != null) {
                item.setMsgCount(msgCount);
                itemAdapter.notifyItemChanged(1);
            }
        }


    }


    public interface OnMeFragmentListener {
        void onShowRecommendFragment();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("onActivityResult___");
        switch (resultCode) {
            case IM_LOGIN_OK://立即登录
                if (fragmentListener != null) {
                    fragmentListener.onShowRecommendFragment();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        registerMsgServicesObser(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        registerMsgServicesObser(false);
    }

    public void registerMsgServicesObser(boolean flag) {
        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, flag);
    }

    //  创建观察者对象
    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    AppLog.print("messageObserver____onEvent___");
                    updateMessageCount(UserHelper.isLogined(getActivity()), 0);
                }
            };

}
