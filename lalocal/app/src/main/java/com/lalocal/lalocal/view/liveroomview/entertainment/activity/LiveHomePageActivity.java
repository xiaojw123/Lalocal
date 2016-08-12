package com.lalocal.lalocal.view.liveroomview.entertainment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/8/5.
 */
public class LiveHomePageActivity extends BaseActivity {
    @BindView(R.id.homepage_head)
    CustomTitleView homepageHead;
    @BindView(R.id.live_verified)
    TextView liveVerified;
    @BindView(R.id.homepage_master_name)
    TextView homepageMasterName;
    @BindView(R.id.homepage_master_signature)
    TextView homepageMasterSignature;
    @BindView(R.id.personal_home_page)
    ImageView personalHomePage;
    @BindView(R.id.homepage_attention_count_tv)
    TextView homepageAttentionCountTv;
    @BindView(R.id.homepage_fans_count)
    TextView homepageFansCount;
    @BindView(R.id.line_g)
    LinearLayout lineG;
    @BindView(R.id.master_attention)
    TextView masterAttention;
    @BindView(R.id.homepage_attention_layout)
    LinearLayout homepageAttentionLayout;
    @BindView(R.id.homepage_fans_layout)
    LinearLayout homepageFansLayout;
    private ContentLoader contentLoader;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_personal_homepage_layout);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getLiveUserInfo(userId);

    }


    @OnClick({R.id.homepage_attention_layout, R.id.homepage_fans_layout, R.id.master_attention})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.homepage_attention_layout:
                Intent intent = new Intent(LiveHomePageActivity.this, LiveAttentionOrFansActivity.class);
                intent.putExtra("liveType", "0");
                intent.putExtra("userId", userId);
                startActivity(intent);

                break;
            case R.id.homepage_fans_layout:
                Intent intent1 = new Intent(LiveHomePageActivity.this, LiveAttentionOrFansActivity.class);
                intent1.putExtra("liveType", "1");
                intent1.putExtra("userId", userId);
                startActivity(intent1);

                break;

            case R.id.master_attention:
                String text = (String) masterAttention.getText();
                if ("关注".equals(text)) {
                    contentLoader.getAddAttention(userId);
                } else {
                    contentLoader.getCancelAttention(userId);
                }

                break;
        }
    }


    public class MyCallBack extends ICallBack {

        private int attentionNum;
        private int fansNum;

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            LiveUserInfoResultBean result = liveUserInfosDataResp.getResult();
            homepageMasterName.setText(result.getNickName());
            attentionNum = result.getAttentionNum();

            fansNum = result.getFansNum();
            String s = new Gson().toJson(liveUserInfosDataResp);
            AppLog.i("TAG", "liveUserInfosDataResp:" + s);
            homepageAttentionCountTv.setText(String.valueOf(attentionNum));
            homepageFansCount.setText(String.valueOf(fansNum));
            DrawableUtils.displayImg(LiveHomePageActivity.this, personalHomePage, result.getAvatar());
            if (!TextUtils.isEmpty(result.getDescription())) {
                homepageMasterSignature.setText(result.getDescription());
            }
            Object statusa = result.getAttentionVO().getStatus();
            double parseDouble = Double.parseDouble(String.valueOf(statusa));
            int status = (int) parseDouble;
            if (status == 0) {
                masterAttention.setText("关注");
                masterAttention.setTextColor(Color.parseColor("#ffaa2a"));

            } else {
                masterAttention.setText("已关注");
                masterAttention.setTextColor(Color.BLACK);
            }
        }

        @Override
        public void onLiveAttentionStatus(LiveAttentionStatusBean liveAttentionStatusBean) {
            super.onLiveAttentionStatus(liveAttentionStatusBean);
            if (liveAttentionStatusBean.getReturnCode() == 0) {
                int status = liveAttentionStatusBean.getResult().getStatus();
                if (status == 1) {
                    Toast.makeText(LiveHomePageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    masterAttention.setText("已关注");
                    attentionNum = attentionNum + 1;
                    homepageFansCount.setText(String.valueOf(attentionNum));
                    masterAttention.setTextColor(Color.BLACK);
                }


            }
        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            if (liveCancelAttention.getReturnCode() == 0) {
                Toast.makeText(LiveHomePageActivity.this, "已取消关注", Toast.LENGTH_SHORT).show();
                masterAttention.setText("关注");
                masterAttention.setTextColor(Color.parseColor("#ffaa2a"));
                attentionNum = attentionNum - 1;
                homepageFansCount.setText(String.valueOf(attentionNum));


            }
        }
    }
}