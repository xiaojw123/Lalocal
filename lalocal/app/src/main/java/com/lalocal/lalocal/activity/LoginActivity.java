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
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.CustomEditText;

public class LoginActivity extends AppCompatActivity implements CustomDialog.CustomDialogListener {
    public static final int REGISTER_OK = 101;
    public static final int LOGIN_OK = 102;
    public static final String EMAIL = "email";
    public static final String PSW = "psw";
    CustomEditText email_edit, psw_edit;
    TextView register_tv, forgetpsw_tv;
    Button login_btn;
    ImageView back_btn;
    ContentService contentService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initContentService();
        initView();


    }

    private void initContentService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        back_btn = (ImageView) findViewById(R.id.common_back_btn);
        register_tv = (TextView) findViewById(R.id.login_register_tv);
        forgetpsw_tv = (TextView) findViewById(R.id.login_forget_psw_tv);
        email_edit = (CustomEditText) findViewById(R.id.login_emial_custom_edit);
        psw_edit = (CustomEditText) findViewById(R.id.login_psw_custom_edit);
        login_btn = (Button) findViewById(R.id.login_btn);
        email_edit.setSelectedButton(login_btn);
        psw_edit.setSelectedButton(login_btn);
        psw_edit.setTextVisible(false);
        email_edit.setClearButtonVisible(false);
        back_btn.setOnClickListener(loginClickListener);
        register_tv.setOnClickListener(loginClickListener);
        forgetpsw_tv.setOnClickListener(loginClickListener);
        login_btn.setOnClickListener(loginClickListener);
    }

    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == register_tv) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, 100);
            } else if (v == back_btn) {
                AppLog.print("login back btn press_____");
                finish();
            } else if (v == login_btn) {
                String email = email_edit.getText().toString();
                String psw = psw_edit.getText().toString();
                login(email, psw);
            } else if (v == forgetpsw_tv) {
                Intent intent = new Intent(LoginActivity.this, PasswordForget1Activity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        setResult(REGISTER_OK, data);
        finish();
    }


    public void login(String email, String psw) {
        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), this);
            return;
        }
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), this);
            return;
        }
        contentService.login(email, psw);
    }


    class CallBack extends ICallBack {
        @Override
        public void onLoginSucess(String code, String message, User user) {
            if ("0".equals(code)) {
                Intent intent = new Intent();
                intent.putExtra(MeFragment.USER, user);
                setResult(LOGIN_OK, intent);
                finish();
            } else {
                CommonUtil.showPromptDialog(LoginActivity.this, message, LoginActivity.this);
            }

        }
    }
    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
    }




}
