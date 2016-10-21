package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalIntroActivity extends BaseActivity implements TextWatcher {
    public static final int RESULT_CODE_PERINTRO = 0X081;
    @BindView(R.id.personal_intro_save_tv)
    TextView personalIntroSaveTv;
    @BindView(R.id.personal_intro_edit)
    EditText personalIntroEdit;
    @BindView(R.id.personal_intro_inputlimt)
    TextView personalIntroInputlimt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_intro);
        ButterKnife.bind(this);
//        String descripton = getDescriptoin();
//        personalIntroEdit.setText(descripton);
//        if (!TextUtils.isEmpty(descripton)) {
//            int len=descripton.length();
//            personalIntroInputlimt.setText(String.valueOf(len)+"/50");
//            personalIntroEdit.setSelection(len);
//        }
        personalIntroEdit.addTextChangedListener(this);
        setLoaderCallBack(new PerCallBack());
    }

    @OnClick(R.id.personal_intro_save_tv)
    public void onClick() {
        String descriptionText = personalIntroEdit.getText().toString();
        if (TextUtils.isEmpty(descriptionText)) {
            CommonUtil.showPromptDialog(this, "个人介绍不能为空", null);
        } else {
            mContentloader.modifyUserProfile(null, -1, null, null, descriptionText, UserHelper.getUserId(this), UserHelper.getToken(this));
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        personalIntroInputlimt.setText(s.length() + "/50");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    class PerCallBack extends ICallBack {

        @Override
        public void onModifyUserProfile(LoginUser user) {
            AppLog.print("modfi desctipn description_____" + user.getDescription());
            Intent intent = new Intent();
            intent.putExtra(KeyParams.DESCRIPTON, user.getDescription());
            setResult(RESULT_CODE_PERINTRO, intent);
            finish();
        }
    }

    public String getDescriptoin() {
        return getIntent().getStringExtra(KeyParams.DESCRIPTON);
    }

}
