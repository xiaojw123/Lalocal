package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.activity.BigPictureActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.BigPictureBean;
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
    @BindView(R.id.master_attention_layout)
    LinearLayout masterAttentionLayout;

    @BindView(R.id.line_layout)
    RelativeLayout lineLayout;
    @BindView(R.id.ffdffhfd)
    View ffdffhfd;

    @BindView(R.id.live_attention_homepage)
    RelativeLayout liveAttentionHomepage;
    private ContentLoader contentLoader;
    private String userId;
    private TextView popuCancel;
    private TextView popuConfirm;
    private PopupWindow popupWindow;

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


    @OnClick({R.id.homepage_attention_layout, R.id.homepage_fans_layout, R.id.master_attention, R.id.personal_home_page})
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
                    showAttentionPopuwindow(userId);
                }
                break;
            case R.id.personal_home_page:
                BigPictureBean bean=new BigPictureBean();
              bean.setUserAvatar(true);
                String avatarOrigin = result.getAvatarOrigin();
                if(result.getAvatarOrigin()==null){
                    avatarOrigin= result.getAvatar();
                }
                bean.setImgUrl(avatarOrigin);
                Intent intent2 = new Intent(this, BigPictureActivity.class);
                intent2.putExtra("bigbean", bean);
                startActivity(intent2);
                overridePendingTransition(R.anim.head_in, R.anim.head_out);

                break;
        }
    }

    private void showAttentionPopuwindow(final String userId) {
        popupWindow = new PopupWindow(this);
        View inflate = View.inflate(this, R.layout.live_attention_popu_layout, null);
        popuCancel = (TextView) inflate.findViewById(R.id.live_attention_popu_cancel);
        popuConfirm = (TextView) inflate.findViewById(R.id.live_attention_popu_confirm);

        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setContentView(inflate);
        popupWindow.setFocusable(true);
        popupWindow.setAnimationStyle(R.style.AnimBottom);
        ColorDrawable dw = new ColorDrawable();
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.showAtLocation(this.findViewById(R.id.live_attention_homepage),
                Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.5f);
        popuConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentLoader.getCancelAttention(userId);
            }
        });
        popuCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    private int attentionNum;
    private int fansNum;
    private LiveUserInfoResultBean result;

    public class MyCallBack extends ICallBack {

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            result = liveUserInfosDataResp.getResult();
            homepageMasterName.setText(result.getNickName());
            attentionNum = result.getAttentionNum();
            fansNum = result.getFansNum();
            int id = UserHelper.getUserId(LiveHomePageActivity.this);
            if (userId.equals(String.valueOf(id))) {
                masterAttentionLayout.setVisibility(View.GONE);
            } else {
                masterAttentionLayout.setVisibility(View.VISIBLE);
            }
            String s = new Gson().toJson(liveUserInfosDataResp);
            AppLog.i("TAG", "liveUserInfosDataResp:" + s);
            homepageAttentionCountTv.setText(String.valueOf(attentionNum));
            homepageFansCount.setText(String.valueOf(fansNum));
            DrawableUtils.displayImg(LiveHomePageActivity.this, personalHomePage, result.getAvatar());
            if (!TextUtils.isEmpty(result.getDescription())) {
                homepageMasterSignature.setText(result.getDescription());
            }

            Object statusa = result.getAttentionVO().getStatus();
            if (statusa != null) {
                double parseDouble = Double.parseDouble(String.valueOf(statusa));
                int status = (int) parseDouble;
                if (status == 0) {
                    masterAttention.setText("关注");
                    masterAttention.setTextColor(Color.parseColor("#ffaa2a"));

                } else {
                    masterAttention.setText("已关注");
                    masterAttention.setTextColor(Color.BLACK);
                }
            } else {
                // Toast.makeText(LiveHomePageActivity.this,"status为空",Toast.LENGTH_SHORT).show();
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
                    fansNum = fansNum + 1;
                    homepageFansCount.setText(String.valueOf(fansNum));
                    masterAttention.setTextColor(Color.BLACK);
                }


            }
        }

        @Override
        public void onLiveCancelAttention(LiveCancelAttention liveCancelAttention) {
            super.onLiveCancelAttention(liveCancelAttention);
            popupWindow.dismiss();
            if (liveCancelAttention.getReturnCode() == 0) {
                Toast.makeText(LiveHomePageActivity.this, "已取消关注", Toast.LENGTH_SHORT).show();
                masterAttention.setText("关注");
                masterAttention.setTextColor(Color.parseColor("#ffaa2a"));
                fansNum = fansNum - 1;
                homepageFansCount.setText(String.valueOf(fansNum));
            }
        }
    }




}