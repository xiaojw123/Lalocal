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
 *
 * 直播首页分类栏视图容器
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
    private CategoryAdapter mAdapter;
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        // 同步里外两个recyclerview的滑动
        syncScroll(mRecyclerView, mRvOutside);
    }

    /**
     * 同步两个RecyclerView的滑动
     *
     * @param rv1
     * @param rv2
     */
    private void syncScroll(final RecyclerView rv1, final RecyclerView rv2) {
        rv1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    int scroll1 = getScrollXDistance(rv1);
                    int scroll2 = getScrollXDistance(rv2);
                    int delta = scroll1 - scroll2;
                    rv2.scrollBy(delta, dy);
                }
            }
        });

        rv2.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    int scroll1 = getScrollXDistance(rv1);
                    int scroll2 = getScrollXDistance(rv2);
                    int delta = scroll2 - scroll1;
                    rv1.scrollBy(delta, dy);
                }
            }
        });
    }

    /**
     * 获取横向列表左侧超出屏幕的位置
     * @param recyclerView
     * @return
     */
    private int getScrollXDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(firstVisibleItemPosition);
        if (firstVisiableChildView != null) {
            int itemHeight = firstVisiableChildView.getWidth();
            return (firstVisibleItemPosition) * itemHeight - firstVisiableChildView.getLeft();
        } else {
            return 0;
        }
    }

    /**
     * 初始化数据
     *
     * @param categoryList
     */
    public void initData(List<CategoryBean> categoryList, int selected, CategoryAdapter.MyOnItemClickListener listener) {
        // 判断列表数据是否为空
        if (categoryList == null || categoryList.size() == 0) {
            return;
        }

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

    /**
     * 设置选中的分类栏
     *
     * @param selected
     */
    public void setSelected(int selected) {
        if (mAdapter != null) {
            mAdapter.setSelected(selected);
            suitRecyclerView(mRecyclerView, mRvOutside, selected, mCategoryList.size());
        }
    }

    /**
     * 点击可视最左侧，列表向左滑动一个item的距离；反之向右滑动一个item的距离
     */
    private void suitRecyclerView(RecyclerView rv1, RecyclerView rv2, int selected, int size) {
        if (rv1 == null || rv2 == null) {
            return;
        }

        LinearLayoutManager layoutManager1 = (LinearLayoutManager) rv1.getLayoutManager();
        LinearLayoutManager layoutManager2 = (LinearLayoutManager) rv2.getLayoutManager();

        // 列表第一个可见的下标
        int firstVisible = layoutManager1.findFirstVisibleItemPosition();
        // 列表第一个完全可见的下标
        int firstCompletlyVisible = layoutManager1.findFirstCompletelyVisibleItemPosition();
        // 列表最后一个可见的下标
        int lastVisible = layoutManager1.findLastVisibleItemPosition();
        // 列表最后一个完全可见的下标
        int lastCompletlyVisible = layoutManager1.findLastCompletelyVisibleItemPosition();

        // 粗略计算一屏宽度显示多少个item
        int delta = lastCompletlyVisible - firstVisible;

        // 如果点击最左边可见的item，列表左移
        if (firstCompletlyVisible >= firstVisible && selected <= firstCompletlyVisible) {

            AppLog.i("srs", "点击左侧");
            if(layoutManager1!=null&&layoutManager2!=null){
                if (selected >= 1) {
                    AppLog.i("srs", "zuo 1");
                    layoutManager1.scrollToPosition(selected - 1);
                    layoutManager2.scrollToPosition(selected - 1);
                } else {
                    AppLog.i("srs", "zuo 2");
                    layoutManager1.scrollToPosition(selected);
                    layoutManager2.scrollToPosition(selected);
                }

            }
        }
        // 如果点击最右边可见的item，列表右移
        else if (lastCompletlyVisible <= lastVisible && selected >= lastCompletlyVisible) {
            AppLog.i("srs", "点击右侧");
            if(layoutManager1!=null&&layoutManager2!=null) {
                if (selected < size - 1) {
                    AppLog.i("srs", "you 1");
                    int target = selected + 1 - delta;
                    layoutManager1.scrollToPositionWithOffset(target, 0);
                    layoutManager2.scrollToPositionWithOffset(target, 0);
                } else {
                    AppLog.i("srs", "you 2");
                    int target = selected - delta;
                    layoutManager1.scrollToPositionWithOffset(target, 0);
                    layoutManager2.scrollToPositionWithOffset(target, 0);
                }

            }
        }
    }
}
