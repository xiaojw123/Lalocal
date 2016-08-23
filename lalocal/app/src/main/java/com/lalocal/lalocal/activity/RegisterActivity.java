package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    CustomEditText email_edit, psw_edit, nickname_edit;
    Button register_btn;
    ContentLoader contentService;
    TextView userprotocol_tv;
    String loginEmail, loginPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initService();
        initView();

    }

    private void initService() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        email_edit = (CustomEditText) findViewById(R.id.reigster_emial_custom_edit);
        psw_edit = (CustomEditText) findViewById(R.id.reigster_psw_custom_edit);
        nickname_edit = (CustomEditText) findViewById(R.id.reigster_nickname_custom_edit);
        register_btn = (Button) findViewById(R.id.register_btn);
        userprotocol_tv = (TextView) findViewById(R.id.register_user_protocal);
        email_edit.setSelectedButton(register_btn);
        psw_edit.setSelectedButton(register_btn);
        nickname_edit.setSelectedButton(register_btn);
        nickname_edit.setFilterSpace(true);
        email_edit.setClearButtonVisible(false);
        register_btn.setOnClickListener(this);
        userprotocol_tv.setOnClickListener(this);
        String email = getEmail();
        if (!TextUtils.isEmpty(email)) {
            email_edit.setText(email);
        }
    }

    private String getEmail() {
        return getIntent().getStringExtra(KeyParams.EMAIL);
    }


    @Override
    public void onClick(View v) {
        if (v == register_btn) {
            register();
        } else if (v == userprotocol_tv) {
            showUserProtocol();
        }
    }

    private void showUserProtocol() {
        Intent intent = new Intent(this, UserProtocolActivity.class);
        startActivity(intent);
    }

    private void register() {
        String email = email_edit.getText();
        String psw = psw_edit.getText();
        String nickname = nickname_edit.getText();
        AppLog.print("check email_____" + CommonUtil.checkEmail(email));
        if (CommonUtil.isEmpty(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
            return;
        }

        if (CommonUtil.isEmpty(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty), null);
            return;
        }
        if (CommonUtil.isEmpty(nickname)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_no_empty), null);
            return;
        }

        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
            return;
        }
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), null);
            return;
        }
        AppLog.print("ceheck nick__" + CommonUtil.checkNickname(nickname));
        if (!CommonUtil.checkNickname(nickname)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_no_right), null);
            return;
        }
        if (contentService != null) {
            register_btn.setEnabled(false);
            contentService.register(email, psw, nickname,register_btn);

        }
    }

    class CallBack extends ICallBack implements CustomDialog.CustomDialogListener {
        @Override
        public void onResigterComplete(String email, String psw, int userid, String token) {
            loginEmail = email;
            loginPsw = psw;
            NIMClient.getService(AuthService.class).logout();//退出登錄的遊客賬號,并清除信息
            DemoCache.setLoginStatus(false);
            DemoCache.clear();
            AuthPreferences.clearUserInfo();
            CommonUtil.showPromptDialog(RegisterActivity.this, getResources().getString(R.string.register_success_prompt), this);
        }

        @Override
        public void onDialogClickListener() {
            Intent intent = new Intent();
            intent.putExtra(LoginActivity.EMAIL, loginEmail);
            intent.putExtra(LoginActivity.PSW, loginPsw);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
