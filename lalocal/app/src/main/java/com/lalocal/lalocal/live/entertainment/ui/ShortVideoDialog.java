package com.lalocal.lalocal.live.entertainment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lalocal.lalocal.R;

/**
 * Created by ${WCJ} on 2017/1/10.
 */
public class ShortVideoDialog extends Dialog implements View.OnClickListener{
    private Context context;

    private OnDialogClickListener listener;


    public ShortVideoDialog(Context context) {
        super(context, R.style.test_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(context).inflate(R.layout.short_video_dialog_layout, null);
        setContentView(view, params);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        Button photoalbum_btn = (Button) findViewById(R.id.photoalbum_btn);
        Button cancleBtn = (Button) findViewById(R.id.cancel_selectephoto_btn);
        photoalbum_btn.setSelected(true);
        photoalbum_btn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
    }

    public void setButtonClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClickListner(this, v);
        }

    }

    public static interface OnDialogClickListener {

        void onClickListner(Dialog dialog, View view);

    }

}
