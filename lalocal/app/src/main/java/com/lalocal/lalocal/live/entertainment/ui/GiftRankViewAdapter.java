package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.TotalRanksBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/9/11.
 */
public class GiftRankViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<TotalRanksBean> currentRanksList;

    public GiftRankViewAdapter(Context mContext, List<TotalRanksBean> currentRanksList) {
        this.mContext = mContext;
        this.currentRanksList = currentRanksList;
    }

    @Override
    public int getCount() {
        return currentRanksList == null ? 0 : currentRanksList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.live_gifts_ranking_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        TotalRanksBean totalRanksBean = currentRanksList.get(position);
        DrawableUtils.displayImg(mContext,holder.liveGiftsRankingHead,totalRanksBean.getUser().getAvatar());
        holder.liveGiftsRankingName.setText(totalRanksBean.getUser().getNickName());
        holder.liveGiftsRankingCount.setText(String.valueOf(totalRanksBean.getGold()));
        holder.liveGiftsRankingNum.setText(String.valueOf(totalRanksBean.getRank()));
        if(totalRanksBean.getRank()>3){
            holder.liveGiftsRankingNum.setTextColor(Color.WHITE);
        }else{
            holder.liveGiftsRankingNum.setTextColor(Color.parseColor("#ffaa2a"));
        }
        if(totalRanksBean.getRank()==1){
            holder.liveGiftRankItemLayout.setBackgroundResource(R.drawable.stbg);
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.live_gifts_ranking_head)
        CircleImageView liveGiftsRankingHead;
        @BindView(R.id.live_gifts_ranking_name)
        TextView liveGiftsRankingName;
        @BindView(R.id.live_gifts_ranking_count)
        TextView liveGiftsRankingCount;
        @BindView(R.id.live_gifts_ranking_num)
        TextView liveGiftsRankingNum;
        @BindView(R.id.live_gift_rank_item_layout)
        RelativeLayout liveGiftRankItemLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
