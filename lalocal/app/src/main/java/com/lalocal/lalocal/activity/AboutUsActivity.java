package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppConfig;

public class AboutUsActivity extends BaseActivity implements View.OnClickListener {
    TextView appversion;
    RelativeLayout email_rl, inland_phone_rl, foreign_phone_rl;
    RelativeLayout grade_rl, useclauses_rl;
    ImageView backImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_layout);
        backImg = (ImageView) findViewById(R.id.common_back_btn);
        appversion = (TextView) findViewById(R.id.about_us_version);
        email_rl = (RelativeLayout) findViewById(R.id.about_us_email);
        inland_phone_rl = (RelativeLayout) findViewById(R.id.about_us_phone_inner);
        foreign_phone_rl = (RelativeLayout) findViewById(R.id.about_us_phone_outer);
        grade_rl = (RelativeLayout) findViewById(R.id.about_us_grade);
        useclauses_rl = (RelativeLayout) findViewById(R.id.about_us_use_clauses);
        appversion.setText("版本" + AppConfig.getVersionName(this));
        backImg.setOnClickListener(this);
        email_rl.setOnClickListener(this);
        inland_phone_rl.setOnClickListener(this);
        foreign_phone_rl.setOnClickListener(this);
        grade_rl.setOnClickListener(this);
        useclauses_rl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_back_btn:
                finish();
                break;
            case R.id.about_us_email:
                sendEmail();
                break;
            case R.id.about_us_phone_inner:
                callPhone(AppConfig.getInlandPhone());
                break;
            case R.id.about_us_phone_outer:
                callPhone(AppConfig.getForeigenPhone());
                break;
            case R.id.about_us_grade:
                grade();
                break;
            case R.id.about_us_use_clauses:
                watchUseClauses();
                break;
        }


    }

    private void sendEmail() {
        //TEST
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822") ; // 真机上使用这行
        String subject = "邮件标题";
        String text = "邮件内容";
        i.putExtra(Intent.EXTRA_SUBJECT, subject);
        i.putExtra(Intent.EXTRA_TEXT, text);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources().getString(R.string.company_email)});
        startActivity(Intent.createChooser(i, "选择邮箱应用"));

    }

    private void watchUseClauses() {
        Intent intent = new Intent(this, UserProtocolActivity.class);
        startActivity(intent);
    }

    public void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void grade() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
