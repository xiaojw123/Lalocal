package com.lalocal.lalocal.view.viewholder.live;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.AttentionActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.util.SPCUtils;

/**
 * Created by wangjie on 2016/12/14.
 */

public class AttentionViewHolder extends RecyclerView.ViewHolder {


    // 我的关注
    private CardView cvAttention;
    // 用户layout
    private RelativeLayout layoutUser;
    // 我的关注头像
    private ImageView imgAvatar;
    // 上下文
    private Context mContext;

    public AttentionViewHolder(Context context, View itemView) {
        super(itemView);

        mContext = context;

        itemView.setFocusable(false);

        // -关联控件
        imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
        layoutUser = (RelativeLayout) itemView.findViewById(R.id.layout_attention_user);
        cvAttention = (CardView) itemView.findViewById(R.id.card_view_attention);

    }

    /**
     * 初始化数据
     *
     * @param bean
     */
    public void initData(final LiveUserBean bean) {
        if (bean != null) {
            layoutUser.setVisibility(View.VISIBLE);
            // 获取头像
            String avatar = bean.getAvatar();
            // 如果接口有头像链接
            if (!TextUtils.isEmpty(avatar)) {
                Glide.with(mContext)
                        .load(avatar)
                        .placeholder(R.drawable.androidloading)
                        .into(imgAvatar);
            }
        } else {
            layoutUser.setVisibility(View.INVISIBLE);
        }

        // 我的关注点击事件
        cvAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户id
                int userId = UserHelper.getUserId(mContext);
                // 如果用户为登录状态
                if (userId != -1) {
                    // 获取接口的时间key
                    String baseGetKey = "live_index_timestamp_get_";
                    // 查看消息的时间key
                    String baseScanKey = "live_index_timestamp_scan_";
                    // 获取最近一次拉取接口的时间戳
                    String dateTime = SPCUtils.getString(mContext, baseGetKey + String.valueOf(userId));
                    // 将拉取接口的时间戳存到最近一次查看消息的时间戳键值对里
                    SPCUtils.put(mContext, baseScanKey + String.valueOf(userId), dateTime);
                }

                // 跳转我的关注页面查看更多关注
                mContext.startActivity(new Intent(mContext, AttentionActivity.class));
            }
        });
    }
}
