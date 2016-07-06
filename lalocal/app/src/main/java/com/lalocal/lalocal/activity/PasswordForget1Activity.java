package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;

public class PasswordForget1Activity extends BaseActivity implements View.OnClickListener {
    public static final String Email = "email";
    CustomEditText email_edit;
    TextView next_tv;
    ContentService contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_forget_layout1);
        initContentSerice();
        initView();
    }

    private void initContentSerice() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        email_edit = (CustomEditText) findViewById(R.id.forgetpsw_email_custom_edit);
        next_tv = (TextView) findViewById(R.id.forgetpsw_next_tv);
        email_edit.setLightText(next_tv);
        next_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == next_tv) {
            String email = email_edit.getText().toString();
            if (CommonUtil.isEmpty(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
                return;
            }
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            next_tv.setEnabled(false);
            contentService.sendVerificationCode(email, next_tv);
        }
    }


    class CallBack extends ICallBack {
        @Override
        public void onSendVerCode(String email) {
            Intent intent = new Intent(PasswordForget1Activity.this, PasswordForget2Activity.class);
            intent.putExtra(Email, email);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String setting = getIntent().getStringExtra(KeyParams.SETTING);
            if (KeyParams.SETTING.equals(setting)) {
                AppLog.print("setting login __");
                setResult(SettingActivity.LOGIN);
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
