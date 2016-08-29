package com.lalocal.lalocal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.activity.fragment.MeFragment;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.model.OrderDetail;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.pingplusplus.android.Pingpp;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayActivity extends BaseActivity implements CustomTitleView.onBackBtnClickListener {
    public static final int RESULT_BACK_PRODUCT = 0x11;
    public static final String ORDER_ID = "order_id";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";
    /**
     * 信诚分期付渠道
     */
    private static final String CHANNEL_XINCHENG = "xc";

    @BindView(R.id.pay_order_title)
    TextView payOrderTitle;
    @BindView(R.id.pay_order_info_item)
    FrameLayout payOrderInfoItem;
    @BindView(R.id.paly_manner_text)
    TextView palyMannerText;
    @BindView(R.id.pay_manner_alipay_fl)
    FrameLayout payMannerAlipayFl;
    @BindView(R.id.pay_manner_weixin_fl)
    FrameLayout payMannerWeixinFl;
    @BindView(R.id.pay_manner_instalments_fl)
    FrameLayout payMannerInstalmentsFl;
    @BindView(R.id.pay_available_instalments)
    TextView payAvailableInstalments;
    @BindView(R.id.pay_showdetail_triangle_container)
    FrameLayout payShowdetailTriangleContainer;
    @BindView(R.id.pay_money_amount)
    TextView payMoneyAmount;
    @BindView(R.id.pay_bottom_container)
    RelativeLayout payBottomContainer;
    @BindView(R.id.pay_manner_alipay_cb)
    ImageView payMannerAlipayCb;
    @BindView(R.id.pay_manner_weixin_cb)
    ImageView payMannerWeixinCb;
    @BindView(R.id.pay_manner_instalments_cb)
    ImageView payMannerInstalmentsCb;
    @BindView(R.id.pay_showdetail_triangle_btn)
    Button payShowdetailTriangleBtn;
    @BindView(R.id.pay_btn)
    Button payBtn;
    String mChannelStr = CHANNEL_ALIPAY;
    OrderDetail mOrderDetail;
    @BindView(R.id.order_pays_llt)
    LinearLayout orderPaysLlt;
    int mOrderid;
    String actionType;
    @BindView(R.id.pay_title_view)
    CustomTitleView payTitleView;
    boolean isPayConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_layout);
        unbinder = ButterKnife.bind(this);
        payTitleView.setOnBackClickListener(this);
        setLoaderCallBack(new PayCallBack());
        mOrderid = getIntent().getIntExtra(ORDER_ID, -1);
        actionType = getIntent().getStringExtra(KeyParams.ACTION_TYPE);
        mContentloader.getOrderDetail(mOrderid);
        payMannerAlipayCb.setSelected(true);
    }


    @OnClick({R.id.pay_showdetail_triangle_btn, R.id.pay_btn, R.id.pay_order_info_item,
            R.id.pay_manner_alipay_fl, R.id.pay_manner_weixin_fl, R.id.pay_manner_instalments_fl, R.id.pay_showdetail_triangle_container})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.pay_manner_alipay_fl:
            case R.id.pay_manner_weixin_fl:
            case R.id.pay_manner_instalments_fl:
                selectPayManner(id);
                break;
            case R.id.pay_order_info_item:
                showOrderDetail();
                break;
            case R.id.pay_showdetail_triangle_container:
                showDetailView();
                break;
            case R.id.pay_btn:
                //TODO:支付流程
                //微信
                if (payMannerWeixinCb.isSelected()) {
                    mChannelStr = CHANNEL_WECHAT;
                    AppLog.print("微信——————支付————");
                }
                //支付宝
                if (payMannerAlipayCb.isSelected()) {
                    mChannelStr = CHANNEL_ALIPAY;
                    AppLog.print("支付宝——————支付————");

                }
                //信诚分期款
                if (payMannerInstalmentsCb.isSelected()) {
                    mChannelStr = CHANNEL_XINCHENG;
                    AppLog.print("信诚——————支付————");
                    return;
                }
                //TODO:test
//                showCompletePay();
                //TODO: payorder  reset
                if (mOrderDetail != null) {
                    PaymentRequest request = new PaymentRequest(mChannelStr, mOrderid, mOrderDetail.getFee());
                    Gson gson = new Gson();
                    String json = gson.toJson(request);
                    AppLog.print("requestJson___" + json);
                    mContentloader.payOrder(json);
                }
                break;

        }
    }

    private void showOrderDetail() {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(KeyParams.ORDER_ID, mOrderid);
        intent.putExtra(KeyParams.PRE_VIEW_PARAMS, true);
//        startActivityForResult(intent,100);
        startActivity(intent);
    }

    private void showDetailView() {
        //TODO ：显示明细
        if (!payShowdetailTriangleBtn.isSelected()) {
            payShowdetailTriangleBtn.setSelected(true);
            orderPaysLlt.setVisibility(View.VISIBLE);
        } else {
            payShowdetailTriangleBtn.setSelected(false);
            orderPaysLlt.setVisibility(View.INVISIBLE);
        }


    }

    public void selectPayManner(int id) {
        switch (id) {
            case R.id.pay_manner_alipay_fl:
                if (!payMannerAlipayCb.isSelected()) {
                    payMannerAlipayCb.setSelected(true);
                }
                payMannerWeixinCb.setSelected(false);
                payMannerInstalmentsCb.setSelected(false);
                break;
            case R.id.pay_manner_weixin_fl:
                if (!payMannerWeixinCb.isSelected()) {
                    payMannerWeixinCb.setSelected(true);
                }
                payMannerAlipayCb.setSelected(false);
                payMannerInstalmentsCb.setSelected(false);
                break;
            case R.id.pay_manner_instalments_fl:
                if (!payMannerInstalmentsCb.isSelected()) {
                    payMannerInstalmentsCb.setSelected(true);
                }
                payMannerAlipayCb.setSelected(false);
                payMannerWeixinCb.setSelected(false);
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        AppLog.print("PayActivity onActivityResult___reqCODE__" + requestCode + "___resultCode___" + resultCode);
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                AppLog.print("pay result___" + result);
                String text = "未支付";
                switch (result) {
                    case "success":
                        CommonUtil.showPromptDialog(this, "支付成功", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                showCompletePay();
                            }
                        });
                        break;
                    default:
                        //微信支付返回有问题，需二次确认
                        isPayConfirm = true;
                        mContentloader.getOrderDetail(mOrderid);
                        break;
//                    case "cancel":
//                        text = "支付取消";
//                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//                        break;
//                    case "invalid":
//                        text = "支付失效";
//                        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//                        break;
                }
            }
        } else if (requestCode == 101) {
            AppLog.print("setResult___" + resultCode);
            if (resultCode == RESULT_BACK_PRODUCT) {
                setResult(RESULT_BACK_PRODUCT);
                finish();
            }

        }
    }

    public void showCompletePay() {
        if (KeyParams.ACTION_UPDATE_ORDER.equals(actionType)) {
            setResult(MeFragment.UPDATE_MY_ORDER);
        }
        Intent intent = new Intent(this, PayCompleteActivity.class);
        intent.putExtra(KeyParams.ORDER_ID, mOrderDetail.getId());
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackClick() {
        backToOrderDetail();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToOrderDetail();
    }


    private void backToOrderDetail() {
        if (KeyParams.ACTION_BOOK.equals(actionType)) {
            Intent intent = new Intent(this, OrderActivity.class);
            intent.putExtra(KeyParams.ORDER_ID, mOrderid);
            startActivityForResult(intent, 101);
        }
    }


    class PaymentRequest {
        String channel;
        double amount;
        int orderId;

        public PaymentRequest(String channel, int orderId, double amount) {
            this.channel = channel;
            this.orderId = orderId;
            this.amount = amount;
        }
    }

    class PayCallBack extends ICallBack {
        @Override
        public void onGetPayResult(String result) {
            AppLog.print("onGetPayResult result___" + result);
            Pingpp.createPayment(PayActivity.this, result);

        }

        @Override
        public void onRequestFailed(VolleyError volleyError) {
            int code = volleyError.networkResponse.statusCode;
            if (code == 401) {
                Intent intent = new Intent(PayActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }

        @Override
        public void onGetOrderDetail(OrderDetail detail) {
            if (isPayConfirm) {
                isPayConfirm = false;
                if (detail != null) {
                    mOrderDetail = detail;
                    AppLog.print("ordepay status____" + detail.getStatus());
                    int status = detail.getStatus();
                    switch (status) {
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                            CommonUtil.showPromptDialog(PayActivity.this, "支付成功", new CustomDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    showCompletePay();
                                }
                            });
                            break;
                        default:
                            CommonUtil.showPromptDialog(PayActivity.this, "支付失败", new CustomDialog.CustomDialogListener() {
                                @Override
                                public void onDialogClickListener() {
                                    backToOrderDetail();
                                }
                            });
                            break;
                    }
                }
            } else {
                updateView(detail);
            }

        }
    }

    private void updateView(OrderDetail detail) {
        if (detail != null) {
            mOrderDetail = detail;
            payOrderTitle.setText(detail.getName());
            double couponValue = detail.getCouponValue();
            List<OrderDetail.OrderPayListBean> payList = detail.getOrderPayList();
            LayoutInflater inflater = LayoutInflater.from(this);
            for (OrderDetail.OrderPayListBean orderpay : payList) {
                int num = orderpay.getAmount();
                RelativeLayout itemView = (RelativeLayout) inflater.inflate(R.layout.order_pays_item, orderPaysLlt, false);
                TextView accoutTv = (TextView) itemView.findViewById(R.id.order_pay_accout);
                TextView titleTv = (TextView) itemView.findViewById(R.id.order_pay_item_title);
                String orderPayName = orderpay.getName();
                titleTv.setText(orderPayName + "*" + num);
                accoutTv.setText(CommonUtil.formartOrderPrice(orderpay.getUnit()));
                orderPaysLlt.addView(itemView);
            }
            if (couponValue > 0) {
                RelativeLayout couponView = (RelativeLayout) inflater.inflate(R.layout.order_pays_item, orderPaysLlt, false);
                TextView titleTv = (TextView) couponView.findViewById(R.id.order_pay_item_title);
                TextView accoutTv = (TextView) couponView.findViewById(R.id.order_pay_accout);
                titleTv.setText("优惠券");
                accoutTv.setText(CommonUtil.formartOrderPrice(couponValue));
                orderPaysLlt.addView(couponView);
            }
            payMoneyAmount.setText(CommonUtil.formartOrderPrice(detail.getFee()));
        }


    }


}
