package com.lalocal.lalocal.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.lalocal.lalocal.activity.BookActivity;
import com.lalocal.lalocal.activity.MyCouponActivity;
import com.lalocal.lalocal.activity.PayActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.util.AppLog;

/**
 * Created by xiaojw on 2016/8/8.
 */
public class JsInterface {
    Context mContext;
    public JsInterface(Context context){
        mContext=context;
    }
    @JavascriptInterface
    public void postMessage(String callback, String message) {
        AppLog.print("postMessage@param  callback:" + callback + "\nmessage:" + message);
        switch (callback){
            case BookActivity.PAGE_TO_PAY:
                Intent intent=new Intent(mContext,PayActivity.class);
                intent.putExtra(PayActivity.ORDER_ID,Integer.parseInt(message));
                intent.putExtra(KeyParams.ACTION_TYPE,KeyParams.ACTION_BOOK);
//                ((Activity)mContext).startActivityForResult(intent,100);
                mContext.startActivity(intent);
                ((Activity)mContext).finish();
                break;
            case BookActivity.PAGET_TO_COUPON:
                //TODO:优惠券影响预定流程
                gotoMyCoupon(message);
                break;
        }

    }


//    @JavascriptInterface
//    public void postMessage(String callback) {
//        AppLog.print("postMessage___callback__"+callback);
//        switch (callback){
//            case BookActivity.PAGET_TO_COUPON:
//                //TODO:优惠券影响预定流程
////                gotoMyCoupon();
//                break;
//
//        }
//
//
//    }




    private void gotoMyCoupon(String productionId) {
        Intent intent=new Intent(mContext, MyCouponActivity.class);
        intent.putExtra(MyCouponActivity.PRODUCTION_ID,productionId);
        ((Activity)mContext).startActivityForResult(intent,KeyParams.REQUEST_CODE);
    }

}
