// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

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

public class MyTravelTicketActivity_ViewBinding<T extends MyTravelTicketActivity> implements Unbinder {
  protected T target;

  private View view2131624737;

  public MyTravelTicketActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.myTicketTitleview = Utils.findRequiredViewAsType(source, R.id.my_ticket_titleview, "field 'myTicketTitleview'", FrameLayout.class);
    target.myScoreNumTv = Utils.findRequiredViewAsType(source, R.id.my_score_num_tv, "field 'myScoreNumTv'", TextView.class);
    target.myTicketCurFl = Utils.findRequiredViewAsType(source, R.id.my_ticket_cur_fl, "field 'myTicketCurFl'", FrameLayout.class);
    target.myTicketCosumeXrv = Utils.findRequiredViewAsType(source, R.id.my_ticket_cosume_xrv, "field 'myTicketCosumeXrv'", XRecyclerView.class);
    view = Utils.findRequiredView(source, R.id.my_ticket_exchargegold_tv, "field 'myTicketRechargegoldTv' and method 'onClick'");
    target.myTicketRechargegoldTv = Utils.castView(view, R.id.my_ticket_exchargegold_tv, "field 'myTicketRechargegoldTv'", TextView.class);
    view2131624737 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.myTicketNoScore = Utils.findRequiredViewAsType(source, R.id.my_ticket_no_score, "field 'myTicketNoScore'", TextView.class);
    target.myTicketCtv = Utils.findRequiredViewAsType(source, R.id.my_travelticket_ctv, "field 'myTicketCtv'", CustomTitleView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.myTicketTitleview = null;
    target.myScoreNumTv = null;
    target.myTicketCurFl = null;
    target.myTicketCosumeXrv = null;
    target.myTicketRechargegoldTv = null;
    target.myTicketNoScore = null;
    target.myTicketCtv = null;

    view2131624737.setOnClickListener(null);
    view2131624737 = null;

    this.target = null;
  }
}
