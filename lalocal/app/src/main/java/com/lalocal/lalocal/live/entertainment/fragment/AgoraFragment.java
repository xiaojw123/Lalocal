package com.lalocal.lalocal.live.entertainment.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.BaseFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.entertainment.agora.openlive.AGEventHandler;
import com.lalocal.lalocal.live.entertainment.agora.openlive.EngineConfig;
import com.lalocal.lalocal.live.entertainment.agora.openlive.MyEngineEventHandler;
import com.lalocal.lalocal.live.entertainment.agora.openlive.WorkerThread;
import com.lalocal.lalocal.util.AppLog;

import io.agora.rtc.RtcEngine;
/**
 * Created by ${WCJ} on 2017/1/6.
 */
public abstract class AgoraFragment extends BaseFragment implements AGEventHandler {
    public String cname;
    RelativeLayout playerLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        playerLayout = (RelativeLayout) inflater.inflate(R.layout.agora_player_layout, container, false);
        initData();
        ((MyApplication) (getActivity().getApplication())).initWorkerThread();
        initUIandEvent();
        ViewTreeObserver vto = playerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                playerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

            }
        });

        return playerLayout;
    }


    protected abstract void initUIandEvent();
    protected abstract void deInitUIandEvent();

    private void initData() {
        Bundle bundle = getArguments();
        if(bundle!=null){
            cname = bundle.getString(KeyParams.CNAME);
            AppLog.i("TAG","用户端cname:"+cname);
        }
    }

    protected RtcEngine rtcEngine() {
        return ((MyApplication) (getActivity().getApplication())).getWorkerThread().getRtcEngine();
    }
    protected final WorkerThread worker() {
        return ((MyApplication) (getActivity().getApplication())).getWorkerThread();
    }

    protected final EngineConfig config() {
        return ((MyApplication) (getActivity().getApplication())).getWorkerThread().getEngineConfig();
    }

    protected final MyEngineEventHandler event() {
        return ((MyApplication) (getActivity().getApplication())).getWorkerThread().eventHandler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        deInitUIandEvent();
    }
}
