package com.lalocal.lalocal.view.viewholder.article;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.RePlyActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.CommentRowBean;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.view.adapter.ArticleCommentListAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

/**
 * Created by wangjie on 2016/12/16.
 */

public class ArticleCommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private Context mContext;

    private LinearLayout mLayoutClick;
    private RoundedImageView mRoundAvatar;
    private TextView mTvNickname;
    private TextView mTvReplyTime;
    private TextView mTvContent;
    private TextView mTvReplyUser;

    private int mPosition = 0;

    private ArticleCommentListAdapter.MyOnItemClickListener mItemClickListener;

    public ArticleCommentViewHolder(final Context context, View itemView, ArticleCommentListAdapter.MyOnItemClickListener listener) {
        super(itemView);

        mContext = context;
        mItemClickListener = listener;

        // 关联控件
        mRoundAvatar = (RoundedImageView) itemView.findViewById(R.id.img_user_avatar);
        mTvNickname = (TextView) itemView.findViewById(R.id.tv_nickname);
        mTvReplyTime = (TextView) itemView.findViewById(R.id.tv_reply_time);
        mTvContent = (TextView) itemView.findViewById(R.id.tv_content);
        mTvReplyUser = (TextView) itemView.findViewById(R.id.tv_reply_user);
        mLayoutClick = (LinearLayout) itemView.findViewById(R.id.layout_click);
    }

    /**
     * 数据初始化
     * @param bean
     * @param position
     */
    public void initData(CommentRowBean bean, int position) {

        mPosition = position;

        // 评论内容
        String content = bean.getContent();
        // 被回复的用户
        LiveUserBean targetUser = (LiveUserBean) bean.getTargetUser();
        // 被回复的用户的昵称
        String targetNickname = "一位不愿意透露姓名的网友";
        // 评论时间
        String commentTime = bean.getDateTime();
        // 如果被回复的用户不为空
        if (targetUser != null) {
            targetNickname = targetUser.getNickName();
        }
        // 被回复的用户的评论
        CommentRowBean parentComment = bean.getPartentComment();
        // 获取评论人信息
        LiveUserBean user = bean.getUser();
        // 获取评论人id
        int userId = user.getId();
        // 评论人头像
        String avatar = "";
        // 评论人昵称
        String userNickname = null;
        // 如果评论人存在
        if (user != null) {
            // 获取评论人头像
            avatar = user.getAvatar();
            // 获取评论人昵称
            userNickname = user.getNickName();
        }

        // 如果评论人昵称为空
        if (TextUtils.isEmpty(userNickname)) {
            userNickname = "一位不愿意透露姓名的网友";
        }

        // 如果没有回复对象
        if (parentComment == null) {
            // 隐藏被回复内容
            mTvReplyUser.setVisibility(View.GONE);
        } else { // 如果有回复对象
            // 显示被回复内容
            mTvReplyUser.setVisibility(View.VISIBLE);
            // 被回复的用户的评论内容
            String parentContent = parentComment.getContent();
            // 被回复的信息填充，使用SpannableString
            SpannableString spannableString = new SpannableString(targetNickname + "：" + parentContent);
            // 设置被回复的用户的昵称字体颜色
            int colorBlue = ContextCompat.getColor(mContext, R.color.color_0065b2);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(colorBlue);
            spannableString.setSpan(colorSpan, 0, targetNickname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            mTvReplyUser.setText(spannableString);
        }

        // 如果评论人头像不为空
        if (!TextUtils.isEmpty(avatar)) {
            // 填充评论人头像
            Glide.with(mContext)
                    .load(avatar)
                    .placeholder(R.drawable.androidloading)
                    .into(mRoundAvatar);
        }
        // 如果有评论时间
        if (!TextUtils.isEmpty(commentTime)) {
            mTvReplyTime.setVisibility(View.VISIBLE);
            mTvReplyTime.setText(commentTime);
        } else {
            mTvReplyTime.setVisibility(View.GONE);
        }
        // 填充评论人昵称
        mTvNickname.setText(userNickname);
        // 填充评论内容
        mTvContent.setText(content);
        // 布局点击事件
        mLayoutClick.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mItemClickListener.onItemClick(v, mPosition);
    }
}
