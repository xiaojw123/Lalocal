package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.CategoryBean;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.util.AppLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangjie on 2016/12/14.
 */

public class CategoryAdapter extends RecyclerView.Adapter {

    // 上下文
    private Context mContext;
    // 分类列表
    private List<CategoryBean> mCategoryList = new ArrayList<>();
    // 选中的下表
    private int mSelected = 0;
    // item点击事件
    private MyOnItemClickListener mListener;

    private RecyclerView mRecyclerView;

    public CategoryAdapter(Context context, List<CategoryBean> categoryList, MyOnItemClickListener listener, RecyclerView recyclerView, int selected) {
        mContext = context;
        mListener = listener;
        mRecyclerView = recyclerView;
        mSelected = selected;
        AppLog.i("sls", "recyclerview is " + (mRecyclerView == null ? "null" : "not null"));

        AppLog.i("sls", "CategoryAdapter :listener is " + (listener == null ? "null" : "not null"));

        if (categoryList != null && categoryList.size() > 0) {
            mCategoryList.clear();
            mCategoryList.addAll(categoryList);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 加载item视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_category_item, parent, false);
        // 初始化视图容器
        MyViewHolder holder = new MyViewHolder(mContext, view, mListener);
        // 返回视图容器
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // 获取分类bean
        CategoryBean categoryBean = mCategoryList.get(position);
        AppLog.i("sls", "onBindView " + mSelected);
        // 判断当前分类是否选中
        boolean isSelected = (position == mSelected ? true : false);
        AppLog.i("sls", "onBindViewHolder " + mSelected + "; isSelected " + isSelected);
        // 初始化数据
        ((MyViewHolder)holder).initData(categoryBean, isSelected);
    }

    /**
     * 设置选中的分类item
     * 注意：在MyViewHolder initData之前调用
     * @param selected
     */
    public void setSelected(int selected) {
        mSelected = selected;
        this.notifyDataSetChanged();
        AppLog.i("sls", "Mselected is " + mSelected);
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public interface MyOnItemClickListener {
        void onItemClick(View view, int position);
    }

    private class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // 上下文
        private Context mContext;
        // 分类
        private CategoryBean mCategory;

        // 分类外层
        private RelativeLayout mContainer;
        // 分类图标
        private ImageView mImgCategory;
        // 分类名称
        private TextView mTvCategory;
        // 点击区域
        private LinearLayout mLayoutClick;
        // item点击事件
        private MyOnItemClickListener mListener;

        public MyViewHolder(Context context, View itemView, MyOnItemClickListener listener) {
            super(itemView);

            itemView.setFocusable(false);

            AppLog.i("lsck", "MyViewHolder :listener is " + (listener == null ? "null" : "not null"));
            mContext = context;
            mListener = listener;
            // 关联控件
            mContainer = (RelativeLayout) itemView.findViewById(R.id.category_container);
            mImgCategory = (ImageView) itemView.findViewById(R.id.img_category);
            mTvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            mLayoutClick = (LinearLayout) itemView.findViewById(R.id.layout_click);
            mLayoutClick.setOnClickListener(this);

            mContainer.setFocusable(false);
            mImgCategory.setFocusable(false);
            mTvCategory.setFocusable(false);
            mLayoutClick.setFocusable(false);
        }

        /**
         * 初始化数据
         * @param category
         * @param isSelected
         */
        public void initData(CategoryBean category, boolean isSelected) {
            AppLog.i("sls", "isSelec data is " + isSelected);
            // 获取分类的id
            int id = category.getId();

            // 如果是“热门直播”（接口未给出热门直播的数据，因此将其id标记为-1）
            if (id == Constants.CATEGORY_HOT_LIVE) {
                // 如果选中
                if (isSelected) {
                    mImgCategory.setImageResource(R.drawable.category_hot_live_sel);
                } else { // 如果未选中
                    mImgCategory.setImageResource(R.drawable.category_hot_live_unsel);
                }
                // 填充分类名称
                mTvCategory.setText("热门直播");
            } else {
                String photo = "";
                // 如果选中
                if (isSelected) {
                    photo = category.getPhoto();
                } else { // 如果未选中
                    photo = category.getPrePhoto();
                }
                // 加载分类图片
                Glide.with(mContext)
                        .load(photo)
                        .placeholder(R.drawable.androidloading)
                        .centerCrop()
                        .into(mImgCategory);
                // 获取分类名称
                String cateName = category.getName();
                mTvCategory.setText(cateName);
            }
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();int size = getItemCount();

//                AppLog.i("ddddd", "position is " + position);
//                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//                int firstVisible = manager.findFirstCompletelyVisibleItemPosition();
//                int lastVisible = manager.findLastCompletelyVisibleItemPosition();
//                View childView = mRecyclerView.getChildAt(position);
//                AppLog.i("ddddd", "child view is " + (childView == null ? "null" : "not null"));
//                int scrollDistance = childView.getMeasuredWidth();
//                AppLog.i("ddddd", "scrollDistance is " + scrollDistance);
//                if (position == firstVisible && position != 0) {
//                    AppLog.i("ddddd", "left " + position + "; first:" + firstVisible);
//                    mRecyclerView.smoothScrollBy(scrollDistance, 0);
//                } else if (position == lastVisible && position != size - 1) {
//                    AppLog.i("ddddd", "right " + position + "; last:" + lastVisible);
//                    mRecyclerView.smoothScrollBy(-scrollDistance, 0);
//                }

                mListener.onItemClick(v, position);


            }
        }
    }
}
