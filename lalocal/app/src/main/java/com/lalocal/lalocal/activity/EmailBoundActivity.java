package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.CustomEditText;

public class EmailBoundActivity extends AppCompatActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {
    public static final int RESULIT_CODE_BOUND_EMAIL = 103;
    CustomEditText email_edit;
    Button change_email_btn;
    ContentService contentService;
    ImageView backImg;
    boolean isBoundEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_bound_layout);
        initService();
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        email_edit = (CustomEditText) findViewById(R.id.emailbound_email_edit);
        change_email_btn = (Button) findViewById(R.id.emailbound_change_email_btn);
        email_edit.setEidtType(CustomEditText.TYPE_1);
        email_edit.setText(getUserEmail());
        email_edit.setClearButtonVisible(false);
        backImg.setOnClickListener(this);
        change_email_btn.setOnClickListener(this);



    }

    private void initService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    public String getUserEmail() {
        return getIntent().getStringExtra(KeyParams.EMAIL);
    }

    @Override
    public void onClick(View v) {
        if (v == backImg) {
            finish();
        } else if (v == change_email_btn) {
            String email = email_edit.getText().toString();
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), this);
                return;
            }
            contentService.boundEmail(email, getUserId(), getToken());
        }

    }

    public int getUserId() {
        return getIntent().getIntExtra(KeyParams.USERID, -1);
    }

    public String getToken() {
        return getIntent().getStringExtra(KeyParams.TOKEN);
    }

    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
        if (isBoundEmail) {
            isBoundEmail = false;
            Intent intent = new Intent();
            intent.putExtra(KeyParams.EMAIL, email_edit.getText().toString());
            setResult(RESULIT_CODE_BOUND_EMAIL, intent);
            finish();
        }
    }

    class CallBack extends ICallBack {

        @Override
        public void onSendActivateEmmailComplete(int code, String message) {
            if (code == 0) {
                isBoundEmail = true;
                CommonUtil.showPromptDialog(EmailBoundActivity.this, getResources().getString(R.string.register_success_prompt), EmailBoundActivity.this);
            } else {
                CommonUtil.showPromptDialog(EmailBoundActivity.this, message, EmailBoundActivity.this);
            }
        }
    }


}
