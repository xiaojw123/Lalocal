package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.LiveHomeAreaResp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/10/10.
 */
public class LiveClassifyGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private  List<LiveHomeAreaResp.ResultBean> result;
    private int selectedPosition = 0;// 选中的位置
    public LiveClassifyGridViewAdapter(Context mContext,List<LiveHomeAreaResp.ResultBean> result) {
        this.mContext = mContext;
        this.result = result;
    }
    public void setSelectedPosition(int position){
        this.selectedPosition=position;
    }


    @Override
    public int getCount() {
        return result == null ? 0 : result.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.live_classify_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        if(selectedPosition==position){
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.select_ic);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            viewHolder.liveClassifyItemTv.setCompoundDrawables(drawable,null,null,null);
            viewHolder.liveClassifyItemTv.setTextColor(Color.parseColor("#ffaa2a"));
        }else {
            viewHolder.liveClassifyItemTv.setCompoundDrawables(null,null,null,null);
            viewHolder.liveClassifyItemTv.setTextColor(Color.BLACK);
        }
        LiveHomeAreaResp.ResultBean resultBean = result.get(position);
        String name = resultBean.getName();
        viewHolder.liveClassifyItemTv.setText(name);
        return convertView;
    }


    static class ViewHolder {
        @BindView(R.id.live_classify_item_tv)
        TextView liveClassifyItemTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
