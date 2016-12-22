package com.lalocal.lalocal.im.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.base.util.TimeUtil;
import com.lalocal.lalocal.model.RecentContactInfo;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.ShapeTextView;
import com.lalocal.lalocal.view.adapter.BaseRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiaojw on 2016/12/19.
 */

public class RecentContactAdapter extends BaseRecyclerAdapter {
    Context mContext;
    List<RecentContactInfo> mInfoList;

    public RecentContactAdapter(List<RecentContactInfo> infoList) {
        mInfoList = infoList;
    }

    public void updateItems(List<RecentContactInfo> infoList) {
        mInfoList = infoList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_recent_contact, parent, false);
        return new RecentContactHolder(view);
    }

    @Override
    public int getItemCount() {
        return mInfoList != null && mInfoList.size() > 0 ? mInfoList.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mInfoList == null || mInfoList.size() < 1) {
            return;
        }
        RecentContactInfo userInfo = mInfoList.get(position);
        if (userInfo == null) {
            return;
        }
        RecentContactHolder contactHolder = (RecentContactHolder) holder;
        DrawableUtils.displayImg(mContext, contactHolder.myImmessageAvatar, userInfo.getAvatar(),R.drawable.home_me_personheadnormal);
        contactHolder.itemRecentContactNickname.setText(userInfo.getNickName());
        int count = userInfo.getUnReadCount();
        if (count > 0) {
            String fomartCount = count > 99 ? count + "+" : String.valueOf(count);
            contactHolder.myImmessageCount.setVisibility(View.VISIBLE);
            contactHolder.myImmessageCount.setText(fomartCount);
        } else {
            contactHolder.myImmessageCount.setVisibility(View.INVISIBLE);
        }
        contactHolder.itemRecentContactLastmsg.setText(userInfo.getContent());
        contactHolder.itemRecentContactLasttime.setText(TimeUtil.getTimeShowString(userInfo.getTime(),true));
        contactHolder.itemView.setTag(userInfo);
    }


    class RecentContactHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.my_immessage_avatar)
        CircleImageView myImmessageAvatar;
        @BindView(R.id.my_immessage_count)
        ShapeTextView myImmessageCount;
        @BindView(R.id.item_recent_contact_nickname)
        TextView itemRecentContactNickname;
        @BindView(R.id.item_recent_contact_lastmsg)
        TextView itemRecentContactLastmsg;
        @BindView(R.id.item_recent_contact_lasttime)
        TextView itemRecentContactLasttime;

        public RecentContactHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }



}
