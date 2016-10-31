package com.lalocal.lalocal.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.lalocal.lalocal.model.ArticleDetailsResultBean;

import java.util.List;

/**
 * Created by xiaojw on 2016/10/20.
 */

public class MyArticleAdapter extends BaseRecyclerAdapter {

    private List<ArticleDetailsResultBean> mItems;

    public MyArticleAdapter(List<ArticleDetailsResultBean> items){
        mItems=items;
    }

    public void updateItems(List<ArticleDetailsResultBean> items){
        mItems=items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
