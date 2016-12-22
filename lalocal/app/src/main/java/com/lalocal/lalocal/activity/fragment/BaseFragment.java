package com.lalocal.lalocal.activity.fragment;

import android.app.Fragment;

import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;

/**
 * Created by xiaojw on 2016/9/29.
 */

public class BaseFragment extends Fragment {
  public   ContentLoader mContentloader;
    public void setLoaderCallBack(ICallBack callBack) {
        if (mContentloader == null) {
            mContentloader = new ContentLoader(getActivity());
        }
        mContentloader.setCallBack(callBack);
    }




}
