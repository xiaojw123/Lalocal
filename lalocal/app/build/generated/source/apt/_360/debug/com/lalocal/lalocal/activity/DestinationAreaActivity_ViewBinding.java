// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import com.lalocal.lalocal.view.xlistview.XListView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DestinationAreaActivity_ViewBinding<T extends DestinationAreaActivity> implements Unbinder {
  protected T target;

  private View view2131625007;

  private View view2131624364;

  private View view2131624365;

  private View view2131624366;

  private View view2131624367;

  private View view2131624368;

  public DestinationAreaActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.destinationAreaTitle = Utils.findRequiredViewAsType(source, R.id.destination_area_title, "field 'destinationAreaTitle'", CustomTitleView.class);
    view = Utils.findRequiredView(source, R.id.search_view, "field 'searchView' and method 'search'");
    target.searchView = Utils.castView(view, R.id.search_view, "field 'searchView'", FrameLayout.class);
    view2131625007 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.search();
      }
    });
    view = Utils.findRequiredView(source, R.id.des_areanav_menu_hot, "field 'desAreanavMenuHot' and method 'onClick'");
    target.desAreanavMenuHot = Utils.castView(view, R.id.des_areanav_menu_hot, "field 'desAreanavMenuHot'", TextView.class);
    view2131624364 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.des_areanav_menu_strategy, "field 'desAreanavMenuStrategy' and method 'onClick'");
    target.desAreanavMenuStrategy = Utils.castView(view, R.id.des_areanav_menu_strategy, "field 'desAreanavMenuStrategy'", TextView.class);
    view2131624365 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.des_areanav_menu_packagetour, "field 'desAreanavMenuPackagetour' and method 'onClick'");
    target.desAreanavMenuPackagetour = Utils.castView(view, R.id.des_areanav_menu_packagetour, "field 'desAreanavMenuPackagetour'", TextView.class);
    view2131624366 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.des_areanav_menu_freewarker, "field 'desAreanavMenuFreewarker' and method 'onClick'");
    target.desAreanavMenuFreewarker = Utils.castView(view, R.id.des_areanav_menu_freewarker, "field 'desAreanavMenuFreewarker'", TextView.class);
    view2131624367 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.des_areanav_menu_lacoalplay, "field 'desAreanavMenuLacoalplay' and method 'onClick'");
    target.desAreanavMenuLacoalplay = Utils.castView(view, R.id.des_areanav_menu_lacoalplay, "field 'desAreanavMenuLacoalplay'", TextView.class);
    view2131624368 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.desAreaMenunavContainer = Utils.findRequiredViewAsType(source, R.id.des_area_menunav_container, "field 'desAreaMenunavContainer'", LinearLayout.class);
    target.desAreaItemsXlv = Utils.findRequiredViewAsType(source, R.id.des_area_items_xlv, "field 'desAreaItemsXlv'", XListView.class);
    target.loadingPage = Utils.findRequiredView(source, R.id.page_base_loading, "field 'loadingPage'");
    target.destionAreaContainer = Utils.findRequiredViewAsType(source, R.id.destion_area_container, "field 'destionAreaContainer'", RelativeLayout.class);
    target.destinationAreaEmpView = Utils.findRequiredViewAsType(source, R.id.destination_area_emptv, "field 'destinationAreaEmpView'", TextView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.destinationAreaTitle = null;
    target.searchView = null;
    target.desAreanavMenuHot = null;
    target.desAreanavMenuStrategy = null;
    target.desAreanavMenuPackagetour = null;
    target.desAreanavMenuFreewarker = null;
    target.desAreanavMenuLacoalplay = null;
    target.desAreaMenunavContainer = null;
    target.desAreaItemsXlv = null;
    target.loadingPage = null;
    target.destionAreaContainer = null;
    target.destinationAreaEmpView = null;

    view2131625007.setOnClickListener(null);
    view2131625007 = null;
    view2131624364.setOnClickListener(null);
    view2131624364 = null;
    view2131624365.setOnClickListener(null);
    view2131624365 = null;
    view2131624366.setOnClickListener(null);
    view2131624366 = null;
    view2131624367.setOnClickListener(null);
    view2131624367 = null;
    view2131624368.setOnClickListener(null);
    view2131624368 = null;

    this.target = null;
  }
}
