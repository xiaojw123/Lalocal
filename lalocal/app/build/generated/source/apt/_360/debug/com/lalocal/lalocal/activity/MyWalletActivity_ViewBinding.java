// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyWalletActivity_ViewBinding<T extends MyWalletActivity> implements Unbinder {
  protected T target;

  private View view2131624742;

  private View view2131624744;

  private View view2131624746;

  public MyWalletActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.myDiamondNum = Utils.findRequiredViewAsType(source, R.id.my_diamond_num, "field 'myDiamondNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.my_diamond_llt, "field 'myDiamondLlt' and method 'onClick'");
    target.myDiamondLlt = Utils.castView(view, R.id.my_diamond_llt, "field 'myDiamondLlt'", LinearLayout.class);
    view2131624742 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.myTravelticketNum = Utils.findRequiredViewAsType(source, R.id.my_travelticket_num, "field 'myTravelticketNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.my_travelticket_llt, "field 'myTravelticketLlt' and method 'onClick'");
    target.myTravelticketLlt = Utils.castView(view, R.id.my_travelticket_llt, "field 'myTravelticketLlt'", LinearLayout.class);
    view2131624744 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.myCouponNum = Utils.findRequiredViewAsType(source, R.id.my_coupon_num, "field 'myCouponNum'", TextView.class);
    view = Utils.findRequiredView(source, R.id.my_coupon_llt, "field 'myCouponLlt' and method 'onClick'");
    target.myCouponLlt = Utils.castView(view, R.id.my_coupon_llt, "field 'myCouponLlt'", LinearLayout.class);
    view2131624746 = view;
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

    target.myDiamondNum = null;
    target.myDiamondLlt = null;
    target.myTravelticketNum = null;
    target.myTravelticketLlt = null;
    target.myCouponNum = null;
    target.myCouponLlt = null;

    view2131624742.setOnClickListener(null);
    view2131624742 = null;
    view2131624744.setOnClickListener(null);
    view2131624744 = null;
    view2131624746.setOnClickListener(null);
    view2131624746 = null;

    this.target = null;
  }
}
