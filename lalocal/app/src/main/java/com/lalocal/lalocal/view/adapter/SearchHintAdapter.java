package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;

import java.util.List;

/**
 * Created by xiaojw on 2016/7/19.
 */
public class SearchHintAdapter extends BaseRecyclerAdapter{
    public static final int ITEM_HISTORY = 10;
    public static final int ITEM_HOT = 11;
    public static final int ITEM_HISTORY_TITLE = 12;
    public static final int ITEM_HOT_TITLE = 13;
    List<SparseArray<String>> datas;
    Context context;
    Resources res;


    public SearchHintAdapter(Context context, List<SparseArray<String>> datas) {
        this.datas = datas;
        this.context = context;
        res = context.getResources();
    }

    public void updateItems(List<SparseArray<String>> datas){
        this.datas = datas;
        notifyDataSetChanged();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HISTORY) {
            View mybg = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_detete_menu, null);
            mybg.setLayoutParams(new ViewGroup.LayoutParams((int) res.getDimension(R.dimen.search_history_delete_width), ViewGroup.LayoutParams.MATCH_PARENT));
            //获取item布局
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_history, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //生成返回RecyclerView.ViewHolder
            return new HistoryItem(context, mybg, view, RecyclerViewDragHolder.EDGE_RIGHT).getDragViewHolder();
        } else if (viewType == ITEM_HOT)

        {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item_hot, parent, false);
            return new HotItem(view);
        } else if (viewType == ITEM_HISTORY_TITLE)

        {
            TextView textView = new TextView(context);
            textView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.search_history_title_Height)));
            textView.setTextColor(context.getResources().getColor(R.color.color_b3));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_12_sp));
            return new HistoryTitle(textView);
        } else if (viewType == ITEM_HOT_TITLE)

        {
            TextView textView = new TextView(context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = (int) res.getDimension(R.dimen.dimen_size_8_dp);
            textView.setLayoutParams(params);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_14_sp));
            Drawable drawable = res.getDrawable(R.drawable.search_fire_icon);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            textView.setCompoundDrawables(drawable, null, null, null);
            textView.setCompoundDrawablePadding((int) res.getDimension(R.dimen.dimen_size_6_dp));
            textView.setTextColor(context.getResources().getColor(R.color.color_ffaa2a));
            textView.setGravity(Gravity.BOTTOM);
            return new HotTitle(textView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SparseArray<String> sp = datas.get(position);
        final String value = sp.valueAt(0);
        if (holder instanceof RecyclerViewDragHolder.DragViewHolder) {
            HistoryItem myHolder = (HistoryItem) RecyclerViewDragHolder.getHolder(holder);
            myHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        v.setTag(value);
                        listener.onItemClickListener(v, position);
                    }
                }
            });
            myHolder.title.setText(value);
            myHolder.deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(position);
                }
            });

        } else if (holder instanceof HotItem) {
            ((HotItem) holder).itemKey.setText(value);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        v.setTag(value);
                        listener.onItemClickListener(v,position);
                    }
                }
            });
        } else if (holder instanceof HotTitle) {
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) ((HotTitle) holder).title.getLayoutParams();
            if (position == 0) {
                params.topMargin = (int) res.getDimension(R.dimen.dimen_size_15_dp);
            } else {
                params.topMargin = (int) res.getDimension(R.dimen.dimen_size_50_dp);
            }
            ((HotTitle) holder).title.setText(value);
        } else if (holder instanceof HistoryTitle) {
            ((HistoryTitle) holder).title.setText(value);
        }


    }

    public void remove(int position) {
        datas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

    }


    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }


    @Override
    public int getItemViewType(int position) {
        SparseArray<String> sp = datas.get(position);
        return sp.keyAt(0);
    }


    class HistoryItem extends RecyclerViewDragHolder {

        private TextView title;
        private FrameLayout deleteItem;

        public HistoryItem(Context context, View bgView, View topView) {
            super(context, bgView, topView);
        }

        public HistoryItem(Context context, View bgView, View topView, int mTrackingEdges) {
            super(context, bgView, topView, mTrackingEdges);
        }

        @Override
        public void initView(View itemView) {
            title = (TextView) itemView.findViewById(R.id.item_history_key_tv);
            deleteItem = (FrameLayout) itemView.findViewById(R.id.delete);
        }
    }

    class HotItem extends RecyclerView.ViewHolder {
        TextView itemKey;

        public HotItem(View itemView) {
            super(itemView);
            itemKey = (TextView) itemView.findViewById(R.id.item_hot_key);
        }
    }

    class HotTitle extends RecyclerView.ViewHolder {

        TextView title;

        public HotTitle(View itemView) {
            super(itemView);
            title = (TextView) itemView;
        }
    }

    class HistoryTitle extends RecyclerView.ViewHolder {

        TextView title;

        public HistoryTitle(View itemView) {
            super(itemView);
            title = (TextView) itemView;
        }
    }


}
