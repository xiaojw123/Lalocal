package com.lalocal.lalocal.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by xiaojw on 2016/9/24.
 */

public class ExchargeDialog extends AlertDialog {
    protected ExchargeDialog(Context context) {
        super(context);
    }

    protected ExchargeDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    protected ExchargeDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
