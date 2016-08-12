package com.lalocal.lalocal.net.callback;


import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveDetailsDataResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;

import com.lalocal.lalocal.model.OrderDetail;

import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;

import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.VersionInfo;

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
    //直播列表
    public  void onLiveList(LiveListDataResp liveListDataResp){}
    //推荐直播列表
    public void onLiveRecommendList(LiveRecommendListDataResp liveRecommendListDataResp) {
    }
    //直播详情
    public void onLiveDetails(LiveDetailsDataResp liveDetailsDataResp) {
    }
    //创建直播间
    public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
    }
    //修改直播间
    public void onAlterLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp){}
    //关闭直播
    public void onCloseLive(CloseLiveBean closeLiveBean){}
    //上传图片token
    public void onImgToken(ImgTokenBean imgTokenBean) {
    }
    //修改直播封面
    public void onAlterLiveCover(CreateLiveRoomDataResp createLiveRoomDataResp) {
    }
    //获取直播用户信息
    public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
    }
    //添加关注
    public void onLiveAttentionStatus( LiveAttentionStatusBean liveAttentionStatusBean){}
    //粉丝或关注列表
    public void onLiveFansOrAttention(LiveFansOrAttentionResp liveFansOrAttentionResp,boolean isSearch) {
    }
    //驱取消关注
    public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
    }
   /* //搜索关注和粉丝
    public void onSearchAttentionOrFans(LiveFansOrAttentionResp liveFansOrAttentionResp) {
    }*/
}
