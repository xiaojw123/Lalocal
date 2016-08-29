package com.lalocal.lalocal.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.lalocal.lalocal.activity.BookActivity;
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/8/8.
 */
public class JsModeul {
    Context mContext;
    public JsModeul(Context context){
        mContext=context;
    }


    @JavascriptInterface
    public void postMessage(String callback, String message) {
        AppLog.print("callback___" + callback + ", message__" + message);
        switch (callback){
            case BookActivity.PAGE_TO_PAY:
                Intent intent=new Intent(mContext,PayActivity.class);
                intent.putExtra(PayActivity.ORDER_ID,Integer.parseInt(message));
                intent.putExtra(KeyParams.ACTION_TYPE,KeyParams.ACTION_BOOK);
//                ((Activity)mContext).startActivityForResult(intent,100);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                break;

        }

    }

    @JavascriptInterface
    public void postMessage(String callback) {
        AppLog.print("postMessage___callback__"+callback);


    }

}
