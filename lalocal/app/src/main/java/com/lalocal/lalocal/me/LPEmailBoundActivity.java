package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.ProgressButton;

public class LPEmailBoundActivity extends BaseActivity implements View.OnClickListener {
    CustomEditText email_edit;
    ProgressButton next_btn;
    TextView skp_tv;

    int mPagetType;
    String mSocialUserPramas;
    String uidPrams;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_bound);
        skp_tv = (TextView) findViewById(R.id.p1_emailbound_skip_tv);
        email_edit = (CustomEditText) findViewById(R.id.p1_emailbound_edit);
        next_btn = (ProgressButton) findViewById(R.id.p1_next_btn);
        email_edit.setEidtType(CustomEditText.TYPE_1);
        email_edit.setDefaultSelectionEnd(true);
        next_btn.setOnClickListener(this);
        skp_tv.setOnClickListener(this);
        initParams();
        setLoaderCallBack(new LPEmailBoundCallback());
        setLoginBackResult(true);
    }

    private void initParams() {
        mPagetType = getPageType();
        uidPrams = getUidPrams();
        mSocialUserPramas = getSocialPramas();

    }


    @Override
    public void onClick(View v) {
        if (v == skp_tv) {//跳过邮箱绑定
            MobHelper.sendEevent(this, MobEvent.BINDING_JUMP);
            String phone = getIntent().getStringExtra(KeyParams.PHONE);
            String code = getIntent().getStringExtra(KeyParams.CODE);
            if (getPageType() == PageType.Page_BIND_EMAIL_SOCIAL) {
                mContentloader.registerBySocial(mSocialUserPramas,null);
            } else {
                mContentloader.registerByPhone(phone, code, null, null,null);
            }

        } else if (v == next_btn) {
            MobHelper.sendEevent(this, MobEvent.BINDING_NEXT);
            String email = email_edit.getText();
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            //检查邮箱是否注册过
            mContentloader.checkEmail(email, uidPrams,next_btn);

        }
    }

    class LPEmailBoundCallback extends ICallBack {
        //三方注册成功
        @Override
        public void onSocialRegisterSuccess(User user) {
            AppLog.print("onSocialRegisterSuccess__user_name_" + user.getNickName() + ", id=" + user.getId());
            UserHelper.setLoginSuccessResult(LPEmailBoundActivity.this, user);
        }
       //手机号注册成功
        @Override
        public void onRegisterByPhone(User user) {
            UserHelper.setLoginSuccessResult(LPEmailBoundActivity.this, user);
        }

        @Override
        public void onCheckEmail(String email, String userid) {
            Intent intent = getIntent();
            intent.setClass(LPEmailBoundActivity.this, LPEmailBound2Activity.class);
            intent.putExtra(KeyParams.EMAIL, email);
            //存在userid，则邮箱被注册; 反之，未注册
            intent.putExtra(LPEmailBound2Activity.IS_REGISTERED, !TextUtils.isEmpty(userid));
            startActivityForResult(intent, KeyParams.REQUEST_CODE);
        }
    }

    public String getSocialPramas() {
        return getIntent().getStringExtra(KeyParams.SOCIAL_PARAMS);
    }

    public String getUidPrams() {
        return getIntent().getStringExtra(KeyParams.UID_PARAMS);
    }


}
