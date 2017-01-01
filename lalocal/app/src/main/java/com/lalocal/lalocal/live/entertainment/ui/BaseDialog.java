package com.lalocal.lalocal.live.entertainment.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.DialogUtil;

import butterknife.ButterKnife;

/**
 * Created by android on 2016/10/14.
 */
public abstract class BaseDialog extends Dialog {

    public BaseDialog(Context context) {
        super(context, R.style.live_dialog);
    }
    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    protected BaseDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        DialogUtil.getInstance().addDialog(this);
        initView();
    }

    public abstract void initView();

    public abstract int getLayoutId();

    public void closeCurrent() {
        DialogUtil.getInstance().removeCurrent();
    }

    @Override
    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }

    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        closeCurrent();
    }
}
