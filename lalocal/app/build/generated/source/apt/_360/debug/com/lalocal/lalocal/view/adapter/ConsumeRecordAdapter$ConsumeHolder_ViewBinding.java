// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.view.adapter;

import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ConsumeRecordAdapter$ConsumeHolder_ViewBinding<T extends ConsumeRecordAdapter.ConsumeHolder> implements Unbinder {
  protected T target;

  public ConsumeRecordAdapter$ConsumeHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.channel_tv = Utils.findRequiredViewAsType(source, R.id.cri_channel_tv, "field 'channel_tv'", TextView.class);
    target.date_tv = Utils.findRequiredViewAsType(source, R.id.cri_date_tv, "field 'date_tv'", TextView.class);
    target.num_tv = Utils.findRequiredViewAsType(source, R.id.cri_num_tv, "field 'num_tv'", TextView.class);
    target.lin1 = Utils.findRequiredView(source, R.id.consume_record_item_line1, "field 'lin1'");
    target.lin2 = Utils.findRequiredView(source, R.id.consume_record_item_line2, "field 'lin2'");
    target.lin3 = Utils.findRequiredView(source, R.id.consume_record_item_line3, "field 'lin3'");
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.channel_tv = null;
    target.date_tv = null;
    target.num_tv = null;
    target.lin1 = null;
    target.lin2 = null;
    target.lin3 = null;

    this.target = null;
  }
}
