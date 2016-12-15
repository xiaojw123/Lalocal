package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;

import java.util.List;

/**
 * Created by android on 2016/7/19.
 */
public class LiveVisitorsAdapter extends RecyclerView.Adapter implements  View.OnClickListener {
    private Context mContext;
    private LayoutInflater inflater;
    private List<String> mDatas;
    public  LiveVisitorsAdapter(Context context,List<String> datas){
        this.mContext=context;
        this.mDatas=datas;
        inflater=LayoutInflater. from(mContext);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.live_telecast_visitors_item,parent, false);
        LiveVisitorsViewHodler holder= new LiveVisitorsViewHodler(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveVisitorsViewHodler visitorsViewHodler= (LiveVisitorsViewHodler) holder;
    }

    @Override
    public int getItemCount() {
        return mDatas==null?0:mDatas.size();
    }

    class  LiveVisitorsViewHodler extends RecyclerView.ViewHolder{

        public LiveVisitorsViewHodler(View itemView) {
            super(itemView);

        }
    }
    //recyclerview条目点击事件
    private OnLiveVisitorsItemClickListener onLiveVisitorsItemClickListener;
    public interface  OnLiveVisitorsItemClickListener{
        void checkVisitorsInfo();
    }
    public void setOnLiveVisitorsItemClickListener(OnLiveVisitorsItemClickListener onLiveVisitorsItemClickListener){
        this.onLiveVisitorsItemClickListener=onLiveVisitorsItemClickListener;
    }
    @Override
    public void onClick(View v) {
        if(onLiveVisitorsItemClickListener!=null){
            onLiveVisitorsItemClickListener.checkVisitorsInfo();
        }
    }
}
