package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.xlistview.PowerImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/8/17.
 */
public class LiveMainListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private List<LiveRowsBean> rowsBeen;

    public LiveMainListAdapter(Context context, List<LiveRowsBean> rowsBeen) {
        this.mContext = context;
        this.rowsBeen = rowsBeen;
        inflater = LayoutInflater.from(mContext);
    }
    public void refresh(List<LiveRowsBean> rowsBeen) {
        this.rowsBeen = rowsBeen;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return rowsBeen == null ? 0 : rowsBeen.size();
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
        LiveViewHodler liveViewHodler=null;
        if(convertView==null){
            convertView= inflater.inflate(R.layout.live_mian_recy_item, parent, false);
            liveViewHodler=new LiveViewHodler();
            liveViewHodler.liveStatus= (TextView) convertView.findViewById(R.id.live_status);
            liveViewHodler.liveTheme= (TextView) convertView.findViewById(R.id.live_theme);
            liveViewHodler.liveOnlineCountTv= (TextView) convertView.findViewById(R.id.live_online_count_tv);
            liveViewHodler.liveCompereLocation= (TextView) convertView.findViewById(R.id.live_compere_location);
            liveViewHodler.livePeopleLayout= (LinearLayout) convertView.findViewById(R.id.live_people_layout);
            liveViewHodler.liveCompereHeadPortrait= (CircleImageView) convertView.findViewById(R.id.live_compere_head_portrait);
            liveViewHodler.liveCompereHeadTv= (TextView) convertView.findViewById(R.id.live_compere_head_tv);
            liveViewHodler.liveCoverIv= (PowerImageView) convertView.findViewById(R.id.live_cover_iv);
            convertView.setTag(liveViewHodler);
        }else {
            liveViewHodler= (LiveViewHodler) convertView.getTag();
        }
        LiveRowsBean liveRowsBean = rowsBeen.get(position);
        liveViewHodler.liveTheme.setText(liveRowsBean.getTitle());
        liveViewHodler.liveOnlineCountTv.setText(liveRowsBean.getOnlineUser() + "");
        LiveUserBean user = liveRowsBean.getUser();
        liveViewHodler.liveCompereHeadTv.setText(user.getNickName());
        DrawableUtils.displayImg(mContext, liveViewHodler.liveCompereHeadPortrait, user.getAvatar());
        DrawableUtils.displayRadiusImg(mContext,liveViewHodler.liveCoverIv,liveRowsBean.getPhoto(), DensityUtil.dip2px(mContext,3),R.drawable.androidloading);
        return convertView;
    }

    public class LiveViewHodler {
        TextView liveStatus;
        TextView liveTheme;
        TextView liveOnlineCountTv;
        TextView liveCompereLocation;
        LinearLayout livePeopleLayout;
        CircleImageView liveCompereHeadPortrait;
        TextView liveCompereHeadTv;
        PowerImageView liveCoverIv;

    }
}
