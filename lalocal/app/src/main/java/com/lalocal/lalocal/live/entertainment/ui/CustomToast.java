package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lalocal.lalocal.R;

/**
 * Created by android on 2016/9/26.
 */
public class CustomToast {

    public  CustomToast(Context context, String prompt) {
        Toast toast = new Toast(context);

        toast.setDuration(Toast.LENGTH_SHORT);
        View view = View.inflate(context, R.layout.custom_toast_layout,null);
        TextView tvPrompt = (TextView)view.findViewById(R.id.custom_toast_content);
        tvPrompt.setText(prompt);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
