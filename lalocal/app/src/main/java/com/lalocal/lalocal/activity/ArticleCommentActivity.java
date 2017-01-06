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
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.live.entertainment.ui.CustomLinearLayoutManager;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.CommentOperateResp;
import com.lalocal.lalocal.model.CommentRowBean;
import com.lalocal.lalocal.model.CommentsResp;
import com.lalocal.lalocal.model.Constants;
import com.lalocal.lalocal.model.LiveUserBean;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.view.adapter.ArticleCommentListAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * create by Wangjie
 *
 * 文章评论列表Activity
 */
public class ArticleCommentActivity extends BaseActivity {

    @BindView(R.id.img_write_comment)
    ImageView imgWriteComment;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.xrv_article_comments)
    XRecyclerView xrvArticleComments;

    // 内容加载器
    private ContentLoader mContentLoader;
    // 适配器
    private ArticleCommentListAdapter mAdapter;
    // 文章id
    private int mArticleId;
    // 评论列表
    private List<CommentRowBean> mCommentList = new ArrayList<>();

    // 评论列表item点击事件
    private ArticleCommentListAdapter.MyOnItemClickListener mItemClickListener;

    public static final int  REPLY_REQUESTCODE=110;

    // 标签
    private static final int REPLY = 0;
    private static final int REPORT = 1;
    private String[] mDialogItems = new String[] {"回复", "举报"};
    private int pageNumber;
    private int totalPages;

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

    /**
     * 根据界面跳转返回判断是否进行数据请求
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ArticleCommentActivity.REPLY_REQUESTCODE&&resultCode==KeyParams.REPLY_RESULTCODE){
            if(mCommentList!=null){
                mContentLoader.getArticleComments(mArticleId,1);
            }
        }
    }

    /**
     * 初始化XRecyclerView
     */
    boolean isRefresh = false;
    private void initXRecyclerView() {
        // 实例化布局管理器
        CustomLinearLayoutManager layoutManager=new CustomLinearLayoutManager(this);
        // 添加布局管理器
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrvArticleComments.setLayoutManager(layoutManager);
        // 设置监听事件
        xrvArticleComments.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

                isRefresh=true;
                mContentLoader.getArticleComments(mArticleId,1);
            }
            @Override
            public void onLoadMore() {
                isRefresh=false;
                if(totalPages>=(pageNumber+1)){
                    mContentLoader.getArticleComments(mArticleId,pageNumber+1);
                }else{
                    xrvArticleComments.setNoMore(true);
                }
            }
        });
        // 设置可下拉刷新
        xrvArticleComments.setPullRefreshEnabled(true);
        // 设置不可加载更多
        xrvArticleComments.setLoadingMoreEnabled(true);
        xrvArticleComments.setRefreshing(true);
        mAdapter = new ArticleCommentListAdapter(this, null);
        xrvArticleComments.setAdapter(mAdapter);
        MyArticleItemClickListener myArticleItemClickListener = new MyArticleItemClickListener();
        mAdapter.setOnItemClickListener(myArticleItemClickListener);

    }

    /**
     * 接口数据返回处理回调
     */
    private class MyCallBack extends ICallBack {
        @Override
        public void onGetArticleComments(CommentsResp.ResultBean resultBean) {
            super.onGetArticleComments(resultBean);
            if(resultBean!=null){
                pageNumber = resultBean.getPageNumber();
                totalPages = resultBean.getTotalPages();
                if(isRefresh){
                    xrvArticleComments.refreshComplete();
                    mCommentList.clear();
                }else{
                    if(pageNumber==totalPages){
                        xrvArticleComments.setNoMore(true);
                    }else{
                        xrvArticleComments.loadMoreComplete();
                    }
                }
                mCommentList.addAll(resultBean.getRows());
                if(mCommentList.size()>0){
                    setShowList(true);
                }else{
                    setShowList(false);
                }
                mAdapter.refreshList(mCommentList);
            }else{
                setShowList(false);
            }
        }
        @Override
        public void onDeleteComment(CommentOperateResp commentOperateResp) {
            super.onDeleteComment(commentOperateResp);
            if (commentOperateResp.getReturnCode() == 0) {
                String message = commentOperateResp.getMessage();
                if (TextUtils.equals(message, "success")) {
                    Toast.makeText(ArticleCommentActivity.this, "评论删除成功", Toast.LENGTH_SHORT).show();
                    // 请求评论
                    mContentLoader.getArticleComments(mArticleId,1);
                    return;
                }
            }

            Toast.makeText(ArticleCommentActivity.this, "评论删除失败，请稍后再试~", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);

            // 重置xrecyclerview刷新加载状态

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
                                ArticleCommentActivity.this.startActivityForResult(replyIntent,ArticleCommentActivity.REPLY_REQUESTCODE);
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
                .setItems(new String[]{"删除","取消"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 删除评论
                        if(which==0){
                            showHintDialog(commentId);
                        }
                    }
                }).show();
    }

    public void showHintDialog(final int targetId){
        final CustomChatDialog customChatDialog = new CustomChatDialog(ArticleCommentActivity.this);
        customChatDialog.setTitle(getString(R.string.live_hint));
        customChatDialog.setContent("确定要删除当前评论吗?");
        customChatDialog.setCancelable(false);
        customChatDialog.setCancelBtn("取消", new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
            }
        });
        customChatDialog.setSurceBtn("确认", new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                mCommentList.clear();
                mContentLoader.deleteComment(targetId);
            }
        });
        customChatDialog.show();
    }

    @OnClick({R.id.img_back, R.id.img_write_comment})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ArticleCommentActivity.this.finish();
                break;
            case R.id.img_write_comment:
                /*
                因为发起评论页面是直播评论和文章评论共用的，所以需要传入不同的值来判断是哪一个评论及是发起评论还是回复评论
                 */
                if (UserHelper.isLogined(this)) {
                    Intent replyIntent = new Intent(ArticleCommentActivity.this, RePlyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(KeyParams.REPLY_TITLE, "发起评论");
                    bundle.putInt(KeyParams.REPLY_TYPE, KeyParams.REPLY_TYPE_NEW);
                    bundle.putInt(KeyParams.TARGET_ID, mArticleId);
                    bundle.putInt(KeyParams.TARGET_TYPE, Constants.TARGET_TYPE_ARTICLE);
                    replyIntent.putExtras(bundle);
                    startActivityForResult(replyIntent, ArticleCommentActivity.REPLY_REQUESTCODE);
                } else {
                    showLoginDialog();
                }
                break;
        }
    }
}
