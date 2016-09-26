// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lalocal.lalocal.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LiveSearchActivity_ViewBinding<T extends LiveSearchActivity> implements Unbinder {
  protected T target;

  private View view2131624681;

  private View view2131624679;

  public LiveSearchActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.liveSearchEdt = Utils.findRequiredViewAsType(source, R.id.live_search_edt, "field 'liveSearchEdt'", EditText.class);
    view = Utils.findRequiredView(source, R.id.live_search_cancel_tv, "field 'liveSearchCancelTv' and method 'onClick'");
    target.liveSearchCancelTv = Utils.castView(view, R.id.live_search_cancel_tv, "field 'liveSearchCancelTv'", TextView.class);
    view2131624681 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.liveSearchXrlv = Utils.findRequiredViewAsType(source, R.id.live_search_xrlv, "field 'liveSearchXrlv'", XRecyclerView.class);
    target.liveSearchNull = Utils.findRequiredViewAsType(source, R.id.live_search_null, "field 'liveSearchNull'", TextView.class);
    view = Utils.findRequiredView(source, R.id.live_back_img, "field 'backImg' and method 'onClick'");
    target.backImg = Utils.castView(view, R.id.live_back_img, "field 'backImg'", ImageView.class);
    view2131624679 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });

    Context context = source.getContext();
    Resources res = context.getResources();
    Resources.Theme theme = context.getTheme();
    target.searchIconColor = Utils.getColor(res, theme, R.color.color_b3);
    target.drawablePadding = res.getDimensionPixelSize(R.dimen.dimen_size_8_dp);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.liveSearchEdt = null;
    target.liveSearchCancelTv = null;
    target.liveSearchXrlv = null;
    target.liveSearchNull = null;
    target.backImg = null;

    view2131624681.setOnClickListener(null);
    view2131624681 = null;
    view2131624679.setOnClickListener(null);
    view2131624679 = null;

    this.target = null;
  }
}
