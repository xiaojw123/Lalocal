package com.lalocal.lalocal.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lalocal.lalocal.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by xiaojw on 2016/6/22.
 */
public class ConvertDialog extends Dialog implements View.OnClickListener {
    Context context;
    EditText codeInputEdit;

    public ConvertDialog(Context context) {
        this(context, R.style.prompt_dialog);
    }

    public ConvertDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.coupon_convert_dialog, null);
        codeInputEdit = (EditText) view.findViewById(R.id.convertdialog_input_code_edit);
        Button convertBtn = (Button) view.findViewById(R.id.convertdialog_convert_btn);
        convertBtn.setOnClickListener(this);
        codeInputEdit.setFocusable(true);
        codeInputEdit.setFocusableInTouchMode(true);
        codeInputEdit.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputManager = (InputMethodManager) codeInputEdit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(codeInputEdit, 0);
            }

        }, 50);
        setContentView(view);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawableResource(R.color.color_70_dark);
    }

    @Override
    public void onClick(View v) {
        String text = codeInputEdit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            Toast.makeText(context, "兑换码不能为空！", Toast.LENGTH_LONG).show();
            return;
        }
        dismiss();
    }
}
