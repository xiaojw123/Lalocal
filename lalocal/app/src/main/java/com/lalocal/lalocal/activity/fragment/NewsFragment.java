package com.lalocal.lalocal.activity.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.TouristInfoResp;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.LiveMainListAdapter;
import com.lalocal.lalocal.view.liveroomview.DemoCache;
import com.lalocal.lalocal.view.liveroomview.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.view.liveroomview.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.view.liveroomview.im.config.AuthPreferences;
import com.lalocal.lalocal.view.liveroomview.im.ui.dialog.EasyAlertDialogHelper;
import com.lalocal.lalocal.view.liveroomview.permission.MPermission;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionGranted;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.umeng.analytics.MobclickAgent;

import net.robinx.lib.blur.widget.BlurMaskRelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class NewsFragment extends Fragment implements  View.OnClickListener{
    private static final  String PAGE_NAME="NewsFragment";
    private CustomTitleView liveTitleBack;
    private ContentLoader contentService;
    private ListView liveRecyclearView;


    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private ImageView layoutBg;
    private RelativeLayout bgBlur;
    private BlurMaskRelativeLayout blurMaskRelativeLayout;
    private int roomId=0;
    public static final int RESQUEST_CODE=701;
    private LiveMainListAdapter liveMainListAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MyCallBack());
        contentService.liveList(10,1);
        contentService.liveRecommendList();

       requestBasicPermission(); // 申请APP基本权限

    }
    boolean isHiddenChanged=true;
    @Override
    public void onHiddenChanged(boolean hidden) {//切换fragment刷新fragment
        super.onHiddenChanged(hidden);
        AppLog.i("TAG","onHiddenChanged:"+(hidden==true?"ture":"false"));
        if(!hidden){
            isFirstLoad=true;

            allRows.clear();
            contentService.liveList(10,1);
            if (!DemoCache.getLoginStatus()){
                String imccId = AuthPreferences.getUserAccount();
                String imToken = AuthPreferences.getUserToken();
                AppLog.i("TAG","onHiddenChanged:imccId"+imccId+"imToken:"+imToken);
                if(imccId==null||imToken==null){
                    contentService.getTouristInfo();
                }else {
                    loginIMServer(imccId,imToken);
                }
            }
        }

    }
    boolean isFirstInit=true;
    private void loginIMServer(final  String imccId, String imToken) {
        NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                AppLog.i("TAG", "NewsFragment,手动登录成功");
                DemoCache.setAccount(imccId);
                DemoCache.getRegUserInfo();
                DemoCache.setLoginStatus(true);
                if(isFirstInit){
                    isFirstInit=false;

                }

            }

            @Override
            public void onFailed(int i) {
                AppLog.i("TAG", "NewsFragment,手动登录失败" + i);
                DemoCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {
                AppLog.i("TAG", "NewsFragment,手动登录失败2");
                DemoCache.setLoginStatus(false);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AppLog.i("TAG","走了那个晚上发货速度加粉丝疯狂活动");
        if(requestCode==RESQUEST_CODE&&(resultCode==101||resultCode==105)){
            if(data!=null){
                String email = data.getStringExtra(LoginActivity.EMAIL);
                String psw = data.getStringExtra(LoginActivity.PSW);
                contentService.login(email, psw);
                claerImLoginInfo();//清除im登錄信息
                AppLog.i("TAG","NewsFragment：走了登錄方法");
            }else{

                AppLog.i("TAG","NewsFragment：没走登錄方法");
            }
        }else if(requestCode==RESQUEST_CODE&&resultCode==702){
            AppLog.i("TAG","拿到数据。。。702");

        }else if(requestCode==RESQUEST_CODE&&resultCode==703){
            AppLog.i("TAG","拿到数据。。。703");
        }


    }

    private void claerImLoginInfo() {
        DemoCache.clear();;
       AuthPreferences.clearUserInfo();
        NIMClient.getService(AuthService.class).logout();
        DemoCache.setLoginStatus(false);
    }

    // 权限控制

    private void requestBasicPermission() {
        MPermission.with(getActivity())
                .addRequestCode(BASIC_PERMISSION_REQUEST_CODE)
                .permissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)

                .request();
    }

    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {

            AppLog.i("TAG","newsfragment監聽用戶登錄狀態："+statusCode);
            if (statusCode.wontAutoLogin()) {

                AppLog.i("TAG","NewsFragment:密码错误等");
            }else if(statusCode.shouldReLogin()) {
                AppLog.i("TAG", "NewsFragment:没有登录  roomId:"+roomId);

                    final String imccId = AuthPreferences.getUserAccount();
                    String imToken = AuthPreferences.getUserToken();
                    if (imccId != null || imToken != null) {
                        AppLog.i("TAG","没有登录并走了这里1："+"imccId:"+imccId+"   imToken:"+imToken);
                        loginIMServer(imccId,imToken);

                    }else if(roomId==0){
                        roomId=roomId+1;
                        contentService.getTouristInfo();

                    }

            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.home_news_layout,container,false);
        liveTitleBack = (CustomTitleView) view.findViewById(R.id.live_title_back_ctv);
        ImageView createLiveRoom= (ImageView) view.findViewById(R.id.live_create_room);
        layoutBg = (ImageView) view.findViewById(R.id.home_news_layout_bg);
        bgBlur = (RelativeLayout) view.findViewById(R.id.home_news_bg_blur);

        createLiveRoom.setOnClickListener(this);
        liveTitleBack.setOnClickListener(this);
        liveTitleBack.setTitle("世界直播间");
        liveRecyclearView = (ListView) view.findViewById(R.id.live_recy_list);
        return view;
    }
    int userId=-1;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.live_create_room:
                userId = UserHelper.getUserId(getActivity());
                if(userId!=-1){
                    contentService.createLiveRoom();//直播接口
                }else {

                    EasyAlertDialogHelper.createOkCancelDiolag(getActivity(), null,"没登录，快去登录吧",
                            "确定", getString(R.string.cancel), true,
                            new EasyAlertDialogHelper.OnDialogActionListener(){
                                @Override
                                public void doCancelAction() {
                                }
                                @Override
                                public void doOkAction() {
                                    //TODO 去登录页面

                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivityForResult(intent, RESQUEST_CODE);
                                }
                            }).show();
                }

                break;
        }
    }



    private  List<LiveRowsBean> allRows=new ArrayList<LiveRowsBean>();
    private  boolean isFirstLoad=true;
    public class MyCallBack extends ICallBack{
        @Override
        public void onLiveList(LiveListDataResp liveListDataResp) {
            super.onLiveList(liveListDataResp);
            List<LiveRowsBean> rows = liveListDataResp.getResult().getRows();
            if(rows.size()>0&&isFirstLoad){
                DrawableUtils.displayImg(getActivity(), layoutBg, rows.get(0).getPhoto());
                bgBlur.setBackgroundColor(Color.parseColor("#dc8E8E8E"));
                allRows.addAll(0,rows);
                Collections.sort(allRows);//排序
                initRecyclerView(allRows);//获取正在直播列表
                isFirstLoad=false;
            }else if(rows.size()>0&&!isFirstLoad){
                allRows.addAll(allRows.size(),rows);
                Collections.sort(allRows);

                liveMainListAdapter.refresh(allRows);
            }

        }

        @Override
        public void onLiveRecommendList(LiveRecommendListDataResp liveRecommendListDataResp) {
            super.onLiveRecommendList(liveRecommendListDataResp);
           /* List<LiveRowsBean> recommendRows = liveRecommendListDataResp.getResult();

            if(recommendRows.size()>0){
                initRecyclerView(recommendRows);//获取正在直播列表
            }*/
        }

        @Override
        public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onCreateLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                String mliveStreamingURL = createLiveRoomDataResp.getResult().getPushUrl();
                String pullUrl = createLiveRoomDataResp.getResult().getPullUrl();
                int roomId = createLiveRoomDataResp.getResult().getRoomId();
                Object annoucement = createLiveRoomDataResp.getResult().getAnnoucement();
                String ann=null;
                if (annoucement!=null){
                    ann=annoucement.toString();
                }else {
                    ann="这是公告";
                }
                AppLog.i("TAG","onCreateLiveRoom:"+"pullUrl:"+pullUrl);
                //初始化直播间
                int userId = createLiveRoomDataResp.getResult().getId();
                int liveUserId = createLiveRoomDataResp.getResult().getUser().getId();
                String avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                SpecialShareVOBean shareVO = createLiveRoomDataResp.getResult().getShareVO();
                LiveActivity.start(getActivity(),String.valueOf(roomId) , mliveStreamingURL,String.valueOf(userId),avatar,String.valueOf(liveUserId),shareVO,ann);

        }


        }

        @Override
        public void onTouristInfo(String json) {
            super.onTouristInfo(json);
            AppLog.i("TAG","onTouristInfo:"+json);
            TouristInfoResp touristInfoResp = new Gson().fromJson(json, TouristInfoResp.class);
            if(touristInfoResp.getReturnCode()==0){
                TouristInfoResp.ResultBean result = touristInfoResp.getResult();
              final  String accid = result.getAccid();
                final String token = result.getToken();

                if(accid!=null||token!=null){
                    DemoCache.clear();
                    AuthPreferences.saveUserAccount(accid);
                    AuthPreferences.saveUserToken(token);
                    AppLog.i("TAG","走了tourist");
                    loginIMServer(accid,token);

                }

            }
        }

        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
            loginIMServer(user.getImUserInfo().getAccId(),user.getImUserInfo().getToken());

        }

        @Override
        public void onRequestFailed(VolleyError volleyError) {
            super.onRequestFailed(volleyError);
        }

    }

    private int pageCount=2;
    private boolean isLoading=false;
    private int pageSize=9;

    private void initRecyclerView(final List<LiveRowsBean> rows) {
        if(getActivity()!=null) {

            liveMainListAdapter = new LiveMainListAdapter(getActivity(), rows);
            liveRecyclearView.setAdapter(liveMainListAdapter);
            liveRecyclearView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    String imccId = AuthPreferences.getUserAccount();
                    String imToken=AuthPreferences.getUserToken();
                    AppLog.i("TAG","setOnItemClickListener:"+"imccId:"+imccId+"   imToken:"+imToken);
                    LiveRowsBean liveRowsBean = allRows.get(position);

                    roomId = liveRowsBean.getRoomId();
                    String avatar = liveRowsBean.getUser().getAvatar();
                    String nickName = liveRowsBean.getUser().getNickName();
                    String pullUrl = liveRowsBean.getPullUrl();
                    Object annoucement = liveRowsBean.getAnnoucement();
                    int userId  =liveRowsBean.getUser().getId();
                    int type = liveRowsBean.getType();
                    String ann=null;
                    if (annoucement!=null){
                        ann=annoucement.toString();
                    }else {
                        ann="这是公告";
                    }
                    AppLog.i("TAG","MyOnLiveItemClickListener:"+avatar+"///"+nickName+"pullUrl:"+pullUrl);
                    SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
                    String s = new Gson().toJson(shareVO);
                    AppLog.i("TAG","setOnItemClickListener:shareVO:"+s);

                    AudienceActivity.start(getActivity(), String.valueOf(roomId),pullUrl,avatar,nickName, String.valueOf(userId),shareVO,String.valueOf(type),ann);
                }

            });

            liveRecyclearView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int firstVisiblePosition = liveRecyclearView.getFirstVisiblePosition();
                    int lastVisiblePosition = liveRecyclearView.getLastVisiblePosition();

                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                        if (allRows.size()>0&&allRows.size() >= firstVisiblePosition) {
                            String photo = allRows.get(firstVisiblePosition).getPhoto();
                            DrawableUtils.displayImg(getActivity(), layoutBg, photo);
                        }
                    }
                    if (lastVisiblePosition < allRows.size() && lastVisiblePosition > pageSize) {
                        contentService.liveList(10, pageCount);
                        pageSize = pageSize + 10;
                        pageCount = pageCount + 1;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }


    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        //     Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        //  Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册监听
        registerObservers(true);
        AppLog.i("TAG","onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);

        AppLog.i("TAG","onResume");
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);
        AppLog.i("TAG","onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        registerObservers(false);
        AppLog.i("TAG","onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.i("TAG","onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i("TAG","onDestroy");

    }
}
