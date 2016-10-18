package com.lalocal.lalocal.activity.fragment;

import android.app.Fragment;

import com.lalocal.lalocal.MyApplication;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by xiaojw on 2016/9/29.
 */

public class BaseFragment extends Fragment {
    ContentLoader mContentloader;
    public void setLoaderCallBack(ICallBack callBack) {
        if (mContentloader == null) {
            mContentloader = new ContentLoader(getActivity());
        }
        mContentloader.setCallBack(callBack);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (!MyApplication.isDebug) {
            MobclickAgent.onPageStart(getClass().getName());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!MyApplication.isDebug) {
            MobclickAgent.onPageEnd(getClass().getName());
        }
    }


}
