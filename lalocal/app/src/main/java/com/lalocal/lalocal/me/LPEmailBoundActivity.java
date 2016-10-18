package com.lalocal.lalocal.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomEditText;

public class LPEmailBoundActivity extends BaseActivity implements View.OnClickListener {
    CustomEditText email_edit;
    Button change_email_btn;
    TextView skp_tv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_bound);
        skp_tv = (TextView) findViewById(R.id.p1_emailbound_skip_tv);
        email_edit = (CustomEditText) findViewById(R.id.p1_emailbound_edit);
        change_email_btn = (Button) findViewById(R.id.p1_next_btn);
        email_edit.setEidtType(CustomEditText.TYPE_1);
        email_edit.setDefaultSelectionEnd(true);
        email_edit.setClearButtonVisible(false);
        change_email_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        String email = email_edit.getText();
        if (!CommonUtil.checkEmail(email)) {
            CommonUtil.showPromptDialog(this, getResources().getString(R.string.email_no_right), null);
            return;
        }
        Intent intent = new Intent(this, LPEmailBound2Activity.class);
        startActivity(intent);
    }


}
