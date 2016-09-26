// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCouponActivity_ViewBinding<T extends MyCouponActivity> implements Unbinder {
  protected T target;

  private View view2131624143;

  private View view2131624149;

  public MyCouponActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.my_coupon_exchage_btn, "field 'myCouponExchageBtn' and method 'onClick'");
    target.myCouponExchageBtn = Utils.castView(view, R.id.my_coupon_exchage_btn, "field 'myCouponExchageBtn'", Button.class);
    view2131624143 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.myCouponRlv = Utils.findRequiredViewAsType(source, R.id.my_coupon_rlv, "field 'myCouponRlv'", RecyclerView.class);
    target.myCouponCtv = Utils.findRequiredViewAsType(source, R.id.my_coupon_ctv, "field 'myCouponCtv'", CustomTitleView.class);
    target.couponContainer = Utils.findRequiredViewAsType(source, R.id.my_coupon_container, "field 'couponContainer'", RelativeLayout.class);
    target.myCouponReductionTv = Utils.findRequiredViewAsType(source, R.id.my_coupon_reduction_tv, "field 'myCouponReductionTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.my_coupon_use_btn, "field 'myCouponUseBtn' and method 'onClick'");
    target.myCouponUseBtn = Utils.castView(view, R.id.my_coupon_use_btn, "field 'myCouponUseBtn'", Button.class);
    view2131624149 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.myCouponUseContainer = Utils.findRequiredViewAsType(source, R.id.my_coupon_use_container, "field 'myCouponUseContainer'", FrameLayout.class);
    target.myCouponReminderTv = Utils.findRequiredViewAsType(source, R.id.my_coupon_friendlyreminder_tv, "field 'myCouponReminderTv'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.myCouponExchageBtn = null;
    target.myCouponRlv = null;
    target.myCouponCtv = null;
    target.couponContainer = null;
    target.myCouponReductionTv = null;
    target.myCouponUseBtn = null;
    target.myCouponUseContainer = null;
    target.myCouponReminderTv = null;

    view2131624143.setOnClickListener(null);
    view2131624143 = null;
    view2131624149.setOnClickListener(null);
    view2131624149 = null;

    this.target = null;
  }
}
