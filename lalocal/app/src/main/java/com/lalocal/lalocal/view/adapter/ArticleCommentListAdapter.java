package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.CommentRowBean;
import com.lalocal.lalocal.view.viewholder.article.ArticleCommentViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/12/16.
 */

public class ArticleCommentListAdapter extends RecyclerView.Adapter {

    // 上下文
    private Context mContext;
    // 评论列表
    private List<CommentRowBean> mCommentList = new ArrayList<>();
    // Item点击事件
    private MyOnItemClickListener mItemClickListener;

    public ArticleCommentListAdapter(Context context, List<CommentRowBean> commentList) {
        mContext = context;

        // 评论列表
        if (commentList.size() > 0) {
            mCommentList.clear();
            mCommentList.addAll(commentList);
        }
    }

    /**
     * 刷洗列表
     * @param commentList
     */
    public void refreshList(List<CommentRowBean> commentList) {
        mCommentList.clear();
        mCommentList.addAll(commentList);
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_comment_list, parent, false);
        RecyclerView.ViewHolder holder = new ArticleCommentViewHolder(mContext, view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 获取评论
        CommentRowBean commentRowBean = mCommentList.get(position);
        // 评论数据初始化
        ((ArticleCommentViewHolder) holder).initData(commentRowBean, position);
    }

    @Override
    public int getItemCount() {
        return mCommentList.size();
    }

    /**
     * 设置item点击事件
     * @param listener
     */
    public void setOnItemClickListener(MyOnItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface MyOnItemClickListener {

        void onItemClick(View v, int position);
    }
}
