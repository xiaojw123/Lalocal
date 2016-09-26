package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by android on 2016/7/18.
 */
public class LiveMainAdapter extends RecyclerView.Adapter implements View.OnClickListener {


    private Context mContext;
    private LayoutInflater inflater;
    private List<LiveRowsBean> rowsBeen;

    public LiveMainAdapter(Context context, List<LiveRowsBean> rowsBeen) {
        this.mContext = context;
        this.rowsBeen = rowsBeen;
        inflater = LayoutInflater.from(mContext);
    }

    public void refresh(List<LiveRowsBean> rowsBeen) {
        this.rowsBeen = rowsBeen;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.live_mian_recy_item, parent, false);
        LiveViewHodler holder = new LiveViewHodler(view);
        view.setOnClickListener(this);
        return holder;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveViewHodler liveViewHodler = (LiveViewHodler) holder;
       final LiveRowsBean liveRowsBean = rowsBeen.get(position);
        liveViewHodler.liveTheme.setText(liveRowsBean.getTitle());
        liveViewHodler.liveOnlineCountTv.setText(liveRowsBean.getOnlineUser() + "");
        LiveUserBean user = liveRowsBean.getUser();
        liveViewHodler.liveCompereHeadTv.setText(user.getNickName());
        DrawableUtils.displayImg(mContext, liveViewHodler.liveCompereHeadPortrait, user.getAvatar());
        DrawableUtils.displayRadiusImg(mContext,liveViewHodler.liveCoverIv,liveRowsBean.getPhoto(), DensityUtil.dip2px(mContext,3),R.drawable.androidloading);



        liveViewHodler.liveCoverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onLiveItemClickListener != null) {
                    onLiveItemClickListener.goLiveRoom(liveRowsBean);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return rowsBeen == null ? 0 : rowsBeen.size();
    }

    @Override
    public void onClick(View v) {


    }

    public class LiveViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.live_status)
        TextView liveStatus;
        @BindView(R.id.live_theme)
        TextView liveTheme;
        @BindView(R.id.live_online_count_tv)
        TextView liveOnlineCountTv;
        @BindView(R.id.live_compere_location)
        TextView liveCompereLocation;
        @BindView(R.id.live_people_layout)
        LinearLayout livePeopleLayout;
        @BindView(R.id.live_compere_head_portrait)
        CircleImageView liveCompereHeadPortrait;
        @BindView(R.id.live_compere_head_tv)
        TextView liveCompereHeadTv;
        @BindView(R.id.live_cover_iv)
        ImageView liveCoverIv;

        public LiveViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    private OnLiveItemClickListener onLiveItemClickListener;

    public interface OnLiveItemClickListener {
        void goLiveRoom( LiveRowsBean liveRowsBean);
    }

    public void setOnLiveItemClickListener(OnLiveItemClickListener onLiveItemClickListener) {
        this.onLiveItemClickListener = onLiveItemClickListener;
    }
}
