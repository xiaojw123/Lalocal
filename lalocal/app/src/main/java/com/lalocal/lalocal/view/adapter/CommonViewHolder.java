package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 通用适配器使用的viewholder
 * Created by wangjie on 2016/9/14.
 */
public class CommonViewHolder {

    private SparseArray<View> mViews;
    private int mPosition;
    private View mConvertView;

    public CommonViewHolder(Context context, ViewGroup parent, int layoutId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<>();

        mConvertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static CommonViewHolder get(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new CommonViewHolder(context, parent, layoutId, position);
        } else {
            CommonViewHolder holder = (CommonViewHolder) convertView.getTag();
            holder.mPosition = position;
            return holder;
        }
    }

    /**

     * 通过viewId获取控件

     * @param viewId

     * @param <T>

     * @return

     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);

        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    /**

     * 设置TextView的值

     * @param viewId

     * @param text

     * @return

     */
    public CommonViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置文本颜色
     * @param viewId
     * @param color
     * @return
     */
    public CommonViewHolder setTextColor(int viewId, int color) {
        TextView tv = getView(viewId);
        tv.setTextColor(color);
        return this;
    }

    /**

     * 设置ImageView的图片资源

     * @param viewId

     * @param resId

     * @return

     */
    public CommonViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    /**

     * 设置ImageView的图片

     * @param viewId

     * @param bitmap

     * @return

     */
    public CommonViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    /**

     * 通过uri设置ImageView的图片

     * @param viewId

     * @param url

     * @return

     */
    public CommonViewHolder setImageUrl(int viewId, String url) {
        ImageView view = getView(viewId);
//        ImageLoader.getInstance().loadImg(view, url);


        return this;
    }

    public int getPosition() {
        return mPosition;
    }
}
