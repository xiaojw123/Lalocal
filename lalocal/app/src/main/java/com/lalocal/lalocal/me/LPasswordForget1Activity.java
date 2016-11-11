package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.SettingActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;
import com.lalocal.lalocal.view.ProgressButton;

public class LPasswordForget1Activity extends BaseActivity implements View.OnClickListener {
    MyEditText email_edit;
    ProgressButton next_btn;
    ContentLoader contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget1);
        initContentSerice();
        initView();
    }

    private void initContentSerice() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        email_edit = (MyEditText) findViewById(R.id.forgetpsw_email_custom_edit);
        next_btn = (ProgressButton) findViewById(R.id.forgetpsw_next_btn);
        next_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == next_btn) {
            MobHelper.sendEevent(this, MobEvent.LOGIN_FORGET_NEXT);
            String email = email_edit.getText().toString();
            if (CommonUtil.isEmpty(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
                return;
            }
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            next_btn.setEnabled(false);
            contentService.sendVerificationCode(email,next_btn);
        }
    }


    class CallBack extends ICallBack {
        @Override
        public void onSendVerCode(String email) {
            Intent intent = new Intent(LPasswordForget1Activity.this, LPasswordForget2Activity.class);
            intent.putExtra(KeyParams.EMAIL, email);
            startActivityForResult(intent, 100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String setting = getIntent().getStringExtra(KeyParams.SETTING);
            if (KeyParams.SETTING.equals(setting)) {
                setResult(SettingActivity.LOGIN);
            }
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
