package com.lalocal.lalocal.live.entertainment.constant;


import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/9/4.
 */
public class LiveConstant {

    public  static final int IS_LIVEER=1;//主播
    public static final int IS__MANAGER=2;//管理员
    public static final int IS_ONESELF=3;//我自己
    public static final int MANAGER_IS_ME=4;//我是管理员
    public  static final  int LIVEER_CHECK_ADMIN=5;//主播设置管理员
    public  static  final  int ME_CHECK_OTHER=6;//我看别人
    public static  int IDENTITY=0;
    public  static  int ROLE=-1;
    public  static String  ROOM_ID="";
    public  static  boolean isUnDestory=false;
    public  static  int newMessageCount=0;
    public  static  boolean refreshManager=false;//当为true时才检测直播间用户列表是否有管理员
    public static  String creatorAccid="";//创建直播的云信id；
    public  static  String liveTitle="";
    public  static List<LiveManagerListBean> result=new ArrayList<>();

    public  static boolean enterRoom=false;

    //网络类型检测
    public static  int NET_CHECK=0;

    //直播清晰度
    public static  int LIVE_DEFINITION=0;

    //充值
    public static  boolean IS_FIRST_CLICK_PAGE=true;

    //用户信息dialog
    public static  boolean USER_INFO_FIRST_CLICK=true;
    //视频方向
    public  static int DEFAULT_DIRECTION=1;


    //礼物

    public static final String WATER="009";
    public static final String FRUIT="008";
    public static final String STAR="007";
    public static final String UMBRELLA="006";
    public static final String GLASSES="005";
    public static final String FACE="004";
    public static final String PLAN="003";
    public static final String TRAVELLINGCASE="002";
    public static final String ROSE="001";




}