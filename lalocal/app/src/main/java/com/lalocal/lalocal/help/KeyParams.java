package com.lalocal.lalocal.help;

/**
 * Created by xiaojw on 2016/6/16.
 */
public interface KeyParams {
    public static final  int CHANNEL_LOGIN_PHONE=0x01;
    public static final  int CHANNEL_LOGIN_EMAIL=0x02;
    public static final  int CHANNEL_LOGIN_SOCIAL=0x03;
    public static final  String DATE_TIME="dateTime";
    public static final  String TARGE_URL="target_url";
    public static final  String TARGE_TITLE="target_title";
    public static final  String TARGET_ID = "target_id";
    public static final String TARGET_TYPE = "target_type";
    public static  final String UID_PARAMS="social_uid_params";
    public static final  String SOCIAL_PARAMS="social_params";
    public static final  String USER="user";
    public static final  String CODE="code";
    public static final  String DESCRIPTON="descriptoin";
    public static  final String EMAIL="email";
    public static final  String PHONE="phone";
    public static final  String AREA_Code="areaCode";
    public static  final String NICKNAME="nickname";
    public static final String USERID="userid";
    public static final  String TOKEN="token";
    public static final  String STATUS="status";
    public static  final  String AVATAR="avatar";
    public static  final String SORTVALUE="sortValue";
    public static  final  String ORDER_ID="orderid";
    public static  final  String PAY_NO="payno";
    public static  final  String TRAVEL_PERSONS_CONCACT="concact";
    public static  final String SETTING="setting";
    public static  final  String IM_LOGIN="im_login";
    public static  final  String LOGIN_CHANNEL="login_channel";
    public static  final  String NORMAIL_BACK="normal_back";
    public static  final  String PASSWORD="psw";
    public static  final  String IS_LOGIN="isLogin";
    public static final String IM_TOKEN="imtoken";
    public static final String IM_CCID="ccid";
    public static final String ORDDER_DETFAIL="orderdetail";
    public static  final  String AMOUNT_PRICE="amountprice";
    public static final  String PRE_VIEW_PARAMS="previewparams";
    public static  final  String ACTION_TYPE="actointype";
    public static final  String ACTION_BOOK="actionbook";
    public static final String ACTION_UPDATE_ORDER="updateorder";
    public static  final  String SOCRE_NUM="score_num";
    public static final  String SCALE="scale";
    public static  final  String WALLET_CONTENT="wallet_content";
    // onActivityResult相关
    public static  final int REQUEST_CODE=100;
    public static  final  int RESULT_ChARGE_SUCCESS =200;
    public static final  int RESULT_EXCHARGE_SUCCESS=201;
    public static final  int RESULT_UPDATE_WALLET=203;
    //页面类型
    public static final String PAGE_TYPE="page_type";
    public static final int PAGE_TYPE_WALLET=0x11;


    public static final  String REPLY_CONTENT="reply_content";
    public static final String REPLY_TITLE="reply_title";
    public static final String REPLY_TYPE = "reply_type";
    public static final String REPLY_PARENT_ID = "reply_parent_id";
    public static final String POST_TITLE="post_title";
    public static final String POST_LOCATION="post_location";
    public static final String POST_PHOTO="post_photo";
    public static final String POST_HISTORY_ID="post_history_id";
    public static final String POST_GET_LOCATION="post_get_location";
    public static final int REPLY_RESULTCODE=300;
    public static final int REPLY_REQUESTCODE=301;
    public static final int POST_REQUESTCODE=302;
    public static final int POST_RESULTCODE=303;
    public static final int LOCATION_RESULTCODE=304;
    public static final int LOCATION_REQUESTCODE=305;


    public static final int REPLY_TYPE_NEW = 400; // 发起评论
    public static final int REPLY_TYPE_REPLY = 401; // 回复评论
}
