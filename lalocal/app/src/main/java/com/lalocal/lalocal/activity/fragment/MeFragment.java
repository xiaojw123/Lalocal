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

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt1Activity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.MyArticleActivity;
import com.lalocal.lalocal.activity.MyFavoriteActivity;
import com.lalocal.lalocal.activity.MyLiveActivity;
import com.lalocal.lalocal.activity.MyOrderActivity;
import com.lalocal.lalocal.activity.MyWalletActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.activity.LiveAttentionOrFansActivity;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindDimen;
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
    public boolean isImLogin;
    User user;
    OnMeFragmentListener fragmentListener;
    Intent imLoginData;
    WalletContent walletContent;
    @BindView(R.id.home_me_fans_tab)
    LinearLayout homeMeFansTab;
    @BindView(R.id.home_me_flow_tab)
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

    @BindDimen(R.dimen.home_me_username_top)
    int userNameTop;
    int authorTop;
    @BindString(R.string.login_prompt)
    String loginPrmotText;
    RelativeLayout.LayoutParams userNameParams;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        AppLog.print("onAttach(Activity)___");
        fragmentListener = (OnMeFragmentListener) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.bind(this, view);
        authorTop = userNameTop / 2;
        userNameParams = (RelativeLayout.LayoutParams) username_tv.getLayoutParams();
        setLoaderCallBack(new MeCallBack());
        initLogin();
        return view;
    }

    private void initLogin() {
        if (UserHelper.isLogined(getActivity())) {
            //恢复上一次登录的状态
            String email = UserHelper.getUserEmail(getActivity());
            String psw = UserHelper.getPassword(getActivity());
            mContentloader.login(email, psw);
        }
    }


    //从其他页面切换到我的页面
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        AppLog.print("onHiddenChanged____" + hidden);
        if (isImLogin) {
            //立即登录
            isImLogin = false;
            if (imLoginData != null) {
                User user = imLoginData.getParcelableExtra(USER);
                if (user != null) {
                    updateFragmentView(true, user);
                } else {
                    String email = imLoginData.getStringExtra(LoginActivity.EMAIL);
                    String psw = imLoginData.getStringExtra(LoginActivity.PSW);
                    mContentloader.login(email, psw);
                }
            }
        } else {
            //正常登录方式  刷新邮箱验证状态 刷新我的收藏状态(我的收藏没被选中时更新我的收藏适配器)
            if (!hidden) {
                AppLog.print("onHiddenChanged__showFrament____");
                if (UserHelper.isLogined(getActivity())) {
                    mContentloader.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
                }
            }
        }


    }

    private void gotoSettingPage() {
        Intent intent = new Intent(getActivity(), SettingActivity.class);
        startActivityForResult(intent, 101);
    }

    private void gotoLoginPage() {
        //TODO:登录改版
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, 100);
//        Intent intent = new Intent(getActivity(), LLoginActivity.class);
//        startActivityForResult(intent, KeyParams.REQUEST_CODE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("onActivityResult  resCode___" + resultCode);
        if (resultCode == LoginActivity.REGISTER_OK) {
            String email = data.getStringExtra(LoginActivity.EMAIL);
            String psw = data.getStringExtra(LoginActivity.PSW);
            mContentloader.login(email, psw);
        } else if (resultCode == LoginActivity.LOGIN_OK) {
            User user = data.getParcelableExtra(USER);
            updateFragmentView(true, user);
        } else if (resultCode == SettingActivity.UN_LOGIN_OK) {
            signOut();
        } else if (resultCode == AccountEidt1Activity.UPDATE_ME_DATA) {
            String nickname = data.getStringExtra(KeyParams.NICKNAME);
            String avatar = data.getStringExtra(KeyParams.AVATAR);
            if (user != null) {
                user.setNickName(nickname);
                user.setAvatar(avatar);
//                updateFragmentView(UserHelper.isLogined(getActivity()), user);
                mContentloader.getUserProfile(UserHelper.getUserId(getActivity()), UserHelper.getToken(getActivity()));
            }
        } else if (resultCode == SettingActivity.IM_LOGIN) {
            isImLogin = true;
            imLoginData = data;
            if (fragmentListener != null) {
                fragmentListener.onShowRecommendFragment();
            }
            String ccid = imLoginData.getStringExtra(KeyParams.IM_CCID);
            String imtken = imLoginData.getStringExtra(KeyParams.IM_TOKEN);
            AppLog.i("TAG", "ccid:" + ccid + "token:" + imtken);
        } else if (resultCode == UPDAE_MY_WALLET) {
            mContentloader.getMyWallet();
        }
    }

    private void signOut() {
        MobHelper.singOff();
        UserHelper.updateSignOutInfo(getActivity());
        updateFragmentView(false, null);
    }

    private void updateFragmentView(boolean isLogined, User user) {
        this.user = user;
        if (isLogined && user != null) {
            String description = user.getDescription();
            if (TextUtils.isEmpty(description)) {
                loginPrompt.setText("");
            } else {
                loginPrompt.setText(description);
            }
            AppLog.print("账号登录————————");
            String nickname = user.getNickName();
            if (!TextUtils.isEmpty(nickname)) {
                username_tv.setActivated(true);
                username_tv.setText(nickname);
            }

            int role = user.getRole();
            if (role == 1) {
                //专栏作者
                userNameParams.topMargin = authorTop;
                authorTag.setVisibility(View.VISIBLE);
                if (verified_tv.getVisibility() == View.VISIBLE) {
                    verified_tv.setVisibility(View.GONE);
                }

            } else {
                userNameParams.topMargin = userNameTop;
                authorTag.setVisibility(View.GONE);
                if (verified_tv.getVisibility() != View.VISIBLE) {
                    verified_tv.setVisibility(View.VISIBLE);
                }
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
            }

            String avatar = user.getAvatar();
            if (!TextUtils.isEmpty(avatar)) {
                DrawableUtils.displayImg(getActivity(), headImg, avatar);

            }

            mContentloader.getLiveUserInfo(String.valueOf(UserHelper.getUserId(getActivity())));
            mContentloader.getMyWallet();
        } else {
            userNameParams.topMargin = userNameTop;
            AppLog.print("账号退出————————");
            loginPrompt.setText(loginPrmotText);
            homeMeItemWalletAmountTv.setText("0");
            username_tv.setActivated(false);
            username_tv.setText(getResources().getString(R.string.please_login));
            verified_tv.setVisibility(View.GONE);
            headImg.setImageResource(R.drawable.home_me_personheadnormal);
            homeMeFansNum.setText("0");
            homeMeFollowNum.setText("0");

        }
    }

    @OnClick({R.id.home_me_item_artice, R.id.home_me_set_btn, R.id.home_me_username, R.id.home_me_headportrait_img, R.id.home_me_item_live, R.id.home_me_fans_tab, R.id.home_me_flow_tab, R.id.home_me_item_message, R.id.home_me_item_favoirte, R.id.home_me_item_wallet, R.id.home_me_item_order, R.id.home_me_invitefriends})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.home_me_item_artice:
                gotoMyItemPage(MyArticleActivity.class);
                break;
            case R.id.home_me_username:
            case R.id.home_me_headportrait_img:
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
                //TODO：进入我的直播
                gotoMyItemPage(MyLiveActivity.class);
                break;
            case R.id.home_me_fans_tab:
                gotoLiveUserPage("1");
                break;
            case R.id.home_me_flow_tab:
                gotoLiveUserPage("0");
                break;
            case R.id.home_me_item_message:
                //TODO:待开发
                break;
            case R.id.home_me_item_favoirte:
                gotoMyItemPage(MyFavoriteActivity.class);
                break;
            case R.id.home_me_item_wallet:
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyWalletActivity.class);
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.home_me_item_order:
                if (UserHelper.isLogined(getActivity())) {
                    gotoMyItemPage(MyOrderActivity.class);
                } else {
                    showLoginDialog();
                }
                break;
            case R.id.home_me_invitefriends:
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

    private void showLoginDialog() {
        CustomDialog dialog = new CustomDialog(getActivity());
        dialog.setMessage("您还未登录，请登录");
        dialog.setTitle("提示");
        dialog.setCancelBtn("取消", null);
        dialog.setSurceBtn("确认", new CustomDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                gotoLoginPage();
            }
        });
        dialog.show();
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
        public void onLoginSucess(User user) {
            updateFragmentView(true, user);
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
            homeMeFollowNum.setText(String.valueOf(result.getAttentionNum()));
            homeMeFansNum.setText(String.valueOf(result.getFansNum()));
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


}
