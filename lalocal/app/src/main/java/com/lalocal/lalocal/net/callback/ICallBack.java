package com.lalocal.lalocal.net.callback;


import android.widget.CompoundButton;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResp;
import com.lalocal.lalocal.live.entertainment.model.LiveGiftRanksResp;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeAreaResp;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerBean;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListResp;
import com.lalocal.lalocal.live.entertainment.model.LiveRoomAvatarSortResp;
import com.lalocal.lalocal.live.entertainment.model.PlayBackMsgResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewResultBean;
import com.lalocal.lalocal.model.AreaItem;
import com.lalocal.lalocal.model.ArticleDetailsResp;
import com.lalocal.lalocal.model.ArticleItem;
import com.lalocal.lalocal.model.ArticlesResp;
import com.lalocal.lalocal.model.ChannelIndexTotalResult;
import com.lalocal.lalocal.model.ChannelRecord;
import com.lalocal.lalocal.model.CloseLiveBean;
import com.lalocal.lalocal.model.CmbPay;
import com.lalocal.lalocal.model.ConsumeRecord;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.FavoriteItem;
import com.lalocal.lalocal.model.HomepageUserArticlesResp;
import com.lalocal.lalocal.model.ImgTokenBean;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveDetailsDataResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionResp;
import com.lalocal.lalocal.model.LiveFansOrAttentionRowsBean;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveSeachItem;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.LoginUser;
import com.lalocal.lalocal.model.MessageItem;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.model.OrderItem;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.ProductDetailsDataResp;
import com.lalocal.lalocal.model.ProductItem;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.model.RecommendAdResp;
import com.lalocal.lalocal.model.RecommendDataResp;
import com.lalocal.lalocal.model.RecommendListDataResp;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.model.RecommendationsBean;
import com.lalocal.lalocal.model.RouteDetail;
import com.lalocal.lalocal.model.RouteItem;
import com.lalocal.lalocal.model.SearchItem;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.model.SocialUser;
import com.lalocal.lalocal.model.SpectialDetailsResp;
import com.lalocal.lalocal.model.SysConfigItem;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.model.VersionInfo;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.model.WelcomeImg;

import java.util.List;

/**
 * Created by xiaojw on 2016/6/1.
 */
public abstract class ICallBack {
    public void onEMLoginSucess(){

    }



    public void onExchargeGoldSuccess(){


    }


    public void onChargeGold(String result,String channel){

    }

    public void onGetRechargeProducts(List<RechargeItem> items){

    }

    public void onGetScoreLog(ConsumeRecord log){

    }


    public void onGetMyWallet(WalletContent content){

    }


    public void onGetSysConfigs(List<SysConfigItem> items){}


    public void onCancelSuccess(){

    }
    public void onDelSuccess(){

    }

    public void onGetWelcomeImgs(WelcomeImg welcomeImg){

    }

    public void onGetPayResult(String result){

    }

    public void onGetRouteDetail(RouteDetail routeDetail){}


    public void onGetHotItems(List<SearchItem> items,int type){

    }

    public void onGetAreaItems(int pageNumber, int totalPages, List<SearchItem> items, int type,int collectionId){


    }
    public void onGetMoreItems(String key,int pageNumber,int totalPages,List<SearchItem> items){


    }
    public void onGetMoreItems(int type,String key,int pageNumber,int totalPages,List<SearchItem> items){


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


    public void onResetPasswordComplete(String  password) {

    }

    public void onResigterComplete(String email, String psw,int userid,String token) {

    }

    public void onCheckEmail(String email,String userid) {


    }

    public void onSendVerCode(String email) {

    }


    public void onLoginSucess(User user) {


    }

    public void onError(VolleyError volleyError) {
    }
    public void onResponseFailed(int returnCode,String message){
    }
    public void onResponseFailed(String message,int requestCode){
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
    //获取游客账号登录聊天
    public void onTouristInfo(String json) {
    }
   /* //搜索关注和粉丝
    public void onSearchAttentionOrFans(LiveFansOrAttentionResp liveFansOrAttentionResp) {
    }*/
    //直播礼物商城
    public void onGiftsStore(GiftDataResp giftDataResp) {
    }
    //管理员
    public  void onLiveManager(LiveManagerBean liveManagerBean){}
    //查看是否为管理员
    public  void onCheckManager(LiveManagerBean liveManagerBean){}
    //管理员列表
    public  void onManagerList(LiveManagerListResp liveManagerListResp){}

    //送礼物
    public void onSendGiftsBack(String result){}

    public void onSearchLive(LiveSeachItem item){
    }
    //礼物排行榜
    public void onGiftRanks(LiveGiftRanksResp liveGiftRanksResp) {
    }
    public void onGetExchargeResult(){
    }

    //在线人数
    public void onOnLinesCount(String json) {
    }

    // 首页推荐列表，包含：直播列表、专题列表、商品列表
    public void onRecommendList(RecommendListDataResp recommendListDataResp) {}

    // 文章列表
    public void onArticleListResult(ArticlesResp articlesResp) {}

    //获取在线人数
    public void onGetAudienceOnLineUserCount(String json) {
    }
    //分享统计
    public void onShareStatistics(String json) {
    }
    public void onGetUserLive(UserLiveItem item) {
    }
    //发起挑战
    public void onChallengeInitiate( ChallengeDetailsResp.ResultBean resultBean) {

    }
    //发起挑战
    public void onChallengeInitiate(String json){

        }
    //挑战详情
    public void onChallengeDetails(String json) {
    }
    //主播操作挑战
    public void onLiveChallengeStatus(ChallengeDetailsResp.ResultBean resultBean) {
    }
    //挑战列表
    public void onChallengeList(String json) {
    }
    //直播首页列表
    public void onLiveHomeList(LiveHomeListResp liveListDataResp,String attenFlag) {
    }
    //历史回放
    public void onPlayBackList(String json,String attentionFlag) {
    }
    //直播地区分类
    public void onLiveHomeArea(LiveHomeAreaResp liveHomeAreaResp) {
    }
    //历史回放详情
    public void onPlayBackDetails(PlayBackResultBean liveRowsBean) {
    }
    public void onGetChannelRecord(ChannelRecord record){

    }
    //手机号登录
    public void onLoginByPhone(User user,String phone,String code){


    }
    //手机号注册
    public void onRegisterByPhone(User user){


    }
    public void onGetSmsCodeSuccess(){

    }

    // 获取用户当前直播
    public void onGetUserCurLive(LiveRowsBean liveRowsBean) {}

    // 获取用户文章列表
    public void onGetUserArticles(HomepageUserArticlesResp articlesResp) {}


    public void onDeleteLiveHistory(){

    }

    public void onSocialLogin(User user, String bodyParams, String uidParams){

    }

    public void onSocialRegisterSuccess(User user){


    }
    public  void onBindPhoneSuccess(String phone){}

    public void onGetSocialUsers(SocialUser wexinUser,SocialUser qqUser,SocialUser weiboUser){



    }

    public void onBindSocialUser(CompoundButton switchBtn,SocialUser wexinUser, SocialUser qqUser, SocialUser weiboUser){}

    public void onUnBindSocialUser(CompoundButton switchBtn){


    }

    public void onLiveRoomAvatar(LiveRoomAvatarSortResp.ResultBean result) {
    }

    public void onResponseLog(String json) {


    }

    /**
     * 获取首页我的关注
     * @param bean
     */
    public void onGetHomeAttention(LiveRowsBean bean) {

    }

    /**
     * 获取每日推荐
     * @param bean
     */
    public void onGetDailyRecommend(RecommendationsBean bean) {

    }

    /**
     * 直播间举报
     * @param json
     */
    public void onGetChannelReport(String json) {
    }


    public void onCancelManager(String json) {
    }

    public void onUserMuteResult(String json) {
    }

    public void onPerpetualMute(int json) {
    }

    public void onResponseGetTags(List<String> tags){}

    public void onGetCmbPayParams(CmbPay cmbPay){
    }
    public void onGetOrderStatus(int status){}
    public void onGetPayStatus(int status){

    }
    public void onGetPushLogs(String date, List<MessageItem> items){}
    public void onGetGLiveSearch(List<LiveRowsBean> rowList,String name){

    }
    public void onGetGPlayBackSearch(int pageNum,int toalPages,List<LiveRowsBean> rowList){

    }

    public void onGetGUserSearch(String key,int pageNum, int toalPages, List<LiveFansOrAttentionRowsBean> beanList){

    }

    public void onGetGSpecialSearch(String key,int pageNum, int toalPages, List<RecommendRowsBean> beanList){

    }
    public void onGetMessageCount(String msgCount){

    }

    public void onSendMessage(int code) {
    }

    public void onGetChannelIndexTotal(ChannelIndexTotalResult result, long dateTime) {}

    public void onPlayBackReviewDetails(PlayBackReviewResultBean reviewResultBean) {
    }

    public void onPlayBackMsgDetails(PlayBackMsgResultBean msgResultBean) {

    }


    public void onGetArticleComments(String json) {}

}
