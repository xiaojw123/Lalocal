package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
import com.lalocal.lalocal.view.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class LPEmailBound2Activity extends BaseActivity implements View.OnClickListener {
    public static String IS_REGISTERED = "is_registered";
    CustomEditText pEmailBoundEdit;
    Button pLalocalStart;
    boolean isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lemail_psw);
        pEmailBoundEdit = (CustomEditText) findViewById(R.id.p_emailbound_edit);
        pLalocalStart = (Button) findViewById(R.id.p_emailbound_startlalocal);
        pLalocalStart.setOnClickListener(this);
        isRegistered = getIntent().getBooleanExtra(IS_REGISTERED, false);
        if (isRegistered) {
            pEmailBoundEdit.setHint(getResources().getString(R.string.set_pssword));
        } else {
            pEmailBoundEdit.setHint(getResources().getString(R.string.please_input_password));
        }
        setLoaderCallBack(new PemailBound2CallBack());
    }

    @Override
    public void onClick(View v) {
        MobHelper.sendEevent(this, MobEvent.BINDING_START);
        String phone = getIntent().getStringExtra(KeyParams.PHONE);
        String code = getIntent().getStringExtra(KeyParams.CODE);
        String email = getIntent().getStringExtra(KeyParams.EMAIL);
        String psw = pEmailBoundEdit.getText();
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_no_right), null);
            return;
        }
        AppLog.print("register phone=" + phone + ", code=" + code + ", email=" + email + ", psw=" + psw);

        if (getPageType() == PageType.Page_BIND_EMAIL_SOCIAL) {
            //TODO：注册三方账号 p e
            String bodyParams = getIntent().getStringExtra(KeyParams.SOCIAL_PARAMS);
            try {
                JSONObject body = new JSONObject(bodyParams);
                body.put("email", email);
                body.put("password", psw);
                if (isRegistered) {
                    mContentloader.socialBind(body.toString());
                } else {
                    mContentloader.registerBySocial(body.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mContentloader.registerByPhone(phone, code, email, psw);
        }
    }


    class PemailBound2CallBack extends ICallBack implements CustomDialog.CustomDialogListener {
        User user;

        @Override
        public void onRegisterByPhone(User user) {
            this.user = user;
            CommonUtil.showPromptDialog(LPEmailBound2Activity.this, "绑定成功", this);

        }

        @Override
        public void onDialogClickListener() {
            UserHelper.setLoginSuccessResult(LPEmailBound2Activity.this, user);
        }

        @Override
        public void onSocialRegisterSuccess(User user) {
            UserHelper.setLoginSuccessResult(LPEmailBound2Activity.this,user);
        }
    }


}
