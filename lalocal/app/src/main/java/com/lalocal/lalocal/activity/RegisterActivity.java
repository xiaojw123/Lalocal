package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomDialog;
import com.lalocal.lalocal.view.CustomEditText;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    CustomEditText email_edit, psw_edit, nickname_edit;
    Button register_btn;
    ImageView back_btn;
    ContentService contentService;
    CustomDialog promptDialog;
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
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        back_btn = (ImageView) findViewById(R.id.common_back_btn);
        email_edit = (CustomEditText) findViewById(R.id.reigster_emial_custom_edit);
        psw_edit = (CustomEditText) findViewById(R.id.reigster_psw_custom_edit);
        nickname_edit = (CustomEditText) findViewById(R.id.reigster_nickname_custom_edit);
        register_btn = (Button) findViewById(R.id.register_btn);
        userprotocol_tv = (TextView) findViewById(R.id.register_user_protocal);
        email_edit.setSelectedButton(register_btn);
        psw_edit.setSelectedButton(register_btn);
        nickname_edit.setSelectedButton(register_btn);
        email_edit.setClearButtonVisible(false);
        register_btn.setOnClickListener(this);
        back_btn.setOnClickListener(this);
        userprotocol_tv.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == back_btn) {
            AppLog.print("register back btn press_____");
            finish();
        } else if (v == register_btn) {
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
        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), promptDialogClicklistener);
            return;
        }
        AppLog.print("ceheck psw__" + CommonUtil.checkPassword(psw));
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), promptDialogClicklistener);
            return;
        }
        AppLog.print("ceheck nick__" + CommonUtil.checkNickname(nickname));
        if (!CommonUtil.checkNickname(nickname)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_no_right), promptDialogClicklistener);
            return;
        }
        if (contentService != null) {
            contentService.register(email, psw, nickname);
        }
    }

    class CallBack extends ICallBack {
        @Override
        public void onResigterComplete(String resultCode, String message, String email, String psw) {
            if ("0".equals(resultCode)) {
                loginEmail = email;
                loginPsw = psw;
                CommonUtil.showPromptDialog(RegisterActivity.this, getResources().getString(R.string.register_success_prompt), successDialogClicklistener);
            } else {
                CommonUtil.showPromptDialog(RegisterActivity.this, message, promptDialogClicklistener);
            }

        }

        @Override
        public void onRequestFailed(String msg) {
            CommonUtil.showPromptDialog(RegisterActivity.this,"注册失败", promptDialogClicklistener);
        }
    }


    CustomDialog.CustomDialogListener promptDialogClicklistener = new CustomDialog.CustomDialogListener() {
        @Override
        public void onDialogClickListener(Dialog dialog, View view) {
            dialog.dismiss();
        }

    };

    CustomDialog.CustomDialogListener successDialogClicklistener = new CustomDialog.CustomDialogListener() {
        @Override
        public void onDialogClickListener(Dialog dialog, View view) {
            dialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra(LoginActivity.EMAIL, loginEmail);
            intent.putExtra(LoginActivity.PSW, loginPsw);
            setResult(RESULT_OK, intent);
            finish();
        }
    };


}
