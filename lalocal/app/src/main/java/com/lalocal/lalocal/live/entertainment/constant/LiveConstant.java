package com.lalocal.lalocal.live.entertainment.constant;


import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by android on 2016/9/4.
 */
public class LiveConstant {
    public  static  final  int ME_CHECK_OTHER=6;//我看别人
    public static  int IDENTITY=0;
    public  static  int ROLE=-1;
    public  static String  ROOM_ID="";
    public  static  boolean isUnDestory=false;
    public  static  int newMessageCount=0;
    public static  String creatorAccid="";//创建直播的云信id；
    public  static  String liveTitle="";
    public static  String liveLocation="";
    public static boolean isAttention=false;
    public  static List<LiveManagerListBean> result=new ArrayList<>();
    public static int level=0;
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







}