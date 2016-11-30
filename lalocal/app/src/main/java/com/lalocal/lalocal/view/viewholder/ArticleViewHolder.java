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
public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mContext;

    // 头部
    private RelativeLayout mLayoutHeader;
    // 主标题
    private TextView mTvTitle;
    // 副标题
    private TextView mTvSubTitle;
    // item可点击区域
    private LinearLayout mLayoutClick;
    // 文章附图
    private ImageView mImgArticle;
    // 文章标题
    private TextView mTvArticleTitle;
    // 阅读次数
    private TextView mTvReadNum;
    // 点赞次数
    private TextView mTvPraiseNum;
    // 文章内容描述
    private TextView mTvDescription;
    // 文章作者名称
    private TextView mTvAuthorName;

    // 目标编号
    private int mTargetId;

    public ArticleViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;

        // -关联控件
        mLayoutHeader = (RelativeLayout) itemView.findViewById(R.id.layout_article_header);
        mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_subtitle);
        mLayoutClick = (LinearLayout) itemView.findViewById(R.id.layout_click);
        mImgArticle = (ImageView) itemView.findViewById(R.id.img_article);
        mTvArticleTitle = (TextView) itemView.findViewById(R.id.tv_article_title);
        mTvReadNum = (TextView) itemView.findViewById(R.id.tv_read_num);
        mTvPraiseNum = (TextView) itemView.findViewById(R.id.tv_praise_num);
        mTvDescription = (TextView) itemView.findViewById(R.id.tv_description);
        mTvAuthorName = (TextView) itemView.findViewById(R.id.tv_author_name);
    }

    public void initData(ArticleDetailsResultBean bean, String title, String subtitle, boolean showHeader) {

        // 判断是否显示头部
        if (showHeader) {
            // 显示头部
            mLayoutHeader.setVisibility(View.VISIBLE);
            // 设置主标题
            mTvTitle.setText(title);
            // 设置副标题
            mTvSubTitle.setText(subtitle);
        } else { // 隐藏头部
            mLayoutHeader.setVisibility(View.GONE);
        }

        // -解析数据
        // 获取目标编号
        mTargetId = bean.getId();
        // 获取图片链接
        String photoUrl = bean.getPhoto();
        // 获取控件宽高
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mImgArticle.getLayoutParams();
        int width = lp.width;
        int height = lp.height;
        // 传入七牛云居中裁剪参数
        photoUrl = QiniuUtils.centerCrop(photoUrl, width, height);

        // 获取文章标题
        String articleTitle = bean.getTitle();
        // 获取文章阅读次数
        int readNum = bean.getReadNum();
        // 获取文章点赞次数
        int praiseNum = bean.getPraiseNum();
        // 获取文章描述
        String description = bean.getDescription();
        // 获取文章作者名字
        SpecialAuthorBean author = bean.getAuthorVO();
        String authorName = author.authorName;


        // 使用Glide加载url图片
        Glide.with(mContext)
                .load(photoUrl)
                .centerCrop()
                .crossFade()
                // 只缓存原图，其他参数：DiskCacheStrategy.NONE不缓存到磁盘，DiskCacheStrategy.RESULT缓存处理后的图片，DiskCacheStrategy.ALL两者都缓存
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(mImgArticle);

        mTvArticleTitle.setText(articleTitle);
        mTvReadNum.setText(String.valueOf(readNum));
        mTvPraiseNum.setText(String.valueOf(praiseNum));
        mTvDescription.setText(description);
        mTvAuthorName.setText(authorName);

        mLayoutClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_click:
                Intent intent = new Intent(mContext, ArticleActivity.class);
                String targetId = String.valueOf(mTargetId);
                intent.putExtra("targetID", targetId);
                mContext.startActivity(intent);
                break;
        }
    }
}
