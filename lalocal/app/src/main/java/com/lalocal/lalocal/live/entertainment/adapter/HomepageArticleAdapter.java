package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.view.VerticalTextView;

/**
 * Created by wangjie on 2016/10/12.
 */
public class HomepageArticleAdapter extends RecyclerView.Adapter {

    private Context mContext;

    public HomepageArticleAdapter(Context context) {
        AppLog.i("ttt", "ARTICLE_HomepageArticleAdapter");
        this.mContext = context;
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
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class ArticleViewHolder extends RecyclerView.ViewHolder {
        ImageView imgArticle;
        VerticalTextView vtvArticleTitle;
        ImageView imgArticleTitleUp;
        ImageView imgArticleTitleBottom;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            AppLog.i("ttt", "ARTICLE_viewholder init");
            imgArticle = (ImageView) itemView.findViewById(R.id.img_article);
            vtvArticleTitle = (VerticalTextView) itemView.findViewById(R.id.vtv_article_title);
            imgArticleTitleUp = (ImageView) itemView.findViewById(R.id.icon_article_title_up);
            imgArticleTitleBottom = (ImageView) itemView.findViewById(R.id.icon_article_title_bottom);

            // 初始化VerticalTextView
            vtvArticleTitle.setText("这是测试！");
//            是测试！懂咩?你们这些开发呀！一天就知道生产BUG，测试怎么你了？啊？！你说我说的有道理吗？测试，来，我们把枪放下，好好说..
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
            vtvArticleTitle.setTextMaxLines(4);

            RelativeLayout.LayoutParams lpRight = (RelativeLayout.LayoutParams) imgArticleTitleUp.getLayoutParams();
            // 与标题右对齐
//            lpRight.addRule(RelativeLayout.ALIGN_RIGHT);
            lpRight.addRule(RelativeLayout.ALIGN_RIGHT, R.id.vtv_article_title);
//            // 在标题上方
//            lpRight.addRule(RelativeLayout.ABOVE, R.id.vtv_article_title);
//            // 下外边距 20dp
//            lpRight.setMargins(0, 0, 0, DensityUtil.dip2px(mContext, 20));
            // 标题括号图标位置控制
            imgArticleTitleUp.setLayoutParams(lpRight);

            RelativeLayout.LayoutParams lpLeft = (RelativeLayout.LayoutParams) imgArticleTitleBottom.getLayoutParams();
            AppLog.i("ttt", "icon width = " + lpLeft.width + "; height = " + lpLeft.height);
            // 与标题左对齐
//            lpLeft.addRule(RelativeLayout.ALIGN_LEFT);
            lpLeft.addRule(RelativeLayout.ALIGN_LEFT, R.id.vtv_article_title);
            // 在标题下方
//            lpRight.addRule(RelativeLayout.BELOW, R.id.vtv_article_title);
//            // 上外边距 20dp
//            lpRight.setMargins(0, DensityUtil.dip2px(mContext, 20), 0, 0);
            // 标题括号图标位置控制
            imgArticleTitleBottom.setLayoutParams(lpLeft);

        }
    }
}
