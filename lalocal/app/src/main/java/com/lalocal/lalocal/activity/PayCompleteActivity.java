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
import com.lalocal.lalocal.easemob.Constant;
import com.lalocal.lalocal.easemob.ui.ChatActivity;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayCompleteActivity extends BaseActivity {

    @BindView(R.id.pay_complete_service)
    TextView customerService;
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
    double mAcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_complete_layout);
        unbinder = ButterKnife.bind(this);
        Intent intent = getIntent();
        mOrderDetail = intent.getParcelableExtra(KeyParams.ORDDER_DETFAIL);
        mAcount = intent.getDoubleExtra(KeyParams.AMOUNT_PRICE, 0);
        updateView();
    }

    private void updateView() {
        String formartPricee = CommonUtil.formartOrderPrice(mAcount);
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

    @OnClick({R.id.pay_complete_service, R.id.pay_complete_vieworder_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pay_complete_service:
                if (mOrderDetail != null) {
                    Intent csIntent = new Intent(this, ChatActivity.class);
                    csIntent.putExtra(Constant.ITEM_TITLE, mOrderDetail.getName());
                    csIntent.putExtra(Constant.ITEM_POST_URL, mOrderDetail.getPhone());
                    csIntent.putExtra(Constant.ITEM_PRICE, String.valueOf(mAcount));
                    startActivity(csIntent);
                }
                break;
            case R.id.pay_complete_vieworder_btn:
                if (mOrderDetail != null) {
                    Intent intent = new Intent(this, OrderActivity.class);
                    intent.putExtra(KeyParams.ORDER_ID, mOrderDetail.getId());
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }


}
