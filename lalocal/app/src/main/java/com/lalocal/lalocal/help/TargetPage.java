package com.lalocal.lalocal.help;

import android.content.Context;
import android.content.Intent;

import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.activity.MyCouponActivity;
import com.lalocal.lalocal.activity.ProductDetailsActivity;
import com.lalocal.lalocal.activity.RechargeActivity;
import com.lalocal.lalocal.activity.RouteDetailActivity;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveHomePageActivity;
import com.lalocal.lalocal.live.entertainment.activity.PlayBackActivity;
import com.lalocal.lalocal.me.TargetWebActivity;
import com.lalocal.lalocal.model.SpecialToH5Bean;

/**
 * Created by xiaojw on 2016/11/22.
 */

public class TargetPage {

   //直播
    public static  void gotoLive(Context context,int id){
        Intent intent = new Intent(context, AudienceActivity.class);
        intent.putExtra("id", String.valueOf(id));
        context.startActivity(intent);
    }

    //充值
    public static void gotoRecharge(Context context){
        MobHelper.sendEevent(context, MobEvent.MY_WALLET_DIAMOND_RECHARGE);
        Intent rechargeIntent = new Intent(context, RechargeActivity.class);
        context.startActivity(rechargeIntent);
    }
    //优惠券
    public static void gotoCoupon(Context context) {
        Intent couponIntent = new Intent(context, MyCouponActivity.class);
        couponIntent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
        context.startActivity(couponIntent);
    }

    //回放
    public static void gotoPlayBack(Context context, int id) {
        Intent intent = new Intent(context, PlayBackActivity.class);
        intent.putExtra("id", String.valueOf(id));
        context.startActivity(intent);
    }




    //h5
    public static void gotoWebDetail(Context context, String targetUrl,String title) {
        Intent intent = new Intent(context, TargetWebActivity.class);
        intent.putExtra(KeyParams.TARGE_URL, targetUrl);
        intent.putExtra(KeyParams.TARGE_TITLE,title);
        context.startActivity(intent);
    }

    public static void gotoUser(Context context, int targetId) {
        Intent intent = new Intent(context, LiveHomePageActivity.class);
        intent.putExtra("userId",String.valueOf(targetId));
        context.startActivity(intent);
    }

    public static void gotoProductDetail(Context context, int targetId, int targetType) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(targetId);
        bean.setTargetType(targetType);
        intent.putExtra("productdetails", bean);
        context.startActivity(intent);
    }

    public static void gotoSpecialDetail(Context context, int targetId) {
        Intent intent = new Intent(context, SpecialDetailsActivity.class);
        intent.putExtra("rowId", String.valueOf(targetId));
        context.startActivity(intent);
    }


    public static void gotoArticleDetail(Context context, int targetId) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("targetID", String.valueOf(targetId));
        context.startActivity(intent);
    }

    public static void gotoRouteDetail(Context context, int targetId) {
        Intent intent = new Intent(context, RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, targetId);
        context.startActivity(intent);
    }

}
