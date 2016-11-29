package com.lalocal.lalocal.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.MyArticleActivity;
import com.lalocal.lalocal.activity.MyFavoriteActivity;
import com.lalocal.lalocal.activity.MyLiveActivity;
import com.lalocal.lalocal.activity.MyOrderActivity;
import com.lalocal.lalocal.activity.MyWalletActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.help.ErrorMessage;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveAttentionOrFansActivity;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;

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
    WalletContent walletContent;
    @BindView(R.id.home_me_fans_tab)
    LinearLayout homeMeFansTab;
    @BindView(R.id.home_me_atten_tab)
    LinearLayout homeMeFlowTab;
    @BindView(R.id.home_me_item_message)
    RelativeLayout homeMeItemMessage;
    @BindView(R.id.home_me_item_favoirte)
    FrameLayout homeMeItemFavoirte;
    @BindView(R.id.home_me_item_wallet_amount_tv)
    TextView homeMeItemWalletAmountTv;
    @BindView(R.id.home_me_item_wallet)
    RelativeLayout homeMeItemWallet;
    @BindView(R.id.home_me_item_order)
    FrameLayout homeMeItemOrder;
    @BindView(R.id.home_me_invitefriends)
    FrameLayout homeMeInvitefriends;
    @BindView(R.id.home_me_fans_num)
    TextView homeMeFansNum;
    @BindView(R.id.home_me_follow_num)
    TextView homeMeFollowNum;
    @BindView(R.id.home_me_username)
    TextView username_tv;
    @BindView(R.id.home_me_verified)
    TextView verified_tv;
    @BindView(R.id.home_me_headportrait_img)
    ImageView headImg;
    @BindView(R.id.home_me_set_btn)
    ImageButton settingBtn;
    @BindView(R.id.home_me_message_num)
    TextView messageNumTv;
    @BindView(R.id.home_me_login_prompt)
    TextView loginPrompt;
    @BindView(R.id.home_me_item_live)
    FrameLayout liveItem;
    @BindView(R.id.home_me_author_tag)
    ImageView authorTag;
    @BindView(R.id.home_me_item_artice)
    FrameLayout articleFl;
    @BindView(R.id.home_me_item_artice_line)
    View articleLine;
    @BindString(R.string.login_prompt)
    String loginPrmotText;
    @BindString(R.string.default_description)
    String defaultDecription;

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
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        setLoaderCallBack(new MeCallBack());
        return view;
    }

    private void initLogin() {
        if (UserHelper.isLogined(getActivity())) {
            //恢复上一次登录的状态
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
            initLogin();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        AppLog.print("meFragment____onStart");
        initLogin();
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.print("meFragment____onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        AppLog.print("meFragment____onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        AppLog.print("meFragment____onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.print("meFragment____onDestry__");
    }

    private void gotoSettingPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivityForResult(intent, 101);
    }

    private void gotoLoginPage() {
        //TODO:登录改版
//        Intent intent = new Intent(getActivity(), LoginActivity.class);
//        startActivityForResult(intent, 100);
        Intent intent = new Intent(getActivity(), LLoginActivity.class);
        startActivityForResult(intent, KeyParams.REQUEST_CODE);
    }


    private void updateFragmentView(boolean isLogined, User user) {
        if (isLogined && user != null) {
            String description = user.getDescription();
            if (TextUtils.isEmpty(description)) {
                loginPrompt.setText(defaultDecription);
            } else {
                loginPrompt.setText(description);
            }
            String nickname = user.getNickName();
            AppLog.print("账号登录———————nickName—" + nickname);
            if (!TextUtils.isEmpty(nickname)) {
                username_tv.setActivated(true);
                username_tv.setText(nickname);
            }

            int role = user.getRole();
            if (role == 1) {
                //专栏作者
                verified_tv.setVisibility(View.GONE);
                showArticleTag();
            } else {

                verified_tv.setVisibility(View.VISIBLE);
                int status = user.getStatus();
                switch (status) {
                    case 0:
                        verified_tv.setActivated(true);
                        verified_tv.setText(getResources().getString(R.string.verified));
                        break;
                    case -1:
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResources().getString(R.string.no_verified));
                        break;
                    case 1:
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResources().getString(R.string.user_black));
                        break;
                    case 2:
                        verified_tv.setActivated(false);
                        verified_tv.setText(getResources().getString(R.string.user_forbiden));
                        break;
                }
                hidenArticeTag();
            }
            String email = user.getEmail();
            if (TextUtils.isEmpty(email)) {
                verified_tv.setText("未绑定");
            }
            String avatar = user.getAvatar();
            if (!TextUtils.isEmpty(avatar)) {
                DrawableUtils.displayImg(getActivity(), headImg, avatar);
            }
            homeMeFollowNum.setText(user.getAttentionNum());
            homeMeFansNum.setText(user.getFansNum());
            homeMeItemWalletAmountTv.setVisibility(View.VISIBLE);
            mContentloader.getMyWallet();
        } else {
            AppLog.print("账号退出————————");
            loginPrompt.setText(loginPrmotText);
            homeMeItemWalletAmountTv.setText("0");
            username_tv.setActivated(false);
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
            homeMeFansNum.setText("0");
            homeMeFollowNum.setText("0");
            hidenArticeTag();
            homeMeItemWalletAmountTv.setVisibility(View.GONE);
        }
    }

    public void showArticleTag() {
        authorTag.setVisibility(View.VISIBLE);
        articleFl.setVisibility(View.VISIBLE);
        articleLine.setVisibility(View.VISIBLE);

    }

    public void hidenArticeTag() {
        authorTag.setVisibility(View.GONE);
        articleFl.setVisibility(View.GONE);
        articleLine.setVisibility(View.GONE);
    }

    @OnClick({R.id.home_me_item_artice, R.id.home_me_set_btn, R.id.home_me_username, R.id.home_me_headportrait_img, R.id.home_me_item_live, R.id.home_me_fans_tab, R.id.home_me_atten_tab, R.id.home_me_item_message, R.id.home_me_item_favoirte, R.id.home_me_item_wallet, R.id.home_me_item_order, R.id.home_me_invitefriends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_me_item_artice:
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyArticleActivity.class);
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_username:
            case R.id.home_me_headportrait_img:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_AVATAR);
                if (UserHelper.isLogined(getActivity())) {
                    gotoEditPage();
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_set_btn:
                gotoSettingPage();
                break;

            case R.id.home_me_item_live:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_LIVE);
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyLiveActivity.class);
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_fans_tab:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_FANS);
                if (UserHelper.isLogined(getActivity())) {
                    gotoLiveUserPage("1");
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_atten_tab:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_ATTENTION);
                if (UserHelper.isLogined(getActivity())) {
                    gotoLiveUserPage("0");

                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_item_message:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_NOTICE);
                if (UserHelper.isLogined(getActivity())) {
                    //TODO:待开发
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_item_favoirte:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_COLLECTION);
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyFavoriteActivity.class);
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_item_wallet:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_WALLET);
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyWalletActivity.class);
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_item_order:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_ORDER);
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyOrderActivity.class);
                } else {
                    gotoLoginPage();
                }
                break;
            case R.id.home_me_invitefriends:
                MobHelper.sendEevent(getActivity(), MobEvent.MY_FIND);
                //TODO:待开发
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
        if (cls == MyWalletActivity.class) {
            intent.putExtra(KeyParams.WALLET_CONTENT, walletContent);
            startActivityForResult(intent, KeyParams.REQUEST_CODE);
        } else {
            startActivity(intent);
        }
    }

    private void gotoLiveUserPage(String liveType) {
        Intent intent = new Intent(getActivity(), LiveAttentionOrFansActivity.class);
        intent.putExtra("liveType", liveType);
        intent.putExtra("userId", String.valueOf(UserHelper.getUserId(getActivity())));
        startActivity(intent);
    }


    class MeCallBack extends ICallBack {


        @Override
        public void onError(VolleyError volleyError) {
            if (ErrorMessage.AUTHOR_FIALED.equals(volleyError.toString())) {
                if (!UserHelper.isLogined(getActivity())) {
                    updateFragmentView(false, null);
                }
            }

        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();

        }


        //只刷新验证状态————
        @Override
        public void onGetUserProfile(LoginUser user) {
            AppLog.print("onGetUserProfiles____获取用户信息————");
            updateFragmentView(true, user);
        }

        @Override
        public void onGetMyWallet(WalletContent content) {
            walletContent = content;
            if (content != null) {
                String goldText = CommonUtil.formartNum(content.getGold());
                homeMeItemWalletAmountTv.setText(goldText);
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

            case LOGIN_OK: //登录成功
                User user = data.getParcelableExtra(USER);
                updateFragmentView(true, user);
                break;
            case UN_LOGIN_OK://退出成功
                UserHelper.updateSignOutInfo(getActivity());
                updateFragmentView(false, null);
                break;
            case UPDATE_ME_DATA://更新我的页面
                if (UserHelper.isLogined(getActivity())) {
                    mContentloader.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
                } else {
                    updateFragmentView(false, null);
                }
                break;
            case IM_LOGIN_OK://立即登录
                if (fragmentListener != null) {
                    fragmentListener.onShowRecommendFragment();
                }
                break;
            case UPDAE_MY_WALLET://更新我的钱包
                if (UserHelper.isLogined(getActivity())) {
                    mContentloader.getMyWallet();
                } else {
                    updateFragmentView(false, null);
                }
                break;
        }
    }


}
