package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.viewholder.article.ArticleCommentViewHolder;

/**
 * Created by wangjie on 2016/12/16.
 */

public class ArticleCommentListAdapter extends RecyclerView.Adapter {

    // 上下文
    private Context mContext;

    public ArticleCommentListAdapter(Context context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_comment_list, parent, false);
        RecyclerView.ViewHolder holder = new ArticleCommentViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}
