package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.model.Constants;
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

        this.mContext = context;
        this.mThemeList = themeList;

    }
    public  void setResh(List<RecommendRowsBean> themeList){
        this.mThemeList=themeList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.theme_list_item, null);

        ThemeViewHolder holder = new ThemeViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        RecommendRowsBean bean = mThemeList.get(position);
        ThemeViewHolder holder = (ThemeViewHolder) viewHolder;
        String name = bean.getName();
        String subtitle = bean.getSubTitle();
        String photo = bean.getPhoto();
        int type = bean.getType();
        int tagColor = Color.WHITE;
        String tag = bean.getTag();
        String readNum = formatNum(bean.getReadNum());
        String praiseNum = formatNum(bean.getPraiseNum());
        final String rowId = String.valueOf(bean.getId());
        if (type == Constants.THEME_AUTHOR) {
            holder.layoutThemeTag.setVisibility(View.VISIBLE);
            tag = "作者专栏";
            tagColor = ContextCompat.getColor(mContext, R.color.color_theme_tag_author);
        } else if (TextUtils.isEmpty(tag)) {
            holder.layoutThemeTag.setVisibility(View.GONE);
        } else {
            holder.layoutThemeTag.setVisibility(View.VISIBLE);
        }

        if (holder.layoutThemeTag.getVisibility() == View.VISIBLE) {
            if (type == Constants.THEME_AUTHOR) {
                Drawable bg = ContextCompat.getDrawable(mContext, R.drawable.nextsharemeeting_tag);
                bg = DrawableUtils.tintDrawable(bg, ColorStateList.valueOf(Color.BLACK));
                holder.layoutThemeTag.setBackgroundDrawable(bg);
            } else {
                Drawable bg = ContextCompat.getDrawable(mContext, R.drawable.nextsharemeeting_tag);
                bg = DrawableUtils.tintDrawable(bg, ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.color_theme_tag_red)));
                holder.layoutThemeTag.setBackgroundDrawable(bg);
            }
            holder.tvThemeTag.setText(tag);
            holder.tvThemeTag.setTextColor(tagColor);
        }

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
        return mThemeList == null ? 0 : mThemeList.size();
    }

    private class ThemeViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutContainer;
        RelativeLayout layoutThemeTag;
        ImageView imgTheme;
        TextView tvThemeName;
        TextView tvThemeSubTitle;
        TextView tvReadNum;
        TextView tvPraiseNum;
        TextView tvThemeTag;

        public ThemeViewHolder(View view) {
            super(view);

            layoutContainer = (LinearLayout) view.findViewById(R.id.container);
            imgTheme = (ImageView) view.findViewById(R.id.img_theme);
            tvThemeName = (TextView) view.findViewById(R.id.tv_theme_name);
            tvThemeSubTitle = (TextView) view.findViewById(R.id.tv_theme_sub_title);
            tvReadNum = (TextView) view.findViewById(R.id.tv_read_num);
            tvPraiseNum = (TextView) view.findViewById(R.id.tv_praise_num);
            layoutThemeTag = (RelativeLayout) view.findViewById(R.id.layout_theme_tag);
            tvThemeTag = (TextView) view.findViewById(R.id.tv_theme_tag);
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
