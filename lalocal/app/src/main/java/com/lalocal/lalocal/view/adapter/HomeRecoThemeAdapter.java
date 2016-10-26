package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.viewholder.SubThemeViewHolder;
import com.lalocal.lalocal.view.viewholder.ThemeViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class HomeRecoThemeAdapter extends PagerAdapter {
    private Context mContext;
    private List<RecommendRowsBean> mThemeList;

    public HomeRecoThemeAdapter(Context context, List<RecommendRowsBean> mThemeList) {
        super();
        this.mContext = context;
        this.mThemeList = mThemeList;
    }

    @Override
    public int getCount() {
//                return Math.min(mThemeList == null ? 0 : mThemeList.size(), MAX_THEME);
        return mThemeList == null ? 0 : mThemeList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.home_recommend_theme_viewpager_item, null);
        RecommendRowsBean bean = mThemeList.get(position);
        SubThemeViewHolder holder = new SubThemeViewHolder();
        holder.imgTheme = (RoundedImageView) view.findViewById(R.id.img_theme);
        holder.tvSpecialName = (TextView) view.findViewById(R.id.tv_theme_name);
        holder.tvSpecialSubTitle = (TextView) view.findViewById(R.id.tv_theme_sub_title);
        holder.tvReadQuantity = (TextView) view.findViewById(R.id.tv_read_quantity);
        holder.tvSaveQuantity = (TextView) view.findViewById(R.id.tv_save_quantity);

        // 设置专题图片
        DrawableUtils.displayImg(mContext, holder.imgTheme, bean.getPhoto(), R.drawable.androidloading);

        // 设置名字
        String name = bean.getName();
        holder.tvSpecialName.setText(name);

        // 设置副标题
        String subtitle = bean.getSubTitle();
        holder.tvSpecialSubTitle.setText(subtitle);

        // 设置阅读人数
        String readQuantity = CommonUtil.formatNumWithComma(bean.getReadNum());
        holder.tvReadQuantity.setText(readQuantity);

        // 设置保存人数
        String saveQuantity = CommonUtil.formatNumWithComma(bean.getPraiseNum());
        holder.tvSaveQuantity.setText(saveQuantity);

        container.addView(view);

        final String rowId = String.valueOf(bean.getId());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SpecialDetailsActivity.class);
                intent.putExtra("rowId", rowId);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
