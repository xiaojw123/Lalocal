package com.lalocal.lalocal.net.callback;


import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.RouteDetail;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.VersionInfo;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/1.
 */
public abstract class ICallBack {
    public void onGetPayResult(String result){

    }

    public void onGetRouteDetail(RouteDetail routeDetail){}


    public void onGetHotItems(List<SearchItem> items,int type){

    }

    public void onGetAreaItems(int pageNumber, int totalPages, List<SearchItem> items, int type){


    }
    public void onGetMoreItems(int pageNumber,int totalPages,List<SearchItem> items){


    }

    public void onGetDestinationCollections(List<SiftModle> items){

    }

    public void onGetSearchResult(String searchKey, List<ArticleItem> articleItems, int aritcleToalNmb, List<ProductItem> productItems, int productToalNmb , List<RouteItem> routeItems, int routeToalNumb){

    }

    public void onGetSearchTag(List<String> keys){

    }

    public void onGetSearchHot(List<String> keys){}


    public void onGetDestinationAreas(List<AreaItem> items){
    }

    public void onGetOrderDetail(OrderDetail detail){

    }

    public void onGetOrderItem(List<OrderItem> items){

    }


    public void onGetCounponItem(List<Coupon> items){

    }

    public void onGetFavoriteItem(List<FavoriteItem> items,int pageNumber,int totalPages,int toalRows){

    }

    public void onModifyUserProfile(LoginUser user){

    }


    public void onSendActivateEmmailComplete() {

    }

    public void onGetUserProfile(LoginUser user) {


    }


    public void onResetPasswordComplete() {

    }

    public void onResigterComplete(String email, String psw,int userid,String token) {

    }

    public void onCheckEmail(String email) {


    }

    public void onSendVerCode(String email) {

    }


    public void onLoginSucess(User user) {


    }

    public void onRequestFailed() {


    }
    //推荐
    public void onRecommend(RecommendDataResp recommendDataResp){}
    //推荐广告
    public void onRecommendAd( RecommendAdResp recommendAdResp){}
    //specialdetail
    public void onRecommendSpecial(SpectialDetailsResp spectialDetailsResp){}
    //产品详情
    public void onProductDetails(ProductDetailsDataResp detailsDataResp){}
    //取消赞
    public  void onPariseResult(PariseResult pariseResult){};
    //点赞
    public void onInputPariseResult(PariseResult pariseResult) {

    }
    //文章详情
    public void onArticleResult(ArticleDetailsResp articleDetailsResp){}

    //版本更新
    public void onVersionResult(VersionInfo versionInfo){}
}
