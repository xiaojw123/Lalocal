package com.lalocal.lalocal.help;

/**
 * Created by xiaojw on 2016/6/16.
 */
public interface KeyParams {
    int CHANNEL_LOGIN_PHONE = 0x01;
    int CHANNEL_LOGIN_EMAIL = 0x02;
    int CHANNEL_LOGIN_SOCIAL = 0x03;
    String DATE_TIME = "dateTime";
    String TARGE_URL = "target_url";
    String TARGE_TITLE = "target_title";
    String TARGET_ID = "target_id";
    String TARGET_TYPE = "target_type";
    String UID_PARAMS = "social_uid_params";
    String SOCIAL_PARAMS = "social_params";
    String USER = "user";
    String CODE = "code";
    String DESCRIPTON = "descriptoin";
    String EMAIL = "email";
    String PHONE = "phone";
    String AREA_Code = "areaCode";
    String NICKNAME = "nickname";
    String ACCID = "accid";
    String USERID = "userid";
    String TOKEN = "token";
    String STATUS = "status";
    String AVATAR = "avatar";
    String SORTVALUE = "sortValue";
    String ORDER_ID = "orderid";
    String PAY_NO = "payno";
    String TRAVEL_PERSONS_CONCACT = "concact";
    String SETTING = "setting";
    String IM_LOGIN = "im_login";
    String LOGIN_CHANNEL = "login_channel";
    String NORMAIL_BACK = "normal_back";
    String PASSWORD = "psw";
    String IS_LOGIN = "isLogin";
    String IM_TOKEN = "imtoken";
    String IM_CCID = "ccid";
    String ORDDER_DETFAIL = "orderdetail";
    String AMOUNT_PRICE = "amountprice";
    String PRE_VIEW_PARAMS = "previewparams";
    String ACTION_TYPE = "actointype";
    String ACTION_BOOK = "actionbook";
    String ACTION_UPDATE_ORDER = "updateorder";
    String SOCRE_NUM = "score_num";
    String SCALE = "scale";
    String WALLET_CONTENT = "wallet_content";
    // onActivityResult相关
    int REQUEST_CODE = 100;
    int RESULT_ChARGE_SUCCESS = 200;
    int RESULT_EXCHARGE_SUCCESS = 201;
    int RESULT_UPDATE_WALLET = 203;
    //页面类型
    String PAGE_TYPE = "page_type";
    int PAGE_TYPE_WALLET = 0x11;
    String HAST_CANCLE = "has_cancel";
    String HAST_TITLE = "has_tilte";
    String REPLY_CONTENT = "reply_content";
    String REPLY_TITLE = "reply_title";
    String REPLY_TYPE = "reply_type";
    String REPLY_PARENT_ID = "reply_parent_id";
    int REPLY_RESULTCODE = 300;
    int REPLY_REQUESTCODE = 301;
    int REPLY_TYPE_NEW = 400; // 发起评论
    int REPLY_TYPE_REPLY = 401; // 回复评论
    String POST_TITLE = "post_title";
    String POST_LOCATION = "post_location";
    String POST_PHOTO = "post_photo";
    String POST_HISTORY_ID = "post_history_id";
    String POST_GET_LOCATION = "post_get_location";
    String POST_VIDEO_INFO = "post_video_info";
    int POST_REQUESTCODE = 302;
    int POST_RESULTCODE = 303;
    int LOCATION_RESULTCODE = 304;
    int LOCATION_REQUESTCODE = 305;
    int PLAYER_OVER_FIRST_REQUESTCODE = 306;
    int PLAYER_OVER_FIRST_RESULTCODE = 307;


}
