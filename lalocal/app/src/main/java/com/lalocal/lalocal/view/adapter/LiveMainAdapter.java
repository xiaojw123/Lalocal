package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveRowsBean;
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

    //  DrawableUtils.displayImg(mContext, liveViewHodler.liveCompereHeadPortrait, user.getAvatar());
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.live_list_item_new_layout, parent, false);
        LiveViewHodler holder = new LiveViewHodler(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LiveViewHodler liveViewHodler = (LiveViewHodler) holder;
        final LiveRowsBean liveRowsBean = rowsBeen.get(position);
        boolean challengeStatus = liveRowsBean.isChallengeStatus();
      if(liveRowsBean.getStartAt()!=null&&liveRowsBean.getEndAt()!=null){
            liveViewHodler.liveListItemStatus.setText("回放");
            liveViewHodler.liveListItemStatus.setBackgroundResource(R.drawable.live_status_playback_bg);
        }else if(challengeStatus){
          liveViewHodler.liveListItemStatus.setText("挑战任务中");
          liveViewHodler.liveListItemStatus.setBackgroundResource(R.drawable.live_status_challenge_bg);
      }else {
          liveViewHodler.liveListItemStatus.setText("直播中");
          liveViewHodler.liveListItemStatus.setBackgroundResource(R.drawable.live_status_living_bg);
      }
        liveViewHodler.itemLiveName.setText(liveRowsBean.getUser().getNickName());
        if(liveRowsBean.getTitle()==null){
            liveViewHodler.liveListItemTitle.setText(liveRowsBean.getUser().getNickName());
        }else {
            liveViewHodler.liveListItemTitle.setText(liveRowsBean.getTitle());
        }

        liveViewHodler.itemLiveAdress.setText(liveRowsBean.getAddress());
        DrawableUtils.displayImg(mContext, liveViewHodler.liveCompereHeadPortrait,liveRowsBean.getUser().getAvatar());
        DrawableUtils.displayImg(mContext, liveViewHodler.itemLiveCoverIv,liveRowsBean.getPhoto());
        liveViewHodler.itemLiveCoverIv.setOnClickListener(new View.OnClickListener() {
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

        @BindView(R.id.live_list_item_title)
        TextView liveListItemTitle;
        @BindView(R.id.live_list_item_status)
        TextView liveListItemStatus;
        @BindView(R.id.item_live_cover_iv)
        ImageView itemLiveCoverIv;
        @BindView(R.id.live_compere_head_portrait)
        CircleImageView liveCompereHeadPortrait;
        @BindView(R.id.item_live_name)
        TextView itemLiveName;
        @BindView(R.id.item_live_adress)
        TextView itemLiveAdress;

        public LiveViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    private OnLiveItemClickListener onLiveItemClickListener;

    public interface OnLiveItemClickListener {
        void goLiveRoom(LiveRowsBean liveRowsBean);
    }

    public void setOnLiveItemClickListener(OnLiveItemClickListener onLiveItemClickListener) {
        this.onLiveItemClickListener = onLiveItemClickListener;
    }
}
