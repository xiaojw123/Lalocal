package com.lalocal.lalocal.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/6/6.
 * 关闭硬件加速CardView显示阴影会有问题
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
    FrameLayout chooseContainer;


    public CustomDialog(Context context) {
        super(context, R.style.prompt_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
        initView();

    }

    private void initView() {
        title_tv = (TextView) findViewById(R.id.custom_dialog_title);
        message_tv = (TextView) findViewById(R.id.custom_dialog_message);
        neutral_btn = (Button) findViewById(R.id.custom_neutral_btn);
        sure_btn = (Button) findViewById(R.id.custom_sure_btn);
        cancel_btn = (Button) findViewById(R.id.custom_cancel_btn);
        chooseContainer = (FrameLayout) findViewById(R.id.custom_dialog_choose_continaer);
        neutral_btn.setOnClickListener(this);
        sure_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        if (!TextUtils.isEmpty(title)) {
            title_tv.setVisibility(View.VISIBLE);
            title_tv.setText(title);
        } else {
            title_tv.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(message)) {
            message_tv.setVisibility(View.VISIBLE);
            message_tv.setText(message);
        } else {
            message_tv.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(neturalText)) {
            neutral_btn.setVisibility(View.VISIBLE);
            neutral_btn.setSelected(true);
            neutral_btn.setText(neturalText);
        } else {
            neutral_btn.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(sureText) && !TextUtils.isEmpty(cancelText)) {
            sure_btn.setSelected(true);
            chooseContainer.setVisibility(View.VISIBLE);
            sure_btn.setText(sureText);
            cancel_btn.setText(cancelText);
        } else {
            chooseContainer.setVisibility(View.GONE);
        }
    }


    public void setTitle(String text) {
        title = text;
//        if (title_tv != null) {
//            title_tv.setText(title);
//        }
    }


    public void setMessage(String text) {
        message = text;
//        if (message_tv != null) {
//            message_tv.setText(message);
//        }
    }

    public void setNeturalBtn(String text, CustomDialogListener listener) {
        neturalText = text;
        neutralBtnlistener = listener;

    }

    public void setSurceBtn(String text, CustomDialogListener listener) {
        sureText = text;
        sureBtnLisener = listener;

    }

    public void setCancelBtn(String text, CustomDialogListener listener) {
        cancelText = text;
        cancelBtnListener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_neutral_btn:
                dismiss();
                if (neutralBtnlistener != null) {
                    neutralBtnlistener.onDialogClickListener();
                }
                break;
            case R.id.custom_sure_btn:
                dismiss();
                if (sureBtnLisener != null) {
                    sureBtnLisener.onDialogClickListener();
                }
                break;
            case R.id.custom_cancel_btn:
                dismiss();
                if (cancelBtnListener != null) {
                    cancelBtnListener.onDialogClickListener();
                }
                break;

        }


    }

    public static interface CustomDialogListener {
        void onDialogClickListener();
    }


}
