// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PayActivity_ViewBinding<T extends PayActivity> implements Unbinder {
  protected T target;

  private View view2131624876;

  private View view2131624889;

  private View view2131624891;

  private View view2131624879;

  private View view2131624884;

  private View view2131624885;

  private View view2131624887;

  public PayActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.payOrderTitle = Utils.findRequiredViewAsType(source, R.id.pay_order_title, "field 'payOrderTitle'", TextView.class);
    view = Utils.findRequiredView(source, R.id.pay_order_info_item, "field 'payOrderInfoItem' and method 'onClick'");
    target.payOrderInfoItem = Utils.castView(view, R.id.pay_order_info_item, "field 'payOrderInfoItem'", FrameLayout.class);
    view2131624876 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.palyMannerText = Utils.findRequiredViewAsType(source, R.id.paly_manner_text, "field 'palyMannerText'", TextView.class);
    view = Utils.findRequiredView(source, R.id.pay_manner_alipay_fl, "field 'payMannerAlipayFl' and method 'onClick'");
    target.payMannerAlipayFl = Utils.castView(view, R.id.pay_manner_alipay_fl, "field 'payMannerAlipayFl'", FrameLayout.class);
    view2131624889 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.pay_manner_weixin_fl, "field 'payMannerWeixinFl' and method 'onClick'");
    target.payMannerWeixinFl = Utils.castView(view, R.id.pay_manner_weixin_fl, "field 'payMannerWeixinFl'", FrameLayout.class);
    view2131624891 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.pay_manner_instalments_fl, "field 'payMannerInstalmentsFl' and method 'onClick'");
    target.payMannerInstalmentsFl = Utils.castView(view, R.id.pay_manner_instalments_fl, "field 'payMannerInstalmentsFl'", FrameLayout.class);
    view2131624879 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.payAvailableInstalments = Utils.findRequiredViewAsType(source, R.id.pay_available_instalments, "field 'payAvailableInstalments'", TextView.class);
    view = Utils.findRequiredView(source, R.id.pay_showdetail_triangle_container, "field 'payShowdetailTriangleContainer' and method 'onClick'");
    target.payShowdetailTriangleContainer = Utils.castView(view, R.id.pay_showdetail_triangle_container, "field 'payShowdetailTriangleContainer'", FrameLayout.class);
    view2131624884 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.payMoneyAmount = Utils.findRequiredViewAsType(source, R.id.pay_money_amount, "field 'payMoneyAmount'", TextView.class);
    target.payBottomContainer = Utils.findRequiredViewAsType(source, R.id.pay_bottom_container, "field 'payBottomContainer'", RelativeLayout.class);
    target.payMannerAlipayCb = Utils.findRequiredViewAsType(source, R.id.pay_manner_alipay_cb, "field 'payMannerAlipayCb'", ImageView.class);
    target.payMannerWeixinCb = Utils.findRequiredViewAsType(source, R.id.pay_manner_weixin_cb, "field 'payMannerWeixinCb'", ImageView.class);
    target.payMannerInstalmentsCb = Utils.findRequiredViewAsType(source, R.id.pay_manner_instalments_cb, "field 'payMannerInstalmentsCb'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.pay_showdetail_triangle_btn, "field 'payShowdetailTriangleBtn' and method 'onClick'");
    target.payShowdetailTriangleBtn = Utils.castView(view, R.id.pay_showdetail_triangle_btn, "field 'payShowdetailTriangleBtn'", Button.class);
    view2131624885 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.pay_btn, "field 'payBtn' and method 'onClick'");
    target.payBtn = Utils.castView(view, R.id.pay_btn, "field 'payBtn'", Button.class);
    view2131624887 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.orderPaysLlt = Utils.findRequiredViewAsType(source, R.id.order_pays_llt, "field 'orderPaysLlt'", LinearLayout.class);
    target.payTitleView = Utils.findRequiredViewAsType(source, R.id.pay_title_view, "field 'payTitleView'", CustomTitleView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.payOrderTitle = null;
    target.payOrderInfoItem = null;
    target.palyMannerText = null;
    target.payMannerAlipayFl = null;
    target.payMannerWeixinFl = null;
    target.payMannerInstalmentsFl = null;
    target.payAvailableInstalments = null;
    target.payShowdetailTriangleContainer = null;
    target.payMoneyAmount = null;
    target.payBottomContainer = null;
    target.payMannerAlipayCb = null;
    target.payMannerWeixinCb = null;
    target.payMannerInstalmentsCb = null;
    target.payShowdetailTriangleBtn = null;
    target.payBtn = null;
    target.orderPaysLlt = null;
    target.payTitleView = null;

    view2131624876.setOnClickListener(null);
    view2131624876 = null;
    view2131624889.setOnClickListener(null);
    view2131624889 = null;
    view2131624891.setOnClickListener(null);
    view2131624891 = null;
    view2131624879.setOnClickListener(null);
    view2131624879 = null;
    view2131624884.setOnClickListener(null);
    view2131624884 = null;
    view2131624885.setOnClickListener(null);
    view2131624885 = null;
    view2131624887.setOnClickListener(null);
    view2131624887 = null;

    this.target = null;
  }
}
