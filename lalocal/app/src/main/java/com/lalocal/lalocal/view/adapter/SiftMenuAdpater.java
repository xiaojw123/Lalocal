package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.SiftModle;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/22.
 */
public class SiftMenuAdpater extends BaseRecyclerAdapter {
    Context mContext;
    List<SiftModle> mItems;
    Resources res;

    public SiftMenuAdpater(Context context, List<SiftModle> items) {
        mContext = context;
        mItems = items;
        res = mContext.getResources();
    }

    public void updateItems(List<SiftModle> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout contianer = new LinearLayout(mContext);
        contianer.setOrientation(LinearLayout.VERTICAL);
        contianer.setGravity(Gravity.CENTER_HORIZONTAL);
        contianer.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ImageView menuImg = new ImageView(mContext);
        menuImg.setLayoutParams(new LinearLayout.LayoutParams((int) res.getDimension(R.dimen.sift_menu_img_width), (int) res.getDimension(R.dimen.sift_menu_img_width)));
        menuImg.setScaleType(ImageView.ScaleType.FIT_XY);
        TextView name = new TextView(mContext);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_12_sp));
        name.setTextColor(res.getColor(R.color.white));
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = (int) res.getDimension(R.dimen.dimen_size_5_dp);
        name.setLayoutParams(params);
        contianer.addView(menuImg);
        contianer.addView(name);
        return new ViewHolder(contianer);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mItems != null && mItems.size() > 0) {
            final SiftModle item = mItems.get(position);
            if (item != null) {
                DrawableUtils.displayImg(mContext, ((ViewHolder) holder).img, item.getPhoto(),R.drawable.mdd_loading_img);
                ((ViewHolder) holder).title.setText(item.getName());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener!=null){
                            v.setTag(item);
                            listener.onItemClickListener(v,position);
                        }

                    }
                });
            }


        }


    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) ((LinearLayout) itemView).getChildAt(0);
            title = (TextView) ((LinearLayout) itemView).getChildAt(1);

        }
    }
}
