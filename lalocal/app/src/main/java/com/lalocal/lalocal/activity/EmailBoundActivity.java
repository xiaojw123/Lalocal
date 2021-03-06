package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.ProgressButton;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class EmailBoundActivity extends BaseActivity implements View.OnClickListener {
    public static final int RESULIT_CODE_BOUND_EMAIL = 103;
    CustomEditText email_edit;
    ProgressButton change_email_btn;
    ContentLoader contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_bound_layout);
        initService();
        email_edit = (CustomEditText) findViewById(R.id.emailbound_email_edit);
        change_email_btn = (ProgressButton) findViewById(R.id.emailbound_change_email_btn);
        String email=getUserEmail();
        if (TextUtils.isEmpty(email)){
            change_email_btn.setText(getResources().getString(R.string.send_email_vercode));
        }else{
            change_email_btn.setText(getResources().getString(R.string.change_email));
        }
        email_edit.setEidtType(CustomEditText.TYPE_1);
        email_edit.setText(email);
        email_edit.setDefaultSelectionEnd(true);
        change_email_btn.setOnClickListener(this);
    }

    private void initService() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
    }

    public String getUserEmail() {
        return getIntent().getStringExtra(KeyParams.EMAIL);
    }

    @Override
    public void onClick(View v) {
        if (v == change_email_btn) {
            String email = email_edit.getText().toString();
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            if (email.equals(getUserEmail())) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_change), null);
                return;
            }
            contentService.boundEmail(email, getUserId(), getToken(),change_email_btn);
        }

    }

    public int getUserId() {
        return getIntent().getIntExtra(KeyParams.USERID, -1);
    }

    public String getToken() {
        return getIntent().getStringExtra(KeyParams.TOKEN);
    }


    class CallBack extends ICallBack implements CustomDialog.CustomDialogListener {

        @Override
        public void onSendActivateEmmailComplete() {
            CommonUtil.showPromptDialog(EmailBoundActivity.this, getResources().getString(R.string.register_success_prompt), this);
        }

        @Override
        public void onDialogClickListener() {
            Intent intent = new Intent();
            intent.putExtra(KeyParams.EMAIL, email_edit.getText().toString());
            setResult(RESULIT_CODE_BOUND_EMAIL, intent);
            finish();
        }
    }


}
