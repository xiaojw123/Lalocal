package com.lalocal.lalocal.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/6/13.
 */
public class PhotoSelectDialog extends Dialog implements View.OnClickListener {
    private Context context;

    private OnDialogClickListener listener;


    public PhotoSelectDialog(Context context) {
        super(context, R.style.prompt_dialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View view = LayoutInflater.from(context).inflate(R.layout.phototselect_layout, null);
        setContentView(view, params);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        Button photograph_btn = (Button) findViewById(R.id.photograph_btn);
        Button photoalbum_btn = (Button) findViewById(R.id.photoalbum_btn);
        Button cancleBtn = (Button) findViewById(R.id.cancel_selectephoto_btn);
        photoalbum_btn.setSelected(true);
        photograph_btn.setSelected(true);
        photoalbum_btn.setOnClickListener(this);
        photograph_btn.setOnClickListener(this);
        cancleBtn.setOnClickListener(this);
    }

    public void setButtonClickListener(OnDialogClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onButtonClickListner(this, v);
        }

    }

  public static interface OnDialogClickListener {

        void onButtonClickListner(Dialog dialog, View view);

    }


}
