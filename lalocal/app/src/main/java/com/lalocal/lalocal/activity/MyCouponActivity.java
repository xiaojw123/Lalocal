package com.lalocal.lalocal.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.UserHelper;
import com.lalocal.lalocal.model.Coupon;
import com.lalocal.lalocal.model.CouponItem;
import com.lalocal.lalocal.model.WalletContent;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.adapter.MyCouponRecyclerAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCouponActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
    private static final String MY_COUPON_FORMART = "我的优惠券(%1$s)";
    public static final int MSG_UPDATE_USE = 0x11;

    @BindView(R.id.my_coupon_exchage_btn)
    Button myCouponExchageBtn;
    @BindView(R.id.my_coupon_rlv)
    RecyclerView myCouponRlv;
    EditText couponInputEdt;
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
    PopupWindow window;
    int pageType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        ButterKnife.bind(this);
        pageType = getPageType();
        if (pageType == KeyParams.PAGE_TYPE_WALLET) {
            WalletContent content = getWalletContent();
            myCouponCtv.setTitle(String.format(MY_COUPON_FORMART, content.getCouponNumb()));
        } else {
            myCouponCtv.setTitle("优惠券");
            myCouponUseContainer.setVisibility(View.VISIBLE);
        }
        setLoaderCallBack(new MyCouponCallBack());
        mContentloader.getMyCoupon(UserHelper.getUserId(this), UserHelper.getToken(this));
        myCouponCtv.setOnBackClickListener(this);
    }

    @OnClick(R.id.my_coupon_exchage_btn)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_coupon_exchage_btn:
                if (window == null) {
                    View popContentView = LayoutInflater.from(this).inflate(R.layout.pop_coupon_excharge, couponContainer, false);
                    couponInputEdt = (EditText) popContentView.findViewById(R.id.pop_coupon_edt);
                    Button exchargeBtn = (Button) popContentView.findViewById(R.id.pop_coupon_excharge_btn);
                    couponInputEdt.setOnClickListener(exhargeBtnClickListener);
                    exchargeBtn.setOnClickListener(exhargeBtnClickListener);
                    window = new PopupWindow(popContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    window.setOutsideTouchable(true);
                    window.setFocusable(true);
                    window.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
                    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                }
                window.showAsDropDown(myCouponCtv);
                break;
        }
    }


    private View.OnClickListener exhargeBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.pop_coupon_excharge_btn) {
                String text = couponInputEdt.getText().toString();
                mContentloader.exchargeCopon(text);
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
            MyCouponRecyclerAdapter adapter = new MyCouponRecyclerAdapter(items);
            adapter.setUseGroup(myCouponReductionTv,myCouponUseBtn);
            myCouponRlv.setLayoutManager(new LinearLayoutManager(MyCouponActivity.this));
            myCouponRlv.setAdapter(adapter);
            AppLog.print("reclcerview childcout___"+myCouponRlv.getChildCount());
        }

        @Override
        public void onGetExchargeResult(CouponItem couponItem) {
            if (window != null && window.isShowing()) {
                window.dismiss();
            }
            mContentloader.getMyCoupon(UserHelper.getUserId(MyCouponActivity.this), UserHelper.getToken(MyCouponActivity.this));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setBackResult();
    }

    public void setBackResult() {
//        KeyboardUtil.hidenSoftKey(couponInputEdt);
        if (pageType == KeyParams.PAGE_TYPE_WALLET) {
            setResult(KeyParams.RESULT_UPDATE_WALLET);
        }

    }

    public WalletContent getWalletContent() {
        return getIntent().getParcelableExtra(KeyParams.WALLET_CONTENT);

    }



}
