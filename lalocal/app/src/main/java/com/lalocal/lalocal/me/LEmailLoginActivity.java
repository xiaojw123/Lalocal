package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lalocal.lalocal.R.id.login_qq_btn;

public class LEmailLoginActivity extends BaseActivity {
    @BindView(R.id.login_emial_custom_edit)
    MyEditText email_edit;
    @BindView(R.id.login_psw_custom_edit)
    MyEditText psw_edit;
    @BindView(R.id.login_register_tv)
    TextView register_tv;
    @BindView(R.id.login_forget_psw_tv)
    TextView forgetpsw_tv;
    @BindView(R.id.login_btn)
    Button login_btn;
    @BindView(login_qq_btn)
    Button loginQqBtn;
    @BindView(R.id.login_wechat_btn)
    Button loginWechatBtn;
    @BindView(R.id.login_weibo_btn)
    Button loginWeiboBtn;
    @BindString(R.string.forget_password)
    String forgetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);
        setLoginBackResult(true);
        ButterKnife.bind(this);
        initView();
        setLoaderCallBack(new CallBack());
    }


    private void initView() {
        forgetpsw_tv.setText(Html.fromHtml("<u>" + forgetPassword + "</u>"));
        email_edit.setSelectedButton(login_btn);
        psw_edit.setSelectedButton(login_btn);
    }


    public void login(String email, String psw) {
        if (CommonUtil.isEmpty(email) && TextUtils.isEmpty(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_or_psw_no_empty), null);
            return;
        }
        if (CommonUtil.isEmpty(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
            return;
        }
        if (CommonUtil.isEmpty(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty), null);
            return;
        }
        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
            return;
        }
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_or_psw_no_right), null);
            return;
        }
        mContentloader.login(email, psw);
    }

    @OnClick({login_qq_btn, R.id.login_wechat_btn, R.id.login_weibo_btn, R.id.login_register_tv, R.id.login_btn, R.id.login_forget_psw_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case login_qq_btn:
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.QQ);
                break;
            case R.id.login_wechat_btn:
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_weibo_btn:
                MobHelper.getInstance().socialLogin(this, mContentloader, SHARE_MEDIA.SINA);
                break;
            case R.id.login_register_tv:
                Intent registerIntent = new Intent(LEmailLoginActivity.this, LRegister1Activity.class);
                startActivityForResult(registerIntent, KeyParams.REQUEST_CODE);
                break;
            case R.id.login_btn:
                String email = email_edit.getText();
                String psw = psw_edit.getText();
                login(email, psw);
                break;
            case R.id.login_forget_psw_tv:
                Intent forgetPswIntent = new Intent(LEmailLoginActivity.this, LPasswordForget1Activity.class);
                startActivity(forgetPswIntent);
                break;
        }
    }


    class CallBack extends ICallBack {
        @Override
        public void onLoginSucess(User user) {
            UserHelper.setLoginSuccessResult(LEmailLoginActivity.this, user);
        }

    }

}
