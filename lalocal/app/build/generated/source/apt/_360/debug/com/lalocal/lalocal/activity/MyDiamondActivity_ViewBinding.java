// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.content.res.Resources;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MyDiamondActivity_ViewBinding<T extends MyDiamondActivity> implements Unbinder {
  protected T target;

  private View view2131624726;

  private View view2131624730;

  public MyDiamondActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.my_diamond_recharge_tv, "field 'myDiamondRechargeTv' and method 'onClick'");
    target.myDiamondRechargeTv = Utils.castView(view, R.id.my_diamond_recharge_tv, "field 'myDiamondRechargeTv'", TextView.class);
    view2131624726 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.myDiamondNumTv = Utils.findRequiredViewAsType(source, R.id.my_diamond_num_tv, "field 'myDiamondNumTv'", TextView.class);
    target.myDiamondCosumeXrv = Utils.findRequiredViewAsType(source, R.id.my_diamond_cosume_xrv, "field 'myDiamondCosumeXrv'", XRecyclerView.class);
    target.consumeDoubtTv = Utils.findRequiredViewAsType(source, R.id.consume_doubt_tv, "field 'consumeDoubtTv'", TextView.class);
    target.myDiamondNoRecharge = Utils.findRequiredViewAsType(source, R.id.my_diamond_no_recharge, "field 'myDiamondNoRecharge'", TextView.class);
    target.myDiamondCtv = Utils.findRequiredViewAsType(source, R.id.my_diamond_ctv, "field 'myDiamondCtv'", CustomTitleView.class);
    view = Utils.findRequiredView(source, R.id.consume_doubt_container, "field 'consumeDoubtContainer' and method 'onClick'");
    target.consumeDoubtContainer = Utils.castView(view, R.id.consume_doubt_container, "field 'consumeDoubtContainer'", FrameLayout.class);
    view2131624730 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });

    Resources res = source.getResources();
    target.consumeDoubt = res.getString(R.string.consume_doubt);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.myDiamondRechargeTv = null;
    target.myDiamondNumTv = null;
    target.myDiamondCosumeXrv = null;
    target.consumeDoubtTv = null;
    target.myDiamondNoRecharge = null;
    target.myDiamondCtv = null;
    target.consumeDoubtContainer = null;

    view2131624726.setOnClickListener(null);
    view2131624726 = null;
    view2131624730.setOnClickListener(null);
    view2131624730 = null;

    this.target = null;
  }
}
