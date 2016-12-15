package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by wangjie on 2016/9/14.
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int mLayoutId;
    private int mMaxSize;

    private static final String TAG = "CommonAdapter";

    public CommonAdapter(Context context, List<T> datas, int layoutId) {
        this(context, datas, layoutId, Integer.MAX_VALUE);
    }

    public CommonAdapter(Context context, List<T> datas, int layoutId, int maxSize) {
        Log.d(TAG, "the context is " + context.toString());
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
        mInflater = LayoutInflater.from(context);
        this.mMaxSize = maxSize;
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : Math.min(mDatas.size(), mMaxSize);
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder holder = CommonViewHolder.get(mContext, convertView, parent, mLayoutId, position);

        convert(holder, getItem(position));

        return holder.getConvertView();
    }

    public abstract void convert(CommonViewHolder holder, T t);
}
