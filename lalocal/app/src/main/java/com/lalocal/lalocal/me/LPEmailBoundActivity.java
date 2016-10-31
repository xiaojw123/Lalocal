package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;

public class LPEmailBoundActivity extends BaseActivity implements View.OnClickListener {
    CustomEditText email_edit;
    Button next_btn;
    TextView skp_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_bound);
        skp_tv = (TextView) findViewById(R.id.p1_emailbound_skip_tv);
        email_edit = (CustomEditText) findViewById(R.id.p1_emailbound_edit);
        next_btn = (Button) findViewById(R.id.p1_next_btn);
        email_edit.setEidtType(CustomEditText.TYPE_1);
        email_edit.setDefaultSelectionEnd(true);
        email_edit.setClearButtonVisible(false);
        next_btn.setOnClickListener(this);
        skp_tv.setOnClickListener(this);
        setLoaderCallBack(new LPEmailBoundCallback());
        setBackResult(true);
    }


    @Override
    public void onClick(View v) {
        if (v == skp_tv) {
            MobHelper.sendEevent(this, MobEvent.BINDING_JUMP);
            String phone = getIntent().getStringExtra(KeyParams.PHONE);
            String code = getIntent().getStringExtra(KeyParams.CODE);
            AppLog.print("register phone="+phone+", code="+code);
            mContentloader.registerByPhone(phone, code, null, null);

        } else if (v == next_btn) {
            MobHelper.sendEevent(this,MobEvent.BINDING_NEXT);
            String email = email_edit.getText();
            if (!CommonUtil.checkEmail(email)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                return;
            }
            Intent intent = getIntent();
            intent.setClass(this, LPEmailBound2Activity.class);
            intent.putExtra(KeyParams.EMAIL, email);
            startActivityForResult(intent, KeyParams.REQUEST_CODE);
        }
    }

    class LPEmailBoundCallback extends ICallBack {
        @Override
        public void onRegisterByPhone(User user) {
            Intent intent = new Intent();
            intent.putExtra(MeFragment.USER, user);
            setResult(LoginActivity.LOGIN_OK, intent);
            finish();
        }

    }


}
