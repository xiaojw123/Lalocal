package com.lalocal.lalocal.view.adapter;

import android.content.Context;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/12/18.
 */
public class CategoryNewAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<CategoryBean> categoryList;
    private int mSelected = 0;
    public CategoryNewAdapter(Context context, List<CategoryBean> categoryList) {
        this.mContext = context;
        this.categoryList = categoryList;
    }
    public void  setSelectePosition(int mSelected){
        this.mSelected=mSelected;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 加载item视图
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_category_item, parent, false);
        // 初始化视图容器
        MyViewHolder holder = new MyViewHolder(view);
        // 返回视图容器
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final CategoryBean categoryBean = categoryList.get(position);
        MyViewHolder myViewHolder=(MyViewHolder)holder;
        if(mSelected==position){
            if(position==0){
                myViewHolder.imgCategory.setImageResource(R.drawable.category_hot_live_sel);
            }else {
                Glide.with(mContext)
                        .load(categoryBean.getPhoto())
                        .placeholder(R.drawable.androidloading)
                        .centerCrop()
                        .into(myViewHolder.imgCategory);
            }
        }else{
            if(position==0){
                myViewHolder.imgCategory.setImageResource(R.drawable.category_hot_live_unsel);
            }else {
                Glide.with(mContext)
                        .load(categoryBean.getPrePhoto())
                        .placeholder(R.drawable.androidloading)
                        .centerCrop()
                        .into(myViewHolder.imgCategory);
            }
        }
        myViewHolder.tvCategory.setText(categoryBean.getName());
        myViewHolder.layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setTag(categoryBean);
                if(mListener!=null){
                    mListener.onItemClick(v);
                }
            }
        });

    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_category)
        ImageView imgCategory;
        @BindView(R.id.tv_category)
        TextView tvCategory;
        @BindView(R.id.layout_click)
        LinearLayout layoutClick;
        @BindView(R.id.category_container)
        RelativeLayout categoryContainer;
        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getItemCount() {
        return categoryList == null ? 0 : categoryList.size();
    }
    private MyOnItemClickListener mListener;
    public interface MyOnItemClickListener {
        void onItemClick(View view);
    }
    public  void setOnItemClickListener(MyOnItemClickListener mListener){
        this.mListener=mListener;
    }
}
