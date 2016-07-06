package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DataCleanManager;
import com.lalocal.lalocal.view.dialog.CustomDialog;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    public static final int UN_LOGIN_OK = 103;
    public static final int LOGIN = 104;
    public static final int IM_LOGIN = 105;
    Button signOUtBtn;
    TextView chacheSizeTv;
    FrameLayout aboutUsContainer, clearCacheContainer, modifyPswContianer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        initView();
    }

    private void initView() {
        aboutUsContainer = (FrameLayout) findViewById(R.id.setting_about_us);
        clearCacheContainer = (FrameLayout) findViewById(R.id.setting_clear_cache);
        modifyPswContianer = (FrameLayout) findViewById(R.id.setting_modify_password);
        chacheSizeTv = (TextView) findViewById(R.id.setting_cache_size);
        signOUtBtn = (Button) findViewById(R.id.setting_sign_out);
        chacheSizeTv.setText(DataCleanManager.getTotalCacheSize(this));
        if (isLogin()) {
            modifyPswContianer.setVisibility(View.VISIBLE);
            signOUtBtn.setText(getResources().getString(R.string.sign_out));
        } else {
            modifyPswContianer.setVisibility(View.GONE);
            signOUtBtn.setText(getResources().getString(R.string.immediately_login));
        }
        aboutUsContainer.setOnClickListener(this);
        clearCacheContainer.setOnClickListener(this);
        modifyPswContianer.setOnClickListener(this);
        signOUtBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.setting_about_us:
                Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                break;
            case R.id.setting_clear_cache:
                final String chacheSize = DataCleanManager.getTotalCacheSize(this);
                if (!"0KB".equals(chacheSize)) {
                    DataCleanManager.clearAllCache(this);
                }
                if ("0KB".equals(chacheSize)) {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.setMessage("已成功清理缓存");
                    dialog.setNeturalBtn("确定", null);
                    dialog.show();
                }
                chacheSizeTv.setText(chacheSize);
                break;
            case R.id.setting_modify_password:
                Intent intent1 = new Intent(this, PasswordForget1Activity.class);
                intent1.putExtra(KeyParams.SETTING, KeyParams.SETTING);
                startActivityForResult(intent1, 100);

                break;
            case R.id.setting_sign_out:
                String text = signOUtBtn.getText().toString();
                if (getResources().getString(R.string.immediately_login).equals(text)) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(KeyParams.IM_LOGIN, true);
                    startActivityForResult(intent, 100);
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.setMessage("确定要退出登录吗");
                    dialog.setCancelable(false);
                    dialog.setCancelBtn("取消", null);
                    dialog.setSurceBtn("确定", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            unLogin();
                        }
                    });
                    dialog.show();
                }
                break;

        }

    }


    public boolean isLogin() {
        return getIntent().getBooleanExtra(MeFragment.LOGIN_STATUS, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == LoginActivity.LOGIN_OK) {
            setResult(LoginActivity.LOGIN_OK, data);
            finish();
        } else if (resultCode == UN_LOGIN_OK) {
            unLogin();
        } else if (resultCode == LOGIN) {
            AppLog.print("rescoude_____login__");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra(KeyParams.SETTING, KeyParams.SETTING);
            startActivityForResult(intent, 100);
        } else if (resultCode == IM_LOGIN) {
            setResult(IM_LOGIN, data);
            finish();
        }

    }

    private void unLogin() {
        setResult(UN_LOGIN_OK);
        finish();
    }
}
