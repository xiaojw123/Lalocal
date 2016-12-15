package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.LiveAttentionBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/10/10.
 */
public class LiveAttentionDemoAdapter extends RecyclerView.Adapter {


    private List<LiveAttentionBean> list;
    private Context mContext;
    private LayoutInflater inflater;
    private boolean one=true;
    private boolean two=true;
    private boolean three=true;

    public LiveAttentionDemoAdapter(Context context, List<LiveAttentionBean> list) {
        this.list = list;
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.demo_item_layout, parent, false);
        DemoAdapter demoAdapter = new DemoAdapter(view);
        return demoAdapter;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DemoAdapter demoAdapter = (DemoAdapter) holder;
        LiveAttentionBean liveAttentionBean = list.get(position);
        switch (liveAttentionBean.getType()) {
            case 0:
                if(one){
                    demoAdapter.tvTitle.setVisibility(View.VISIBLE);
                    demoAdapter.tvTitle.setText("关注的好友直播");
                    demoAdapter.tvTitle.setBackgroundColor(Color.GREEN);
                    one=false;
                }
                demoAdapter.tvContent.setText(liveAttentionBean.getLiveContent());
                break;
            case 1:
                if(two){
                    demoAdapter.tvTitle.setVisibility(View.VISIBLE);
                    demoAdapter.tvTitle.setText("关注的好友直播回放");
                    demoAdapter.tvTitle.setBackgroundColor(Color.RED);
                    two=false;
                }
                demoAdapter.tvContent.setText(liveAttentionBean.getLiveContent());
                break;
            case 2:
                demoAdapter.tvTitle.setText("推荐达人");
                demoAdapter.tvTitle.setBackgroundColor(Color.BLUE);
                demoAdapter.tvContent.setText(liveAttentionBean.getLiveContent());
                break;

        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class DemoAdapter extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_content)
        TextView tvContent;

        @BindView(R.id.tv_title)
        TextView tvTitle;
        public DemoAdapter(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
