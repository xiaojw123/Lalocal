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
        if (v == skp_tv) {
            MobHelper.sendEevent(this, MobEvent.BINDING_JUMP);
            String phone = getIntent().getStringExtra(KeyParams.PHONE);
            String code = getIntent().getStringExtra(KeyParams.CODE);
            AppLog.print("register phone=" + phone + ", code=" + code);
            if (getPageType() == PageType.Page_BIND_EMAIL_SOCIAL) {
                AppLog.print("pageType_socila__bindEmail___");
                mContentloader.registerBySocial(mSocialUserPramas,null);
            } else {
                AppLog.print("pageType phono_bindEmail___");
                mContentloader.registerByPhone(phone, code, null, null,null);
            }

        } else if (v == next_btn) {
            MobHelper.sendEevent(this, MobEvent.BINDING_NEXT);
            String email = email_edit.getText();
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            mContentloader.checkEmail(email, uidPrams,next_btn);

        }
    }

    class LPEmailBoundCallback extends ICallBack {
        @Override
        public void onSocialRegisterSuccess(User user) {
            AppLog.print("onSocialRegisterSuccess__user_name_" + user.getNickName() + ", id=" + user.getId());
            UserHelper.setLoginSuccessResult(LPEmailBoundActivity.this, user);
        }

        @Override
        public void onRegisterByPhone(User user) {
            UserHelper.setLoginSuccessResult(LPEmailBoundActivity.this, user);
        }

        @Override
        public void onCheckEmail(String email, String userid) {
            Intent intent = getIntent();
            intent.setClass(LPEmailBoundActivity.this, LPEmailBound2Activity.class);
            intent.putExtra(KeyParams.EMAIL, email);
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
