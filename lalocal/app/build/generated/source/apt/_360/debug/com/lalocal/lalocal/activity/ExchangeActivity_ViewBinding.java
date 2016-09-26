// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ExchangeActivity_ViewBinding<T extends ExchangeActivity> implements Unbinder {
  protected T target;

  private View view2131624446;

  private View view2131624445;

  public ExchangeActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.exchageScoreNumTv = Utils.findRequiredViewAsType(source, R.id.exchage_score_num_tv, "field 'exchageScoreNumTv'", TextView.class);
    target.exchageGoldNumTv = Utils.findRequiredViewAsType(source, R.id.exchage_gold_num_tv, "field 'exchageGoldNumTv'", TextView.class);
    target.exchageScoreEdit = Utils.findRequiredViewAsType(source, R.id.exchage_score_edit, "field 'exchageScoreEdit'", EditText.class);
    view = Utils.findRequiredView(source, R.id.exchage_btn, "field 'exchageBtn' and method 'onClick'");
    target.exchageBtn = Utils.castView(view, R.id.exchage_btn, "field 'exchageBtn'", Button.class);
    view2131624446 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.excharge_score_unit, "field 'unitTv' and method 'onClick'");
    target.unitTv = Utils.castView(view, R.id.excharge_score_unit, "field 'unitTv'", TextView.class);
    view2131624445 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.exchangeCtv = Utils.findRequiredViewAsType(source, R.id.exchange_title_ctv, "field 'exchangeCtv'", CustomTitleView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.exchageScoreNumTv = null;
    target.exchageGoldNumTv = null;
    target.exchageScoreEdit = null;
    target.exchageBtn = null;
    target.unitTv = null;
    target.exchangeCtv = null;

    view2131624446.setOnClickListener(null);
    view2131624446 = null;
    view2131624445.setOnClickListener(null);
    view2131624445 = null;

    this.target = null;
  }
}
