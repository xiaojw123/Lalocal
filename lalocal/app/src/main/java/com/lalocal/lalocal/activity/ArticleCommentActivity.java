package com.lalocal.lalocal.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.DemoCache;
import com.lalocal.lalocal.live.entertainment.activity.LivePlayerBaseActivity;
import com.lalocal.lalocal.live.entertainment.constant.LiveConstant;
import com.lalocal.lalocal.live.entertainment.helper.ChatRoomMemberCache;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.CommentOperateResp;
import com.lalocal.lalocal.model.CommentRowBean;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomXRecyclerView;
import com.lalocal.lalocal.view.adapter.ArticleCommentListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleCommentActivity extends BaseActivity {

    @BindView(R.id.img_write_comment)
    ImageView imgWriteComment;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.xrv_article_comments)
    CustomXRecyclerView xrvArticleComments;

    // 内容加载器
    private ContentLoader mContentLoader;
    // 适配器
    private ArticleCommentListAdapter mAdapter;

    // 文章id
    private int mArticleId;

    // 当前加载页
//    private int mCurPage = 1;

    // 是否在刷新
    private boolean isRefresh;
    // 是否在加载
    private boolean isLoadMore;

    // 评论列表
    private List<CommentRowBean> mCommentList = new ArrayList<>();

    // 评论列表item点击事件
    private ArticleCommentListAdapter.MyOnItemClickListener mItemClickListener;

    // 标签
    private static final int REPLY = 0;
    private static final int REPORT = 1;
    private String[] mDialogItems = new String[] {"回复", "举报"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_comment);

        // 使用ButterKnife框架
        ButterKnife.bind(this);

        // 解析Intent
        parseIntent();
        // 初始化ContentLoader
        initLoader();
        // 初始化XRecyclerView
        initXRecyclerView();
    }

    /**
     * 解析intent
     */
    private void parseIntent() {
        // 获取intent
        Intent intent = getIntent();
        // 如果有intent传入
        if (intent != null) {
            // 获取文章id
            mArticleId = intent.getIntExtra(Constants.KEY_ARTICLE_ID, 0);
        }
    }

    /**
     * 初始化ContentLoader
     */
    private void initLoader() {
        // 实例化ContentLoader
        mContentLoader = new ContentLoader(this);
        // 设置回调
        mContentLoader.setCallBack(new MyCallBack());
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 请求评论
        mContentLoader.getArticleComments(mArticleId);
    }

    /**
     * 初始化XRecyclerView
     */
    private void initXRecyclerView() {
        // 实例化布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 添加布局管理器
        xrvArticleComments.setLayoutManager(layoutManager);
        // 设置可下拉刷新
        xrvArticleComments.setPullRefreshEnabled(false);
        // 设置不可加载更多
        xrvArticleComments.setLoadingMoreEnabled(false);
        // 设置监听事件
//        xrvArticleComments.setLoadingListener(new XRecyclerView.LoadingListener() {
//            @Override
//            public void onRefresh() {
//                xrvArticleComments.refreshComplete();

//                if (isRefresh == false) {
//                    // 正在刷新
//                    isRefresh = true;
                    // 不再加载
//                    isLoadMore = false;
                    // 重置当前加载页
//                    mCurPage = 1;
                    // 请求
//                    mContentLoader.getArticleComments(mArticleId);
//                }
//            }

//            @Override
//            public void onLoadMore() {
//                if (isLoadMore = false) {
//                    // 正在加载更多
//                    isLoadMore = true;
//                    // 不再刷新
//                    isRefresh = false;
//                    // 加载页+1
//                    mCurPage++;
//                    // 请求
//                    mContentLoader.getArticleComments(mArticleId, mCurPage);
//                }
//            }
//        });
    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onGetArticleComments(List<CommentRowBean> commentList) {
            super.onGetArticleComments(commentList);

//            if (isRefresh) {
//
//                // 重置标记
//                isRefresh = false;
//                // 刷新结束
//                xrvArticleComments.refreshComplete();
//                // 加载结束
//                xrvArticleComments.loadMoreComplete();
//
//                // 清空评论列表
//                mCommentList.clear();
//                // 填充评论列表
//                mCommentList.addAll(commentList);
//            } else if (isLoadMore) {
//
//                // 重置标记
//                isLoadMore = false;
//
//                // 结束刷新
//                xrvArticleComments.refreshComplete();
//
//                // 已加载完毕
//                if (commentList.size() <= 0) {
//                    // 添加footer，已加载完毕
//                    xrvArticleComments.setNoMore(true);
//
//                    return;
//                } else {
//                    // 加载结束
//                    xrvArticleComments.loadMoreComplete();
//
//                    // 添加获取的评论列表
//                    mCommentList.addAll(commentList);
//                }
//            } else {
                // 清空评论列表
                mCommentList.clear();
                // 填充评论列表
                mCommentList.addAll(commentList);
//            }

            // 设置适配器
            setAdapter();
        }

        @Override
        public void onDeleteComment(CommentOperateResp commentOperateResp) {
            super.onDeleteComment(commentOperateResp);

            if (commentOperateResp.getReturnCode() == 0) {
                String message = commentOperateResp.getMessage();
                if (TextUtils.equals(message, "success")) {
                    Toast.makeText(ArticleCommentActivity.this, "评论删除成功", Toast.LENGTH_SHORT).show();

                    // 请求评论
                    mContentLoader.getArticleComments(mArticleId);
                    return;
                }
            }

            Toast.makeText(ArticleCommentActivity.this, "评论删除失败，请稍后再试~", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);

            // 重置xrecyclerview刷新加载状态
            resetStatus();
        }
    }

    /**
     * 重置xrecyclerview刷新加载状态
     */
    private void resetStatus() {
//        isRefresh = false;
//        isLoadMore = false;
//        xrvArticleComments.refreshComplete();
//        xrvArticleComments.loadMoreComplete();
    }

    /**
     * 设置适配器
     */
    public void setAdapter() {
        if (mCommentList.size() <= 0) {
            // 隐藏列表
            setShowList(false);
        } else {
            // 显示列表
            setShowList(true);

            // 如果适配器不存在
            if (mAdapter == null) {
                // 初始化适配器
                mAdapter = new ArticleCommentListAdapter(this, mCommentList);
                // 初始化item点击事件
                mItemClickListener = new MyArticleItemClickListener();
                // 设置item点击事件
                mAdapter.setOnItemClickListener(mItemClickListener);
                // 设置适配器
                xrvArticleComments.setAdapter(mAdapter);
            } else {
                // 刷新列表
                mAdapter.refreshList(mCommentList);
            }
        }
    }

    /**
     * 显示列表
     * @param isShow
     */
    private void setShowList(boolean isShow) {
        if (isShow) {
            xrvArticleComments.setVisibility(View.VISIBLE);
            tvTip.setVisibility(View.GONE);
        } else {
            xrvArticleComments.setVisibility(View.GONE);
            tvTip.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 文章列表item点击事件
     */
    private class MyArticleItemClickListener implements ArticleCommentListAdapter.MyOnItemClickListener {

        @Override
        public void onItemClick(View v, int position) {
            if (UserHelper.isLogined(ArticleCommentActivity.this)) {
                // 获取当前评论
                CommentRowBean comment = mCommentList.get(position);
                // 如果评论信息为空
                if (comment == null) {
                    Toast.makeText(ArticleCommentActivity.this, "评论信息出现问题，请刷新后再试~", Toast.LENGTH_SHORT).show();
                    return;
                }
                LiveUserBean user = comment.getUser();
                int commentId = comment.getId();
                // 如果评论人信息为空
                if (user == null) {
                    Toast.makeText(ArticleCommentActivity.this, "评论人信息出现问题，请刷新后再试~", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 获取评论人id
                int userId = user.getId();
                // 获取评论人昵称
                String nickname = user.getNickName();
                // 如果评论人是自己
                if (userId == UserHelper.getUserId(ArticleCommentActivity.this)) {
                    // 显示删除评论对话框
                    showDeleteDialog(commentId);
                } else {
                    // 显示回复对话框
                    showReplyDialog(nickname, commentId);
                }
            } else {
                // 显示登录对话框
                showLoginDialog();
            }
        }
    }

    /**
     * 显示登录提示对话框
     */
    private void showLoginDialog() {
        CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent(getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                LLoginActivity.start(ArticleCommentActivity.this);
            }
        });
        customDialog.show();
    }

    /**
     * 显示回复评论对话框
     * @param nickname
     */
    private void showReplyDialog(final String nickname, final int commentId) {
        new AlertDialog.Builder(this)
                .setItems(mDialogItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case REPLY:
                                Intent replyIntent = new Intent(ArticleCommentActivity.this, RePlyActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString(KeyParams.REPLY_TITLE, "回复 " + nickname);
                                bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_REPLY);
                                bundle.putInt(KeyParams.REPLY_PARENT_ID, commentId);
                                replyIntent.putExtras(bundle);
                                ArticleCommentActivity.this.startActivity(replyIntent);
                                break;
                            case REPORT:
                                Toast.makeText(ArticleCommentActivity.this, "举报功能尚未开放~", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 显示删除对话框
     */
    private void showDeleteDialog(final int commentId) {
        new AlertDialog.Builder(this)
                .setItems(new String[]{"删除"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 删除评论
                        mContentLoader.deleteComment(commentId);
                    }
                }).show();
    }

    @OnClick({R.id.img_back, R.id.img_write_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ArticleCommentActivity.this.finish();
                break;
            case R.id.img_write_comment:
                Intent replyIntent = new Intent(ArticleCommentActivity.this, RePlyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(KeyParams.REPLY_TITLE, "发起评论");
                bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_NEW);
                bundle.putInt(KeyParams.TARGET_ID, mArticleId);
                bundle.putInt(KeyParams.TARGET_TYPE, Constants.TARGET_TYPE_ARTICLE);
                replyIntent.putExtras(bundle);
                startActivity(replyIntent);
                break;
        }
    }
}
