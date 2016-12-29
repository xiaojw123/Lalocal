package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.CommentOperateResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/12/15.
 */
public class RePlyActivity extends BaseActivity {
    @BindView(R.id.reply_back)
    ImageView replyBack;
    @BindView(R.id.reply_title_content)
    TextView replyTitleContent;
    @BindView(R.id.reply_send)
    TextView replySend;
    @BindView(R.id.reply_title)
    LinearLayout replyTitle;
    @BindView(R.id.reply_content)
    EditText replyContent;
    @BindView(R.id.input_count)
    TextView inputCount;

    private String mContent;
    private String mTitle;
    private int mTargetId;
    private int mTargetType;
    private int mReplyType;
    private int mParentId;

    private int length;
    private boolean setColor = true;

    private ContentLoader contentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_activity);
        ButterKnife.bind(this);

        // 解析Intent
        parseIntent();
        // 初始化视图
        initView();
        // 初始化ContentLoader
        initContentLoader();
    }

    /**
     * 解析Intent
     */
    private void parseIntent() {
        // 获取Intent
        Intent intent = getIntent();
        if (intent == null)
            return;
        // 获取bundle
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mTitle = bundle.getString(KeyParams.REPLY_TITLE, "发起评论");
            mReplyType = bundle.getInt(KeyParams.REPLY_TYPE);
            // 发起评论
            if (mReplyType == KeyParams.REPLY_TYPE_NEW) {
                mTargetType = bundle.getInt(KeyParams.TARGET_TYPE);
                mTargetId = bundle.getInt(KeyParams.TARGET_ID);
            } else if (mReplyType == KeyParams.REPLY_TYPE_REPLY) {
                mParentId = bundle.getInt(KeyParams.REPLY_PARENT_ID);
            }
        }
    }
    /**
     * 初始化视图
     */
    private void initView() {
        // 设置标题
        replyTitleContent.setText(mTitle);
        // 设置输入控件的文本监听事件
        replyContent.setFocusable(true);
        replyContent.requestFocus();
        replyContent.addTextChangedListener(watcher);
    }
    /**
     * 初始化ContentLoader
     */
    private void initContentLoader() {
        contentService = new ContentLoader(this);
        contentService.setCallBack(new MyCallBack());
    }
    /**
     * 初始化文本监听器
     */
    private TextWatcher watcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            mContent = replyContent.getText().toString().trim();
            length = mContent.length();
            if (length > 0) {
                if (setColor) {
                    setColor = false;
                    replySend.setTextColor(getResources().getColor(R.color.color_ffaa2a));
                }
            } else {
                setColor = true;
                replySend.setTextColor(getResources().getColor(R.color.color_70190f00));
            }
            inputCount.setText(length + "/140");
        }
    };

    boolean isFirstClick=true;

    @OnClick({R.id.reply_send, R.id.reply_back})
    public void clickBtn(View v) {
        switch (v.getId()) {
            case R.id.reply_send:
                if (length > 0) {
                    if(isFirstClick){
                        isFirstClick=false;
                        if (mReplyType == KeyParams.REPLY_TYPE_NEW) {
                            // 请求发送评论
                            contentService.sendComments(mContent, mTargetId, mTargetType);
                        } else if (mReplyType == KeyParams.REPLY_TYPE_REPLY) {
                            // 请求发送回复评论
                            contentService.replyComments(mContent, mParentId);
                        } else {
                            isFirstClick=true;
                            Toast.makeText(this, "获取文章信息失败，请稍后再试~", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(this, "请输入有效内容~", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.reply_back:
                finish();
                break;
        }

    }

    private class MyCallBack extends ICallBack {

        @Override
        public void onError(VolleyError volleyError) {
            super.onError(volleyError);
            AppLog.i("ReplyA", "error is " + volleyError.getMessage());
        }

        @Override
        public void onSendComment(CommentOperateResp commentOperateResp) {
            super.onSendComment(commentOperateResp);
            if (commentOperateResp.getReturnCode() == 0) {
                String message = commentOperateResp.getMessage();
                if (TextUtils.equals(message, "success")) {
                    Toast.makeText(RePlyActivity.this, "评论发表成功", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent();
                    intent.putExtra(KeyParams.REPLY_CONTENT,"success");
                    setResult(KeyParams.REPLY_RESULTCODE,intent);
                    RePlyActivity.this.finish();
                    return;
                }
            }
            isFirstClick=true;
            Toast.makeText(RePlyActivity.this, "评论发表失败，请稍后再试~", Toast.LENGTH_SHORT).show();
        }
    }
}
