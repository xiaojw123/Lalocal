package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LRegister1Activity extends BaseActivity {

    @BindView(R.id.lregister1_input_email)
    MyEditText lregister1InputEmail;
    @BindView(R.id.lregister1_next)
    Button lregister1Next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lregister1);
        ButterKnife.bind(this);
        setLoginBackResult(true);
    }

    @OnClick({R.id.lregister1_input_email, R.id.lregister1_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lregister1_input_email:
                break;
            case R.id.lregister1_next:
                String email=lregister1InputEmail.getText();
                if (CommonUtil.isEmpty(email)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), null);
                    return;
                }
                if (!CommonUtil.checkEmail(email)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
                    return;
                }
                LRegister2Activity.start(this,email);
                break;
        }
    }
}
