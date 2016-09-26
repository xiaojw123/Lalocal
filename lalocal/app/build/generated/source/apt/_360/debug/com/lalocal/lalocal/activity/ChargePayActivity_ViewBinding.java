// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChargePayActivity_ViewBinding<T extends ChargePayActivity> implements Unbinder {
  protected T target;

  private View view2131624889;

  private View view2131624891;

  private View view2131624275;

  public ChargePayActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.chargePayFeeTv = Utils.findRequiredViewAsType(source, R.id.charge_pay_fee_tv, "field 'chargePayFeeTv'", TextView.class);
    target.chargePayGoldNum = Utils.findRequiredViewAsType(source, R.id.charge_pay_gold_num, "field 'chargePayGoldNum'", TextView.class);
    target.palyMannerText = Utils.findRequiredViewAsType(source, R.id.paly_manner_text, "field 'palyMannerText'", TextView.class);
    target.payMannerAlipayCb = Utils.findRequiredViewAsType(source, R.id.pay_manner_alipay_cb, "field 'payMannerAlipayCb'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.pay_manner_alipay_fl, "field 'payMannerAlipayFl' and method 'onClick'");
    target.payMannerAlipayFl = Utils.castView(view, R.id.pay_manner_alipay_fl, "field 'payMannerAlipayFl'", FrameLayout.class);
    view2131624889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.payMannerWeixinCb = Utils.findRequiredViewAsType(source, R.id.pay_manner_weixin_cb, "field 'payMannerWeixinCb'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.pay_manner_weixin_fl, "field 'payMannerWeixinFl' and method 'onClick'");
    target.payMannerWeixinFl = Utils.castView(view, R.id.pay_manner_weixin_fl, "field 'payMannerWeixinFl'", FrameLayout.class);
    view2131624891 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.charge_pay_btn, "field 'chargePayBtn' and method 'onClick'");
    target.chargePayBtn = Utils.castView(view, R.id.charge_pay_btn, "field 'chargePayBtn'", Button.class);
    view2131624275 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.chargePayFeeTv = null;
    target.chargePayGoldNum = null;
    target.palyMannerText = null;
    target.payMannerAlipayCb = null;
    target.payMannerAlipayFl = null;
    target.payMannerWeixinCb = null;
    target.payMannerWeixinFl = null;
    target.chargePayBtn = null;

    view2131624889.setOnClickListener(null);
    view2131624889 = null;
    view2131624891.setOnClickListener(null);
    view2131624891 = null;
    view2131624275.setOnClickListener(null);
    view2131624275 = null;

    this.target = null;
  }
}
