package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;

public class LEmailLoginActivity extends BaseActivity {
    MyEditText email_edit, psw_edit;
    TextView register_tv, forgetpsw_tv;
    Button login_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        initContentService();
        initView();
        setLoginBackResult(true);
    }


    private void initContentService() {
        setLoaderCallBack(new CallBack());
    }

    private void initView() {
        register_tv = (TextView) findViewById(R.id.login_register_tv);
        forgetpsw_tv = (TextView) findViewById(R.id.login_forget_psw_tv);
        email_edit = (MyEditText) findViewById(R.id.login_emial_custom_edit);
        psw_edit = (MyEditText) findViewById(R.id.login_psw_custom_edit);
        login_btn = (Button) findViewById(R.id.login_btn);
        forgetpsw_tv.setText(Html.fromHtml("<u>"+getResources().getString(R.string.forget_password)+"</u>"));
        email_edit.setSelectedButton(login_btn);
        psw_edit.setSelectedButton(login_btn);
        register_tv.setOnClickListener(loginClickListener);
        forgetpsw_tv.setOnClickListener(loginClickListener);
        login_btn.setOnClickListener(loginClickListener);


    }


    private View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == register_tv) {
                Intent intent = new Intent(LEmailLoginActivity.this, LRegister1Activity.class);
                startActivityForResult(intent, KeyParams.REQUEST_CODE);
            } else if (v == login_btn) {
                String email = email_edit.getText().toString();
                String psw = psw_edit.getText().toString();
                login(email, psw);
            } else if (v == forgetpsw_tv) {
                Intent intent = new Intent(LEmailLoginActivity.this, LPasswordForget1Activity.class);
                startActivity(intent);
            }
        }
    };


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
        mContentloader.login(email, psw);
    }


    class CallBack extends ICallBack {
        @Override
        public void onLoginSucess(User user) {
            UserHelper.setLoginSuccessResult(LEmailLoginActivity.this,user);
        }

    }

}
