package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.SpecialDetailsActivity;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.view.viewholder.find.SubThemeViewHolder;

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
        return mThemeList == null ? 0 : mThemeList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 填充视图
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.home_recommend_theme_viewpager_item, null);
        // 关联控件
        SubThemeViewHolder holder = new SubThemeViewHolder();
        holder.imgTheme = (ImageView) view.findViewById(R.id.img_theme);
        holder.tvSpecialName = (TextView) view.findViewById(R.id.tv_theme_name);
        holder.tvSpecialSubTitle = (TextView) view.findViewById(R.id.tv_theme_sub_title);
        holder.tvReadQuantity = (TextView) view.findViewById(R.id.tv_read_quantity);
        holder.tvSaveQuantity = (TextView) view.findViewById(R.id.tv_save_quantity);


        // 获取bean
        RecommendRowsBean bean = mThemeList.get(position);
        // 获取并解析图片
        String photoUrl = bean.getPhoto();
        // 获取图片控件的宽高
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.imgTheme.getLayoutParams();
        int width = holder.imgTheme.getMeasuredWidth();
        int height = holder.imgTheme.getMeasuredWidth();
        photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
        AppLog.i("photoU", "url is " + photoUrl + ";width: " + width + "; height : " + height);

        // 获取专题标题
        String name = bean.getName();
        // 获取专题副标题
        String subtitle = bean.getSubTitle();
        // 获取阅读人数
        String readQuantity = CommonUtil.formatNumWithComma(bean.getReadNum());
        // 获取收藏人数
        String saveQuantity = CommonUtil.formatNumWithComma(bean.getPraiseNum());


        // 使用Glide加载url图片
        Glide.with(mContext)
                .load(photoUrl)
                .placeholder(R.drawable.androidloading)
                .centerCrop()
                .crossFade()
                // 只缓存原图，其他参数：DiskCacheStrategy.NONE不缓存到磁盘，DiskCacheStrategy.RESULT缓存处理后的图片，DiskCacheStrategy.ALL两者都缓存
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(holder.imgTheme);

        // 设置名字
        holder.tvSpecialName.setText(name);
        // 设置副标题
        holder.tvSpecialSubTitle.setText(subtitle);
        // 设置阅读人数
        holder.tvReadQuantity.setText(readQuantity);
        // 设置保存人数
        holder.tvSaveQuantity.setText(saveQuantity);
        // 添加视图到容器
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
