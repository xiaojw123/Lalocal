package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xiaojw on 2016/9/9.
 */
public class LiveSearchAdapter extends BaseRecyclerAdapter {

    List<LiveRowsBean> mItems;
    Context mContext;

    public LiveSearchAdapter(List<LiveRowsBean> items) {
        mItems = items;
    }

    public void updatItems(List<LiveRowsBean> items) {
        mItems = items;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.live_search_item_layout, parent, false);
        return new LiveSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (mItems != null && mItems.size() > 0) {
            LiveRowsBean item = mItems.get(position);
            if (item != null) {
                LiveSearchViewHolder itemHolder = (LiveSearchViewHolder) holder;
                if (item.getStatus() == 1) {
                    itemHolder.meetingTagLayout.setVisibility(View.VISIBLE);
                } else {
                    if (itemHolder.meetingTagLayout.getVisibility() == View.VISIBLE) {
                        itemHolder.meetingTagLayout.setVisibility(View.INVISIBLE);
                    }
                }
                DrawableUtils.displayImg(mContext, itemHolder.photoImg, item.getPhoto());
                String title = item.getTitle();
                String onlinUserNumb = String.valueOf(item.getOnlineNumber());
                if (!TextUtils.isEmpty(title)) {
                    itemHolder.titleTv.setText(title);
                }
                if (!TextUtils.isEmpty(onlinUserNumb)) {
                    itemHolder.onlinUserNumTv.setText(onlinUserNumb);
                }
                itemHolder.onlinUserNumTv.getCompoundDrawables()[0].setAlpha(20);
                itemHolder.locTv.setText(item.getAddress());
                itemHolder.locTv.getCompoundDrawables()[0].setAlpha(20);
                LiveUserBean userBean = item.getUser();
                if (userBean != null) {
                    DrawableUtils.displayImg(mContext, itemHolder.avatarImg, userBean.getAvatar(), -1);
                    String nickName = userBean.getNickName();
                    if (!TextUtils.isEmpty(nickName)) {
                        itemHolder.userNameTv.setText(nickName);
                    }
                }
                itemHolder.itemView.setTag(item);
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null) {
                            listener.onItemClickListener(v, position);
                        }

                    }
                });


            }


        }


    }

    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    class LiveSearchViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.live_search_item_photo)
        ImageView photoImg;
        @BindView(R.id.live_search_item_useravatar)
        CircleImageView avatarImg;
        @BindView(R.id.live_search_item_title)
        TextView titleTv;
        @BindView(R.id.live_search_onlinUser_num)
        TextView onlinUserNumTv;
        @BindView(R.id.live_search_item_username)
        TextView userNameTv;
        @BindView(R.id.live_search_item_loc)
        TextView locTv;
        @BindView(R.id.live_search_item_meeting_tag)
        LinearLayout meetingTagLayout;


        public LiveSearchViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
