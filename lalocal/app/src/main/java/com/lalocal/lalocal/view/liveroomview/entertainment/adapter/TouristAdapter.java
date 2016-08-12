package com.lalocal.lalocal.view.liveroomview.entertainment.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.util.DrawableUtils;
import com.netease.nimlib.sdk.chatroom.model.ChatRoomMember;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/7/28.
 */
public class TouristAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ChatRoomMember> items;
    private LayoutInflater inflater;
    private List<LiveRowsBean> rowsBeen;

    public TouristAdapter(Context context, List<ChatRoomMember> items) {
        this.mContext = context;
        this.items = items;
        inflater = LayoutInflater.from(mContext);
    }
    public void refresh(List<ChatRoomMember> items) {
        this.items = items;

        notifyDataSetChanged();
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
      final  ChatRoomMember member = items.get(position);
        DrawableUtils.displayImg(mContext, liveViewHodler.touristItem, member.getAvatar());
        liveViewHodler.touristItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onTouristItemClickListener!=null){
                    if(position==0){
                        onTouristItemClickListener.showTouristInfo(member,true);
                    }else {
                        onTouristItemClickListener.showTouristInfo(member,false);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items==null?0:items.size();
    }


    public class LiveViewHodler extends RecyclerView.ViewHolder {
        @BindView(R.id.tourist_item)
        CircleImageView touristItem;
        public LiveViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //recyclerview条目点击事件
    private OnTouristItemClickListener onTouristItemClickListener;

    public interface OnTouristItemClickListener {
        void showTouristInfo( ChatRoomMember member,boolean isMasterAccount );
    }

    public void setOnTouristItemClickListener(OnTouristItemClickListener onTouristItemClickListener) {
        this.onTouristItemClickListener = onTouristItemClickListener;
    }
}
