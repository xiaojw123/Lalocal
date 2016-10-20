package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.ArticleActivity;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.VerticalTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/10/12.
 */
public class HomepageArticleAdapter extends RecyclerView.Adapter {

    private Context mContext;

    // 用户文章列表
    private List<ArticleDetailsResultBean> mUserArticleList;

    public HomepageArticleAdapter(Context context, List<ArticleDetailsResultBean> userArticleList) {
        AppLog.i("ttt", "ARTICLE_HomepageArticleAdapter");
        this.mContext = context;
        // 用户文章列表
        this.mUserArticleList = userArticleList;

        if (mUserArticleList == null) {
            mUserArticleList = new ArrayList<>();
        }
    }
    public void updateItems(List<ArticleDetailsResultBean> userArticleList){
        this.mUserArticleList = userArticleList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AppLog.i("ttt", "ARTICLE_onCreateViewHolder");
        // 加载布局
        View view = LayoutInflater.from(mContext).inflate(R.layout.homepage_article_item, parent, false);
        // 初始化文章ViewHolder
        ArticleViewHolder holder = new ArticleViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AppLog.i("ttt", "ARTICLE_IS__"+position);
        // 获取文章数据
        ArticleDetailsResultBean bean = mUserArticleList.get(position);
        // 数据初始化
        ((ArticleViewHolder) holder).initData(bean);
    }

    @Override
    public int getItemCount() {
        return mUserArticleList.size();
    }

    private class ArticleViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imgArticle;
        RelativeLayout titleContainer;
        VerticalTextView vtvArticleTitle;
        ImageView imgArticleTitleUp;
        ImageView imgArticleTitleBottom;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            AppLog.i("ttt", "ARTICLE_viewholder init");
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            imgArticle = (ImageView) itemView.findViewById(R.id.img_article);
            titleContainer = (RelativeLayout) itemView.findViewById(R.id.article_title_container);
            vtvArticleTitle = (VerticalTextView) itemView.findViewById(R.id.vtv_article_title);
            imgArticleTitleUp = (ImageView) itemView.findViewById(R.id.icon_article_title_up);
            imgArticleTitleBottom = (ImageView) itemView.findViewById(R.id.icon_article_title_bottom);

            // 初始化VerticalTextView
            vtvArticleTitle.setText("这是测试！这是测试！这是测试！这是测试！");
            // 设置VerticalTextView字体颜色
            vtvArticleTitle.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            // 行宽
            int lineWidth = DensityUtil.dip2px(mContext, 16);
            // 文本字体大小
            int textSize = DensityUtil.sp2px(mContext, 16);
            // 设置行宽
            vtvArticleTitle.setLineWidth(lineWidth);
            // 设置字体大小
            vtvArticleTitle.setTextSize(textSize);
            // 设置最大行号
//            vtvArticleTitle.setTextMaxLines(4);

            // -标题括号对齐
            RelativeLayout.LayoutParams lpRight = (RelativeLayout.LayoutParams) imgArticleTitleUp.getLayoutParams();
            // 与标题右对齐
            lpRight.addRule(RelativeLayout.ALIGN_RIGHT, R.id.vtv_article_title);
            // 标题括号图标位置控制
            imgArticleTitleUp.setLayoutParams(lpRight);

            RelativeLayout.LayoutParams lpLeft = (RelativeLayout.LayoutParams) imgArticleTitleBottom.getLayoutParams();
            AppLog.i("ttt", "icon width = " + lpLeft.width + "; height = " + lpLeft.height);
            // 与标题左对齐
            lpLeft.addRule(RelativeLayout.ALIGN_LEFT, R.id.vtv_article_title);
            // 标题括号图标位置控制
            imgArticleTitleBottom.setLayoutParams(lpLeft);

            // -标题背景固定比例
            // 宽高比例
            float scale = 0.62f;
            // CardView布局参数
            LinearLayout.LayoutParams cardLp = (LinearLayout.LayoutParams) cardView.getLayoutParams();
            // 标题容器布局参数
            RelativeLayout.LayoutParams titleLp = (RelativeLayout.LayoutParams) titleContainer.getLayoutParams();
            // CardView的高度与标题容器高度一致，标题容器高度为MATCH_PARENT，值为-1，通过CardView来获取
            int height = cardLp.height;
            // 得到呈比例后的宽度
            int width = (int) (height * scale);
            // 宽度放入标题容器的布局参数里面
            titleLp.width = width;
            // 配置标题容器的布局参数
            titleContainer.setLayoutParams(titleLp);

        }

        public void initData(final ArticleDetailsResultBean articleBean) {
            // 获取图片路径
            String picUrl = articleBean.getPhoto();
            // 获取文章标题
            String title = articleBean.getTitle();

            // 空数据处理
            if (TextUtils.isEmpty(title)) {
                title = "标题获取失败";
            }

            // 设置标题
//            vtvArticleTitle.setText(title);
            // 设置图片
            DrawableUtils.displayRadiusImg(mContext, imgArticle, picUrl,
                    DensityUtil.dip2px(mContext, 3), R.drawable.androidloading);

            // 监听事件回调
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ArticleActivity.class);
                    String targetId = String.valueOf(articleBean.getId());
                    intent.putExtra("targetID", targetId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
