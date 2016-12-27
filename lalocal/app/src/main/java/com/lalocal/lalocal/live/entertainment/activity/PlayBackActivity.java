package com.lalocal.lalocal.live.entertainment.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.RePlyActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.adapter.PlayBackReviewAdapter;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.model.PlayBackResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewResultBean;
import com.lalocal.lalocal.live.entertainment.model.PlayBackReviewRowsBean;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.CommentOperateResp;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.PariseResult;
import com.lalocal.lalocal.model.SpecialShareVOBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DensityUtil;
import com.lalocal.lalocal.util.DialogUtils;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.util.QiniuUtils;
import com.lalocal.lalocal.util.SPCUtils;
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

public static final  String PLAYER_OVER_FIRST="player_over_first";
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
    @BindView(R.id.tips_close_iv)
    ImageView tipsCloseIv;
    @BindView(R.id.reply_write_iv)
    ImageView replyWriteIv;
    @BindView(R.id.reply_title_layout)
    RelativeLayout replyTitleLayout;
    @BindView(R.id.play_back_bottom_table_reply_count)
    TextView playBackBottomTableReplyCount;
    @BindView(R.id.play_back_bottom_table_reply_layout)
    LinearLayout playBackBottomTableReplyLayout;
    @BindView(R.id.reply_tips_layout)
    LinearLayout replyTipsLayout;
    @BindView(R.id.page_base_loading)
    LinearLayout pageBaseLoading;
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
    private int replyId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_back_new_activity);
        ButterKnife.bind(this);
        contentLoader = new ContentLoader(this);
        MyCallBack myCallBack = new MyCallBack();
        contentLoader.setCallBack(myCallBack);
        intentId = getIntent().getStringExtra("id");
        AppLog.i("TAG", "获取回放视屏Id:" + intentId);
        initXRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KeyParams.REPLY_REQUESTCODE && resultCode == KeyParams.REPLY_RESULTCODE) {
            if(allRows!=null){
                allRows.clear();
            }
            contentLoader.getPlayBackLiveReview(intentId, 20, 10, 1);
        }

        if(requestCode==KeyParams.POST_REQUESTCODE&&resultCode==KeyParams.POST_RESULTCODE){
            allRows.clear();
            contentLoader.getPlayBackLiveDetails(Integer.parseInt(intentId));
            contentLoader.getPlayBackLiveReview(intentId, 20, 10, 1);
        }

        if(requestCode==KeyParams.PLAYER_OVER_FIRST_REQUESTCODE&&resultCode==KeyParams.PLAYER_OVER_FIRST_RESULTCODE){
            replyTipsLayout.setVisibility(View.VISIBLE);
            //第一次返回。。。。
        }
    }
    boolean isAttentions;
    private String[] mDialogItems = new String[] {"回复", "举报"};
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
            public void itemClick(int targetId,int userId,String userName) {
                //TODO 回复评论
                if(userId== UserHelper.getUserId(PlayBackActivity.this)){
                    showDeleteDialog(targetId);
                }else {
                    showReplyDialog(targetId,userId,userName);

                }
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

    private void showReplyDialog(final int targetId, final int userId, final String userName) {
        DialogUtils.createListDialog(PlayBackActivity.this,0,null,mDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==0){
                    //回复
                    Intent intent = new Intent(PlayBackActivity.this, RePlyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(KeyParams.REPLY_TITLE, "回复 " + userName);
                    bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_REPLY);
                    bundle.putInt(KeyParams.REPLY_PARENT_ID, targetId);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
                }else{
                    //举报
                    Toast.makeText(PlayBackActivity.this,"举报功能暂未开启!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showDeleteDialog(final int targetId) {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 删除评论
                        allRows.clear();
                        contentLoader.deleteComment(targetId);
                    }
                }).show();

    }



    @OnClick({R.id.back_btn, R.id.enter_playback_layout, R.id.reply_write_iv,R.id.play_back_bottom_table_collect_layout,
            R.id.play_back_bottom_table_reply_layout,R.id.play_back_bottom_table_transmit_layout,R.id.play_back_bottom_table_like_layout,R.id.tips_close_iv})
    public void clickBtn(View view) {

        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.enter_playback_layout:
                // TODO 视频回放
                Intent intent = new Intent(PlayBackActivity.this, PlayBackNewActivity.class);
                intent.putExtra("id",intentId);
            if(!SPCUtils.getBoolean(PlayBackActivity.this,PLAYER_OVER_FIRST)){
                SPCUtils.put(PlayBackActivity.this,PLAYER_OVER_FIRST,true);
                startActivityForResult(intent,KeyParams.PLAYER_OVER_FIRST_REQUESTCODE);
            }else{
                startActivity(intent);
            }
                break;
            case R.id.reply_write_iv:
                if(UserHelper.isLogined(PlayBackActivity.this)){
                    toRePlyActivity();
                }else{
                    if(LiveConstant.USER_INFO_FIRST_CLICK){
                        LiveConstant.USER_INFO_FIRST_CLICK = false;
                        showLoginViewDialog();
                    }
                }

                break;
            case R.id.play_back_bottom_table_collect_layout:
                if(praiseFlag&&praiseId != null){//取消
                    contentLoader.cancelParises(praiseId, Integer.parseInt(intentId));//取消赞
                }else{//点赞
                    contentLoader.specialPraise(Integer.parseInt(intentId),20);
                }
                break;
            case R.id.play_back_bottom_table_reply_layout:
                if(SPCUtils.getBoolean(PlayBackActivity.this,PLAYER_OVER_FIRST)){
                    replyTipsLayout.setVisibility(View.GONE);
                }
                if(UserHelper.isLogined(PlayBackActivity.this)){
                    toRePlyActivity();
                }else{
                    if(LiveConstant.USER_INFO_FIRST_CLICK){
                        LiveConstant.USER_INFO_FIRST_CLICK = false;
                        showLoginViewDialog();
                    }
                }
                break;
            case R.id.play_back_bottom_table_transmit_layout:
                if(shareVO!=null){
                    SharePopupWindow shareActivity = new SharePopupWindow(PlayBackActivity.this,shareVO);
                    shareActivity.show();
                }
                break;
            case R.id.play_back_bottom_table_like_layout:
                break;
            case R.id.tips_close_iv:
                replyTipsLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void toRePlyActivity() {
        if(replyId!=0){
            Intent intent=new Intent(PlayBackActivity.this, RePlyActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(KeyParams.REPLY_TITLE, "发起评论");
            bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_NEW);
            bundle.putInt(KeyParams.TARGET_ID, replyId);
            bundle.putInt(KeyParams.TARGET_TYPE, Constants.PLAY_BACK_TYPE_URL);
            intent.putExtras(bundle);
            startActivityForResult(intent, KeyParams.REPLY_REQUESTCODE);
        }

    }

    public void showLoginViewDialog() {
        final CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent(getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                LLoginActivity.startForResult(PlayBackActivity.this, 701);
            }
        });
        customDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LiveConstant.USER_INFO_FIRST_CLICK = true;
            }
        });
        customDialog.show();
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
        public void onDeleteComment(CommentOperateResp commentOperateResp) {
            super.onDeleteComment(commentOperateResp);
            if (commentOperateResp.getReturnCode() == 0) {
                String message = commentOperateResp.getMessage();
                if (TextUtils.equals(message, "success")) {
                    Toast.makeText(PlayBackActivity.this, "评论删除成功", Toast.LENGTH_SHORT).show();
                    // 请求评论
                    contentLoader.getPlayBackLiveReview(intentId, 20, 10, 1);
                    return;
                }
            }

            Toast.makeText(PlayBackActivity.this, "评论删除失败，请稍后再试~", Toast.LENGTH_SHORT).show();
        }

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
            try{
                if (liveRowsBean != null) {
                    String photo = liveRowsBean.getPhoto();
                    praiseNum = liveRowsBean.getPraiseNum();
                    shareNum = liveRowsBean.getShareNum();
                    commentNum = liveRowsBean.getCommentNum();
                    userId = liveRowsBean.getUser().getId();
                    shareVO = liveRowsBean.getShareVO();
                    replyId = liveRowsBean.getId();
                    playBackBottomTableReplyCount.setText(String.valueOf(commentNum));
                    playBackBottomTableTranmitCount.setText(String.valueOf(shareNum));
                    praiseFlag = liveRowsBean.isPraiseFlag();
                    praiseId = liveRowsBean.getPraiseId();
                    playBackBottomTableCollectCount.setText(String.valueOf(praiseNum));
                    if(liveRowsBean.isPraiseFlag()){
                        playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.color_ff6f6f));
                        playBackBottomTableCollectImg.setImageResource(R.drawable.collect_like_red);
                    }else{
                        playBackBottomTableCollectImg.setImageResource(R.drawable.collect_unlike);
                        playBackBottomTableCollectCount.setTextColor(getResources().getColor(R.color.thin_dark));
                    }
                    if (photo != null) {
                        AppLog.i("TAG","获取图片地址:"+photo);
                        DrawableUtils.loadingImg(PlayBackActivity.this, videoCover, QiniuUtils.centerCrop(photo, DensityUtil.getWindowWidth(PlayBackActivity.this), DensityUtil.dip2px(PlayBackActivity.this, 258)));
                    }
                    detailsRefresh = true;
                    if (reviewRefresh) {
                        xRecyclerView.refreshComplete();
                        if(pageBaseLoading.getVisibility()==View.VISIBLE){
                            pageBaseLoading.setVisibility(View.GONE);
                        }
                    }
                    playBackReviewAdapter.setRefreshVideoInfo(liveRowsBean);
                    contentLoader.getLiveUserInfo(String.valueOf(userId));
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        @Override
        public void onPlayBackReviewDetails(PlayBackReviewResultBean reviewResultBean) {
            super.onPlayBackReviewDetails(reviewResultBean);
            if (reviewResultBean != null) {
                List<PlayBackReviewRowsBean> rows = reviewResultBean.getRows();
                if (rows == null) {
                    reviewRefresh = true;
                    if (detailsRefresh) {
                        xRecyclerView.refreshComplete();
                    }
                    return;
                }
                allRows.addAll(rows);
                playBackReviewAdapter.setRefreshReviewList(allRows);
                pageNumber = reviewResultBean.getPageNumber();
                totalPages = reviewResultBean.getTotalPages();
                reviewRefresh = true;
                if (detailsRefresh) {
                    xRecyclerView.refreshComplete();
                    if(pageBaseLoading.getVisibility()==View.VISIBLE){
                        pageBaseLoading.setVisibility(View.GONE);
                    }
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
