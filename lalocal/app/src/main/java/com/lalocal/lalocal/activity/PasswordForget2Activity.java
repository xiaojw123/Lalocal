package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.CustomEditText;

public class PasswordForget2Activity extends AppCompatActivity implements View.OnClickListener, CustomDialog.CustomDialogListener {

    public static final String VERITIED_CODE = "verited_code";
    public static final String NEW_PASSWORD = "new_password";
    ImageView backImg;
    CustomEditText veritiedcode_edit;
    CustomEditText passwrod_edit;
    Button sureBtn;
    ContentService contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_forget_layout2);
        initContentService();
        initView();
    }

    private void initContentService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        veritiedcode_edit = (CustomEditText) findViewById(R.id.forgetpsw_veritifiedcode_custom_edit);
        passwrod_edit = (CustomEditText) findViewById(R.id.forgetpsw_lastest_psw_custom_edit);
        sureBtn = (Button) findViewById(R.id.forgetpsw_sure_btn);
        veritiedcode_edit.setSelectedButton(sureBtn);
        passwrod_edit.setSelectedButton(sureBtn);
        backImg.setOnClickListener(this);
        sureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == backImg) {
            finish();
        } else if (v == sureBtn) {
            String veritiedcode = veritiedcode_edit.getText().toString();
            if (TextUtils.isEmpty(veritiedcode)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.veritied_code_no_empty), this);
                return;
            }
            String psw = passwrod_edit.getText().toString();
            if (TextUtils.isEmpty(psw)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty),
                        this);
                return;
            }
            if (!CommonUtil.checkPassword(psw)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), this);
                return;
            }
            contentService.resetPasword(getEmail(), veritiedcode, psw);
        }

    }

    public String getEmail() {
        return getIntent().getStringExtra(PasswordForget1Activity.Email);
    }

    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
    }

    class CallBack extends ICallBack {
        @Override
        public void onResetPasswordComplete(int code, String msg) {
            AppLog.print("CallBack______"+code);
            if (code == 0) {
                Toast.makeText(PasswordForget2Activity.this, "密码重置成功!", Toast.LENGTH_LONG).show();
                setResult(RESULT_OK, null);
                finish();
            } else {
                Toast.makeText(PasswordForget2Activity.this, msg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
