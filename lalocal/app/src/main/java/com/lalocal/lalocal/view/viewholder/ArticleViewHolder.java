package com.lalocal.lalocal.view.viewholder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.SpecialAuthorBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.view.WrapContentListView;
import com.lalocal.lalocal.view.adapter.CommonAdapter;
import com.lalocal.lalocal.view.adapter.CommonViewHolder;

import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class ArticleViewHolder extends RecyclerView.ViewHolder {

    private Context mContext;

    private TextView mTvTitle;
    private TextView mTvSubTitle;
    private FrameLayout mLayoutContainer;
    private WrapContentListView mLvArticle;

    public ArticleViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;

        mLayoutContainer = (FrameLayout) itemView.findViewById(R.id.article_container);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLvArticle = (WrapContentListView) itemView.findViewById(R.id.lv_article);
    }

    public void initData(List<ArticleDetailsResultBean> list, String title, String subtitle) {
        final List<ArticleDetailsResultBean> articleList = list;

        int size = list == null ? 0 : list.size();

        if (size == 0) {
            mLayoutContainer.setVisibility(View.GONE);
            return;
        }

        mTvTitle.setText(title);
        mTvSubTitle.setText(subtitle);

        mLvArticle.setFocusable(false);
        mLvArticle.setAdapter(new CommonAdapter<ArticleDetailsResultBean>(mContext, articleList, R.layout.home_recommend_article_list_item) {
            @Override
            public void convert(CommonViewHolder holder, ArticleDetailsResultBean bean) {
                // 设置图片
                ImageView imgArticle = holder.getView(R.id.img_article);
                // 获取图片链接
                String photoUrl = bean.getPhoto();
                // 获取控件宽高
                LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgArticle.getLayoutParams();
                int width = lp.width;
                int height = lp.height;
                // 传入七牛云居中裁剪参数
                photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);
                AppLog.i("urll", "the link is -- " + photoUrl);
                Glide.with(mContext)
                        .load(photoUrl)
                        .centerCrop()
                        .crossFade()
                        // 只缓存原图，其他参数：DiskCacheStrategy.NONE不缓存到磁盘，DiskCacheStrategy.RESULT缓存处理后的图片，DiskCacheStrategy.ALL两者都缓存
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(imgArticle);

                // 设置标题
                holder.setText(R.id.tv_article_title, bean.getTitle());

                // 设置浏览人数
                String readNum = CommonUtil.formatNumWithComma(bean.getReadNum());
                holder.setText(R.id.tv_read_num, readNum);

                // 设置点赞人数
                String praiseNum = CommonUtil.formatNumWithComma(bean.getPraiseNum());
                holder.setText(R.id.tv_praise_num, praiseNum);

                // 设置描述内容
                String description = bean.getDescription();
                holder.setText(R.id.tv_description, description);

                // 设置作者名字
                SpecialAuthorBean author = bean.getAuthorVO();
                String authorName = author.authorName;
                holder.setText(R.id.tv_author_name, authorName);
            }
        });

        mLvArticle.setFocusable(false);
        mLvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                String targetId = String.valueOf(articleList.get(position).getId());
                intent.putExtra("targetID", targetId);
                mContext.startActivity(intent);
            }
        });
    }
}
