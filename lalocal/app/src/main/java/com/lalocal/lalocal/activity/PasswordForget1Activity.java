package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class PasswordForget1Activity extends BaseActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {
    public static final String Email = "email";
    ImageView backImg;
    CustomEditText email_edit;
    TextView next_tv;
    ContentService contentService;
    boolean isForbidenNext;

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
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        email_edit = (CustomEditText) findViewById(R.id.forgetpsw_email_custom_edit);
        next_tv = (TextView) findViewById(R.id.forgetpsw_next_tv);
        next_tv.setOnClickListener(this);
        backImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == backImg) {
            finish();
        } else if (v == next_tv) {
            AppLog.print("click__enable___");
            String email = email_edit.getText().toString();
            if (TextUtils.isEmpty(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), this);
                return;
            }
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), this);
                return;
            }
            next_tv.setEnabled(false);
            AppLog.print("nextv__"+next_tv);
            contentService.checkEmail(email, next_tv);
        }
    }

    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
    }


    class CallBack extends ICallBack {
        @Override
        public void onCheckEmail(String email) {
            next_tv.setEnabled(false);
            contentService.sendVerificationCode(email);
        }

        @Override
        public void onSendVerCode(int code, String email) {
            next_tv.setEnabled(true);
            if (code == 0) {
                Intent intent = new Intent(PasswordForget1Activity.this, PasswordForget2Activity.class);
                intent.putExtra(Email, email);
                startActivityForResult(intent, 100);
            } else {
                CommonUtil.showPromptDialog(PasswordForget1Activity.this, getResources().getString(R.string.ver_code_send_failed), PasswordForget1Activity.this);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
