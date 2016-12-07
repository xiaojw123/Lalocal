package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.LiveManagerListBean;
import com.lalocal.lalocal.live.entertainment.model.LiveRoomAvatarSortResp;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/7/28.
 */
public class TouristAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private LayoutInflater inflater;
    List<LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean> userAvatars;

    public TouristAdapter(Context context,List<LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean> userAvatars) {
        this.mContext = context;
        this.userAvatars = userAvatars;
        inflater = LayoutInflater.from(mContext);
    }
    public void refresh(List<LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean> userAvatars) {
        this.userAvatars = userAvatars;
        notifyDataSetChanged();
        AppLog.i("TAG","头像列表刷新");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.tourist_list_item_layout, parent, false);
        LiveViewHodler holder = new LiveViewHodler(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        LiveViewHodler liveViewHodler = (LiveViewHodler) holder;
        final LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean userAvatarsBean = userAvatars.get(position);
        liveViewHodler.managerMark.setVisibility(View.GONE);
        if(userAvatarsBean.getAvatar()==null||userAvatarsBean.getAvatar().length()==0){
            liveViewHodler.touristItem.setImageResource(R.drawable.androidloading);
        }else{
            DrawableUtils.displayImg(mContext, liveViewHodler.touristItem, userAvatarsBean.getAvatar());
        }
        if(LiveConstant.result.size()>0){
            for(LiveManagerListBean bean :LiveConstant.result){
               if(bean.getId()==userAvatarsBean.getId()){
                   liveViewHodler.managerMark.setVisibility(View.VISIBLE);
               }
            }
        }
        liveViewHodler.touristItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onTouristItemClickListener!=null){
                    AppLog.i("TAG","点击头像查看用户role："+userAvatarsBean.getRole());
                    onTouristItemClickListener.showTouristInfo(userAvatarsBean,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return userAvatars==null?0:userAvatars.size();
    }


    public class LiveViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.tourist_item)
        CircleImageView touristItem;
        @BindView(R.id.live_item_manager_mark)
        ImageView managerMark;
        public LiveViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    private OnTouristItemClickListener onTouristItemClickListener;

    public interface OnTouristItemClickListener {
        void showTouristInfo( LiveRoomAvatarSortResp.ResultBean.UserAvatarsBean userAvatarsBean , boolean isMasterAccount);
    }

    public void setOnTouristItemClickListener(OnTouristItemClickListener onTouristItemClickListener) {
        this.onTouristItemClickListener = onTouristItemClickListener;
    }
}
