package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomDialog;

public class EmailBoundActivity extends AppCompatActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {
    EditText email_edit;
    Button change_email_btn;
    ContentService contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_bound_layout);
        initService();
        email_edit = (EditText) findViewById(R.id.emailbound_email_edit);
        change_email_btn = (Button) findViewById(R.id.emailbound_change_email_btn);
        email_edit.setText(getUserEmail());
        change_email_btn.setOnClickListener(this);


    }

    private void initService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    public String getUserEmail() {
        return getIntent().getStringExtra("email");
    }

    @Override
    public void onClick(View v) {
        contentService.sendVerificationCode(getUserEmail());
    }

    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
        finish();
    }

    class CallBack extends ICallBack {

        @Override
        public void onSendVerCode(int code, String email) {
            if (code == 0) {
                CommonUtil.showPromptDialog(EmailBoundActivity.this, getResources().getString(R.string.register_success_prompt), EmailBoundActivity.this);
            }

        }
    }


}
