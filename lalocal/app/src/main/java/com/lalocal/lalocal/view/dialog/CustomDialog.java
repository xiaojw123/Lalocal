package com.lalocal.lalocal.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/6/6.
 */
public class CustomDialog extends Dialog implements View.OnClickListener {
    TextView title_tv;
    TextView message_tv;
    Button neutral_btn;
    Button sure_btn;
    Button cancel_btn;
    Context context;
    String title, message, neturalText, sureText, cancelText;
    CustomDialogListener neutralBtnlistener, sureBtnLisener, cancelBtnListener;
    LinearLayout chooseContainer;


    public CustomDialog(Context context) {
        super(context, R.style.prompt_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        AppLog.print("dialog oncreate");
        initView();

    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.custom_dialog_title);
        message_tv = (TextView) findViewById(R.id.custom_dialog_message);
        neutral_btn = (Button) findViewById(R.id.custom_neutral_btn);
        sure_btn = (Button) findViewById(R.id.custom_sure_btn);
        cancel_btn = (Button) findViewById(R.id.custom_cancel_btn);
        chooseContainer = (LinearLayout) findViewById(R.id.custom_dialog_choose_continaer);
        neutral_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            title_tv.setText(title);
        }
        AppLog.print("set message__" + message);
        if (!TextUtils.isEmpty(message)) {
            AppLog.print("set msg__");
            message_tv.setText(message);
        }
        if (!TextUtils.isEmpty(neturalText)) {
            neutral_btn.setVisibility(View.VISIBLE);
            neutral_btn.setSelected(true);
            neutral_btn.setText(neturalText);
        }
        if (!TextUtils.isEmpty(sureText) && !TextUtils.isEmpty(cancelText)) {
            chooseContainer.setVisibility(View.VISIBLE);
            sure_btn.setText(sureText);
            cancel_btn.setText(cancelText);
        }


    }


    public void setTitle(String text) {
        title = text;
        if (title_tv != null) {
            title_tv.setText(title);
        }
    }

    public void setMessage(String text) {
        message = text;
        if (message_tv != null) {
            message_tv.setText(message);
        }
    }

    public void setNeturalBtn(String text, CustomDialogListener listener) {
        neturalText = text;
        neutralBtnlistener = listener;
        if (neutral_btn != null) {
            neutral_btn.setText(neturalText);
            neutral_btn.setOnClickListener(this);
        }

    }

    public void setSurceBtn(String text, CustomDialogListener listener) {
        sureText = text;
        sureBtnLisener = listener;
        if (sure_btn != null) {
            sure_btn.setText(sureText);
            sure_btn.setOnClickListener(this);
        }

    }

    public void setCancelBtn(String text, CustomDialogListener listener) {
        cancelText = text;
        cancelBtnListener = listener;
        if (cancel_btn != null) {
            cancel_btn.setText(cancelText);
            cancel_btn.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_neutral_btn:
                if (neutralBtnlistener != null) {
                    neutralBtnlistener.onDialogClickListener(this, v);
                }
                break;
            case R.id.custom_sure_btn:
                if (sureBtnLisener != null) {
                    sureBtnLisener.onDialogClickListener(this, v);
                }
                break;
            case R.id.custom_cancel_btn:
                if (cancelBtnListener != null) {
                    cancelBtnListener.onDialogClickListener(this, v);
                }
                break;

        }


    }

   public  static  interface CustomDialogListener {
        void onDialogClickListener(Dialog dialog, View view);
    }


}
