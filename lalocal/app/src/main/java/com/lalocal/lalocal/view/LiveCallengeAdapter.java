package com.lalocal.lalocal.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.ChannelRecord;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiaojw on 2016/10/12.
 */

public class LiveCallengeAdapter extends BaseRecyclerAdapter {
    Context mContext;
    List<ChannelRecord.ChallengeRecordsBean> mItems;
    public LiveCallengeAdapter(List<ChannelRecord.ChallengeRecordsBean> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_challenge, parent, false);
        return new CallengeHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mItems != null&&mItems.size()>0) {
            ChannelRecord.ChallengeRecordsBean item = mItems.get(position);
            if (item != null) {
                ChannelRecord.ChallengeRecordsBean.UserBean user=item.getUser();
                CallengeHolder itemHolder= (CallengeHolder) holder;
                itemHolder.itemChllengeTimepoint.setText(item.getTime());
                itemHolder.itemChllengeContent.setText(item.getContent());
                itemHolder.itemChllengeScore.setText("+"+ CommonUtil.formartNum(item.getScore()));
                if (user!=null){
                    DrawableUtils.displayImg(mContext,itemHolder.itemChallengeAvatar,user.getAvatar());
                    itemHolder.itemChallengeUsername.setText(user.getNickName());
                }


            }


        }


    }

    @Override
    public int getItemCount() {
        return mItems!=null&&mItems.size()>0?mItems.size():0;
    }

    class CallengeHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_challenge_avatar)
        CircleImageView itemChallengeAvatar;
        @BindView(R.id.item_challenge_username)
        TextView itemChallengeUsername;
        @BindView(R.id.item_chllenge_timepoint)
        TextView itemChllengeTimepoint;
        @BindView(R.id.item_chllenge_content)
        TextView itemChllengeContent;
        @BindView(R.id.item_chllenge_score)
        TextView itemChllengeScore;
        public CallengeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
