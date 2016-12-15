package com.lalocal.lalocal.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.help.KeyParams;
import com.lalocal.lalocal.help.PageType;
import com.lalocal.lalocal.model.CmbPay;
import com.lalocal.lalocal.model.RechargeItem;
import com.lalocal.lalocal.net.callback.ICallBack;
import com.lalocal.lalocal.util.AppConfig;
import com.lalocal.lalocal.util.AppLog;
import com.lalocal.lalocal.util.CommonUtil;
import com.lalocal.lalocal.view.dialog.CustomDialog;
import com.pingplusplus.android.Pingpp;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChargePayActivity extends BaseActivity {
    public static final String RECHARGE_ITEM = "recharge_item";

    @BindView(R.id.charge_pay_fee_tv)
    TextView chargePayFeeTv;
    @BindView(R.id.charge_pay_gold_num)
    TextView chargePayGoldNum;
    @BindView(R.id.paly_manner_text)
    TextView palyMannerText;
    @BindView(R.id.pay_manner_alipay_cb)
    ImageView payMannerAlipayCb;
    @BindView(R.id.pay_manner_alipay_fl)
    FrameLayout payMannerAlipayFl;
    @BindView(R.id.pay_manner_weixin_cb)
    ImageView payMannerWeixinCb;
    @BindView(R.id.pay_manner_weixin_fl)
    FrameLayout payMannerWeixinFl;
    @BindView(R.id.pay_manner_cmb_cb)
    ImageView payMannerCmbCb;
    @BindView(R.id.charge_pay_btn)
    Button chargePayBtn;
    @BindView(R.id.pay_manner_cmb_fl)
    FrameLayout payMannerCmbFl;
    RechargeItem item;
    boolean isPayConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.charge_pay_layout);
        ButterKnife.bind(this);
        selectPayManner(R.id.pay_manner_alipay_fl);
        item = getRechargeItem();
        chargePayFeeTv.setText(CommonUtil.formartNum(item.getFee()));
        chargePayGoldNum.setText(CommonUtil.formartNum(item.getValue()));
        setLoaderCallBack(new ChargePayCallBack());
    }

    @OnClick({R.id.pay_manner_alipay_fl, R.id.pay_manner_weixin_fl, R.id.pay_manner_cmb_fl, R.id.charge_pay_btn})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.pay_manner_alipay_fl:
            case R.id.pay_manner_weixin_fl:
            case R.id.pay_manner_cmb_fl:
                selectPayManner(id);
                break;
            case R.id.charge_pay_btn:
                chargePay();
                break;
        }
    }

    private void chargePay() {
        String channel = "";
        if (payMannerWeixinCb.isSelected()) {
            channel = PayActivity.CHANNEL_WECHAT;
            AppLog.print("微信——————支付————");
        }
        //支付宝
        if (payMannerAlipayCb.isSelected()) {
            channel = PayActivity.CHANNEL_ALIPAY;
            AppLog.print("支付宝——————支付————");

        }
        if (payMannerCmbCb.isSelected()) {
            channel = PayActivity.CHANNEL_CMB;
        }
        JSONObject jobj = new JSONObject();
        try {
            jobj.put("channel", channel);
            jobj.put("productId", item.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mContentloader.chargeGold(jobj.toString(), channel);

    }

    public void selectPayManner(int id) {
        switch (id) {
            case R.id.pay_manner_alipay_fl:
                if (!payMannerAlipayCb.isSelected()) {
                    payMannerAlipayCb.setSelected(true);
                }
                payMannerWeixinCb.setSelected(false);
                payMannerCmbCb.setSelected(false);
                break;
            case R.id.pay_manner_weixin_fl:
                if (!payMannerWeixinCb.isSelected()) {
                    payMannerWeixinCb.setSelected(true);
                }
                payMannerAlipayCb.setSelected(false);
                payMannerCmbCb.setSelected(false);
                break;
            case R.id.pay_manner_cmb_fl:
                if (!payMannerCmbCb.isSelected()) {
                    payMannerCmbCb.setSelected(true);
                }
                payMannerAlipayCb.setSelected(false);
                payMannerWeixinCb.setSelected(false);
                break;
        }
    }

    public RechargeItem getRechargeItem() {
        return getIntent().getParcelableExtra(RECHARGE_ITEM);
    }

    class ChargePayCallBack extends ICallBack {
        @Override
        public void onChargeGold(String result, String channel) {
            if (PayActivity.CHANNEL_CMB.equals(channel)) {
                AppLog.print("channel cmb_____" + result);
                cmbCharge(result);
            } else {
                Pingpp.createPayment(ChargePayActivity.this, result);

            }
        }

        private void cmbCharge(String result) {
            Gson gson = new Gson();
            CmbPay cmbPay = gson.fromJson(result, CmbPay.class);
            String retPara = cmbPay.getMerchantRetPara();
            String cmbPayCommandUrl = AppConfig.getCmbPayCommand(
                    cmbPay.getBranchId(),
                    cmbPay.getCoNo(),
                    cmbPay.getBillNo(),
                    cmbPay.getAmount(),
                    cmbPay.getDate(),
                    cmbPay.getExpireTimeSpan(),
                    cmbPay.getMerchantUrl(),
                    cmbPay.getMerchantPara(),
                    cmbPay.getMerchantCode(),
                    cmbPay.getMerchantRetUrl(),
                    retPara);
            AppLog.print("招行支付命令————" + cmbPayCommandUrl);
            Intent intent = new Intent(ChargePayActivity.this, CmbPayActivity.class);
            intent.putExtra(CmbPayActivity.CMB_PAY_URL, cmbPayCommandUrl);
            if (!TextUtils.isEmpty(retPara)) {
                String payNo = retPara.substring(retPara.indexOf("=") + 1, retPara.length());
                AppLog.print("payNo:"+payNo);
                intent.putExtra(KeyParams.PAY_NO, payNo);
            }
            intent.putExtra(KeyParams.PAGE_TYPE, PageType.PAGE_CHARGE);
            startActivityForResult(intent, KeyParams.REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Pingpp.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
                AppLog.print("pay result___" + result);
                switch (result) {
                    case "success":
                        CommonUtil.showPromptDialog(this, "支付成功", new CustomDialog.CustomDialogListener() {
                            @Override
                            public void onDialogClickListener() {
                                setResult(KeyParams.RESULT_ChARGE_SUCCESS, null);
                                finish();

                            }
                        });
                        break;
                    case "cancel":
                        Toast.makeText(this, "支付取消", Toast.LENGTH_SHORT).show();
                        break;
                    case "invalid":
                        Toast.makeText(this, "支付失效", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        } else {
            if (resultCode == PayActivity.RESULT_CMB_PAY_SUCCESS) {
                setResult(KeyParams.RESULT_ChARGE_SUCCESS, null);
                finish();
            }
        }
    }


}
