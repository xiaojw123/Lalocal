package com.lalocal.lalocal.live.entertainment.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.GiftDataResultBean;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by android on 2016/9/1.
 */
public class GiftGridViewAdpter extends BaseAdapter {
    private Context mContext;
    private List<GiftDataResultBean> resultBeen;
    private int selectedPosition = -1;// 选中的位置
    public GiftGridViewAdpter(Context mContext, List<GiftDataResultBean> resultBeen) {
        this.mContext = mContext;
        this.resultBeen = resultBeen;
    }

    @Override
    public int getCount() {
        return resultBeen == null ? 0 : resultBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    public void setSelectedPosition(int position){
        this.selectedPosition=position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.gift_list_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        GiftDataResultBean giftDataResultBean = resultBeen.get(position);
        DrawableUtils.displayImg(mContext,holder.giftPhotoIv,giftDataResultBean.getPhoto());
        int gold = giftDataResultBean.getGold();
        holder.giftPriceTv.setText(String.valueOf(gold));
        if(selectedPosition!=position){
            holder.giftSendCount.setText("");
            holder.giftSendCount.setVisibility(View.GONE);
            holder.giftItemBg.setBackgroundResource(0);
        }else {

            holder.giftSendCount.setVisibility(View.VISIBLE);
            holder.giftItemBg.setBackgroundResource(R.drawable.gift_sel);
        }
        convertView.setTag(R.id.giftdatabean,giftDataResultBean);
        return convertView;
    }

    static

    public class ViewHolder {
        @BindView(R.id.gift_send_count)
        TextView giftSendCount;
        @BindView(R.id.gift_photo_iv)
        ImageView giftPhotoIv;
        @BindView(R.id.gift_price_tv)
        TextView giftPriceTv;
        @BindView(R.id.gift_item_bg)
        LinearLayout giftItemBg;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);

        }
    }


}
