package com.lalocal.lalocal.view;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.dialog.CustomDialog;

/**
 * Created by xiaojw on 2017/1/9.
 * 公用ChromeCLient
 * 接管H5对话框提示
 */

public class CommonChromeClient extends WebChromeClient {
    Context mContext;

    public CommonChromeClient(Context context) {
        mContext = context;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AppLog.print("alert__" + message);
        if (!TextUtils.isEmpty(message)) {
            CustomDialog dialog = new CustomDialog(mContext);
            dialog.setTitle("提示");
            dialog.setMessage(message);
            dialog.setNeturalBtn("确定", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    result.confirm();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
            return true;
        }
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        AppLog.print("onJsConfirm____");
        if (!TextUtils.isEmpty(message)) {
            CustomDialog dialog = new CustomDialog(mContext);
            dialog.setTitle("提示");
            dialog.setMessage(message);
            dialog.setSurceBtn("确定", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    result.confirm();
                }
            });
            dialog.setCancelBtn("取消", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    result.cancel();
                }
            });
            dialog.setCancelable(false);
            dialog.show();
            return true;
        }
        return super.onJsConfirm(view, url, message, result);


    }


}
