package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.me.AccountSecurityActivity;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DataCleanManager;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import static com.lalocal.lalocal.R.id.setting_accout_security;
import static com.lalocal.lalocal.activity.fragment.MeFragment.UN_LOGIN_OK;


public class SettingActivity extends BaseActivity implements View.OnClickListener {
    public static final int LOGIN = 104;
    Button signOUtBtn;
    TextView chacheSizeTv;
    FrameLayout accountSecurityCotainer, aboutUsContainer, clearCacheContainer, gradeCotainer, infoFbCotainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);
        setLoginBackResult(true);
        initView();
    }

    private void initView() {
        gradeCotainer = (FrameLayout) findViewById(R.id.setting_grade);
        infoFbCotainer = (FrameLayout) findViewById(R.id.setting_info_feedback);
        accountSecurityCotainer = (FrameLayout) findViewById(R.id.setting_accout_security);
        aboutUsContainer = (FrameLayout) findViewById(R.id.setting_about_us);
        clearCacheContainer = (FrameLayout) findViewById(R.id.setting_clear_cache);
        chacheSizeTv = (TextView) findViewById(R.id.setting_cache_size);
        signOUtBtn = (Button) findViewById(R.id.setting_sign_out);
        chacheSizeTv.setText(DataCleanManager.getTotalCacheSize(this));
        if (UserHelper.isLogined(this)) {
            accountSecurityCotainer.setVisibility(View.VISIBLE);
            signOUtBtn.setText(getResources().getString(R.string.sign_out));
        } else {
            accountSecurityCotainer.setVisibility(View.GONE);
            signOUtBtn.setText(getResources().getString(R.string.immediately_login));
        }
        gradeCotainer.setOnClickListener(this);
        infoFbCotainer.setOnClickListener(this);
        accountSecurityCotainer.setOnClickListener(this);
        aboutUsContainer.setOnClickListener(this);
        clearCacheContainer.setOnClickListener(this);
        signOUtBtn.setOnClickListener(this);
    }

    public void grade() {
        try {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "未发现安装该应用的市场", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.setting_grade:
                grade();
                break;
            case R.id.setting_info_feedback:
                Intent fbIntent = new Intent(this, ChatActivity.class);
                startActivity(fbIntent);
                break;

            case setting_accout_security:
                Intent asIntent = new Intent(this, AccountSecurityActivity.class);
                startActivityForResult(asIntent, KeyParams.REQUEST_CODE);
                break;

            case R.id.setting_about_us:
                Intent aboutUsIntent = new Intent(this, AboutUsActivity.class);
                startActivity(aboutUsIntent);
                break;
            case R.id.setting_clear_cache:
                String chacheSize = DataCleanManager.getTotalCacheSize(this);
                if (!"0.00MB".equals(chacheSize)) {
                    DataCleanManager.clearAllCache(this);
                } else {
                    CommonUtil.showToast(this, "未发现缓存", Toast.LENGTH_SHORT);
                    return;
                }
                chacheSize = DataCleanManager.getTotalCacheSize(this);
                chacheSizeTv.setText(chacheSize);
                if ("0.00MB".equals(chacheSize)) {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.setMessage("已成功清理缓存");
                    dialog.setNeturalBtn("确定", null);
                    dialog.show();
                } else {
                    CustomDialog dialog = new CustomDialog(this);
                    dialog.setMessage("未完全清空缓存,请重新清理");
                    dialog.setNeturalBtn("确定", null);
                    dialog.show();
                }
                break;
            case R.id.setting_sign_out:
                String text = signOUtBtn.getText().toString();
                if (getResources().getString(R.string.immediately_login).equals(text)) {
                    Intent intent = new Intent(this, LLoginActivity.class);
                    intent.putExtra(KeyParams.IM_LOGIN, true);
                    intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_SETTING);
                    startActivityForResult(intent, 100);
                } else {
                    CustomDialog dialog2 = new CustomDialog(this);
                    dialog2.setMessage("确定要退出登录吗");
                    dialog2.setCancelable(false);
                    dialog2.setCancelBtn("取消", null);
                    dialog2.setSurceBtn("确定", new CustomDialog.CustomDialogListener() {
                        @Override
                        public void onDialogClickListener() {
                            unLogin();
                        }
                    });
                    dialog2.show();
                }
                break;

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == MeFragment.LOGIN_OK) {
            setResult(MeFragment.LOGIN_OK, data);
            finish();
        } else if (resultCode == UN_LOGIN_OK) {
            unLogin();
        } else if (resultCode == MeFragment.IM_LOGIN_OK) {
            setResult(MeFragment.IM_LOGIN_OK, data);
            finish();
        } else if (resultCode == MeFragment.UPDATE_ME_DATA) {
            setResult(resultCode);
        } else if (resultCode == LOGIN) {
            Intent intent = new Intent(this, LLoginActivity.class);
            intent.putExtra(KeyParams.SETTING, KeyParams.SETTING);
            startActivityForResult(intent, 100);
        }
    }

    private void unLogin() {
        UserHelper.updateSignOutInfo(this);
        finish();
    }
}
