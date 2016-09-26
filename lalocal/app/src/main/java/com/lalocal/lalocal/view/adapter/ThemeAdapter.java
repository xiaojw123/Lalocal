package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

import java.text.NumberFormat;
import java.util.List;

/**
 * Created by wangjie on 2016/9/21.
 */
public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<RecommendRowsBean> mThemeList;

    public ThemeAdapter(Context context, List<RecommendRowsBean> themeList) {
        AppLog.i("hehe", "ThemeAdapter()");
        this.mContext = context;
        this.mThemeList = themeList;
        AppLog.i("hehe", themeList == null ? "themeList inner null" : "size is ss " + themeList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.i("hehe", "onCreateViewHolder()");
        View view = LayoutInflater.from(mContext).inflate(R.layout.theme_list_item, null);

        RecyclerView.ViewHolder holder = new ThemeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AppLog.i("hehe", "onBindViewHolder -- " + position);
        RecommendRowsBean bean = mThemeList.get(position);

        ThemeViewHolder holder = (ThemeViewHolder) viewHolder;

        String name = bean.getName();
        String subtitle = bean.getSubTitle();
        String photo = bean.getPhoto();
        String readNum = formatNum(bean.getReadNum());
        String praiseNum = formatNum(bean.getPraiseNum());
        final String rowId = String.valueOf(bean.getId());

        holder.tvThemeName.setText(name);
        holder.tvThemeSubTitle.setText(subtitle);
        DrawableUtils.displayImg(mContext, holder.imgTheme, photo);
        holder.tvReadNum.setText(readNum);
        holder.tvPraiseNum.setText(praiseNum);

        holder.layoutContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SpecialDetailsActivity.class);
                intent.putExtra("rowId", rowId);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = mThemeList == null ? null : mThemeList.size();
        AppLog.i("hehe", "getItemCount() -- " + size);
        return size;
    }

    private class ThemeViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutContainer;
        ImageView imgTheme;
        TextView tvThemeName;
        TextView tvThemeSubTitle;
        TextView tvReadNum;
        TextView tvPraiseNum;

        public ThemeViewHolder(View view) {
            super(view);

            layoutContainer = (LinearLayout) view.findViewById(R.id.container);
            imgTheme = (ImageView) view.findViewById(R.id.img_theme);
            tvThemeName = (TextView) view.findViewById(R.id.tv_theme_name);
            tvThemeSubTitle = (TextView) view.findViewById(R.id.tv_theme_sub_title);
            tvReadNum = (TextView) view.findViewById(R.id.tv_read_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
        }
    }

    /**
     * 给数字加上","分隔符
     *
     * @param num
     * @return
     */
    private String formatNum(double num) {
        NumberFormat nf = NumberFormat.getNumberInstance();
        return nf.format(num);
    }
}
