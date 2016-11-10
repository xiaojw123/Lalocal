package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lalocal.lalocal.R.id.login_wechat_btn;

public class LLoginActivity extends BaseActivity implements View.OnFocusChangeListener {

    @BindView(R.id.login_areacode_tv)
    TextView areaCodeTv;
    @BindView(R.id.login_phone_editext)
    EditText phoneEdt;
    @BindView(R.id.login_phone_edit)
    LinearLayout phoneEdtCotainer;
    @BindView(R.id.login_get_password)
    Button pswGetBtn;
    @BindView(R.id.login_email_btn)
    Button loginEmailBtn;
    @BindView(R.id.login_qq_btn)
    Button loginQqBtn;
    @BindView(login_wechat_btn)
    Button loginWechatBtn;
    @BindView(R.id.login_weibo_btn)
    Button loginWeiboBtn;
    @BindView(R.id.login_other_way)
    LinearLayout loginOtherWay;
    @BindView(R.id.login_close_btn)
    Button closeBtn;
    @BindView(R.id.login_next_btn)
    Button nextBtn;
    @BindView(R.id.login_phonenum_text)
    TextView loginNumText;
    @BindView(R.id.login_vercode_medit)
    MyEditText vercodeMedt;
    @BindString(R.string.get_password)
    String pswGetStr;
    @BindColor(R.color.color_1a)
    int areaColor;
    String setting;
    boolean isImLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        setting = intent.getStringExtra(KeyParams.SETTING);
        isImLogin = intent.getBooleanExtra(KeyParams.IM_LOGIN, false);
        phoneEdt.setOnFocusChangeListener(this);
        setLoaderCallBack(new LoginCallBack());
        setLoginBackResult(true);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            areaCodeTv.setTextColor(areaColor);
            loginNumText.setVisibility(View.VISIBLE);
            phoneEdtCotainer.setSelected(true);
        } else {
            loginNumText.setVisibility(View.GONE);
            phoneEdtCotainer.setSelected(false);
        }


    }

    @OnClick({R.id.login_close_btn, R.id.login_next_btn, R.id.login_get_password, R.id.login_email_btn, R.id.login_wechat_btn, R.id.login_weibo_btn, R.id.login_qq_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_close_btn:
                finish();
                break;
            case R.id.login_next_btn:
                MobHelper.sendEevent(this, MobEvent.LOGIN_PHONE_NEXT);
                String phone = getPhone();
                String code = getCode();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
                    CommonUtil.showPromptDialog(this, "手机号或密码为空", null);
                    return;
                }
                mContentloader.loginByPhone(getPhone(), getCode());
                break;
            case R.id.login_get_password:
                MobHelper.sendEevent(this, MobEvent.LOGIN_PHONE_VERIFICATOIN);
                view.setEnabled(false);
                mContentloader.getSMSCode(view, getPhone(), null);
                break;
            case R.id.login_email_btn:
                MobHelper.sendEevent(this, MobEvent.LOGIN_EMAIL);
                Intent intent = new Intent(this, LEmailLoginActivity.class);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
                break;
            case R.id.login_qq_btn:
                AppLog.print("qq__click__");
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.QQ);
                break;
            case R.id.login_wechat_btn:
                AppLog.print("wechat__click__");
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_weibo_btn:
                AppLog.print("weibo__click__");
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.SINA);
                break;

        }
    }


    public String getPhone() {
        return phoneEdt.getText().toString();
    }

    public String getCode() {

        return vercodeMedt.getText();
    }


    class LoginCallBack extends ICallBack {
        @Override
        public void onSocialLogin(User user, String bodyPrams, String uidParams) {
            if (user == null) {
                //注册
                Intent intent = new Intent(LLoginActivity.this, LPEmailBoundActivity.class);
                intent.putExtra(KeyParams.PAGE_TYPE, PageType.Page_BIND_EMAIL_SOCIAL);
                intent.putExtra(KeyParams.SOCIAL_PARAMS, bodyPrams);
                intent.putExtra(KeyParams.UID_PARAMS, uidParams);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
            } else {
                UserHelper.setLoginSuccessResult(LLoginActivity.this, user);
            }

        }

        @Override
        public void onGetSmsCodeSuccess() {
            AppLog.print("onGetSmsCodeSuccess____");
            pswGetBtn.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, 1000);
        }

        @Override
        public void onLoginByPhone(User user, String phone, String code) {
            if (user == null) {
                Intent intent = new Intent(LLoginActivity.this, LPEmailBoundActivity.class);
                intent.putExtra(KeyParams.PHONE, phone);
                intent.putExtra(KeyParams.CODE, code);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
            } else {
                UserHelper.setLoginSuccessResult(LLoginActivity.this, user);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler.hasMessages(MSG_UPDATE_TIMER)) {
            mHandler.removeMessages(MSG_UPDATE_TIMER);
        }
        pswGetBtn.setEnabled(true);
        pswGetBtn.setText(pswGetStr);
    }

    Handler mHandler = new Handler() {
        int timerLen = 60;

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MSG_UPDATE_TIMER:
                    pswGetBtn.setText(timerLen-- + "s");
                    if (timerLen < 0) {
                        if (hasMessages(MSG_UPDATE_TIMER)) {
                            removeMessages(MSG_UPDATE_TIMER);
                        }
                        pswGetBtn.setEnabled(true);
                        pswGetBtn.setText(pswGetStr);
                    } else {
                        sendEmptyMessageDelayed(MSG_UPDATE_TIMER, 1000);
                    }
                    break;

            }


        }
    };
    public static final int MSG_UPDATE_TIMER = 0x11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (isImLogin) {
            resultCode = MeFragment.IM_LOGIN;
            AppLog.print("isImLogin____imlogin_resultCode_" + resultCode);
            setResult(resultCode, data);
            UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
            finish();
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
