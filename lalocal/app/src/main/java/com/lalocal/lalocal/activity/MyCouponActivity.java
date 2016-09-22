package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.CouponItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.MyCouponRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCouponActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
    private static final String MY_COUPON_FORMART = "我的优惠券(%1$s)";
    @BindView(R.id.my_coupon_exchage_btn)
    Button myCouponExchageBtn;
    @BindView(R.id.my_coupon_rlv)
    RecyclerView myCouponRlv;
    EditText couponInputEdt;
    View spaceView;

    @BindView(R.id.my_coupon_ctv)
    CustomTitleView myCouponCtv;
    @BindView(R.id.my_coupon_container)
    RelativeLayout couponContainer;
    @BindView(R.id.my_coupon_reduction_tv)
    TextView myCouponReductionTv;
    @BindView(R.id.my_coupon_use_btn)
    Button myCouponUseBtn;
    @BindView(R.id.my_coupon_use_container)
    FrameLayout myCouponUseContainer;
    @BindView(R.id.my_coupon_friendlyreminder_tv)
    TextView myCouponReminderTv;
    PopupWindow window;
    int pageType;
    MyCouponRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        ButterKnife.bind(this);
        pageType = getPageType();
        if (pageType == KeyParams.PAGE_TYPE_WALLET) {
            myCouponCtv.setTitle("我的优惠券");
        } else {
            myCouponCtv.setTitle("优惠券");
            myCouponUseContainer.setVisibility(View.VISIBLE);
        }
        setLoaderCallBack(new MyCouponCallBack());
        mContentloader.getMyCoupon(UserHelper.getUserId(this), UserHelper.getToken(this));
        myCouponCtv.setOnBackClickListener(this);
    }

    @OnClick({R.id.my_coupon_exchage_btn, R.id.my_coupon_use_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_coupon_exchage_btn:
                if (window == null) {
                    View popContentView = LayoutInflater.from(this).inflate(R.layout.pop_coupon_excharge, couponContainer, false);
                    couponInputEdt = (EditText) popContentView.findViewById(R.id.pop_coupon_edt);
                    spaceView = popContentView.findViewById(R.id.pop_blank_view);
                    Button exchargeBtn = (Button) popContentView.findViewById(R.id.pop_coupon_excharge_btn);
                    couponInputEdt.setOnClickListener(exhargeBtnClickListener);
                    spaceView.setOnClickListener(exhargeBtnClickListener);
                    exchargeBtn.setOnClickListener(exhargeBtnClickListener);
                    window = new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    window.setFocusable(true);
                }
                couponInputEdt.setText("");
                window.showAsDropDown(myCouponCtv);
                break;
            case R.id.my_coupon_use_btn:
                String text = myCouponUseBtn.getText().toString();
                Intent intent = null;
                if ("使用".equals(text)) {
                    if (adapter != null) {
                        List<View> selectedViews = adapter.getSelectedCouponViews();
                        if (selectedViews.size() > 0) {
                            List<Coupon> coupons = new ArrayList<>();
                            for (View itemView : selectedViews) {
                                Coupon coupon = (Coupon) itemView.getTag();
                                coupons.add(coupon);
                            }
                            Gson gson = new Gson();
                            String json = gson.toJson(coupons);
                            intent = new Intent();
                            AppLog.print("myCoupon____json___" + json);
                            intent.putExtra("selectedCoupons", json);
                        }
                    }
                }
                setResult(BookActivity.RESULT_COUPON_SELECTED, intent);
                finish();
                break;
        }
    }


    private View.OnClickListener exhargeBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.pop_coupon_excharge_btn) {
                String text = couponInputEdt.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    mContentloader.exchargeCopon(text);
                } else {
                    CommonUtil.showPromptDialog(MyCouponActivity.this, "优惠码不能为空",null);
                }
            } else if (id == R.id.pop_blank_view) {
                AppLog.print("blankView__");
                if (window != null && window.isShowing()) {
                    AppLog.print("window dimisss__");
                    window.dismiss();
                }

            }
        }
    };

    @Override
    public void onBackClick() {
        setBackResult();
    }


    class MyCouponCallBack extends ICallBack {
        @Override
        public void onGetCounponItem(List<Coupon> items) {
            if (pageType == KeyParams.PAGE_TYPE_WALLET) {
                myCouponCtv.setTitle(String.format(MY_COUPON_FORMART, items.size()));
            }
            if (items.size() > 0) {
                showCouponItems();
                if (adapter == null) {
                    adapter = new MyCouponRecyclerAdapter(items);
                    adapter.setUseGroup(myCouponReductionTv, myCouponUseBtn);
                    myCouponRlv.setLayoutManager(new LinearLayoutManager(MyCouponActivity.this));
                    if (pageType == KeyParams.PAGE_TYPE_WALLET) {
                        adapter.setItemClickEnale(false);
                    } else {
                        adapter.setItemClickEnale(true);
                    }
                    myCouponRlv.setAdapter(adapter);
                } else {
                    adapter.updateItems(items);
                }
            } else {
                showActionTip();
            }
        }

        private void showActionTip() {
            if (myCouponReminderTv.getVisibility() != View.VISIBLE) {
                myCouponReminderTv.setVisibility(View.VISIBLE);
            }
            if (myCouponRlv.getVisibility() == View.VISIBLE) {
                myCouponRlv.setVisibility(View.GONE);
            }
        }

        private void showCouponItems() {
            if (myCouponRlv.getVisibility() != View.VISIBLE) {
                myCouponRlv.setVisibility(View.VISIBLE);
            }
            if (myCouponReminderTv.getVisibility() == View.VISIBLE) {
                myCouponReminderTv.setVisibility(View.GONE);
            }
        }

        @Override
        public void onGetExchargeResult(CouponItem couponItem) {
            if (window != null && window.isShowing()) {
                window.dismiss();
            }
            CommonUtil.showToast(MyCouponActivity.this, "兑换成功", Toast.LENGTH_SHORT);
            mContentloader.getMyCoupon(UserHelper.getUserId(MyCouponActivity.this), UserHelper.getToken(MyCouponActivity.this));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBackResult();
    }

    public void setBackResult() {
        if (pageType == KeyParams.PAGE_TYPE_WALLET) {
            setResult(KeyParams.RESULT_UPDATE_WALLET);
        } else {
            setResult(BookActivity.RESULT_COUPON_SELECTED, null);
        }

    }


}
