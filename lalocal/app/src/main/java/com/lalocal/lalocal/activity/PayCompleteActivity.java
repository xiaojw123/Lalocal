package com.lalocal.lalocal.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.MobEvent;
import com.lalocal.lalocal.help.MobHelper;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayCompleteActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {

    @BindView(R.id.pay_complete_fee_tv)
    TextView orderFeeTv;
    @BindView(R.id.pay_complete_good_name)
    TextView goodNameTv;
    @BindView(R.id.pay_complete_contact_name)
    TextView contactNameTv;
    @BindView(R.id.pay_complete_contact_mobile)
    TextView contactMobileTv;
    @BindView(R.id.pay_complete_contact_email)
    TextView contactEmailTv;
    @BindView(R.id.pay_complete_vieworder_btn)
    Button vieworderBtn;

    OrderDetail mOrderDetail;
    @BindView(R.id.pay_complete_ctv)
    CustomTitleView payCompleteCtv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_complete_layout);
        ButterKnife.bind(this);
        setLoaderCallBack(new PayCompeteCallBack());
        int id = getIntent().getIntExtra(KeyParams.ORDER_ID, -1);
        mContentloader.getOrderDetail(id);
        updateView();
    }


    @Override
    public void onBackClick() {
        backToOrderDetail();
    }

    class PayCompeteCallBack extends ICallBack {

        @Override
        public void onGetOrderDetail(OrderDetail detail) {
            mOrderDetail = detail;
            updateView();
        }
    }


    private void updateView() {
        payCompleteCtv.setOnCustomClickLister(this);
        if (mOrderDetail != null) {
            String formartPricee = CommonUtil.formartOrderPrice(mOrderDetail.getFee());
            if (!TextUtils.isEmpty(formartPricee)) {
                SpannableString spannableStr = new SpannableString(formartPricee);
                spannableStr.setSpan(new TextAppearanceSpan(this, R.style.PayCompletePriceStyle1), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannableStr.setSpan(new TextAppearanceSpan(this, R.style.PayCompletePriceStyle2), 1, formartPricee.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                orderFeeTv.setText(spannableStr);
            }
            if (mOrderDetail != null) {
                goodNameTv.setText(mOrderDetail.getName());
                contactNameTv.setText(mOrderDetail.getUserName());
                contactMobileTv.setText(mOrderDetail.getPhone());
                contactEmailTv.setText(mOrderDetail.getEmail());
            }
        }
    }


    @Override
    public void onBackPressed() {
        backToOrderDetail();
//        super.onBackPressed();
    }

    private void backToOrderDetail() {
        AppLog.print("backToOrderDetail_____order id__");
        if (mOrderDetail != null) {
            MobHelper.sendEevent(this, MobEvent.ORDER_DETAIL);
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(KeyParams.ORDER_ID, mOrderDetail.getId());
            startActivityForResult(intent, 100);
        } else {
            CommonUtil.showPromptDialog(this, "订单信息为空", new CustomDialog.CustomDialogListener() {
                @Override
                public void onDialogClickListener() {
                    finish();
                }
            });
        }
    }

    @OnClick({R.id.pay_complete_vieworder_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_complete_vieworder_btn:
                //// TODO: 2016/11/22
                backToOrderDetail();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        AppLog.print("onActivitiResult__" + resultCode);
        if (requestCode == 100) {
            setResult(resultCode, data);
            finish();
        }

    }
}
