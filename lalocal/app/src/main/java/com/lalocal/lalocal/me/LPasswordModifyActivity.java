package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.ProgressButton;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LPasswordModifyActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {


    @BindView(R.id.lpsw_modify_ctv)
    CustomTitleView lpswModifyCtv;
    @BindView(R.id.lpswmodify_sendver_btn)
    ProgressButton lpswmodifySendverBtn;
    @BindView(R.id.lpswmodify_input_vercode_edit)
    CustomEditText lpswmodifyInputVercodeEdit;
    @BindView(R.id.lpswmodify_input_psw_edit)
    CustomEditText lpswmodifyInputPswEdit;
    @BindView(R.id.lpswmodify_sure_btn)
    ProgressButton lpswmodifySureBtn;
    @BindView(R.id.lpswmodify_sure_layout)
    LinearLayout lpswmodifySureLayout;
    @BindView(R.id.activity_lpassword_modify)
    RelativeLayout activityLpasswordModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpassword_modify);
        ButterKnife.bind(this);
        setLoaderCallBack(new PswModifyCallBack());
        lpswModifyCtv.setOnBackClickListener(this);
    }

    @OnClick({R.id.lpswmodify_sendver_btn, R.id.lpswmodify_sure_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lpswmodify_sendver_btn:
                AppLog.print("senver_social email____" + UserHelper.getUserEmail(this));
                String email=getEmail();
                if (TextUtils.isEmpty(email)){
                    CommonUtil.showPromptDialog(LPasswordModifyActivity.this,"您的账户未绑定邮箱",null);
                    return;
                }
                mContentloader.sendVerificationCode(email, lpswmodifySendverBtn);
                break;
            case R.id.lpswmodify_sure_btn:
                String verCode = lpswmodifyInputVercodeEdit.getText();
                String psw = lpswmodifyInputPswEdit.getText();
                if (CommonUtil.isEmpty(verCode)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.veritied_code_no_empty), null);
                    return;
                }
                if (CommonUtil.isEmpty(psw)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_empty),
                            null);
                    return;
                }
                if (!CommonUtil.checkPassword(psw)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_limit_num), null);
                    return;
                }
                mContentloader.resetPasword(getEmail(), verCode, psw,lpswmodifySureBtn);
                break;
        }
    }


    class PswModifyCallBack extends ICallBack implements CustomDialog.CustomDialogListener {
        @Override
        public void onSendVerCode(String email) {
            CommonUtil.showPromptDialog(LPasswordModifyActivity.this, "邮件已发送", this);
        }

        @Override
        public void onResetPasswordComplete(String psw) {
            CommonUtil.showToast(LPasswordModifyActivity.this, "密码修改成功!", Toast.LENGTH_SHORT);
            UserHelper.updateSignOutInfo(LPasswordModifyActivity.this);
            Intent intent = new Intent(LPasswordModifyActivity.this, LEmailLoginActivity.class);
            intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_PASSWORD_MODIFY);
            startActivityForResult(intent, KeyParams.REQUEST_CODE);
        }

        @Override
        public void onDialogClickListener() {
            lpswmodifySendverBtn.setVisibility(View.GONE);
            lpswmodifySureLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackClick() {
        if (lpswmodifySureLayout.getVisibility() == View.VISIBLE) {
            lpswmodifySureLayout.setVisibility(View.GONE);
            lpswmodifySendverBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        if (lpswmodifySureLayout.getVisibility() == View.VISIBLE) {
            lpswmodifySureLayout.setVisibility(View.GONE);
            lpswmodifySendverBtn.setVisibility(View.VISIBLE);
        }else{
            super.onBackPressed();
        }

    }
    public  String getEmail(){
        return  getIntent().getStringExtra(KeyParams.EMAIL);
    }


}
