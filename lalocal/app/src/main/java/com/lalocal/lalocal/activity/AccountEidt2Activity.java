package com.lalocal.lalocal.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.Country;
import com.lalocal.lalocal.service.ContentService;
import com.lalocal.lalocal.service.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomDialog;
import com.lalocal.lalocal.view.WheelDialog;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_eidt2_layout);
        actionType = getActionType();
        initService();
        initView();


    }

    private void initService() {
        contentService = new ContentService(this);
        contentService.setCallBack(new CallBack());
    }

    private void initView() {
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
        email_tv.setText(getUserEmail());
        save_tv.setOnClickListener(this);
        sendagain_btn.setOnClickListener(this);
        changeemail_btn.setOnClickListener(this);
        countrycode_tv.setOnClickListener(this);
        if (actionType == ACTION_NICKNAME_MODIFY) {
            nickname_modfiy_edit.setVisibility(View.VISIBLE);
        } else if (actionType == ACTION_PHONE_MODIFY) {
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

    public String getUserEmail() {
        return getIntent().getStringExtra("email");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_edit2_countrycode:
                WheelDialog dialog = new WheelDialog(this);
                dialog.setOnWheelDialogSelectedListener(this);
                dialog.show();
                break;
            case R.id.account_edit_save:
                if (actionType == ACTION_NICKNAME_MODIFY) {
                    String nickname = nickname_modfiy_edit.getText().toString();
                    if (!CommonUtil.checkNickname(nickname)) {
                        CommonUtil.showPromptDialog(this, getResources().getString(R.string.nickname_no_right), this);
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("nickname", nickname);
                    setResult(RESULT_CODE_NICKNAME, intent);
                    contentService.modifyUserProfile(nickname, -1, null);
                } else if (actionType == ACTION_PHONE_MODIFY) {
                    String phone = countrycode_tv.getText().toString() + "  " + phone_modify_edit.getText().toString();
                    if (TextUtils.isEmpty(phone)) {
                        CommonUtil.showPromptDialog(this, "手机号不能为空", this);
                        return;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(RESULT_CODE_PHONE, intent);
                    contentService.modifyUserProfile(null, -1, phone);
                }
                finish();
                break;
            case R.id.account_eidt2_sendagain_btn:
                contentService.sendVerificationCode(getUserEmail());
                break;
            case R.id.account_eidt2_changeemail_btn:
                Intent intent=new Intent(this,EmailBoundActivity.class);
                intent.putExtra("email",getUserEmail());
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onDialogClickListener(Dialog dialog, View view) {
        dialog.dismiss();
    }

    @Override
    public void onSelected(Country item) {
        countrycode_tv.setText(item.getCodePlus());
        phone_modify_edit.setText(item.getName());
    }

    class CallBack extends ICallBack {
        @Override
        public void onSendVerCode(int code, String email) {
            if (code == 0) {
                CommonUtil.showPromptDialog(AccountEidt2Activity.this, getResources().getString(R.string.register_success_prompt), AccountEidt2Activity.this);
            }

        }
    }
}
