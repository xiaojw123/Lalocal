package com.lalocal.lalocal.live.entertainment.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/8/25.
 */
public class CustomChatDialog extends Dialog implements View.OnClickListener {

    Context context;
    CustomDialogListener sureBtnLisener, cancelBtnListener,okBtnListener;
    private TextView remindMetermine;
    private TextView remindCancel;
    private TextView remindContent;
    private TextView remindOk;
    private TextView dialogTitle;
    String title, determine, cancel,okText,content;
    public CustomChatDialog(Context context) {
        super(context, R.style.live_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_remind_login_layout);
        initView();
    }


    private void initView() {
        LinearLayout overLive = (LinearLayout) findViewById(R.id.remind_over_live);
        LinearLayout continueLive = (LinearLayout) findViewById(R.id.remind_contiun_live);
        remindMetermine = (TextView) findViewById(R.id.remind_determine);
        remindCancel = (TextView) findViewById(R.id.remind_cancel);
        remindContent = (TextView) findViewById(R.id.remind_dialog_content);
        remindOk = (TextView) findViewById(R.id.remind_ok);
        dialogTitle = (TextView) findViewById(R.id.remind_dialog_title);
        LinearLayout chooseLayout= (LinearLayout) findViewById(R.id.choose_layout);
        LinearLayout  okLayout = (LinearLayout) findViewById(R.id.ok_layout);

        overLive.setOnClickListener(this);
        continueLive.setOnClickListener(this);
        remindOk.setOnClickListener(this);
        if (!TextUtils.isEmpty(determine)) {
            remindMetermine.setText(determine);
        }
        if (!TextUtils.isEmpty(cancel)) {
            remindCancel.setText(cancel);
        }
        if (!TextUtils.isEmpty(content)) {
            remindContent.setText(content);
        }
        if(!TextUtils.isEmpty(okText)){
            remindOk.setText(okText);
            remindOk.setTextColor(Color.parseColor("#ffaa2a"));
            chooseLayout.setVisibility(View.GONE);
            okLayout.setVisibility(View.VISIBLE);
        }if(!TextUtils.isEmpty(title)){
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setText(title);
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void setContent(String content){
       this.content=content;
    }
    public void setOkBtn(String okText,CustomDialogListener listener){
        this.okText=okText;
        okBtnListener=listener;
    }
    public void setCancelBtn(String cancel, CustomDialogListener listener) {
        this.cancel = cancel;
        cancelBtnListener = listener;
    }

    public void setSurceBtn(String determine, CustomDialogListener listener) {
        this.determine = determine;
        sureBtnLisener = listener;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.remind_over_live:
                dismiss();
                if (sureBtnLisener != null) {
                    sureBtnLisener.onDialogClickListener();
                }
                break;
            case R.id.remind_contiun_live:
                if (cancelBtnListener != null) {
                    cancelBtnListener.onDialogClickListener();
                }
                dismiss();
                break;
            case R.id.remind_ok:
                if(okBtnListener!=null){
                    okBtnListener.onDialogClickListener();
                }
                dismiss();
                break;
        }
    }

public static interface CustomDialogListener {
    void onDialogClickListener();
}
}
