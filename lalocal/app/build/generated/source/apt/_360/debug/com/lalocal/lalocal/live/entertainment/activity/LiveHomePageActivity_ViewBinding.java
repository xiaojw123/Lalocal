// Generated code from Butter Knife. Do not modify!
package com.lalocal.lalocal.live.entertainment.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.lalocal.lalocal.R;
import com.lalocal.lalocal.view.CustomTitleView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LiveHomePageActivity_ViewBinding<T extends LiveHomePageActivity> implements Unbinder {
  protected T target;

  private View view2131624655;

  private View view2131624663;

  private View view2131624658;

  private View view2131624660;

  private View view2131624662;

  public LiveHomePageActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.homepageHead = Utils.findRequiredViewAsType(source, R.id.homepage_head, "field 'homepageHead'", CustomTitleView.class);
    target.liveVerified = Utils.findRequiredViewAsType(source, R.id.live_verified, "field 'liveVerified'", TextView.class);
    target.homepageMasterName = Utils.findRequiredViewAsType(source, R.id.homepage_master_name, "field 'homepageMasterName'", TextView.class);
    target.homepageMasterSignature = Utils.findRequiredViewAsType(source, R.id.homepage_master_signature, "field 'homepageMasterSignature'", TextView.class);
    view = Utils.findRequiredView(source, R.id.personal_home_page, "field 'personalHomePage' and method 'clickButton'");
    target.personalHomePage = Utils.castView(view, R.id.personal_home_page, "field 'personalHomePage'", ImageView.class);
    view2131624655 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    target.homepageAttentionCountTv = Utils.findRequiredViewAsType(source, R.id.homepage_attention_count_tv, "field 'homepageAttentionCountTv'", TextView.class);
    target.homepageFansCount = Utils.findRequiredViewAsType(source, R.id.homepage_fans_count, "field 'homepageFansCount'", TextView.class);
    target.lineG = Utils.findRequiredViewAsType(source, R.id.line_g, "field 'lineG'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.master_attention, "field 'masterAttention' and method 'clickButton'");
    target.masterAttention = Utils.castView(view, R.id.master_attention, "field 'masterAttention'", TextView.class);
    view2131624663 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.homepage_attention_layout, "field 'homepageAttentionLayout' and method 'clickButton'");
    target.homepageAttentionLayout = Utils.castView(view, R.id.homepage_attention_layout, "field 'homepageAttentionLayout'", LinearLayout.class);
    view2131624658 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.homepage_fans_layout, "field 'homepageFansLayout' and method 'clickButton'");
    target.homepageFansLayout = Utils.castView(view, R.id.homepage_fans_layout, "field 'homepageFansLayout'", LinearLayout.class);
    view2131624660 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.master_attention_layout, "field 'masterAttentionLayout' and method 'clickButton'");
    target.masterAttentionLayout = Utils.castView(view, R.id.master_attention_layout, "field 'masterAttentionLayout'", LinearLayout.class);
    view2131624662 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clickButton(p0);
      }
    });
    target.lineLayout = Utils.findRequiredViewAsType(source, R.id.line_layout, "field 'lineLayout'", RelativeLayout.class);
    target.ffdffhfd = Utils.findRequiredView(source, R.id.ffdffhfd, "field 'ffdffhfd'");
    target.liveAttentionHomepage = Utils.findRequiredViewAsType(source, R.id.live_attention_homepage, "field 'liveAttentionHomepage'", RelativeLayout.class);
  }

  @Override
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.homepageHead = null;
    target.liveVerified = null;
    target.homepageMasterName = null;
    target.homepageMasterSignature = null;
    target.personalHomePage = null;
    target.homepageAttentionCountTv = null;
    target.homepageFansCount = null;
    target.lineG = null;
    target.masterAttention = null;
    target.homepageAttentionLayout = null;
    target.homepageFansLayout = null;
    target.masterAttentionLayout = null;
    target.lineLayout = null;
    target.ffdffhfd = null;
    target.liveAttentionHomepage = null;

    view2131624655.setOnClickListener(null);
    view2131624655 = null;
    view2131624663.setOnClickListener(null);
    view2131624663 = null;
    view2131624658.setOnClickListener(null);
    view2131624658 = null;
    view2131624660.setOnClickListener(null);
    view2131624660 = null;
    view2131624662.setOnClickListener(null);
    view2131624662 = null;

    this.target = null;
  }
}
