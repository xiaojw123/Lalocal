package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;

/**
 * Created by xiaojw on 2016/8/1.
 */
public class DayRouteItemAdpater extends BaseRecyclerAdapter {
    public static final int TICKET = 0x11;
    public static final int OPEN_TIME = 0x12;
    public static final int DURATION = 0x13;
    public static final int INTRODUCTION = 0x14;
    public static final int TRAFFIC = 0x15;
    public static final int ADDRESS = 0x16;
    SparseArray<String> mItems;
    Context mContext;
    Resources mRes;

    public DayRouteItemAdpater(Context context, SparseArray<String> items) {
        mItems = items;
        mContext = context;
        mRes = mContext.getResources();
    }

    public void updateItems(SparseArray<String> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        TextView view = new TextView(mContext);
        view.setTextColor(mRes.getColor(R.color.color_66));
        view.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRes.getDimension(R.dimen.text_size_14_sp));
        view.setLineSpacing(3, 1.2f);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) mRes.getDimension(R.dimen.dimen_size_20_dp);
        params.bottomMargin= (int) mRes.getDimension(R.dimen.dimen_size_20_dp);
        view.setLayoutParams(params);
        return new DayRouteItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mItems != null && mItems.size() > 0) {
            TextView title = (TextView) holder.itemView;
            int key = mItems.keyAt(position);
            int resId = -1;
            String attr = "";
            switch (key) {
                case TICKET:
                    resId = R.drawable.index_article_tipes_icon_ticket;
                    attr += "门票:";
                    break;
                case OPEN_TIME:
                    resId = R.drawable.index_article_tipes_icon_open;
                    attr += "开放时间:";
                    break;
                case DURATION:
                    resId = R.drawable.index_article_tipes_icon_time;
                    attr += "游戏时长:";
                    break;
                case INTRODUCTION:
                    resId = R.drawable.index_article_tipes_icon_introduce;
                    attr += "景点介绍:";
                    break;
                case TRAFFIC:
                    resId = R.drawable.index_article_tipes_icon_traffic;
                    attr += "交通:";
                    break;
                case ADDRESS:
                    resId = R.drawable.index_article_tipes_icon_position;
                    attr += "地址:";
                    break;
            }
            if (resId != -1) {
                Drawable drawable = mRes.getDrawable(resId);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                title.setCompoundDrawables(drawable, null, null, null);
                title.setCompoundDrawablePadding((int) mRes.getDimension(R.dimen.day_item_route_detail_drawable_pad));
                String value = mItems.valueAt(position);
                attr += "    " + value;
                title.setText(attr);
            }


        }


    }


    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    class DayRouteItemHolder extends RecyclerView.ViewHolder {


        public DayRouteItemHolder(View itemView) {
            super(itemView);
        }
    }


}
