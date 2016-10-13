package com.lalocal.lalocal.activity.fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.LiveSearchActivity;
import com.lalocal.lalocal.activity.LoginActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.entertainment.activity.LiveActivity;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.im.config.AuthPreferences;
import com.lalocal.lalocal.live.permission.MPermission;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionDenied;
import com.lalocal.lalocal.live.permission.annotation.OnMPermissionGranted;
import com.lalocal.lalocal.model.CreateLiveRoomDataResp;
import com.lalocal.lalocal.model.LiveListDataResp;
import com.lalocal.lalocal.model.LiveRecommendListDataResp;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.model.User;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.SPCUtils;
import com.lalocal.lalocal.view.adapter.LiveMainListAdapter;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.auth.LoginInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xiaojw on 2016/6/3.
 */
public class NewsFragment extends BaseFragment implements View.OnClickListener {

    private final int BASIC_PERMISSION_REQUEST_CODE = 100;
    public static final int RESQUEST_COD = 701;
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    public static final String CREATE_ROOMID = "createRoomId";

    private ContentLoader contentService;
    private ListView liveRecyclearView;
//    private BlurImageView layoutBg;
    private LiveMainListAdapter liveMainListAdapter;
    private List<LiveRowsBean> allRows = new ArrayList<LiveRowsBean>();
    private boolean isFirstLoad = true;//刷新列表
    boolean closeRegister = true;
    private int roomId = 0;
    Handler handler = new Handler();
    private int createRoomId;
    private String mliveStreamingURL;
    private String pullUrl;
    private int userCreateId;
    private String createAvatar;
    private SpecialShareVOBean shareVOCreate;
    private FrameLayout liveSeachFl;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contentService = new ContentLoader(getActivity());
        contentService.setCallBack(new MyCallBack());
        requestBasicPermission(); // 申请APP基本权限


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_news_layout, container, false);
        LinearLayout createLiveRoom = (LinearLayout) view.findViewById(R.id.live_create_room);
        createLiveRoom.setOnClickListener(this);
        liveRecyclearView = (ListView) view.findViewById(R.id.live_recy_list);
        View inflate = View.inflate(getActivity(), R.layout.listview_footerview, null);
        liveRecyclearView.addHeaderView(inflate);
        liveRecyclearView.addFooterView(inflate);
        //TODO:直播搜索 add by xiaojw
        TextView liveSearchTv = (TextView) view.findViewById(R.id.live_search_textview);
        liveSearchTv.setCompoundDrawables(getTextColorDrawable(liveSearchTv), null, null, null);
        liveSeachFl = (FrameLayout) view.findViewById(R.id.live_search_fl);
        liveSeachFl.setOnClickListener(this);


    /*    XRecyclerView xRecyclerView= (XRecyclerView) view.findViewById(R.id.xrecyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
       */


        return view;
    }

    @NonNull
    private Drawable getTextColorDrawable(TextView liveSearchTv) {
        Drawable drawable = getResources().getDrawable(R.drawable.searchbar_searchicon);
        Drawable colorDrawable = DrawableUtils.tintDrawable(drawable, liveSearchTv.getTextColors());
        colorDrawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return colorDrawable;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESQUEST_COD && (resultCode == 101 || resultCode == 105)) {
            if (data != null) {
                isLogining = true;
                String email = data.getStringExtra(LoginActivity.EMAIL);
                String psw = data.getStringExtra(LoginActivity.PSW);
                contentService.login(email, psw);
            }
        }
    }

   boolean firstLoadData=true;
    @Override
    public void onHiddenChanged(boolean hidden) {//切换fragment刷新fragment
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isFirstLoad = true;
            allRows.clear();
            contentService.liveList(10, 1);
        }
    }


    //监听IM账号登录状态
    private void registerObservers(boolean register) {
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(userStatusObserver, register);
    }

    Observer<StatusCode> userStatusObserver = new Observer<StatusCode>() {
        @Override
        public void onEvent(StatusCode statusCode) {
            AppLog.i("TAG", "newsfragment監聽用戶登錄狀態：" + statusCode);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.live_create_room:
                if (Build.VERSION.SDK_INT >= 23) {
                    reminderUserPermission();//创建直播间，判断权限
                } else {
                    prepareLive();
                }
                break;
            case R.id.live_search_fl:
                Intent intent = new Intent(getActivity(), LiveSearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    private int liveUserId;
    private String createNickName;
    String createAnn = null;

    public class MyCallBack extends ICallBack {

        @Override
        public void onLiveList(LiveListDataResp liveListDataResp) {
            super.onLiveList(liveListDataResp);
            firstLoadData=true;
            List<LiveRowsBean> rows = liveListDataResp.getResult().getRows();

            if (rows.size() > 0 && isFirstLoad) {
                allRows.addAll(0, rows);
                Collections.sort(allRows);//排序
                initRecyclerView(allRows);//获取正在直播列表
                isFirstLoad = false;

            } else if (rows.size() > 0 && !isFirstLoad) {
                if (rows.size() == 10) {
                    pageSize = pageSize + rows.size();
                    pageCount = pageCount + 1;
                }

                allRows.addAll(allRows.size(), rows);
                Collections.sort(allRows);
                liveMainListAdapter.refresh(allRows);

            }


        }

        @Override
        public void onLiveRecommendList(LiveRecommendListDataResp liveRecommendListDataResp) {
            super.onLiveRecommendList(liveRecommendListDataResp);

        }

        String reminfBack = "0";

        @Override
        public void onCreateLiveRoom(CreateLiveRoomDataResp createLiveRoomDataResp) {
            super.onCreateLiveRoom(createLiveRoomDataResp);
            if (createLiveRoomDataResp.getReturnCode() == 0) {
                LiveRowsBean result = createLiveRoomDataResp.getResult();
                createRoomId= result.getRoomId();
                SPCUtils.put(getActivity(), CREATE_ROOMID, String.valueOf(createRoomId));
                Object annoucement = createLiveRoomDataResp.getResult().getAnnoucement();
                if (annoucement != null) {
                    createAnn = annoucement.toString();
                } else {
                    createAnn = "这是公告";
                }
                //初始化直播间
                LiveActivity.start(getActivity(),result,createAnn,reminfBack);
            }


        }


        @Override
        public void onLoginSucess(User user) {
            super.onLoginSucess(user);
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
        }

    }

    private int pageCount = 2;
    private boolean isLoading = false;
    private int pageSize = 9;

    private void initRecyclerView(final List<LiveRowsBean> rows) {
        if (getActivity() != null) {
            liveMainListAdapter = new LiveMainListAdapter(getActivity(), rows);
            liveRecyclearView.setAdapter(liveMainListAdapter);
            liveRecyclearView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(position<1){
                        return;
                    }
                    LiveRowsBean liveRowsBean = rows.get(position - 1);
                    roomId = liveRowsBean.getRoomId();
                    String createRoom = SPCUtils.getString(getActivity(), CREATE_ROOMID);
                    String s = String.valueOf(roomId);
                    if (createRoom != null && createRoom.equals(s)) {
                        CommonUtil.REMIND_BACK = 1;
                        SPCUtils.put(getActivity(), CREATE_ROOMID, "fdfdad");
                        prepareLive();
                        return;
                    }
                    Object annoucement = liveRowsBean.getAnnoucement();
                    String ann = null;
                    if (annoucement != null) {
                        ann = annoucement.toString();
                    } else {
                        ann = "这是公告哈";
                    }
                    SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
                    AudienceActivity.start(getActivity(),liveRowsBean,ann);
                }

            });


        }
    }

    boolean isLogining = false;

    private void prepareLive() {
        boolean isLogin = UserHelper.isLogined(getActivity());
        boolean loginStatus = DemoCache.getLoginStatus();
        if (isLogin && loginStatus) {
            contentService.createLiveRoom();//直播接口
        } else if (isLogin && !loginStatus) {
            String imccId = UserHelper.getImccId(getActivity());
            String imToken = UserHelper.getImToken(getActivity());
            if (imccId != null && imToken != null) {
                loginIMServer(imccId, imToken);
            }

        } else {
            CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent(getString(R.string.live_login_hint));
            customDialog.setCancelable(false);
            customDialog.setCancelable(false);
            customDialog.setCancelBtn(getString(R.string.live_canncel), null);
            customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivityForResult(intent, RESQUEST_COD);
                }
            });
            customDialog.show();
        }
    }

    private void loginIMServer(final String imccId, final String imToken) {
        NIMClient.getService(AuthService.class).login(new LoginInfo(imccId, imToken)).setCallback(new RequestCallback() {
            @Override
            public void onSuccess(Object o) {
                prepareLive();
                DemoCache.setAccount(imccId);
                DemoCache.getRegUserInfo();
                DemoCache.setLoginStatus(true);
            }

            @Override
            public void onFailed(int i) {

                DemoCache.setLoginStatus(false);
            }

            @Override
            public void onException(Throwable throwable) {

                DemoCache.setLoginStatus(false);
            }
        });
    }


    private void claerImLoginInfo() {
        DemoCache.clear();
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

    // 权限控制
    private final int LIVE_PERMISSION_REQUEST_CODE = 100;
    private static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
    //开启摄像头权限

    @TargetApi(Build.VERSION_CODES.M)
    private void reminderUserPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(LIVE_PERMISSIONS, MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            prepareLive();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        doNext(requestCode, grantResults);
    }

    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                prepareLive();
            } else {
                Toast.makeText(getActivity(), "没有视频权限，无法开启直播", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        //    Toast.makeText(getActivity(), "授权成功", Toast.LENGTH_SHORT).show();
    }

    @OnMPermissionDenied(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionFailed() {
        //  Toast.makeText(getActivity(), "授权失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        //注册监听
        registerObservers(true);
        if (allRows != null) {
            allRows.clear();
        }
        if(firstLoadData){
            firstLoadData=false;
            contentService.liveList(10, 1);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                allRows.clear();
                if(firstLoadData){
                    firstLoadData=false;
                    contentService.liveList(10, 1);
                }

            }
        }, 1000 * 60 * 5);
        AppLog.i("TAG", "onStart");

    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonUtil.RESULT_DIALOG == 2) {
            CommonUtil.RESULT_DIALOG = 0;
            final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent("摄像头启动失败,请尝试在手机应用权限管理中打开权限");
            customDialog.setCancelable(false);
            customDialog.setOkBtn("确定", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        } else if (CommonUtil.RESULT_DIALOG == 3) {
            CommonUtil.RESULT_DIALOG = 0;
            final CustomChatDialog customDialog = new CustomChatDialog(getActivity());
            customDialog.setContent("音频权限开启失败,请尝试在手机应用权限管理中打开权限");
            customDialog.setCancelable(false);
            customDialog.setOkBtn("确定", new CustomChatDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    customDialog.dismiss();
                }
            });
            customDialog.show();
        }
        AppLog.i("TAG", "onResume");
    }


    @Override
    public void onStop() {
        super.onStop();
        registerObservers(false);
        AppLog.i("TAG", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        AppLog.i("TAG", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        AppLog.i("TAG", "onDestroy");

    }
}
