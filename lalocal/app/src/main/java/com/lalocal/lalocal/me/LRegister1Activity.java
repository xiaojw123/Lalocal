package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.UserProtocolActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;
import com.lalocal.lalocal.view.ProgressButton;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LRegister1Activity extends BaseActivity {

    @BindView(R.id.lregister1_input_email)
    MyEditText lregister1InputEmail;
    @BindView(R.id.lregister1_next)
    ProgressButton lregister1Next;
    @BindView(R.id.lregister1_user_protocol)
    TextView userProtocolTv;
    @BindString(R.string.user_protocol)
    String userProtocol;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lregister1);
        ButterKnife.bind(this);
        String email = getIntent().getStringExtra(KeyParams.EMAIL);
        lregister1InputEmail.setText(email);
        userProtocolTv.setText(Html.fromHtml("<u>" + userProtocol + "</u>"));
        setLoginBackResult(true);
        setLoaderCallBack(new LRegister1CallBack());
    }

    @OnClick({R.id.lregister1_next, R.id.lregister1_user_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lregister1_next:
                String email = lregister1InputEmail.getText();
                if (CommonUtil.isEmpty(email)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
                    return;
                }
                if (!CommonUtil.checkEmail(email)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                    return;
                }
                mContentloader.checkEmail(email, null,lregister1Next);
                break;
            case R.id.lregister1_user_protocol:
                UserProtocolActivity.start(this);
                break;

        }

    }

    class LRegister1CallBack extends ICallBack {
        @Override
        public void onCheckEmail(final String email, String userid) {
            AppLog.print("LRegister1CallBack onCheckEmail____"+email);
            if (!TextUtils.isEmpty(userid)) {
                CommonUtil.showPromptDialog(LRegister1Activity.this, "该邮箱已注册", null);
            } else {
                CustomDialog dialog = new CustomDialog(LRegister1Activity.this);
                dialog.setMessage("请确认您的注册邮箱为\n" + email);
                dialog.setCancelBtn("取消", null);
                dialog.setSurceBtn("确认", new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onDialogClickListener() {
                        LRegister2Activity.start(LRegister1Activity.this, email);
                    }
                });
                dialog.show();
            }
        }
    }


}
