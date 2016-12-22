package com.lalocal.lalocal.view.viewholder.live;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.CategoryBean;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.adapter.CategoryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/12/14.
 */

public class CategoryViewHolder extends RecyclerView.ViewHolder {

    // 上下文
    private Context mContext;
    // 分类列表
    private List<CategoryBean> mCategoryList = new ArrayList<>();

    // 列表控件
    private RecyclerView mRecyclerView;
    // 外部分类列表控件
    private RecyclerView mRvOutside;
    // 分类栏适配器
    private  CategoryAdapter mAdapter;
    // 列表控件滚动的位置
    private int mScrollDistance = 0;

    public CategoryViewHolder(Context context, View itemView, RecyclerView rvOutside) {
        super(itemView);

        mContext = context;
        mRvOutside = rvOutside;

        itemView.setFocusable(false);
        // 关联控件
        mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_category);
        // 初始化RecyclerView
        LinearLayoutManager layoutManager =  new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setFocusable(false);

        // 同步里外两个recyclerview的滑动
        syncScroll(mRecyclerView, mRvOutside);
    }

    /**
     * 同步两个RecyclerView的滑动
     * @param rv1
     * @param rv2
     */
    private void syncScroll(final RecyclerView rv1, final RecyclerView rv2) {
        rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                    rv2.scrollBy(dx, dy);
                    int scroll1 = rv1.getScrollY();
                    mScrollDistance = scroll1;
                    int scroll2 = rv2.getScrollY();
                    rv2.scrollBy(dx, scroll1 - scroll2);
                }
            }
        });

        rv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
//                    rv1.scrollBy(dx, dy);
                    int scroll1 = rv1.getScrollY();
                    int scroll2 = rv2.getScrollY();
                    mScrollDistance = scroll2;
                    rv1.scrollBy(dx, scroll2 - scroll1);
                }
            }
        });
    }

    /**
     * 初始化数据
     * @param categoryList
     */
    public void initData(List<CategoryBean> categoryList, int selected, CategoryAdapter.MyOnItemClickListener listener) {
        AppLog.i("sls", "CategorViewHolder: initData listener is " + (listener == null ? "null" : "not null"));
        // 判断列表数据是否为空
        if (categoryList == null || categoryList.size() == 0) {
            return;
        }

        LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        manager.scrollToPositionWithOffset(0, mScrollDistance);

        // 清空列表
        mCategoryList.clear();
        // 添加分类列表
        mCategoryList.addAll(categoryList);

        if (mAdapter == null) {
            // 初始化适配器
            mAdapter = new CategoryAdapter(mContext, mCategoryList, listener, mRecyclerView, selected);
            // 设置适配器
            mRecyclerView.setAdapter(mAdapter);
        } else {
            // 刷新适配器
            mAdapter.setSelected(selected);
        }
    }
}
