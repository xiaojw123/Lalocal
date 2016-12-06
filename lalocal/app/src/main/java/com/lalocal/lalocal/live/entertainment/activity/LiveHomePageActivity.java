package com.lalocal.lalocal.live.entertainment.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.BaseActivity;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.live.entertainment.adapter.LiveArticleVPAdapter;
import com.lalocal.lalocal.live.entertainment.ui.CustomChatDialog;
import com.lalocal.lalocal.me.LLoginActivity;
import com.lalocal.lalocal.model.ArticleDetailsResultBean;
import com.lalocal.lalocal.model.HomepageUserArticlesResp;
import com.lalocal.lalocal.model.LiveAttentionStatusBean;
import com.lalocal.lalocal.model.LiveCancelAttention;
import com.lalocal.lalocal.model.LiveRowsBean;
import com.lalocal.lalocal.model.LiveUserInfoResultBean;
import com.lalocal.lalocal.model.LiveUserInfosDataResp;
import com.lalocal.lalocal.model.UserLiveItem;
import com.lalocal.lalocal.net.ContentLoader;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.DrawableUtils;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.DisallowParentTouchViewPager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2016/8/5.
 */
public class LiveHomePageActivity extends BaseActivity {

    @BindView(R.id.userpage_headportrait_img)
    ImageView userpageHeadportraitImg;
    @BindView(R.id.userpage_verified)
    TextView userpageVerified;
    @BindView(R.id.userpage_author_tag)
    ImageView userpageAuthorTag;
    @BindView(R.id.userpage_username)
    TextView userpageUsername;
    @BindView(R.id.userpage_login_prompt)
    TextView userpageLoginPrompt;
    @BindView(R.id.userpage_personal_info)
    LinearLayout userpagePersonalInfo;
    @BindView(R.id.userpage_follow_num)
    TextView userpageFollowNum;
    @BindView(R.id.userpage_atten_tab)
    LinearLayout userpageAttenTab;
    @BindView(R.id.userpage_fans_num)
    TextView userpageFansNum;
    @BindView(R.id.userpage_fans_tab)
    LinearLayout userpageFansTab;
    @BindView(R.id.userpage_userprofile_container)
    LinearLayout userpageUserprofileContainer;
    @BindView(R.id.tv_live)
    TextView tvLive;
    @BindView(R.id.img_live_selected)
    ImageView imgLiveSelected;
    @BindView(R.id.tv_article)
    TextView tvArticle;
    @BindView(R.id.img_article_selected)
    ImageView imgArticleSelected;
    @BindView(R.id.layout_article_part)
    LinearLayout layoutArticlePart;
    @BindView(R.id.tv_title_layout)
    LinearLayout tvTitleLayout;
    @BindView(R.id.vp_live_article)
    DisallowParentTouchViewPager vpLiveArticle;
    @BindView(R.id.nested_scroll_view)
    ScrollView nestedScrollView;
    @BindView(R.id.homepage_head)
    CustomTitleView homepageHead;
    @BindView(R.id.master_attention)
    TextView masterAttention;
    @BindView(R.id.master_attention_layout)
    LinearLayout masterAttentionLayout;
    @BindView(R.id.live_attention_homepage)
    RelativeLayout liveAttentionHomepage;
    private LiveArticleVPAdapter mVPAdapter;
    private ContentLoader contentLoader;
    private String userId;
    private TextView popuCancel;
    private TextView popuConfirm;
    private PopupWindow popupWindow;
    private String back;

    // 用户历史直播列表
    private List<LiveRowsBean> mUserLiveList;
    // 用户当前直播
    private LiveRowsBean mUserLiving;
    // 用户文章列表
    private List<ArticleDetailsResultBean> mUserArticleList;

    private boolean isAuthor = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_personal_homepage_layout);
        ButterKnife.bind(this);
        userId = getIntent().getStringExtra("userId");
        //  back = getIntent().getStringExtra("back");
        contentLoader = new ContentLoader(this);
        contentLoader.setCallBack(new MyCallBack());
        contentLoader.getLiveUserInfo(userId);
        // 获取用户历史直播列表
        contentLoader.getUserLive(Integer.valueOf(userId), 1);
        // 获取用户当前直播
        contentLoader.getUserCurLive(Integer.valueOf(userId));
        // 获取用户文章列表
        contentLoader.getUserArticles(Integer.valueOf(userId), 1);
    }

    /**
     * 设置直播、文章列表数据
     */
    private void setLiveArticleData() {
        // 获取屏幕高度
        int minHeight = nestedScrollView.getMeasuredHeight();
        AppLog.i("hhhh", "screenHeight is " + minHeight);

        // 设置高度适应内容
        vpLiveArticle.enableWrapContent(true);
        // 设置ViewPager最小高度
        vpLiveArticle.setMinHeight(minHeight);

        // 初始化数据
        boolean isSelf = false;
        if (masterAttentionLayout.getVisibility() == View.VISIBLE) {
            isSelf = false;
        } else {
            isSelf = true;
        }
        mVPAdapter = new LiveArticleVPAdapter(LiveHomePageActivity.this, isSelf, mUserLiving, mUserLiveList, mUserArticleList, isAuthor);

        // 设置适配器
        vpLiveArticle.setAdapter(mVPAdapter);

        // 默认显示第一页（下标为0）
        selecteTab(0);

        // 设置vp滑动监听事件
        vpLiveArticle.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selecteTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        } else {
        // 更新数据
//            mVPAdapter.notifyDataSetChanged();
//        }
    }

    @OnClick({R.id.userpage_atten_tab, R.id.userpage_fans_tab, R.id.master_attention, R.id.master_attention_layout, R.id.tv_article, R.id.tv_live})
    public void clickButton(View view) {
        switch (view.getId()) {
            case R.id.userpage_atten_tab:
                Intent intent = new Intent(LiveHomePageActivity.this, LiveAttentionOrFansActivity.class);
                intent.putExtra("liveType", "0");
                intent.putExtra("userId", userId);
                startActivity(intent);
             /*   if (back != null) {
                    finish();
                }*/
                break;
            case R.id.userpage_fans_tab:
                Intent intent1 = new Intent(LiveHomePageActivity.this, LiveAttentionOrFansActivity.class);
                intent1.putExtra("liveType", "1");
                intent1.putExtra("userId", userId);
                startActivity(intent1);

                break;

           /* case R.id.personal_home_page:
                BigPictureBean bean = new BigPictureBean();
                bean.setUserAvatar(true);
                String avatarOrigin = result.getAvatarOrigin();
                if (result.getAvatarOrigin() == null) {
                    avatarOrigin = result.getAvatar();
                }
                bean.setImgUrl(avatarOrigin);
                Intent intent2 = new Intent(this, BigPictureActivity.class);
                intent2.putExtra("bigbean", bean);
                startActivity(intent2);
                overridePendingTransition(R.anim.head_in, R.anim.head_out);

                break;*/
            case R.id.master_attention:
                boolean isLogin = UserHelper.isLogined(LiveHomePageActivity.this);
                if (isLogin) {
                    String text = (String) masterAttention.getText();
                    if ("关注".equals(text)) {
                        contentLoader.getAddAttention(userId);
                    } else {
                        showAttentionPopuwindow(userId);
                    }
                } else {
                    showLoginViewDialog();
                }

                break;

            case R.id.tv_live: // 他的直播
                // 选中直播
                selecteTab(TAB_LIVE);
                break;
            case R.id.tv_article: // 他的文章
                // 选中文章
                selecteTab(TAB_ARTICLE);
                break;
        }
    }

    private void showLoginViewDialog() {
        CustomChatDialog customDialog = new CustomChatDialog(this);
        customDialog.setContent(getString(R.string.live_login_hint));
        customDialog.setCancelable(false);
        customDialog.setCancelBtn(getString(R.string.live_canncel), null);
        customDialog.setSurceBtn(getString(R.string.live_login_imm), new CustomChatDialog.CustomDialogListener() {
            @Override
            public void onDialogClickListener() {
                LLoginActivity.start(LiveHomePageActivity.this);
            }
        });
        customDialog.show();
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

    private static final int TAB_LIVE = 0;
    private static final int TAB_ARTICLE = 1;

    private void selecteTab(int whichTab) {
        switch (whichTab) {
            case TAB_LIVE:
                // ContextCompat.getColor(this, R.color.color_1a)用来替代过时的getResources().getColor()
                // 设置直播tab颜色为选中的颜色
                tvLive.setTextColor(ContextCompat.getColor(this, R.color.color_1a));
                // 显示直播选中标记
                imgLiveSelected.setVisibility(View.VISIBLE);
                // 设置文章tab颜色为未选中的颜色
                tvArticle.setTextColor(ContextCompat.getColor(this, R.color.color_b3));
                // 隐藏文章选中标记
                imgLiveSelected.setVisibility(View.INVISIBLE);

                // viewpager滑动到直播
                vpLiveArticle.setCurrentItem(TAB_LIVE);
                break;
            case TAB_ARTICLE:
                // 设置直播tab颜色为未选中的颜色
                tvLive.setTextColor(ContextCompat.getColor(this, R.color.color_b3));
                // 隐藏直播选中标记
                imgLiveSelected.setVisibility(View.INVISIBLE);
                // 设置文章tab颜色为选中的颜色
                tvArticle.setTextColor(ContextCompat.getColor(this, R.color.color_1a));
                // 显示文章选中标记
                imgLiveSelected.setVisibility(View.VISIBLE);

                // viewpager滑动到文章
                vpLiveArticle.setCurrentItem(TAB_ARTICLE);
                break;
        }
    }

    private int attentionNum;
    private int fansNum;
    private LiveUserInfoResultBean result;

    public class MyCallBack extends ICallBack {

        @Override
        public void onLiveUserInfo(LiveUserInfosDataResp liveUserInfosDataResp) {
            super.onLiveUserInfo(liveUserInfosDataResp);
            result = liveUserInfosDataResp.getResult();
            userpageUsername.setText(result.getNickName());
            attentionNum = result.getAttentionNum();
            fansNum = result.getFansNum();
            int id = UserHelper.getUserId(LiveHomePageActivity.this);
            if (userId.equals(String.valueOf(id))) {
                masterAttentionLayout.setVisibility(View.GONE);
            } else {
                masterAttentionLayout.setVisibility(View.VISIBLE);
            }


            if (result.getRole() == 1) {
                userpageVerified.setText("专栏作者");
                userpageVerified.setTextColor(Color.WHITE);
                userpageVerified.setBackgroundColor(Color.parseColor("#ffaa2a"));
                isAuthor = true;
                userpageAuthorTag.setVisibility(View.VISIBLE);
                userpageVerified.setVisibility(View.GONE);
                // 显示文章
                layoutArticlePart.setVisibility(View.VISIBLE);
            } else if (result.getRole() == -1) {
                userpageVerified.setText("管理员");
                userpageVerified.setTextColor(Color.WHITE);
                userpageVerified.setBackgroundColor(Color.parseColor("#ffaa2a"));

                isAuthor = false;
                userpageAuthorTag.setVisibility(View.GONE);
                userpageVerified.setVisibility(View.VISIBLE);
                // 隐藏文章
                layoutArticlePart.setVisibility(View.GONE);
            } else {
                userpageVerified.setText("未验证");
                userpageVerified.setTextColor(Color.BLACK);
                userpageVerified.setBackgroundColor(Color.parseColor("#999999"));
                userpageAuthorTag.setVisibility(View.GONE);
                userpageVerified.setVisibility(View.VISIBLE);
                isAuthor = false;
                // 隐藏文章
                layoutArticlePart.setVisibility(View.GONE);
            }
            String s = new Gson().toJson(liveUserInfosDataResp);
            userpageFollowNum.setText(String.valueOf(attentionNum));
            userpageFansNum.setText(String.valueOf(fansNum));
            DrawableUtils.displayImg(LiveHomePageActivity.this, userpageHeadportraitImg, result.getAvatar());
            if (!TextUtils.isEmpty(result.getDescription())) {
                userpageLoginPrompt.setText(result.getDescription());
            }

            Object statusa = result.getAttentionVO().getStatus();
            if (statusa != null) {
                double parseDouble = Double.parseDouble(String.valueOf(statusa));
                int status = (int) parseDouble;
                if (status == 0) {
                    masterAttention.setText("关注");
                    masterAttention.setTextColor(Color.parseColor("#ffaa2a"));

                } else if (status == 1) {
                    masterAttention.setText("已关注");
                    masterAttention.setTextColor(Color.BLACK);
                } else if (status == 2) {
                    masterAttention.setText("已相互关注");
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
                    userpageFansNum.setText(String.valueOf(fansNum));
                    masterAttention.setTextColor(Color.BLACK);
                } else if (status == 2) {
                    Toast.makeText(LiveHomePageActivity.this, "关注成功", Toast.LENGTH_SHORT).show();
                    masterAttention.setText("已相互关注");
                    fansNum = fansNum + 1;
                    userpageFansNum.setText(String.valueOf(fansNum));
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
                fansNum=fansNum - 1;
                userpageFansNum.setText(String.valueOf(fansNum));
            }
        }

        /**
         * 获取用户历史直播列表
         *
         * @param item
         */
        @Override
        public void onGetUserLive(UserLiveItem item) {
            super.onGetUserLive(item);
            // 获取用户历史直播列表
            mUserLiveList = item.getRows();
            // 设置直播、文章列表数据
            setLiveArticleData();
        }

        /**
         * 获取用户当前直播
         *
         * @param liveRowsBean
         */
        @Override
        public void onGetUserCurLive(LiveRowsBean liveRowsBean) {
            super.onGetUserCurLive(liveRowsBean);
            // 获取用户当前直播
            mUserLiving = liveRowsBean;
            // 设置直播、文章列表数据
            setLiveArticleData();
        }

        /**
         * 获取用户文章
         *
         * @param articlesResp
         */
        @Override
        public void onGetUserArticles(HomepageUserArticlesResp articlesResp) {
            super.onGetUserArticles(articlesResp);
            // 获取用户文章列表
            mUserArticleList = articlesResp.getResult().getRows();
            // 设置直播、文章列表数据
            setLiveArticleData();
        }
    }


}