package com.lalocal.lalocal.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.RecommendRowsBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

/**
 * Created by lenovo on 2016/6/19.
 */
public class XListviewAdapter extends BaseAdapter {
    private Context mContext;
    private List<RecommendRowsBean> allRows;
    public XListviewAdapter(Context context, List<RecommendRowsBean> allRows) {
        this.mContext=context;
        this.allRows=allRows;

    }
    public  void refresh(List<RecommendRowsBean> allRows){
        this.allRows=allRows;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return allRows==null?0:allRows.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            convertView=View.inflate(mContext, R.layout.recommend_listview_item2,null);
            viewHolder=new ViewHolder();
            viewHolder.recommendIv= (ImageView) convertView.findViewById(R.id.recommend_listview_item2_iv);
            viewHolder.recommendIv.setScaleType(ImageView.ScaleType.FIT_XY);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        RecommendRowsBean recommendRowsBean = allRows.get(position);
        DrawableUtils.displayImg(mContext, viewHolder.recommendIv, recommendRowsBean.getPhoto());
        return convertView;
    }

    class ViewHolder{
        ImageView recommendIv;
    }
}
