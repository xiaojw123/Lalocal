package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.util.DensityUtil;

import org.litepal.crud.DataSupport;

import java.util.List;


/**
 * Created by android on 2016/11/19.
 */
public class LocationHistoryAdapter extends BaseRecyclerAdapter {
    List<SparseArray<String>> datas;
    Context context;
    Resources res;
    public static final int ITEM_HISTORY = 10;
    public static final int ITEM_HOT = 11;
    public static final int ITEM_HISTORY_TITLE = 12;
    public static final int ITEM_HOT_TITLE = 13;

    public LocationHistoryAdapter(Context context, List<SparseArray<String>> datas) {
        this.datas = datas;
        this.context = context;
        res = context.getResources();
    }

    public void updateItems(List<SparseArray<String>> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_HISTORY) {//历史地址
            View mybg = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_history_detete_menu, null);
            mybg.setLayoutParams(new ViewGroup.LayoutParams((int) res.getDimension(R.dimen.search_history_delete_width), ViewGroup.LayoutParams.MATCH_PARENT));
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item_history, null);
            view.setPadding(DensityUtil.dip2px(context,15),0,0,0);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            view.setLayoutParams(layoutParams);
            return new LiveHistoryItem(context, mybg, view, RecyclerViewDragHolder.EDGE_RIGHT).getDragViewHolder();
        } else if (viewType == ITEM_HOT)     //附近地址

        {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item_history, parent, false);
            view.setPadding(DensityUtil.dip2px(context,15),0,0,0);
            return new HotItem(view);
        } else if (viewType == ITEM_HISTORY_TITLE)//历史地址头

        {
            TextView textView = new TextView(context);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) res.getDimension(R.dimen.search_history_title_Height));
           layoutParams.setMargins(DensityUtil.dip2px(context,15),0,0,0);
            textView.setLayoutParams(layoutParams);
            textView.setTextColor(context.getResources().getColor(R.color.color_b3));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimension(R.dimen.text_size_12_sp));
            return new HistoryTitle(textView);
        } else if (viewType == ITEM_HOT_TITLE)//附近地址头

        {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item_hot, parent, false);
            view.setPadding(DensityUtil.dip2px(context,15),0,0,0);
            return new HotTitle(view);
        }

        return null;
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final SparseArray<String> sp = datas.get(position);
        final String value = sp.valueAt(0);
        if (holder instanceof RecyclerViewDragHolder.DragViewHolder) {
            LiveHistoryItem myHolder = (LiveHistoryItem) RecyclerViewDragHolder.getHolder(holder);
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
                        listener.onItemClickListener(v, position);
                    }
                }
            });
        }else if (holder instanceof HotTitle) {
            ((HotTitle) holder).itemKey.setText(value);
        } else if (holder instanceof HistoryTitle) {
            ((HistoryTitle) holder).title.setText(value);
        }

    }

    @Override
    public int getItemViewType(int position) {
        SparseArray<String> sp = datas.get(position);
        return sp.keyAt(0);
    }
    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public void remove(int position) {
        SparseArray<String> sp = datas.get(position);
        if (sp != null) {
            String value = sp.valueAt(0);
            if (!TextUtils.isEmpty(value)) {
                DataSupport.deleteAll(com.lalocal.lalocal.model.LiveHistoryItem.class, "name=?", value);
            }
        }
        datas.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }

    class HotTitle extends RecyclerView.ViewHolder {

        TextView itemKey;
        public HotTitle(View itemView) {
            super(itemView);
            itemKey = (TextView) itemView.findViewById(R.id.item_hot_key);
        }
    }


    class HistoryTitle extends RecyclerView.ViewHolder {

    TextView title;

    public HistoryTitle(View itemView) {
        super(itemView);
        title = (TextView) itemView;
    }
}

class HotItem extends RecyclerView.ViewHolder {
    TextView itemKey;
    public HotItem(View itemView) {
        super(itemView);
        itemKey = (TextView) itemView.findViewById(R.id.item_history_key_tv);
    }
}

    class LiveHistoryItem extends RecyclerViewDragHolder {
        private TextView title;
        private FrameLayout deleteItem;
        public LiveHistoryItem(Context context, View bgView, View topView) {
            super(context, bgView, topView);
        }
        public LiveHistoryItem(Context context, View bgView, View topView, int mTrackingEdges) {
            super(context, bgView, topView, mTrackingEdges);
        }
        @Override
        public void initView(View itemView) {

            title = (TextView) itemView.findViewById(R.id.item_history_key_tv);
            deleteItem = (FrameLayout) itemView.findViewById(R.id.delete);
        }
    }
}
