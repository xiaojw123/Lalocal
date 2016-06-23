package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.lalocal.lalocal.view.dialog.WheelDialog;

public class AccountEidt2Activity extends AppCompatActivity implements View.OnClickListener, CustomDialog.CustomDialogListener, WheelDialog.OnWheelSelectedListener {
    public static final int RESULT_CODE_NICKNAME = 101;
    public static final int RESULT_CODE_PHONE = 102;
    public static final int ACTION_NICKNAME_MODIFY = 200;
    public static final int ACTION_PHONE_MODIFY = 201;
    public static final int ACTION_EMAIL_MODIFY = 202;
    public static final String ACTION_TYPE = "action_type";
    EditText nickname_modfiy_edit, phone_modify_edit;
    TextView countrycode_tv;
    int actionType;
    ContentService contentService;
    ImageView backImg;
    int userid;
    String token;
    String email, emailText;
    boolean isModifyEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_eidt2_layout);
        initParams();
        initService();
        initView();


    }

    private void initParams() {
        actionType = getActionType();
        userid = getIntent().getIntExtra(KeyParams.USERID, -1);
        token = getIntent().getStringExtra(KeyParams.TOKEN);
        emailText = getEmailText();
        if (TextUtils.isEmpty(emailText)) {
            email = "";
        } else {
            if (emailText.contains("(")) {
                email = emailText.substring(0, emailText.indexOf("("));
            } else {
                email = emailText;
            }
        }
    }

    private void initService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        nickname_modfiy_edit = (EditText) findViewById(R.id.nickname_modify_edit);
        countrycode_tv = (TextView) findViewById(R.id.account_edit2_countrycode);
        phone_modify_edit = (EditText) findViewById(R.id.account_edit2_phone);
        TextView email_tv = (TextView) findViewById(R.id.account_eidt2_email_tv);
        TextView save_tv = (TextView) findViewById(R.id.account_edit_save);
        LinearLayout phone_modify_view = (LinearLayout) findViewById(R.id.phone_modify_view);
        LinearLayout email_modify_view = (LinearLayout) findViewById(R.id.email_modify_view);
        Button sendagain_btn = (Button) findViewById(R.id.account_eidt2_sendagain_btn);
        Button changeemail_btn = (Button) findViewById(R.id.account_eidt2_changeemail_btn);
        nickname_modfiy_edit.setText(getNickname());
        email_tv.setText(getEmailText());
        backImg.setOnClickListener(this);
        save_tv.setOnClickListener(this);
        sendagain_btn.setOnClickListener(this);
        changeemail_btn.setOnClickListener(this);
        countrycode_tv.setOnClickListener(this);
        if (actionType == ACTION_NICKNAME_MODIFY) {
            nickname_modfiy_edit.setVisibility(View.VISIBLE);
        } else if (actionType == ACTION_PHONE_MODIFY) {
            countrycode_tv.setText(getAreaCode());
            phone_modify_edit.setText(getPhone());
            phone_modify_view.setVisibility(View.VISIBLE);
        } else if (actionType == ACTION_EMAIL_MODIFY) {
            save_tv.setVisibility(View.GONE);
            email_modify_view.setVisibility(View.VISIBLE);

        }

    }

    public int getActionType() {
        return getIntent().getIntExtra(ACTION_TYPE, 0);
    }

    public String getNickname() {

        return getIntent().getStringExtra("nickname");
    }

    public String getEmailText() {
        return getIntent().getStringExtra("emailtext");
    }


    public String getAreaCode() {
        String areaCode = getIntent().getStringExtra(KeyParams.AREA_Code);
        if (TextUtils.isEmpty(areaCode)) {
            areaCode = "+86";
        }
        return areaCode;
    }

    public String getPhone() {
        return getIntent().getStringExtra(KeyParams.PHONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back_btn:
                finish();
                break;
            case R.id.account_edit2_countrycode:
                showAreaCodeSeletor();
                break;
            case R.id.account_edit_save:
                if (actionType == ACTION_NICKNAME_MODIFY) {
                    if (modifyNickName()) return;
                } else if (actionType == ACTION_PHONE_MODIFY) {
                    if (modifyPhone()) return;
                }
                break;
            case R.id.account_eidt2_sendagain_btn:
                if (TextUtils.isEmpty(email)) {
                    CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_empty), this);
                    return;
                }
                contentService.boundEmail(email, userid, token);
                break;
            case R.id.account_eidt2_changeemail_btn:
                changeEmail();
                break;
        }

    }

    private void changeEmail() {
        Intent intent = new Intent(this, EmailBoundActivity.class);
        intent.putExtra(KeyParams.USERID,userid);
        intent.putExtra(KeyParams.TOKEN,token);
        intent.putExtra(KeyParams.EMAIL, email);
        startActivityForResult(intent, 100);
    }

    private void showAreaCodeSeletor() {
        WheelDialog dialog = new WheelDialog(this);
        dialog.setOnWheelDialogSelectedListener(this);
        dialog.show();
    }

    private boolean modifyNickName() {
        String nickname = nickname_modfiy_edit.getText().toString();
        if (!CommonUtil.checkNickname(nickname)) {
            if (TextUtils.isEmpty(nickname)) {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.message_not_empty), this);
            } else {
                CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_input_error), this);
            }
            return true;
        }
        if (nickname.equals(getNickname())) {
            finish();
            return true;
        }
        contentService.modifyUserProfile(nickname, -1, null, null, userid, token);
        return false;
    }

    private boolean modifyPhone() {
        String phoneNumber = phone_modify_edit.getText().toString();
        String areaCode = countrycode_tv.getText().toString();
        if ("国家号".equals(areaCode)) {
            areaCode = getAreaCode();
        }
        String phoneStr = areaCode + "  " + phoneNumber;
        if (TextUtils.isEmpty(phoneNumber)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.message_not_empty), this);
            return true;
        }
        if (areaCode.equals(getAreaCode())) {
            areaCode = null;
        } else if (phoneNumber.equals(getPhone())) {
            phoneNumber = null;
        } else if (areaCode.equals(getAreaCode()) && phoneNumber.equals(getPhone())) {
            finish();
            return true;
        }
        Intent intent = new Intent();
        intent.putExtra(KeyParams.AREA_Code, areaCode);
        intent.putExtra(KeyParams.PHONE, phoneNumber);
        setResult(RESULT_CODE_PHONE, intent);
        contentService.modifyUserProfile(null, -1, areaCode, phoneNumber, userid, token);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == EmailBoundActivity.RESULIT_CODE_BOUND_EMAIL) {
            setResult(EmailBoundActivity.RESULIT_CODE_BOUND_EMAIL, data);
            finish();
        }

    }

    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
        if (isModifyEmail) {
            isModifyEmail = false;
            setResult(EmailBoundActivity.RESULIT_CODE_BOUND_EMAIL, null);
            finish();
        }
    }

    @Override
    public void onSelected(Country item) {
        countrycode_tv.setText(item.getCodePlus());
    }

    class CallBack extends ICallBack {
        @Override
        public void onSendActivateEmmailComplete(int code, String message) {
            if (code == 0) {
                isModifyEmail = true;
                CommonUtil.showPromptDialog(AccountEidt2Activity.this, getResources().getString(R.string.register_success_prompt), AccountEidt2Activity.this);
            } else {
                CommonUtil.showPromptDialog(AccountEidt2Activity.this, message, AccountEidt2Activity.this);
            }
        }

        @Override
        public void onModifyUserProfile(int code, LoginUser user) {
            if (code == 0) {
                Intent intent = new Intent();
                if (actionType == ACTION_NICKNAME_MODIFY) {
                    intent.putExtra(KeyParams.NICKNAME, user.getNickName());
                    setResult(RESULT_CODE_NICKNAME, intent);
                } else if (actionType == ACTION_PHONE_MODIFY) {
                    intent.putExtra(KeyParams.AREA_Code, user.getAreaCode());
                    intent.putExtra(KeyParams.PHONE, user.getPhone());
                    setResult(RESULT_CODE_PHONE, intent);
                }
            }
            finish();

        }
    }
}
