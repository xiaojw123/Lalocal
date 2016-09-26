// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PayCompleteActivity_ViewBinding<T extends PayCompleteActivity> implements Unbinder {
  protected T target;

  private View view2131624866;

  private View view2131624874;

  public PayCompleteActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.pay_complete_service, "field 'customerService' and method 'onClick'");
    target.customerService = Utils.castView(view, R.id.pay_complete_service, "field 'customerService'", TextView.class);
    view2131624866 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.orderFeeTv = Utils.findRequiredViewAsType(source, R.id.pay_complete_fee_tv, "field 'orderFeeTv'", TextView.class);
    target.goodNameTv = Utils.findRequiredViewAsType(source, R.id.pay_complete_good_name, "field 'goodNameTv'", TextView.class);
    target.contactNameTv = Utils.findRequiredViewAsType(source, R.id.pay_complete_contact_name, "field 'contactNameTv'", TextView.class);
    target.contactMobileTv = Utils.findRequiredViewAsType(source, R.id.pay_complete_contact_mobile, "field 'contactMobileTv'", TextView.class);
    target.contactEmailTv = Utils.findRequiredViewAsType(source, R.id.pay_complete_contact_email, "field 'contactEmailTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.pay_complete_vieworder_btn, "field 'vieworderBtn' and method 'onClick'");
    target.vieworderBtn = Utils.castView(view, R.id.pay_complete_vieworder_btn, "field 'vieworderBtn'", Button.class);
    view2131624874 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.payCompleteCtv = Utils.findRequiredViewAsType(source, R.id.pay_complete_ctv, "field 'payCompleteCtv'", CustomTitleView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.customerService = null;
    target.orderFeeTv = null;
    target.goodNameTv = null;
    target.contactNameTv = null;
    target.contactMobileTv = null;
    target.contactEmailTv = null;
    target.vieworderBtn = null;
    target.payCompleteCtv = null;

    view2131624866.setOnClickListener(null);
    view2131624866 = null;
    view2131624874.setOnClickListener(null);
    view2131624874 = null;

    this.target = null;
  }
}
