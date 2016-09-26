// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyCouponRecyclerAdapter$MyCouponHolder_ViewBinding<T extends MyCouponRecyclerAdapter.MyCouponHolder> implements Unbinder {
  protected T target;

  public MyCouponRecyclerAdapter$MyCouponHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.myCouponContainer = Utils.findRequiredViewAsType(source, R.id.my_coupon_card, "field 'myCouponContainer'", RelativeLayout.class);
    target.cornerImg = Utils.findRequiredViewAsType(source, R.id.my_coupon_corner_img, "field 'cornerImg'", ImageView.class);
    target.discount_tv = Utils.findRequiredViewAsType(source, R.id.my_coupon_denomination_tv, "field 'discount_tv'", TextView.class);
    target.discount_units_tv = Utils.findRequiredViewAsType(source, R.id.my_coupon_discount_units, "field 'discount_units_tv'", TextView.class);
    target.rule1_tv = Utils.findRequiredViewAsType(source, R.id.my_coupon_use_rule_tv, "field 'rule1_tv'", TextView.class);
    target.time_tv = Utils.findRequiredViewAsType(source, R.id.my_coupon_time_tv, "field 'time_tv'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.myCouponContainer = null;
    target.cornerImg = null;
    target.discount_tv = null;
    target.discount_units_tv = null;
    target.rule1_tv = null;
    target.time_tv = null;

    this.target = null;
  }
}
