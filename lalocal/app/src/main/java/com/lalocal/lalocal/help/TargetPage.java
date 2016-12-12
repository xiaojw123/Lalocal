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
    public static void gotoLive(Context context, String id, boolean isMessage) {
        Intent intent = new Intent(context, AudienceActivity.class);
        intent.putExtra("id", id);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    //充值
    public static void gotoRecharge(Context context, boolean isMessage) {
        MobHelper.sendEevent(context, MobEvent.MY_WALLET_DIAMOND_RECHARGE);
        Intent rechargeIntent = new Intent(context, RechargeActivity.class);
        if (isMessage) {
            rechargeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(rechargeIntent);
    }

    //优惠券
    public static void gotoCoupon(Context context,boolean isMessage) {
        Intent couponIntent = new Intent(context, MyCouponActivity.class);
        couponIntent.putExtra(KeyParams.PAGE_TYPE, KeyParams.PAGE_TYPE_WALLET);
        if (isMessage) {
            couponIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(couponIntent);
    }

    //回放
    public static void gotoPlayBack(Context context, String id,boolean isMessage) {
        Intent intent = new Intent(context, PlayBackActivity.class);
        intent.putExtra("id", id);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    //h5
    public static void gotoWebDetail(Context context, String targetUrl, String title, boolean isMessage) {
        Intent intent = new Intent(context, TargetWebActivity.class);
        intent.putExtra(KeyParams.TARGE_URL, targetUrl);
        intent.putExtra(KeyParams.TARGE_TITLE, title);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoUser(Context context, String targetId,boolean isMessage) {
        Intent intent = new Intent(context, LiveHomePageActivity.class);
        intent.putExtra("userId", targetId);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoProductDetail(Context context, String targetId, int targetType,boolean isMessage) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        SpecialToH5Bean bean = new SpecialToH5Bean();
        bean.setTargetId(Integer.parseInt(targetId));
        bean.setTargetType(targetType);
        intent.putExtra("productdetails", bean);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoSpecialDetail(Context context, String targetId,boolean isMessage) {
        Intent intent = new Intent(context, SpecialDetailsActivity.class);
        intent.putExtra("rowId", targetId);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }


    public static void gotoArticleDetail(Context context, String targetId,boolean isMessage) {
        Intent intent = new Intent(context, ArticleActivity.class);
        intent.putExtra("targetID", targetId);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    public static void gotoRouteDetail(Context context, int targetId,boolean isMessage) {
        Intent intent = new Intent(context, RouteDetailActivity.class);
        intent.putExtra(RouteDetailActivity.DETAILS_ID, targetId);
        if (isMessage) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

}
