package com.lalocal.lalocal.service.callback;


import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LoginUser;

import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;

import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.User;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/1.
 */
public abstract class ICallBack {
    public void onGetOrderDetail(OrderDetail detail){

    }

    public void onGetOrderItem(List<OrderItem> items){

    }


    public void onGetCounponItem(List<Coupon> items){

    }

    public void onGetFavoriteItem(List<FavoriteItem> items,int totalPages,int toalRows){

    }

    public void onModifyUserProfile(int code,LoginUser user){

    }


    public void onSendActivateEmmailComplete(int code,String message) {

    }

    public void onGetUserProfile(int code, LoginUser user) {


    }


    public void onResetPasswordComplete(int code, String msg) {

    }

    public void onResigterComplete(String resultCode, String message, String email, String psw,int userid,String token) {

    }

    public void onCheckEmail(boolean isChecked, String email) {


    }

    public void onSendVerCode(int code, String email) {

    }


    public void onLoginSucess(String code, String message, User user) {


    }

    public void onRequestFailed(String error) {


    }
    //推荐
    public void onRecommend(RecommendDataResp recommendDataResp){}
    //推荐广告
    public void onRecommendAd( RecommendAdResp recommendAdResp){}
    //specialdetail
    public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp){}
    //产品详情
    public void onProductDetails(ProductDetailsDataResp detailsDataResp){}

}
