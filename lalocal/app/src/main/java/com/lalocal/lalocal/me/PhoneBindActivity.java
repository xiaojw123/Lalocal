package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneBindActivity extends BaseActivity implements View.OnFocusChangeListener {
    @BindView(R.id.pbind_phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.pbind_psw_get_btn)
    Button pbindPswGetBtn;
    @BindView(R.id.pbind_sure_btn)
    Button pbindSureBtn;
    @BindView(R.id.pbind_phone_text)
    TextView pbindPhoneText;
    @BindView(R.id.pbind_phone_edt)
    EditText pbindPhoneEdt;
    @BindView(R.id.pbind_psw_edt)
    MyEditText pbindPswEdt;
    @BindView(R.id.pbind_areacode_tv)
    TextView pbindAreacodeTv;
    @BindColor(R.color.color_1a)
    int areaColor;
    @BindString(R.string.get_password)
    String pswGetStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_bind);
        ButterKnife.bind(this);
        initView();
        setLoaderCallBack(new PbindCallBack());
    }

    private void initView() {
        pbindPhoneEdt.setOnFocusChangeListener(this);
    }

    @OnClick({R.id.pbind_psw_get_btn, R.id.pbind_sure_btn})
    public void onClick(View view) {
        String phone = getPhone();
        switch (view.getId()) {
            case R.id.pbind_psw_get_btn:
                MobHelper.sendEevent(this, MobEvent.LOGIN_PHONE_VERIFICATOIN);
                if (TextUtils.isEmpty(phone)) {
                    CommonUtil.showPromptDialog(this, "手机号不能为空", null);
                    return;
                }
                mContentloader.getSMSCode(view, phone, null);
                break;
            case R.id.pbind_sure_btn:
                String code = getCode();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
                    CommonUtil.showPromptDialog(this, "手机号或密码为空", null);
                    return;
                }
                mContentloader.bindPhone(phone,code);
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            phoneLayout.setSelected(true);
            pbindPhoneText.setVisibility(View.VISIBLE);
            pbindAreacodeTv.setTextColor(areaColor);
        } else {
            phoneLayout.setSelected(false);
            pbindPhoneText.setVisibility(View.GONE);
        }


    }

    class PbindCallBack extends ICallBack {
        @Override
        public void onGetSmsCodeSuccess(){
            AppLog.print("onGetSmsCodeSuccess____");
            pbindPswGetBtn.setEnabled(false);
            mHandler.sendEmptyMessageDelayed(MSG_UPDATE_TIMER, 1000);
        }

        @Override
        public void onBindPhoneSuccess(String phone) {
            setResult(AccountSecurityActivity.RESULT_BIND_PHONE);
            finish();
        }
    }

    Handler mHandler = new Handler() {
        int timerLen = 60;

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case MSG_UPDATE_TIMER:
                    pbindPswGetBtn.setText(timerLen-- + "s");
                    if (timerLen < 0) {
                        if (hasMessages(MSG_UPDATE_TIMER)) {
                            removeMessages(MSG_UPDATE_TIMER);
                        }
                        pbindPswGetBtn.setEnabled(true);
                        pbindPswGetBtn.setText(pswGetStr);
                    } else {
                        sendEmptyMessageDelayed(MSG_UPDATE_TIMER, 1000);
                    }
                    break;

            }


        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler.hasMessages(MSG_UPDATE_TIMER)) {
            mHandler.removeMessages(MSG_UPDATE_TIMER);
        }
        pbindPswGetBtn.setEnabled(true);
        pbindPswGetBtn.setText(pswGetStr);
    }

    public static final int MSG_UPDATE_TIMER = 0x11;

    public String getPhone() {
        return pbindPhoneEdt.getText().toString();
    }

    public String getCode() {

        return pbindPswEdt.getText();
    }
}
