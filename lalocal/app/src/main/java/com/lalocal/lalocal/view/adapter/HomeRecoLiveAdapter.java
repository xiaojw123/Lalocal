package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.activity.AudienceActivity;
import com.lalocal.lalocal.live.im.ui.widget.CircleImageView;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.WrapContentImageView;

import java.util.List;

/**
 * Created by wangjie on 2016/10/25.
 */
public class HomeRecoLiveAdapter extends PagerAdapter {
    private Context context;
    private List<LiveRowsBean> hotLiveList;

    public HomeRecoLiveAdapter(Context context, List<LiveRowsBean> hotLiveList) {
        this.context = context;
        this.hotLiveList = hotLiveList;
    }

    @Override
    public int getCount() {
//                return Math.min(hotLiveList == null ? 0 : hotLiveList.2size(), MAX_HOT_LIVE);
        return hotLiveList == null ? 0 : hotLiveList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_recommend_hotlive_viewpager_item, null);
        final LiveRowsBean liveRowsBean = hotLiveList.get(position);
        SubLiveViewHolder liveViewHolder = new SubLiveViewHolder();
        liveViewHolder.container = (FrameLayout) view.findViewById(R.id.container);
        liveViewHolder.cardView = (CardView) view.findViewById(R.id.card_view);
        liveViewHolder.imgLivePic = (WrapContentImageView) view.findViewById(R.id.img_live_pic);
        liveViewHolder.imgIcon = (ImageView) view.findViewById(R.id.icon);
        liveViewHolder.tvLiveIconContent = (TextView) view.findViewById(R.id.tv_icon_content);
        liveViewHolder.tvLiveTitle = (TextView) view.findViewById(R.id.tv_live_title);
        liveViewHolder.imgLiveAvatar = (CircleImageView) view.findViewById(R.id.img_live_avatar);

        // 设置直播用户头像
        LiveUserBean user = liveRowsBean.getUser();
        // 设置播放图片
        String photo = user. getAvatarOrigin();
        if (!TextUtils.isEmpty(liveRowsBean.getPhoto())) {
            DrawableUtils.displayRadiusImg(context, liveViewHolder.imgLivePic, liveRowsBean.getPhoto(), DensityUtil.dip2px(context, 3), R.drawable.androidloading);
        } else if (!TextUtils.isEmpty(photo)) {
            DrawableUtils.displayRadiusImg(context, liveViewHolder.imgLivePic, user.getAvatarOrigin(), DensityUtil.dip2px(context, 3), R.drawable.androidloading);
        }

        // 如果图片链接一致，说明是公告视频，下方显示地理位置
        if (liveRowsBean.getType() == 1) { // 云信通过type来判断：1-系统，2-用户；声网通过cname来判断：null-系统，否则为用户
            liveViewHolder.imgIcon.setImageResource(R.drawable.peopleliving_location_darkic);
            // 设置图标透明度为20%
            liveViewHolder.imgIcon.getDrawable().setAlpha(20);
            String address = liveRowsBean.getAddress();
            liveViewHolder.tvLiveIconContent.setText(address);
        } else {
            liveViewHolder.imgIcon.setImageResource(R.drawable.manypeople_dark);
            // 设置图标透明度为20%
            liveViewHolder.imgIcon.getDrawable().setAlpha(20);
            // 设置在线用户人数
            int onlineUser = liveRowsBean.getOnlineUser();
            // 人数以“,”将千分位隔开
            liveViewHolder.tvLiveIconContent.setText(CommonUtil.formatNumWithComma(onlineUser));
        }

        // 设置播放标题
        String liveTitle = liveRowsBean.getTitle();
        if (!TextUtils.isEmpty(liveTitle)) {
            liveViewHolder.tvLiveTitle
                    .setText(liveTitle);
        }

        // 获取头像uri
        final String avatar = user.getAvatar();
        // 设置头像
        DrawableUtils.displayImg(context, liveViewHolder.imgLiveAvatar, avatar, R.drawable.androidloading);

        // 获取视频播放相关数据
        final int roomId = liveRowsBean.getRoomId();
        final String pullUrl = liveRowsBean.getPullUrl();
        final String nickName = user.getNickName();
        final int userId = user.getId();
        final SpecialShareVOBean shareVO = liveRowsBean.getShareVO();
        final int type = liveRowsBean.getType();
        Object annoucement = liveRowsBean.getAnnoucement();
        String ann = null;
        if (annoucement != null) {
            ann = annoucement.toString();
        } else {
            ann = "这是公告哈";
        }
        final String finalAnn = ann;
        final int channelId = liveRowsBean.getId();

        final String finalAnn1 = ann;
        liveViewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转播放界面
               // AudienceActivity.start(context, liveRowsBean, finalAnn1);
                Intent intent1=new Intent(context, AudienceActivity.class);
                intent1.putExtra("id",String.valueOf(liveRowsBean.getId()));
                context.startActivity(intent1);
            }
        });
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    class SubLiveViewHolder {
        FrameLayout container;
        CardView cardView;
        WrapContentImageView imgLivePic;
        TextView tvLiveTitle;
        TextView tvLiveIconContent;
        CircleImageView imgLiveAvatar;
        ImageView imgIcon;
    }
}
