// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.view.adapter;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RechargeListAdapter$RechargeListHolder_ViewBinding<T extends RechargeListAdapter.RechargeListHolder> implements Unbinder {
  protected T target;

  public RechargeListAdapter$RechargeListHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.value_tv = Utils.findRequiredViewAsType(source, R.id.recharge_list_item_value, "field 'value_tv'", TextView.class);
    target.fee_tv = Utils.findRequiredViewAsType(source, R.id.recharge_list_item_fee, "field 'fee_tv'", TextView.class);
    target.lin2 = Utils.findRequiredView(source, R.id.recharge_list_item_lin2, "field 'lin2'");
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.value_tv = null;
    target.fee_tv = null;
    target.lin2 = null;

    this.target = null;
  }
}
