package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class LPEmailBound2Activity extends BaseActivity implements View.OnClickListener {
    CustomEditText pEmailBoundEdit;
    Button pLalocalStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lemail_psw);
        pEmailBoundEdit = (CustomEditText) findViewById(R.id.p_emailbound_edit);
        pLalocalStart = (Button) findViewById(R.id.p_emailbound_startlalocal);
        pLalocalStart.setOnClickListener(this);
        setLoaderCallBack(new PemailBound2CallBack());
    }

    @Override
    public void onClick(View v) {
        String phone = getIntent().getStringExtra(KeyParams.PHONE);
        String code = getIntent().getStringExtra(KeyParams.CODE);
        String email = getIntent().getStringExtra(KeyParams.EMAIL);
        String psw = pEmailBoundEdit.getText();
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), null);
            return;
        }
        AppLog.print("register phone="+phone+", code="+code+", email="+email+", psw="+psw);
        mContentloader.registerByPhone(phone, code, email, psw);
    }


    class PemailBound2CallBack extends ICallBack implements CustomDialog.CustomDialogListener {
        User user;

        @Override
        public void onRegisterByPhone(User user) {
            this.user = user;
            CommonUtil.showPromptDialog(LPEmailBound2Activity.this, "绑定成功", this);

        }
        @Override
        public void onDialogClickListener() {
            Intent intent = new Intent();
            intent.putExtra(MeFragment.USER, user);
            setResult(LoginActivity.LOGIN_OK, intent);
            finish();
        }
    }


}
