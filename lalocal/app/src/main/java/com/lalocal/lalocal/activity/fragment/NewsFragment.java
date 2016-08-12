package com.lalocal.lalocal.activity.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.LiveMainAdapter;
import com.lalocal.lalocal.view.liveroomview.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.view.liveroomview.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.view.liveroomview.im.ui.dialog.EasyAlertDialogHelper;
import com.lalocal.lalocal.view.liveroomview.permission.MPermission;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.view.liveroomview.permission.annotation.OnMPermissionGranted;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
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
    private RecyclerView liveRecyclearView;
    private LinearLayoutManager linearLayoutManager;
    private LiveMainAdapter liveMainAdapter;
    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    private ImageView layoutBg;
    private RelativeLayout bgBlur;
    private BlurMaskRelativeLayout blurMaskRelativeLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MyCallBack());
        contentService.liveList(10,1);
        contentService.liveRecommendList();
        //注册监听
       registerObservers(true);
       requestBasicPermission(); // 申请APP基本权限

    }
    boolean isHiddenChanged=true;
    @Override
    public void onHiddenChanged(boolean hidden) {//切换fragment刷新fragment
        super.onHiddenChanged(hidden);
        AppLog.i("TAG","onHiddenChanged:"+(hidden==true?"ture":"false"));
        isFirstLoad=true;
        allRows.clear();
        contentService.liveList(10,1);
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
            if (statusCode.wontAutoLogin()) {
                AppLog.i("TAG","NewsFragment:密码错误等");
              //  LogoutHelper.logout(getActivity(), true);
            }else if(statusCode.shouldReLogin()){
                AppLog.i("TAG","NewsFragment:没有登录");
            }
        }
    };
    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess(){
        Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed(){
        Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
    }

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
        liveRecyclearView = (RecyclerView) view.findViewById(R.id.live_recy_list);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        liveRecyclearView.setLayoutManager(linearLayoutManager);
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
                            getString(R.string.confirm), getString(R.string.cancel), true,
                            new EasyAlertDialogHelper.OnDialogActionListener(){
                                @Override
                                public void doCancelAction() {
                                }
                                @Override
                                public void doOkAction() {
                                    //TODO 去登录页面
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                }
                            }).show();
                }

                break;
        }
    }


    public class  MyOnLiveItemClickListener implements LiveMainAdapter.OnLiveItemClickListener{
        @Override
        public void goLiveRoom(LiveRowsBean liveRowsBean) {
            int roomId = liveRowsBean.getRoomId();
            String avatar = liveRowsBean.getUser().getAvatar();
            String nickName = liveRowsBean.getUser().getNickName();
            String pullUrl = liveRowsBean.getPullUrl();
            int userId  =liveRowsBean.getUser().getId();
            int type = liveRowsBean.getType();
            AppLog.i("TAG","MyOnLiveItemClickListener:"+avatar+"///"+nickName+"pullUrl:"+pullUrl);
            SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
            AudienceActivity.start(getActivity(), String.valueOf(roomId),pullUrl,avatar,nickName, String.valueOf(userId),shareVO,String.valueOf(type));
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
                allRows.addAll(0,rows);
                Collections.sort(allRows);//排序
                initRecyclerView(allRows);//获取正在直播列表
                isFirstLoad=false;
            }else if(rows.size()>0&&!isFirstLoad){
                allRows.addAll(allRows.size(),rows);
                Collections.sort(allRows);
                liveMainAdapter.refresh(allRows);
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
                //初始化直播间
                int userId = createLiveRoomDataResp.getResult().getId();
                int liveUserId = createLiveRoomDataResp.getResult().getUser().getId();
                String avatar = createLiveRoomDataResp.getResult().getUser().getAvatar();
                SpecialShareVOBean shareVO = createLiveRoomDataResp.getResult().getShareVO();

                LiveActivity.start(getActivity(),String.valueOf(roomId) , mliveStreamingURL,String.valueOf(userId),avatar,String.valueOf(liveUserId),shareVO);

        }


        }

        @Override
        public void onRequestFailed() {
            super.onRequestFailed();
        }
    }
    private int pageCount=2;
    private boolean isLoading=false;
    private int pageSize=9;
    private void initRecyclerView(final List<LiveRowsBean> rows) {
        liveMainAdapter = new LiveMainAdapter(getActivity(),rows);
        liveMainAdapter.setOnLiveItemClickListener(new MyOnLiveItemClickListener());
        liveRecyclearView.setAdapter(liveMainAdapter);
        liveRecyclearView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                String photo = rows.get(firstVisibleItemPosition).getPhoto();
               layoutBg.clearColorFilter();
                DrawableUtils.displayImg(getActivity(),layoutBg,photo);
                bgBlur.setBackgroundColor(Color.parseColor("#dc8E8E8E"));
/*            bgBlur.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        bgBlur.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        BlurDrawable blurDrawable = new BlurDrawable(getActivity());
                        blurDrawable.setDrawOffset(bgBlur.getLeft(), bgBlur.getTop() + BlurUtils.getStatusBarHeight(getActivity()));
                        blurDrawable.setCornerRadius(10);
                        blurDrawable.setBlurRadius(15);
                        blurDrawable.setOverlayColor(Color.parseColor("#918E8E8E"));
                        bgBlur.setBackgroundDrawable(blurDrawable);
                    }
                });*/

                if(lastVisibleItemPosition<rows.size()&&lastVisibleItemPosition>pageSize){
                   contentService.liveList(10,pageCount);
                   pageSize=pageSize+10;
                   pageCount=pageCount+1;
               }
            }
        });
    }



    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(PAGE_NAME);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(PAGE_NAME);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

       registerObservers(false);
    }
}
