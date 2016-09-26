// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.amap.api.maps2d.MapView;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomViewPager;
import com.sackcentury.shinebuttonlib.ShineButton;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RouteDetailActivity_ViewBinding<T extends RouteDetailActivity> implements Unbinder {
  protected T target;

  private View view2131624980;

  private View view2131624984;

  private View view2131624986;

  private View view2131624967;

  private View view2131624975;

  private View view2131624976;

  private View view2131624977;

  public RouteDetailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.dayItemDetailRlv = Utils.findRequiredViewAsType(source, R.id.day_item_detail_rlv, "field 'dayItemDetailRlv'", RecyclerView.class);
    target.dayItemDetailContainer = Utils.findRequiredViewAsType(source, R.id.day_item_detail_container, "field 'dayItemDetailContainer'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.day_item_detail_title_container, "field 'dayItemDetailTitle' and method 'closeDayItemDetail'");
    target.dayItemDetailTitle = Utils.castView(view, R.id.day_item_detail_title_container, "field 'dayItemDetailTitle'", RelativeLayout.class);
    view2131624980 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.closeDayItemDetail();
      }
    });
    target.dayItemDetailName = Utils.findRequiredViewAsType(source, R.id.day_item_detail_title, "field 'dayItemDetailName'", TextView.class);
    target.dayItemDetailSubtitle = Utils.findRequiredViewAsType(source, R.id.day_item_detail_subtitle, "field 'dayItemDetailSubtitle'", TextView.class);
    target.dayItemDetailLocTv = Utils.findRequiredViewAsType(source, R.id.day_item_detail_loc_tv, "field 'dayItemDetailLocTv'", TextView.class);
    target.routeDetailBuyBetween = Utils.findRequiredViewAsType(source, R.id.route_detail_buy_between, "field 'routeDetailBuyBetween'", TextView.class);
    view = Utils.findRequiredView(source, R.id.day_item_detail_loc, "field 'dayItemDetailLoc' and method 'planFromMap'");
    target.dayItemDetailLoc = Utils.castView(view, R.id.day_item_detail_loc, "field 'dayItemDetailLoc'", ImageView.class);
    view2131624984 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.planFromMap();
      }
    });
    view = Utils.findRequiredView(source, R.id.day_item_detail_buy_btn, "field 'dayItemDetailBuyBtn' and method 'openProductDetail'");
    target.dayItemDetailBuyBtn = Utils.castView(view, R.id.day_item_detail_buy_btn, "field 'dayItemDetailBuyBtn'", Button.class);
    view2131624986 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openProductDetail(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.route_detail_service, "field 'routeDetailService' and method 'chatToLalocalService'");
    target.routeDetailService = Utils.castView(view, R.id.route_detail_service, "field 'routeDetailService'", TextView.class);
    view2131624967 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.chatToLalocalService();
      }
    });
    target.mapView = Utils.findRequiredViewAsType(source, R.id.route_detail_mapview, "field 'mapView'", MapView.class);
    target.routeDetailRoutedateLlt = Utils.findRequiredViewAsType(source, R.id.route_detail_routedate_llt, "field 'routeDetailRoutedateLlt'", LinearLayout.class);
    target.routeDetailHsv = Utils.findRequiredViewAsType(source, R.id.route_detail_hsv, "field 'routeDetailHsv'", HorizontalScrollView.class);
    view = Utils.findRequiredView(source, R.id.route_detail_btn_share, "field 'routeDetailBtnShare' and method 'showShare'");
    target.routeDetailBtnShare = Utils.castView(view, R.id.route_detail_btn_share, "field 'routeDetailBtnShare'", ImageView.class);
    view2131624975 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.showShare();
      }
    });
    view = Utils.findRequiredView(source, R.id.route_detail_collect_sbtn, "field 'likeBtn' and method 'collect'");
    target.likeBtn = Utils.castView(view, R.id.route_detail_collect_sbtn, "field 'likeBtn'", ShineButton.class);
    view2131624976 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.collect();
      }
    });
    view = Utils.findRequiredView(source, R.id.route_detail_buy, "field 'routeDetailBuy' and method 'openProductDetail'");
    target.routeDetailBuy = Utils.castView(view, R.id.route_detail_buy, "field 'routeDetailBuy'", FrameLayout.class);
    view2131624977 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.openProductDetail(p0);
      }
    });
    target.routeDetailViewpagerRoute = Utils.findRequiredViewAsType(source, R.id.route_detail_viewpager_route, "field 'routeDetailViewpagerRoute'", CustomViewPager.class);
    target.dayPointImg = Utils.findRequiredViewAsType(source, R.id.route_detail_day_point, "field 'dayPointImg'", ImageView.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.dayItemDetailRlv = null;
    target.dayItemDetailContainer = null;
    target.dayItemDetailTitle = null;
    target.dayItemDetailName = null;
    target.dayItemDetailSubtitle = null;
    target.dayItemDetailLocTv = null;
    target.routeDetailBuyBetween = null;
    target.dayItemDetailLoc = null;
    target.dayItemDetailBuyBtn = null;
    target.routeDetailService = null;
    target.mapView = null;
    target.routeDetailRoutedateLlt = null;
    target.routeDetailHsv = null;
    target.routeDetailBtnShare = null;
    target.likeBtn = null;
    target.routeDetailBuy = null;
    target.routeDetailViewpagerRoute = null;
    target.dayPointImg = null;

    view2131624980.setOnClickListener(null);
    view2131624980 = null;
    view2131624984.setOnClickListener(null);
    view2131624984 = null;
    view2131624986.setOnClickListener(null);
    view2131624986 = null;
    view2131624967.setOnClickListener(null);
    view2131624967 = null;
    view2131624975.setOnClickListener(null);
    view2131624975 = null;
    view2131624976.setOnClickListener(null);
    view2131624976 = null;
    view2131624977.setOnClickListener(null);
    view2131624977 = null;

    this.target = null;
  }
}
