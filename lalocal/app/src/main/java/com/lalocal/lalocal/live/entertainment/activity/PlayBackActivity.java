package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.RePlyActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.live.entertainment.adapter.PlayBackReviewAdapter;
import com.lalocal.lalocal.live.entertainment.model.PlayBackResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewRowsBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.view.SharePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/10/12.
 */
public class PlayBackActivity extends BaseActivity {


    @BindView(R.id.video_cover)
    ImageView videoCover;
    @BindView(R.id.back_btn)
    ImageView backBtn;
    @BindView(R.id.enter_playback_layout)
    RelativeLayout enterPlaybackLayout;
    @BindView(R.id.playback_info_layout)
    XRecyclerView xRecyclerView;
    @BindView(R.id.review_title)
    TextView reviewTitle;
    @BindView(R.id.reply_write_iv)
    ImageView replyWriteIv;
    @BindView(R.id.reply_title_layout)
    RelativeLayout replyTitleLayout;
    @BindView(R.id.play_back_bottom_table_reply_count)
    TextView playBackBottomTableReplyCount;
    @BindView(R.id.play_back_bottom_table_reply_layout)
    LinearLayout playBackBottomTableReplyLayout;
    @BindView(R.id.play_back_bottom_table_tranmit_count)
    TextView playBackBottomTableTranmitCount;
    @BindView(R.id.play_back_bottom_table_transmit_layout)
    LinearLayout playBackBottomTableTransmitLayout;
    @BindView(R.id.play_back_bottom_table_collect_img)
    ImageView playBackBottomTableCollectImg;
    @BindView(R.id.play_back_bottom_table_collect_count)
    TextView playBackBottomTableCollectCount;
    @BindView(R.id.play_back_bottom_table_collect_layout)
    LinearLayout playBackBottomTableCollectLayout;
    @BindView(R.id.play_back_bottom_table_like_img)
    ImageView playBackBottomTableLikeImg;
    @BindView(R.id.play_back_bottom_table_like_count)
    TextView playBackBottomTableLikeCount;
    @BindView(R.id.play_back_bottom_table_like_layout)
    LinearLayout playBackBottomTableLikeLayout;
    private PlayBackReviewAdapter playBackReviewAdapter;
    private String intentId;
    private ContentLoader contentLoader;
    private int pageNumber;
    private int userId;
    private boolean praiseFlag;
    private Object praiseId;
    private SpecialShareVOBean shareVO;
    private int praiseNum;
    private int shareNum;
    private int commentNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_back_new_activity);
        ButterKnife.bind(this);
        contentLoader = new ContentLoader(this);
        MyCallBack myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        intentId = getIntent().getStringExtra("id");
        // contentLoader.getLiveUserInfo(userId);
        AppLog.i("TAG", "获取回放视屏Id:" + intentId);
        initXRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyParams.REPLY_REQUESTCODE && resultCode == KeyParams.REPLY_RESULTCODE) {
            String stringExtra = data.getStringExtra(KeyParams.REPLY_CONTENT);
            AppLog.i("TAG", "获取评论内容:" + stringExtra);
        }
    }
    boolean isAttentions;
    private void initXRecyclerView() {
        final CustomLinearLayoutManager layoutManager = new CustomLinearLayoutManager(this);
        layoutManager.setOrientation(CustomLinearLayoutManager.VERTICAL);
        xRecyclerView.setLayoutManager(layoutManager);
        XRecyclerviewLoadingListener xRecyclerviewLoadingListener = new XRecyclerviewLoadingListener();
        xRecyclerView.setLoadingListener(xRecyclerviewLoadingListener);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(true);
        xRecyclerView.setRefreshing(true);
        playBackReviewAdapter = new PlayBackReviewAdapter(this);
        xRecyclerView.setAdapter(playBackReviewAdapter);
        xRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                AppLog.i("TAG", "firstVisibleItemPosition：" + firstVisibleItemPosition);
                if (firstVisibleItemPosition > 1) {
                    replyTitleLayout.setVisibility(View.VISIBLE);
                } else {
                    replyTitleLayout.setVisibility(View.GONE);
                }
            }
        });

        playBackReviewAdapter.setOnReviewItemClickListener(new PlayBackReviewAdapter.OnReviewItemClickListener() {
            @Override
            public void itemClick() {
                //TODO 回复评论
                Intent intent = new Intent(PlayBackActivity.this, RePlyActivity.class);
                intent.putExtra(KeyParams.REPLY_TITLE, "编辑评论");
                startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
            }

            @Override
            public void attentionBtnClick(boolean isAttention, TextView textView) {
                    contentLoader.getAddAttention(String.valueOf(userId));
                    textView.setText("已关注");
                    textView.setBackgroundResource(R.drawable.play_back_info);
                    textView.setCompoundDrawables(null,null,null,null);
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.enter_playback_layout, R.id.reply_write_iv,R.id.play_back_bottom_table_collect_layout,R.id.play_back_bottom_table_reply_layout,R.id.play_back_bottom_table_transmit_layout,R.id.play_back_bottom_table_like_layout})
    public void clickBtn(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.enter_playback_layout:
                // TODO 视频回放
                intent = new Intent(PlayBackActivity.this, PlayBackNewActivity.class);
                intent.putExtra("id",intentId);
                startActivity(intent);
                finish();
                break;
            case R.id.reply_write_iv:
                intent = new Intent(PlayBackActivity.this, RePlyActivity.class);
                intent.putExtra(KeyParams.REPLY_TITLE, "发起评论");
                startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
                break;
            case R.id.play_back_bottom_table_collect_layout:
                if(praiseFlag&&praiseId != null){//取消
                    contentLoader.cancelParises(praiseId, Integer.parseInt(intentId));//取消赞
                }else{//点赞
                    contentLoader.specialPraise(Integer.parseInt(intentId),20);
                }
                break;
            case R.id.play_back_bottom_table_reply_layout:
               intent = new Intent(PlayBackActivity.this, RePlyActivity.class);
                intent.putExtra(KeyParams.REPLY_TITLE, "发起评论");
                startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
                break;
            case R.id.play_back_bottom_table_transmit_layout:
                if(shareVO!=null){
                    SharePopupWindow shareActivity = new SharePopupWindow(PlayBackActivity.this,shareVO);
                    shareActivity.show();
                }
                break;
            case R.id.play_back_bottom_table_like_layout:

                break;

        }
    }

    public class XRecyclerviewLoadingListener implements XRecyclerView.LoadingListener {
        @Override
        public void onRefresh() {
            allRows.clear();
            contentLoader.getPlayBackLiveDetails(Integer.parseInt(intentId));
            contentLoader.getPlayBackLiveReview(intentId, 20, 10, 1);
        }

        @Override
        public void onLoadMore() {
            AppLog.i("TAG", "粉红色咖啡壶咖啡哈咖啡很多坑：" + pageNumber + "      totalPages:" + totalPages);
            if (totalPages != 0 && pageNumber == totalPages) {
                xRecyclerView.setNoMore(true);
            } else {
                contentLoader.getPlayBackLiveReview(intentId, 20, 10, pageNumber + 1);
            }
        }
    }

    boolean detailsRefresh = false;//视频信息加载完毕
    boolean reviewRefresh = false;//评论信息加载完毕
    private int totalPages;
    List<PlayBackReviewRowsBean> allRows = new ArrayList<>();

    public class MyCallBack extends ICallBack {

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if(liveCancelAttention.getReturnCode()==0){

            }
        }

        @Override
        public void onInputPariseResult(PariseResult pariseResult) {//点赞结果
            super.onInputPariseResult(pariseResult);
            if (pariseResult.getReturnCode() == 0) {
                praiseId = pariseResult.getResult();
                praiseFlag = true;
                praiseNum = praiseNum + 1;
                playBackBottomTableCollectCount.setText(String.valueOf(praiseNum));
                playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.color_ff6f6f));
                playBackBottomTableCollectImg.setImageResource(R.drawable.collect_like_red);

            }

        }

        @Override
        public void onPariseResult(PariseResult pariseResult) {//取消赞返回结果
            super.onPariseResult(pariseResult);
            if (pariseResult.getReturnCode() == 0) {
                praiseFlag = false;
                praiseNum = praiseNum - 1;
                playBackBottomTableCollectCount.setText(String.valueOf(praiseNum));
                playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.thin_dark));
                playBackBottomTableCollectImg.setImageResource(R.drawable.collect_unlike);

            }
        }

        @Override
        public void onPlayBackDetails(PlayBackResultBean liveRowsBean) {
            super.onPlayBackDetails(liveRowsBean);
            if (liveRowsBean != null) {
                String photo = liveRowsBean.getPhoto();
                praiseNum = liveRowsBean.getPraiseNum();
                shareNum = liveRowsBean.getShareNum();
                commentNum = liveRowsBean.getCommentNum();
                userId = liveRowsBean.getUser().getId();
                shareVO = liveRowsBean.getShareVO();
                playBackBottomTableReplyCount.setText(String.valueOf(commentNum));
                playBackBottomTableTranmitCount.setText(String.valueOf(shareNum));
                praiseFlag = liveRowsBean.isPraiseFlag();
                praiseId = liveRowsBean.getPraiseId();
                if(liveRowsBean.isPraiseFlag()){
                    playBackBottomTableCollectCount.setText(String.valueOf(praiseNum));
                    playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.color_ff6f6f));
                    playBackBottomTableCollectImg.setImageResource(R.drawable.collect_like_red);
                }else{
                    playBackBottomTableCollectImg.setImageResource(R.drawable.collect_unlike);
                    playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.thin_dark));
                }
                if (photo != null) {
                    DrawableUtils.loadingImg(PlayBackActivity.this, videoCover, QiniuUtils.centerCrop(photo, DensityUtil.getWindowWidth(PlayBackActivity.this), DensityUtil.dip2px(PlayBackActivity.this, 258)));
                }
                detailsRefresh = true;
                if (reviewRefresh) {
                    xRecyclerView.refreshComplete();
                }
                playBackReviewAdapter.setRefreshVideoInfo(liveRowsBean);
                contentLoader.getLiveUserInfo(String.valueOf(userId));

            }
        }

        @Override
        public void onPlayBackReviewDetails(PlayBackReviewResultBean reviewResultBean) {
            super.onPlayBackReviewDetails(reviewResultBean);
            if (reviewResultBean != null) {
                List<PlayBackReviewRowsBean> rows = reviewResultBean.getRows();
                if (rows == null) {
                    return;
                }
                allRows.addAll(rows);
                playBackReviewAdapter.setRefreshReviewList(allRows);
                pageNumber = reviewResultBean.getPageNumber();
                totalPages = reviewResultBean.getTotalPages();
                reviewRefresh = true;
                if (detailsRefresh) {
                    xRecyclerView.refreshComplete();
                }

            }
        }

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            if (liveUserInfosDataResp.getReturnCode() == 0) {
                Object status = liveUserInfosDataResp.getResult().getAttentionVO().getStatus();
                if (status != null) {
                    double parseDouble = Double.parseDouble(String.valueOf(status));
                    int userStatus = (int) parseDouble;//关注状态
                    playBackReviewAdapter.setRefreshAttention(userStatus == 0 ? false : true);
                }
            }
        }
    }


}
