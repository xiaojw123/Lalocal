package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.PasswordForget1Activity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;

public class LPasswordForget2Activity extends BaseActivity implements View.OnClickListener {
    MyEditText veritiedcode_edit;
    MyEditText passwrod_edit;
    Button sureBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forget2);
        initContentService();
        initView();
        CommonUtil.showPromptDialog(this, "邮件已发送", null);
    }

    private void initContentService() {
        setLoaderCallBack(new CallBack());
    }

    private void initView() {
        veritiedcode_edit = (MyEditText) findViewById(R.id.forgetpsw_veritifiedcode_custom_edit);
        passwrod_edit = (MyEditText) findViewById(R.id.forgetpsw_lastest_psw_custom_edit);
        sureBtn = (Button) findViewById(R.id.forgetpsw_sure_btn);
        veritiedcode_edit.setSelectedButton(sureBtn);
        veritiedcode_edit.setFilterSpace(true);
        passwrod_edit.setSelectedButton(sureBtn);
        sureBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == sureBtn) {
            MobHelper.sendEevent(this, MobEvent.LOGIN_FORGET_SURE);
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
            mContentloader.resetPasword(getEmail(), veritiedcode, psw);
        }

    }

    public String getEmail() {
        return getIntent().getStringExtra(PasswordForget1Activity.Email);
    }


    class CallBack extends ICallBack {
        @Override
        public void onResetPasswordComplete(String psw) {
            UserHelper.updatePassword(LPasswordForget2Activity.this,psw);
            CommonUtil.showToast(LPasswordForget2Activity.this, "密码修改成功!", Toast.LENGTH_LONG);
            setResult(RESULT_OK, null);
            finish();
        }
    }
}
