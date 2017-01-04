package com.lalocal.lalocal.me;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.MD5Util;
import com.lalocal.lalocal.view.CustomEditText;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.ProgressButton;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import org.json.JSONException;
import org.json.JSONObject;

public class LPEmailBound2Activity extends BaseActivity implements View.OnClickListener {
    public static String IS_REGISTERED = "is_registered";
    CustomEditText pEmailBoundEdit;
    CustomTitleView pEmailCtv;
    TextView reminderTv;

    ProgressButton pLalocalStart;
    boolean isRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lemail_psw);
        pEmailCtv= (CustomTitleView) findViewById(R.id.p_emailbound_ctv);
        reminderTv= (TextView) findViewById(R.id.p_emailbound_reminder);
        pEmailBoundEdit = (CustomEditText) findViewById(R.id.p_emailbound_edit);
        pLalocalStart = (ProgressButton) findViewById(R.id.p_emailbound_startlalocal);
        pLalocalStart.setOnClickListener(this);
        isRegistered = getIntent().getBooleanExtra(IS_REGISTERED, false);
        if (isRegistered) {
            //已注册-输入密码
            pEmailBoundEdit.setHint(getResources().getString(R.string.please_input_password));
            pEmailCtv.setTitle("绑定已有账号");
            reminderTv.setText("该邮箱已绑定，请输入对应密码进行绑定");
        } else {
            //未注册-设置密码
            pEmailCtv.setTitle("绑定新账号");
            pEmailBoundEdit.setHint(getResources().getString(R.string.set_pssword));
            reminderTv.setText("该邮箱未被注册，请直接设置密码注册绑定");

        }
        setLoaderCallBack(new PemailBound2CallBack());
    }

    //绑定邮箱
    @Override
    public void onClick(View v) {
        MobHelper.sendEevent(this, MobEvent.BINDING_START);
        String phone = getIntent().getStringExtra(KeyParams.PHONE);
        String code = getIntent().getStringExtra(KeyParams.CODE);
        String email = getIntent().getStringExtra(KeyParams.EMAIL);
        String psw = pEmailBoundEdit.getText();
        if (!CommonUtil.checkPassword(psw)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.psw_limit_num), null);
            return;
        }
        if (getPageType() == PageType.Page_BIND_EMAIL_SOCIAL) {
            String bodyParams = getIntent().getStringExtra(KeyParams.SOCIAL_PARAMS);
            try {
                JSONObject body = new JSONObject(bodyParams);
                body.put("email", email);
                body.put("password", MD5Util.getMD5String(psw));
                if (isRegistered) {
                    //三方已经注册直接绑定邮箱
                    mContentloader.socialBind(body.toString(),pLalocalStart);
                } else {
                    //三方未注册，请求注册同时绑定邮箱
                    mContentloader.registerBySocial(body.toString(),pLalocalStart);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            //手机注册同时绑定邮箱
            mContentloader.registerByPhone(phone, code, email, psw,pLalocalStart);
        }
    }


    class PemailBound2CallBack extends ICallBack implements CustomDialog.CustomDialogListener {
        User user;
        //手机注册成功
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
            Toast.makeText(LPEmailBound2Activity.this,getResources().getString(R.string.register_success),Toast.LENGTH_SHORT).show();
            UserHelper.setLoginSuccessResult(LPEmailBound2Activity.this,user);
        }
    }


}
