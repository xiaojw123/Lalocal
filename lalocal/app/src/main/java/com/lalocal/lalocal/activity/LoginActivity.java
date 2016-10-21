package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import static com.lalocal.lalocal.R.id.login_forget_psw_tv;

public class LoginActivity extends BaseActivity {
    public static final int REGISTER_OK = 101;
    public static final int LOGIN_OK = 102;
    public static final String EMAIL = "email";
    public static final String PSW = "psw";
    CustomEditText email_edit, psw_edit;
    CustomTitleView login_ctv;
    TextView register_tv, forgetpsw_tv;
    Button login_btn;
    ContentLoader contentService;
    String setting;
    boolean isImLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        Intent intent = getIntent();
        setting = intent.getStringExtra(KeyParams.SETTING);
        isImLogin = intent.getBooleanExtra(KeyParams.IM_LOGIN, false);
        initContentService();
        initView();
       claerImLoginInfo();//清除im登錄信息

    }

    private void claerImLoginInfo() {
        DemoCache.clear();;
        AuthPreferences.clearUserInfo();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
    }

    private void initContentService() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        login_ctv = (CustomTitleView) findViewById(R.id.login_ctv);
        register_tv = (TextView) findViewById(R.id.login_register_tv);
        forgetpsw_tv = (TextView) findViewById(login_forget_psw_tv);
        email_edit = (CustomEditText) findViewById(R.id.login_emial_custom_edit);
        psw_edit = (CustomEditText) findViewById(R.id.login_psw_custom_edit);
        login_btn = (Button) findViewById(R.id.login_btn);
        email_edit.setSelectedButton(login_btn);
        psw_edit.setSelectedButton(login_btn);
        psw_edit.setTextVisible(false);
        email_edit.setClearButtonVisible(false);
        login_ctv.setOnBackClickListener(backClicklistener);
        register_tv.setOnClickListener(loginClickListener);
        forgetpsw_tv.setOnClickListener(loginClickListener);
        login_btn.setOnClickListener(loginClickListener);


    }

    private CustomTitleView.onBackBtnClickListener backClicklistener = new CustomTitleView.onBackBtnClickListener() {
        @Override
        public void onBackClick() {
            MobHelper.sendEevent(LoginActivity.this,MobEvent.LOGIN_EMAIL_BACK);
            if (KeyParams.SETTING.equals(setting)) {
                setResult(SettingActivity.UN_LOGIN_OK);
            }
        }
    };

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == register_tv) {
                MobHelper.sendEevent(LoginActivity.this,MobEvent.LOGIN_EMAIL_REGISTERED);
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 100);
            } else if (v == login_btn) {
                MobHelper.sendEevent(LoginActivity.this,MobEvent.LOGIN_EMAIL_ENTER);
                String email = email_edit.getText().toString();
                String psw = psw_edit.getText().toString();
                login(email, psw);
            } else if (v == forgetpsw_tv) {
                MobHelper.sendEevent(LoginActivity.this, MobEvent.LOGIN_FORGET);
                Intent intent = new Intent(LoginActivity.this, PasswordForget1Activity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            AppLog.i("TAG","data为空");
            return;
        }
        if (isImLogin) {
            setResult(SettingActivity.IM_LOGIN, data);
            AppLog.i("TAG","LoginActivity:走了这里1");
        } else {
            AppLog.i("TAG","LoginActivity:走了这里2");
            setResult(REGISTER_OK, data);
        }

        finish();
    }


    @Override
    public void onBackPressed() {
        if (KeyParams.SETTING.equals(setting)) {
            setResult(SettingActivity.UN_LOGIN_OK);
        }
        super.onBackPressed();
    }

    public void login(String email, String psw) {
        if (CommonUtil.isEmpty(email) && TextUtils.isEmpty(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_or_psw_no_empty), null);
            return;
        }
        if (CommonUtil.isEmpty(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
            return;
        }
        if (CommonUtil.isEmpty(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty), null);
            return;
        }
        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
            return;
        }
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_or_psw_no_right), null);
            return;
        }
        contentService.login(email, psw);
    }

    private AbortableFuture<LoginInfo> loginRequest;
    class CallBack extends ICallBack {
        @Override
        public void onLoginSucess(final User user) {
            Intent intent = new Intent();
            intent.putExtra(MeFragment.USER, user);
            if (isImLogin) {
                setResult(SettingActivity.IM_LOGIN, intent);
            } else {
                if (KeyParams.SETTING.equals(setting)) {
                    setResult(SettingActivity.IM_LOGIN, intent);
                } else {
                    setResult(LOGIN_OK, intent);
                }
            }

            finish();
        }

    }


}
