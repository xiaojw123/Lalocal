package com.lalocal.lalocal.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.TargetPage;
import com.lalocal.lalocal.help.TargetType;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.im.ui.widget.CircleImageView;
import com.lalocal.lalocal.model.PraiseComment;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by xiaojw on 2016/12/21.
 */

public class PraiseCommentAdapter extends BaseRecyclerAdapter {
    private static final int COMMENT = 0;
    private static final int PRAISE = 1;
    private static final int REPLY = 2;

    Context mContext;
    List<PraiseComment.RowsBean> mItems;

    public void updatItems(List<PraiseComment.RowsBean> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_praise_comment, parent, false);
        return new PraiseCommentHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return mItems != null && mItems.size() > 0 ? mItems.size() : 0;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mItems != null && mItems.size() > 0) {
            PraiseComment.RowsBean rowsBean = mItems.get(position);
            if (rowsBean != null) {
                int opType = rowsBean.getOpType();
                PraiseCommentHolder praComHolder = (PraiseCommentHolder) holder;
                PraiseComment.RowsBean.OpUserVOBean opUserVo = rowsBean.getOpUserVO();
                String acition = "评论";
                switch (opType) {
                    case COMMENT:
                        praComHolder.itemCommentImg.setVisibility(View.VISIBLE);
                        praComHolder.itemCommentContent.setVisibility(View.VISIBLE);
                        praComHolder.itemReplyContent.setVisibility(View.GONE);
                        break;
                    case PRAISE:
                        acition = "赞了";
                        praComHolder.itemCommentImg.setVisibility(View.GONE);
                        praComHolder.itemCommentContent.setVisibility(View.GONE);
                        praComHolder.itemReplyContent.setVisibility(View.GONE);
                        break;
                    case REPLY:
                        praComHolder.itemCommentImg.setVisibility(View.GONE);
                        praComHolder.itemCommentContent.setVisibility(View.VISIBLE);
                        praComHolder.itemReplyContent.setVisibility(View.VISIBLE);
                        break;
                }
                if (opUserVo != null) {
                    String nicKName = opUserVo.getNickName();
                    praComHolder.itemAction.setText(Html.fromHtml("<font color=#b3b3b3>" + nicKName + "</font>  <font color=#666666>" + acition + "</font>"));
                    DrawableUtils.displayImg(mContext, praComHolder.itemAvatar, opUserVo.getAvatar());
                }
                praComHolder.itemCommentContent.setText(rowsBean.getContent());
                praComHolder.itemActionTime.setText(rowsBean.getDateTime());
                PraiseComment.RowsBean.TargetEntityBean targetEntity = rowsBean.getTargetEntity();
                if (targetEntity != null) {
                    final String targetType = targetEntity.getTargetType();
                    final String targetId = targetEntity.getTargetId();
                    if (TargetType.LIVE_VIDEO.equals(targetType) || TargetType.LIVE_PALY_BACK.equals(targetType)) {
                        praComHolder.playBtn.setVisibility(View.VISIBLE);
                    } else {
                        praComHolder.playBtn.setVisibility(View.GONE);
                    }
                    DrawableUtils.displayImg(mContext, praComHolder.itemImg, targetEntity.getPhoto());
                    praComHolder.itemTitle.setText(targetEntity.getTargetName());
                    praComHolder.itemLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TargetPage.gotoTargetPage(mContext, targetType, targetId, null, null);
                        }
                    });
                }
                PraiseComment.ParentDynamics pd = rowsBean.getParentDynamics();
                if (pd != null) {
                    PraiseComment.ParentDynamics.OpUserVOBean replyUserVo = pd.getOpUserVO();
                    String nickName = replyUserVo.getNickName();
                    int userid = replyUserVo.getId();
                    if (userid == UserHelper.getUserId(mContext)) {
                        nickName = "我";
                    }
                    praComHolder.itemReplyContent.setText(Html.fromHtml("<font color=#b3b3b3>" + nickName + ":</font>  <font color=#666666>" + pd.getContent() + "</font>"));

                }
            }
        }

    }

    class PraiseCommentHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_praise_comment_avatar)
        CircleImageView itemAvatar;
        @BindView(R.id.item_praise_comment_action)
        TextView itemAction;
        @BindView(R.id.item_praise_comment_action_time)
        TextView itemActionTime;
        @BindView(R.id.item_comment_content)
        TextView itemCommentContent;
        @BindView(R.id.item_praise_comment_img)
        ImageView itemImg;
        @BindView(R.id.item_praise_comment_title)
        TextView itemTitle;
        @BindView(R.id.item_comment_img)
        ImageView itemCommentImg;
        @BindView(R.id.item_reply_content)
        TextView itemReplyContent;
        @BindView(R.id.item_praise_comment_play)
        Button playBtn;
        @BindView(R.id.item_praise_comment_item)
        LinearLayout itemLayout;

        public PraiseCommentHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
