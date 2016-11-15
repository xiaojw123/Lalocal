package com.lalocal.lalocal.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LRegister2Activity extends BaseActivity {

    @BindView(R.id.lregister_set_password)
    MyEditText lregisterSetPassword;
    @BindView(R.id.lregister_set_nickname)
    MyEditText lregisterSetNickname;
    @BindView(R.id.lregister_next)
    Button lregisterNext;
    String loginEmail,loginPsw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lregister2);
        ButterKnife.bind(this);
        setLoaderCallBack(new RegisterCallBack());
    }

    @OnClick({R.id.lregister_set_password, R.id.lregister_set_nickname, R.id.lregister_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lregister_set_password:
                break;
            case R.id.lregister_set_nickname:
                break;
            case R.id.lregister_next:
                String nickname = lregisterSetNickname.getText();
                String password = lregisterSetPassword.getText();
                if (CommonUtil.isEmpty(nickname) || CommonUtil.isEmpty(password)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.nick_or_password), null);
                    return;
                }
                if (!CommonUtil.checkPassword(password)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), null);
                    return;
                }
                if (!CommonUtil.checkNickname(nickname)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_no_right), null);
                    return;
                }
                lregisterNext.setEnabled(false);
                mContentloader.register(getEmail(), password, nickname, lregisterNext);
                break;
        }
    }


    class RegisterCallBack extends ICallBack implements CustomDialog.CustomDialogListener {

        @Override
        public void onResigterComplete(String email, String psw, int userid, String token) {
            loginEmail = email;
            loginPsw = psw;
            CommonUtil.showPromptDialog(LRegister2Activity.this, getResources().getString(R.string.register_success_prompt), this);
        }
        @Override
        public void onLoginSucess(User user) {
            UserHelper.setLoginSuccessResult(LRegister2Activity.this,user);
        }
        @Override
        public void onDialogClickListener() {
            mContentloader.login(loginEmail, loginPsw);
        }

    }

    public String getEmail() {

        return getIntent().getStringExtra(KeyParams.EMAIL);
    }


    public static void start(Activity activity, String email) {
        Intent intent = new Intent(activity, LRegister2Activity.class);
        intent.putExtra(KeyParams.EMAIL, email);
        activity.startActivityForResult(intent,KeyParams.REQUEST_CODE);
    }
}
