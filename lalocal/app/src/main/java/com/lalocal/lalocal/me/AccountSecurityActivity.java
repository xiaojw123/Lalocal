package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AccountEidt2Activity;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.EmailBoundActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.SocialUser;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountSecurityActivity extends BaseActivity {
    //    public static final int RESULT_BIND_PHONE = 0x81;
//    private static final int MODIFY_USER_PROFILE = 4;
    @BindView(R.id.account_security_phone_layout)
    FrameLayout accountSecurityPhoneLayout;
    @BindView(R.id.account_security_email_layout)
    FrameLayout accountSecurityEmailLayout;
    @BindView(R.id.account_security_modifypsw)
    FrameLayout accountSecurityModifypsw;
    @BindView(R.id.accout_security_weixin_cb)
    Switch accoutSecurityWeixinCb;
    @BindView(R.id.accout_security_qq_cb)
    Switch accoutSecurityQqCb;
    @BindView(R.id.accout_security_weibo_cb)
    Switch accoutSecurityWeiboCb;
    @BindView(R.id.account_security_email_tv)
    TextView emailTv;
    @BindView(R.id.accout_security_email_status)
    TextView emailStatusTv;
    @BindView(R.id.account_security_phone_tv)
    TextView phoneTv;
    @BindView(R.id.account_security_unbind_tv)
    TextView phoneUnBindTv;
    @BindString(R.string.no_verified)
    String noVerifed;
    UMShareAPI mUmShareAPI;
    @BindView(R.id.account_security_name_weixin)
    TextView weixinNameTv;
    @BindView(R.id.account_security_name_qq)
    TextView qqNameTv;
    @BindView(R.id.account_security_name_weibo)
    TextView weiboNameTv;
    @BindString(R.string.no_bind)
    String noBindText;
    @BindString(R.string.no_verified)
    String noVerifedText;
    LoginUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_security);
        ButterKnife.bind(this);
        setLoginBackResult(true);
        setLoaderCallBack(new AccoutSecurityCallBack());
        mContentloader.getUserProfile(UserHelper.getUserId(this), UserHelper.getToken(this));
        mContentloader.getSocialUsers();
    }

    @OnClick({R.id.account_security_phone_layout, R.id.account_security_email_layout, R.id.account_security_modifypsw, R.id.accout_security_weixin_cb, R.id.accout_security_qq_cb, R.id.accout_security_weibo_cb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_security_phone_layout:
                if (phoneUnBindTv.getVisibility() == View.VISIBLE) {
                    Intent phoneIntent = new Intent(this, PhoneBindActivity.class);
                    startActivityForResult(phoneIntent, KeyParams.REQUEST_CODE);
                }
                break;
            case R.id.account_security_email_layout:
                if (mUser != null) {
                    String email = mUser.getEmail();
                    int userId = mUser.getId();
                    if (!TextUtils.isEmpty(email)) {
                        if (mUser != null && mUser.getStatus() == 0) {
                            Intent intent = new Intent(this, EmailBoundActivity.class);
                            intent.putExtra(KeyParams.EMAIL, email);
                            intent.putExtra(KeyParams.USERID, userId);
                            intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(this));
                            startActivityForResult(intent, KeyParams.REQUEST_CODE);
                        } else {
                            Intent intent = new Intent(this, AccountEidt2Activity.class);
                            intent.putExtra(AccountEidt2Activity.ACTION_TYPE, AccountEidt2Activity.ACTION_EMAIL_MODIFY);
                            intent.putExtra("emailtext", email);
                            intent.putExtra(KeyParams.USERID, userId);
                            intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(this));
                            startActivityForResult(intent, KeyParams.REQUEST_CODE);
                        }
                    } else {
                        Intent intent = new Intent(this, EmailBoundActivity.class);
                        intent.putExtra(KeyParams.USERID, userId);
                        intent.putExtra(KeyParams.TOKEN, UserHelper.getToken(this));
                        startActivityForResult(intent, KeyParams.REQUEST_CODE);
                    }
                }


                break;
            case R.id.account_security_modifypsw:
                if (mUser != null) {
                    Intent pswIntent = new Intent(this, LPasswordModifyActivity.class);
                    pswIntent.putExtra(KeyParams.EMAIL, mUser.getEmail());
                    startActivityForResult(pswIntent, KeyParams.REQUEST_CODE);
                }
                break;
            case R.id.accout_security_weixin_cb:
            case R.id.accout_security_qq_cb:
            case R.id.accout_security_weibo_cb:
                switchSocial((CompoundButton) view);
                break;
        }
    }

    private void switchSocial(final CompoundButton button) {
        AppLog.print("switchSocial isChecked___" + button.isChecked());
        /*
        * switch  status 1: close 2:open
        *     1 tag==null bind   tag!=null
        * */
        final Object tag = button.getTag();
        if (button.isChecked() && tag == null) {
            button.setChecked(false);
            mUmShareAPI = UMShareAPI.get(this);
            SHARE_MEDIA share_media = null;
            if (button == accoutSecurityWeixinCb) {
                share_media = SHARE_MEDIA.WEIXIN;
            } else if (button == accoutSecurityQqCb) {
                share_media = SHARE_MEDIA.QQ;
            } else if (button == accoutSecurityWeiboCb) {
                share_media = SHARE_MEDIA.SINA;
            }
            mUmShareAPI.doOauthVerify(this, share_media, authListener);
        } else if (!button.isChecked() && tag != null) {
            button.setChecked(true);
            CustomDialog dialog = new CustomDialog(this);
            dialog.setTitle("解除绑定");
            dialog.setMessage("解除绑定后就不能愉快地快捷登录了");
            dialog.setCancelBtn("我在想想", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    button.setChecked(true);
                }
            });
            dialog.setSurceBtn("我意已决", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    SocialUser user = (SocialUser) tag;
                    mContentloader.unBindSocialAccount(button, user.getId());
                }
            });
            dialog.show();
        }
    }

    private UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (mUmShareAPI != null) {
                mUmShareAPI.getPlatformInfo(AccountSecurityActivity.this, share_media, infoGetListener);
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            Toast.makeText(AccountSecurityActivity.this, "授权失败", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            Toast.makeText(AccountSecurityActivity.this, "取消授权", Toast.LENGTH_LONG).show();

        }
    };

    private UMAuthListener infoGetListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            AppLog.print("onComplete  loginBySocial__");
            if (mContentloader != null) {
                CompoundButton button = null;
                if (share_media == SHARE_MEDIA.SINA) {
                    button = accoutSecurityWeiboCb;
                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                    button = accoutSecurityWeixinCb;

                } else if (share_media == SHARE_MEDIA.QQ) {
                    button = accoutSecurityQqCb;
                }
                mContentloader.bindSocialAccount(button, map, share_media);

            }
        }


        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };


    class AccoutSecurityCallBack extends ICallBack {

        @Override
        public void onGetUserProfile(LoginUser user) {
            if (user != null) {
                mUser = user;
                String phone = user.getPhone();
                String email = user.getEmail();
                int status = user.getStatus();
                if (!TextUtils.isEmpty(phone)) {
                    phoneTv.setText(phone);
                    phoneTv.setVisibility(View.VISIBLE);
                    phoneUnBindTv.setVisibility(View.GONE);
                } else {
                    phoneTv.setVisibility(View.GONE);
                    phoneUnBindTv.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(email)) {
                    emailTv.setVisibility(View.VISIBLE);
                    emailTv.setText(email);
                    if (status == 0) {
                        emailStatusTv.setVisibility(View.GONE);
                    } else {
                        emailStatusTv.setText(noVerifedText);
                        emailStatusTv.setVisibility(View.VISIBLE);
                    }
                    accountSecurityModifypsw.setVisibility(View.VISIBLE);
                } else {
                    emailStatusTv.setText(noBindText);
                    emailStatusTv.setVisibility(View.VISIBLE);
                    accountSecurityModifypsw.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onBindSocialUser(CompoundButton button, SocialUser wexinUser, SocialUser qqUser, SocialUser weiboUser) {
            AppLog.print("onBindSocialUser");
            if (button == accoutSecurityWeixinCb) {
                accoutSecurityWeixinCb.setTag(wexinUser);
                updatSwitch(weixinNameTv, button, wexinUser);
            } else if (button == accoutSecurityQqCb) {
                accoutSecurityQqCb.setTag(qqUser);
                updatSwitch(qqNameTv, button, qqUser);
            } else if (button == accoutSecurityWeiboCb) {
                accoutSecurityWeiboCb.setTag(weiboUser);
                updatSwitch(weiboNameTv, button, weiboUser);
            }
        }

        @Override
        public void onUnBindSocialUser(CompoundButton switchBtn) {
            Toast.makeText(AccountSecurityActivity.this, "解除绑定成功", Toast.LENGTH_SHORT).show();
            mContentloader.getSocialUsers();
        }

        private void updatSwitch(TextView nameTv, CompoundButton button, SocialUser user) {
            if (user != null) {
                button.setChecked(true);
                nameTv.setVisibility(View.VISIBLE);
                nameTv.setText(user.getNickName());
                Toast.makeText(AccountSecurityActivity.this, "绑定成功", Toast.LENGTH_SHORT).show();

            } else {
                nameTv.setVisibility(View.GONE);
                button.setChecked(false);
                Toast.makeText(AccountSecurityActivity.this, "绑定失败", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onGetSocialUsers(SocialUser wexinUser, SocialUser qqUser, SocialUser weiboUser) {
            if (wexinUser != null) {
                accoutSecurityWeixinCb.setChecked(true);
                weixinNameTv.setText(wexinUser.getNickName());
                weixinNameTv.setVisibility(View.VISIBLE);
            } else {
                accoutSecurityWeixinCb.setChecked(false);
                weixinNameTv.setVisibility(View.GONE);
            }
            if (qqUser != null) {
                accoutSecurityQqCb.setChecked(true);
                qqNameTv.setText(qqUser.getNickName());
                qqNameTv.setVisibility(View.VISIBLE);
            } else {
                accoutSecurityQqCb.setChecked(false);
                qqNameTv.setVisibility(View.GONE);
            }
            if (weiboUser != null) {
                accoutSecurityWeiboCb.setChecked(true);
                weiboNameTv.setText(weiboUser.getNickName());
                weiboNameTv.setVisibility(View.VISIBLE);
            } else {
                accoutSecurityWeiboCb.setChecked(false);
                weiboNameTv.setVisibility(View.GONE);
            }
            accoutSecurityWeixinCb.setTag(wexinUser);
            accoutSecurityQqCb.setTag(qqUser);
            accoutSecurityWeiboCb.setTag(weiboUser);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        AppLog.print("onActivityResult__resultCode___" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        mContentloader.getUserProfile(UserHelper.getUserId(this), UserHelper.getToken(this));
    }

}
