// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RechargeActivity_ViewBinding<T extends RechargeActivity> implements Unbinder {
  protected T target;

  private View view2131624949;

  private View view2131624946;

  public RechargeActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.rechargeDiamondNum = Utils.findRequiredViewAsType(source, R.id.recharge_diamond_num, "field 'rechargeDiamondNum'", TextView.class);
    target.rechargeDoubtTv = Utils.findRequiredViewAsType(source, R.id.recharge_doubt_tv, "field 'rechargeDoubtTv'", TextView.class);
    target.rechargePackageRlv = Utils.findRequiredViewAsType(source, R.id.recharge_package_rlv, "field 'rechargePackageRlv'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.rechage_ticket_exchage, "field 'rechageTicketExchage' and method 'onClick'");
    target.rechageTicketExchage = Utils.castView(view, R.id.rechage_ticket_exchage, "field 'rechageTicketExchage'", FrameLayout.class);
    view2131624949 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.fistMsgTv = Utils.findRequiredViewAsType(source, R.id.recharge_package_firstmsg, "field 'fistMsgTv'", TextView.class);
    view = Utils.findRequiredView(source, R.id.recharge_doubt_container, "field 'rechargeDoubtContainer' and method 'onClick'");
    target.rechargeDoubtContainer = Utils.castView(view, R.id.recharge_doubt_container, "field 'rechargeDoubtContainer'", FrameLayout.class);
    view2131624946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.rechargeCtv = Utils.findRequiredViewAsType(source, R.id.recharge_titleview, "field 'rechargeCtv'", CustomTitleView.class);

    Resources res = source.getResources();
    target.rechargeDoubtText = res.getString(R.string.recharge_doubt_customer);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.rechargeDiamondNum = null;
    target.rechargeDoubtTv = null;
    target.rechargePackageRlv = null;
    target.rechageTicketExchage = null;
    target.fistMsgTv = null;
    target.rechargeDoubtContainer = null;
    target.rechargeCtv = null;

    view2131624949.setOnClickListener(null);
    view2131624949 = null;
    view2131624946.setOnClickListener(null);
    view2131624946 = null;

    this.target = null;
  }
}
