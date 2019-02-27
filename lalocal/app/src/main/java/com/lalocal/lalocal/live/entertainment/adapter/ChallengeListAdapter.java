package com.lalocal.lalocal.live.entertainment.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.live.entertainment.model.ChallengeDetailsResp;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by android on 2016/10/8.
 */
public class ChallengeListAdapter extends RecyclerView.Adapter {


    private Context mContext;
    private LayoutInflater inflater;
    private List<ChallengeDetailsResp.ResultBean> result;
    private int newChallengeNumber;
    private int completeChallengeNumber;
    private int newFirstPosition = -1;
    private boolean newFirstItem = true;
    private boolean completeFirstItem = true;
    private int completeFirstPosition = -1;

    public ChallengeListAdapter(Context context, List<ChallengeDetailsResp.ResultBean> result, int newChallengeNumber, int completeChallengeNumber) {
        this.mContext = context;
        this.result = result;
        this.newChallengeNumber = newChallengeNumber;
        this.completeChallengeNumber = completeChallengeNumber;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.challenge_list_item_layout, parent, false);

        ChallengeListHodler holder = new ChallengeListHodler(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChallengeListHodler hodler = (ChallengeListHodler) holder;
        ChallengeDetailsResp.ResultBean resultBean = result.get(position);
        hodler.challengeListItemNew.setVisibility(View.GONE);
        hodler.chalengeListItemComplete.setVisibility(View.GONE);
        hodler.challengeListItemWaive.setVisibility(View.GONE);
        hodler.challengeListItemStatus.setVisibility(View.VISIBLE);
        ViewGroup.LayoutParams layoutParams = hodler.challengeListItem.getLayoutParams();

        final int id = resultBean.getId();
        final int status = resultBean.getStatus();
        DrawableUtils.displayImg(mContext, hodler.challengeListItemInitiateHead, resultBean.getCreater().getAvatar());
        hodler.challengeListItemInitiateName.setText(resultBean.getCreater().getNickName());
        hodler.challengeListItemContent.setText(resultBean.getContent());
        hodler.challengeListItemMoney.setText(String.valueOf(resultBean.getTargetGold()));

        if (status == 0) {
            if (newFirstItem) {
                newFirstItem = false;
                newFirstPosition = position;
            }
            if (newFirstPosition == position) {
                hodler.challengeListItemStatus.setVisibility(View.VISIBLE);
                hodler.challengeListItemNew.setVisibility(View.VISIBLE);
                hodler.challengeListItemStatus.setText("任务列表(" + newChallengeNumber + ")");
                layoutParams.height = DensityUtil.dip2px(mContext, 160);
            } else {
                hodler.challengeListItemStatus.setVisibility(View.GONE);
                layoutParams.height = DensityUtil.dip2px(mContext, 137);
            }
            hodler.chalengeListItemComplete.setVisibility(View.VISIBLE);
            hodler.challengeListItemWaive.setVisibility(View.VISIBLE);
            hodler.chalengeListItemComplete.setText("接受");
            hodler.challengeListItemWaive.setText("拒绝");
            hodler.challengeListItemNew.setVisibility(View.VISIBLE);
        } else if (status == 1) {
            layoutParams.height = DensityUtil.dip2px(mContext, 160);

            hodler.challengeListItemStatus.setText("正在进行(任务完成才能接受新任务)");
            hodler.chalengeListItemComplete.setText("完成");
            hodler.challengeListItemWaive.setText("放弃");
            hodler.chalengeListItemComplete.setVisibility(View.VISIBLE);
            hodler.challengeListItemWaive.setVisibility(View.VISIBLE);
            hodler.challengeListItemNew.setVisibility(View.GONE);
        } else if (status == 2) {
            if (completeFirstItem) {
                completeFirstItem = false;
                completeFirstPosition = position;
            }
            if (completeFirstPosition == position) {
                hodler.challengeListItemStatus.setVisibility(View.VISIBLE);
                hodler.challengeListItemStatus.setText("已完成任务(" + completeChallengeNumber + ")");
                layoutParams.height = DensityUtil.dip2px(mContext, 160);
            } else {
                hodler.challengeListItemStatus.setVisibility(View.GONE);
                layoutParams.height = DensityUtil.dip2px(mContext, 147);
            }

            hodler.challengeListItemStatus.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            hodler.challengeListItemStatus.getPaint().setAntiAlias(true);
            hodler.challengeListItemNew.setVisibility(View.GONE);
        }

        hodler.chalengeListItemComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmmceeClickListener != null) {
                    if (status == 0) {
                        onEmmceeClickListener.emmceeClickStatus(1, id);
                    } else if (status == 1) {
                        onEmmceeClickListener.emmceeClickStatus(2, id);
                    }
                }
            }
        });

        hodler.challengeListItemWaive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onEmmceeClickListener != null) {
                    if (status == 0) {
                        onEmmceeClickListener.emmceeClickStatus(3, id);
                    } else if (status == 1) {
                        onEmmceeClickListener.emmceeClickStatus(4, id);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return result == null ? 0 : result.size();
    }

    public class ChallengeListHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.challenge_list_item_status)
        TextView challengeListItemStatus;
        @BindView(R.id.challenge_list_item_initiate_head)
        CircleImageView challengeListItemInitiateHead;
        @BindView(R.id.challenge_list_item_initiate_name)
        TextView challengeListItemInitiateName;
        @BindView(R.id.challenge_list_item_new)
        TextView challengeListItemNew;
        @BindView(R.id.challenge_list_item_content)
        TextView challengeListItemContent;
        @BindView(R.id.challenge_list_item_money)
        TextView challengeListItemMoney;
        @BindView(R.id.chalenge_list_item_complete)
        TextView chalengeListItemComplete;
        @BindView(R.id.challenge_list_item_waive)
        TextView challengeListItemWaive;
        @BindView(R.id.challenge_list_item)
        RelativeLayout challengeListItem;

        public ChallengeListHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private OnEmmceeClickListener onEmmceeClickListener;

    public interface OnEmmceeClickListener {
        void emmceeClickStatus(int status, int challengeId);
    }

    public void setOnLiveItemClickListener(OnEmmceeClickListener onEmmceeClickListener) {
        this.onEmmceeClickListener = onEmmceeClickListener;
    }
}

