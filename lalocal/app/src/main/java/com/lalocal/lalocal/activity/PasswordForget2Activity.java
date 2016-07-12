package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;

public class PasswordForget2Activity extends BaseActivity implements View.OnClickListener {
    CustomEditText veritiedcode_edit;
    CustomEditText passwrod_edit;
    Button sureBtn;
    ContentLoader contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_forget_layout2);
        initContentService();
        initView();
        CommonUtil.showPromptDialog(this, "邮件已发送", null);
    }

    private void initContentService() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        veritiedcode_edit = (CustomEditText) findViewById(R.id.forgetpsw_veritifiedcode_custom_edit);
        passwrod_edit = (CustomEditText) findViewById(R.id.forgetpsw_lastest_psw_custom_edit);
        sureBtn = (Button) findViewById(R.id.forgetpsw_sure_btn);
        veritiedcode_edit.setSelectedButton(sureBtn);
        veritiedcode_edit.setFilterSpace(true);
        passwrod_edit.setSelectedButton(sureBtn);
        sureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == sureBtn) {
            String veritiedcode = veritiedcode_edit.getText().toString();
            if (CommonUtil.isEmpty(veritiedcode)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.veritied_code_no_empty), null);
                return;
            }
            String psw = passwrod_edit.getText().toString();
            if (CommonUtil.isEmpty(psw)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty),
                        null);
                return;
            }
            if (!CommonUtil.checkPassword(psw)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), null);
                return;
            }
            contentService.resetPasword(getEmail(), veritiedcode, psw);
        }

    }

    public String getEmail() {
        return getIntent().getStringExtra(PasswordForget1Activity.Email);
    }


    class CallBack extends ICallBack {
        @Override
        public void onResetPasswordComplete() {
            CommonUtil.showToast(PasswordForget2Activity.this, "密码修改成功!", Toast.LENGTH_LONG);
            setResult(RESULT_OK, null);
            finish();
        }
    }
}
